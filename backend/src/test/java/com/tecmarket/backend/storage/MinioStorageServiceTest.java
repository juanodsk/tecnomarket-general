package com.tecmarket.backend.storage.service;

import com.tecmarket.backend.common.exception.FileValidationException;
import com.tecmarket.backend.common.exception.StorageException;
import com.tecmarket.backend.storage.config.MinioStorageProperties;
import com.tecmarket.backend.storage.dto.StoredObjectResponse;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MinioStorageServiceTest {

    private static final byte[] ONE_PIXEL_PNG = Base64.getDecoder().decode(
            "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNk+A8AAQUBAScY42YAAAAASUVORK5CYII=");

    @Test
    void rejectsEmptyFile() {
        MinioStorageService service = disabledService(1024);
        FileValidationException exception = assertThrows(FileValidationException.class,
                () -> service.uploadPublicImage("business-assets", "businesses/1",
                        new MockMultipartFile("file", "empty.png", "image/png", new byte[0])));
        assertEquals("storage.file.required", exception.getMessage());
    }

    @Test
    void rejectsUnsupportedContentType() {
        MinioStorageService service = disabledService(1024);
        FileValidationException exception = assertThrows(FileValidationException.class,
                () -> service.uploadPublicImage("business-assets", "businesses/1",
                        new MockMultipartFile("file", "payload.svg", "image/svg+xml", new byte[] {1})));
        assertEquals("storage.file.invalid_type", exception.getMessage());
    }

    @Test
    void rejectsFileLargerThanConfiguredLimit() {
        MinioStorageService service = disabledService(3);
        FileValidationException exception = assertThrows(FileValidationException.class,
                () -> service.uploadPublicImage("business-assets", "businesses/1",
                        new MockMultipartFile("file", "large.jpg", "image/jpeg", new byte[] {1, 2, 3, 4})));
        assertEquals("storage.file.too_large", exception.getMessage());
    }

    @Test
    void reportsDisabledStorageBeforeConnecting() {
        MinioStorageService service = disabledService(1024);
        StorageException exception = assertThrows(StorageException.class,
                () -> service.uploadPublicImage("business-assets", "businesses/1", png("valid.png", ONE_PIXEL_PNG)));
        assertEquals("storage.disabled", exception.getMessage());
    }

    @Test
    void rejectsDeclaredTypeThatDoesNotMatchMagicBytes() {
        MinioStorageService service = disabledService(1024);
        FileValidationException exception = assertThrows(FileValidationException.class,
                () -> service.uploadPublicImage("business-assets", "businesses/1",
                        new MockMultipartFile("file", "fake.jpg", "image/jpeg", ONE_PIXEL_PNG)));
        assertEquals("storage.file.content_type_mismatch", exception.getMessage());
    }

    @Test
    void rejectsExtensionThatDoesNotMatchMagicBytes() {
        MinioStorageService service = disabledService(1024);
        FileValidationException exception = assertThrows(FileValidationException.class,
                () -> service.uploadPublicImage("business-assets", "businesses/1",
                        new MockMultipartFile("file", "fake.jpg", "image/png", ONE_PIXEL_PNG)));
        assertEquals("storage.file.extension_mismatch", exception.getMessage());
    }

    @Test
    void rejectsDoubleExtension() {
        MinioStorageService service = disabledService(1024);
        FileValidationException exception = assertThrows(FileValidationException.class,
                () -> service.uploadPublicImage("business-assets", "businesses/1", png("invoice.pdf.png", ONE_PIXEL_PNG)));
        assertEquals("storage.file.double_extension", exception.getMessage());
    }

    @Test
    void rejectsCorruptedImageEvenWithAllowedContentType() {
        MinioStorageService service = disabledService(1024);
        byte[] truncatedPng = new byte[] {(byte) 0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a};
        FileValidationException exception = assertThrows(FileValidationException.class,
                () -> service.uploadPublicImage("business-assets", "businesses/1", png("broken.png", truncatedPng)));
        assertEquals("storage.file.invalid_image", exception.getMessage());
    }

    @Test
    void rejectsImageAbovePixelLimitBeforeDecodingPixels() {
        MinioStorageProperties properties = baseProperties();
        properties.setEnabled(false);
        properties.setMaxImagePixels(0);
        MinioStorageService service = new MinioStorageService(properties);
        FileValidationException exception = assertThrows(FileValidationException.class,
                () -> service.uploadPublicImage("business-assets", "businesses/1", png("pixel.png", ONE_PIXEL_PNG)));
        assertEquals("storage.file.dimensions_too_large", exception.getMessage());
    }

    @Test
    void reencodesImageStripsTrailingMetadataAndUsesRandomKeysAndChecksum() throws Exception {
        MinioStorageProperties properties = baseProperties();
        MinioClient client = mock(MinioClient.class);
        when(client.bucketExists(any())).thenReturn(true);
        when(client.putObject(any())).thenReturn(mock(ObjectWriteResponse.class));
        AtomicReference<byte[]> uploadedBytes = new AtomicReference<>();
        when(client.putObject(any())).thenAnswer(invocation -> {
            PutObjectArgs args = invocation.getArgument(0);
            uploadedBytes.set(args.stream().readAllBytes());
            return mock(ObjectWriteResponse.class);
        });
        MinioStorageService service = new MinioStorageService(properties, endpoint -> client);
        byte[] marker = "GPSLatitude=4.7110;EXIF".getBytes(StandardCharsets.UTF_8);
        byte[] source = new byte[ONE_PIXEL_PNG.length + marker.length];
        System.arraycopy(ONE_PIXEL_PNG, 0, source, 0, ONE_PIXEL_PNG.length);
        System.arraycopy(marker, 0, source, ONE_PIXEL_PNG.length, marker.length);

        StoredObjectResponse first = service.uploadPublicImage("business-assets", "businesses/1/logo", png("photo.png", source));
        StoredObjectResponse second = service.uploadPublicImage("business-assets", "businesses/1/logo", png("photo.png", source));

        assertFalse(new String(uploadedBytes.get(), StandardCharsets.ISO_8859_1).contains("GPSLatitude"));
        assertArrayEquals(new byte[] {(byte) 0x89, 0x50, 0x4e, 0x47},
                java.util.Arrays.copyOf(uploadedBytes.get(), 4));
        assertTrue(first.checksumSha256().matches("[0-9a-f]{64}"));
        assertNotEquals(first.objectKey(), second.objectKey());
        assertTrue(first.objectKey().matches("businesses/1/logo/[0-9a-f-]{36}\\.png"));
    }

    @Test
    void repairEvidenceIsNeverReturnedAsPublicAndResolutionUsesPresignedUrl() throws Exception {
        MinioStorageProperties properties = baseProperties();
        properties.setRepairEvidencePublic(true);
        MinioClient client = mock(MinioClient.class);
        when(client.bucketExists(any())).thenReturn(true);
        when(client.putObject(any())).thenReturn(mock(ObjectWriteResponse.class));
        when(client.getPresignedObjectUrl(any())).thenReturn("https://evidence.example.com/signed");
        MinioStorageService service = new MinioStorageService(properties, endpoint -> client);

        StoredObjectResponse response = service.uploadRepairEvidence("businesses/1/repair-orders/2", png("proof.png", ONE_PIXEL_PNG));

        assertNull(response.url());
        assertTrue(response.objectKey().endsWith(".png"));
        assertEquals("https://evidence.example.com/signed",
                service.resolveRepairEvidenceUrl(response.bucket(), response.objectKey()));
        verify(client).getPresignedObjectUrl(any());
    }

    @Test
    void rejectsWhenSafeReencodingWouldExceedOutputLimit() {
        MinioStorageProperties properties = baseProperties();
        properties.setEnabled(false);
        properties.setMaxSanitizedImageSizeBytes(1);
        MinioStorageService service = new MinioStorageService(properties);

        FileValidationException exception = assertThrows(FileValidationException.class,
                () -> service.uploadPublicImage("business-assets", "businesses/1", png("photo.png", ONE_PIXEL_PNG)));

        assertEquals("storage.file.sanitized_too_large", exception.getMessage());
    }

    @Test
    void removesNewObjectWhenDatabaseTransactionRollsBack() throws Exception {
        MinioStorageProperties properties = baseProperties();
        MinioClient client = mock(MinioClient.class);
        MinioStorageService service = new MinioStorageService(properties, endpoint -> client);
        StoredObjectResponse uploaded = new StoredObjectResponse(
                "private-evidence", "businesses/1/random.png", "signed", "proof.png",
                "image/png", 80, "a".repeat(64));
        TransactionSynchronizationManager.initSynchronization();
        try {
            service.registerRollbackCleanup(uploaded);
            assertEquals(1, TransactionSynchronizationManager.getSynchronizations().size());
            TransactionSynchronizationManager.getSynchronizations().getFirst()
                    .afterCompletion(TransactionSynchronization.STATUS_ROLLED_BACK);
        } finally {
            TransactionSynchronizationManager.clearSynchronization();
        }

        verify(client).removeObject(any());
    }

    private MinioStorageService disabledService(long maxSize) {
        MinioStorageProperties properties = baseProperties();
        properties.setEnabled(false);
        properties.setMaxImageSizeBytes(maxSize);
        return new MinioStorageService(properties);
    }

    private MinioStorageProperties baseProperties() {
        MinioStorageProperties properties = new MinioStorageProperties();
        properties.setEnabled(true);
        properties.setEndpoint("https://minio.internal.example");
        properties.setPrivateDownloadBaseUrl("https://evidence.example.com");
        properties.setPublicBaseUrl("https://assets.example.com");
        properties.setAccessKey("access-key");
        properties.setSecretKey("secret-key");
        properties.setMaxImageSizeBytes(1024 * 1024);
        properties.setMaxSanitizedImageSizeBytes(1024 * 1024);
        return properties;
    }

    private MockMultipartFile png(String name, byte[] bytes) {
        return new MockMultipartFile("file", name, "image/png", bytes);
    }
}
