package com.tecmarket.backend.storage.service;

import com.tecmarket.backend.common.exception.FileValidationException;
import com.tecmarket.backend.common.exception.StorageException;
import com.tecmarket.backend.storage.config.MinioStorageProperties;
import com.tecmarket.backend.storage.dto.StoredObjectResponse;
import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.Http;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
public class MinioStorageService {

    private static final Logger log = LoggerFactory.getLogger(MinioStorageService.class);
    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of("image/jpeg", "image/png", "image/webp");
    private static final Map<String, Set<String>> ALLOWED_EXTENSIONS = Map.of(
            "image/jpeg", Set.of("jpg", "jpeg"),
            "image/png", Set.of("png"),
            "image/webp", Set.of("webp"));
    private static final String SANITIZED_CONTENT_TYPE = "image/png";

    private final MinioStorageProperties properties;
    private final Function<String, MinioClient> clientFactory;

    @Autowired
    public MinioStorageService(MinioStorageProperties properties) {
        this(properties, endpoint -> MinioClient.builder().endpoint(endpoint)
                .credentials(properties.getAccessKey(), properties.getSecretKey()).build());
    }

    MinioStorageService(MinioStorageProperties properties, Function<String, MinioClient> clientFactory) {
        this.properties = properties;
        this.clientFactory = clientFactory;
    }

    public StoredObjectResponse uploadPublicImage(String bucket, String objectPrefix, MultipartFile file) {
        return uploadImage(bucket, randomObjectKey(objectPrefix), sanitizeImage(file), true);
    }

    public StoredObjectResponse uploadRepairEvidence(String objectPrefix, MultipartFile file) {
        return uploadImage(
                properties.getRepairEvidenceBucket(),
                randomObjectKey(objectPrefix),
                sanitizeImage(file),
                false);
    }

    public String resolveRepairEvidenceUrl(String bucket, String objectKey) {
        if (!StringUtils.hasText(bucket) || !StringUtils.hasText(objectKey)) {
            throw new StorageException("storage.evidence.metadata_missing");
        }
        return presignedUrl(bucket, objectKey);
    }

    public void registerRollbackCleanup(StoredObjectResponse uploaded) {
        if (uploaded == null || !TransactionSynchronizationManager.isSynchronizationActive()) {
            return;
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                if (status != STATUS_ROLLED_BACK) {
                    return;
                }
                try {
                    delete(uploaded.bucket(), uploaded.objectKey());
                } catch (RuntimeException exception) {
                    log.error("Unable to remove orphaned object after transaction rollback: bucket={}, key={}",
                            uploaded.bucket(), uploaded.objectKey(), exception);
                }
            }
        });
    }

    public void delete(String bucket, String objectKey) {
        if (!StringUtils.hasText(bucket) || !StringUtils.hasText(objectKey)) {
            return;
        }
        try {
            client(properties.getEndpoint()).removeObject(RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectKey)
                    .build());
        } catch (Exception exception) {
            throw new StorageException("storage.delete.failed", exception);
        }
    }

    private StoredObjectResponse uploadImage(String bucket, String objectKey, SanitizedImage image, boolean publicAccess) {
        MinioClient minio = client(properties.getEndpoint());
        assertBucketExists(minio, bucket);
        String url = publicAccess ? publicUrl(bucket, objectKey) : null;
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(image.bytes())) {
            Map<String, String> headers = publicAccess
                    ? Map.of("Cache-Control", "public, max-age=31536000, immutable", "Content-Disposition", "inline")
                    : Map.of("Cache-Control", "no-store", "Content-Disposition", "inline");
            minio.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectKey)
                    .stream(inputStream, (long) image.bytes().length, -1L)
                    .contentType(SANITIZED_CONTENT_TYPE)
                    .headers(headers)
                    .build());
            return new StoredObjectResponse(
                    bucket, objectKey, url, image.originalFileName(), SANITIZED_CONTENT_TYPE,
                    image.bytes().length, sha256(image.bytes()));
        } catch (Exception exception) {
            throw new StorageException("storage.upload.failed", exception);
        }
    }

    private SanitizedImage sanitizeImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileValidationException("storage.file.required");
        }
        if (file.getSize() > properties.getMaxImageSizeBytes()) {
            throw new FileValidationException("storage.file.too_large");
        }
        String declaredType = normalizeContentType(file.getContentType());
        if (!ALLOWED_IMAGE_TYPES.contains(declaredType)) {
            throw new FileValidationException("storage.file.invalid_type");
        }

        try {
            byte[] source = file.getBytes();
            String detectedType = detectContentType(source);
            if (!declaredType.equals(detectedType)) {
                throw new FileValidationException("storage.file.content_type_mismatch");
            }
            String originalFileName = sanitizeAndValidateFileName(file.getOriginalFilename(), detectedType);
            BufferedImage decoded = decodeWithinLimits(source);
            ByteArrayOutputStream sanitized = new ByteArrayOutputStream();
            if (!ImageIO.write(decoded, "png", sanitized)) {
                throw new FileValidationException("storage.file.invalid_image");
            }
            byte[] normalized = sanitized.toByteArray();
            if (normalized.length > properties.getMaxSanitizedImageSizeBytes()) {
                throw new FileValidationException("storage.file.sanitized_too_large");
            }
            return new SanitizedImage(normalized, originalFileName);
        } catch (FileValidationException exception) {
            throw exception;
        } catch (IOException exception) {
            throw new FileValidationException("storage.file.invalid_image");
        }
    }

    private BufferedImage decodeWithinLimits(byte[] source) throws IOException {
        try (ImageInputStream imageInput = ImageIO.createImageInputStream(new ByteArrayInputStream(source))) {
            if (imageInput == null) {
                throw new FileValidationException("storage.file.invalid_image");
            }
            Iterator<ImageReader> readers = ImageIO.getImageReaders(imageInput);
            if (!readers.hasNext()) {
                throw new FileValidationException("storage.file.invalid_image");
            }
            ImageReader reader = readers.next();
            try {
                reader.setInput(imageInput, false, true);
                int imageCount;
                try {
                    imageCount = reader.getNumImages(true);
                } catch (UnsupportedOperationException exception) {
                    imageCount = 1;
                }
                if (imageCount != 1) {
                    throw new FileValidationException("storage.file.animated_not_allowed");
                }
                int width = reader.getWidth(0);
                int height = reader.getHeight(0);
                long pixels = Math.multiplyExact((long) width, (long) height);
                if (width <= 0 || height <= 0
                        || width > properties.getMaxImageWidth()
                        || height > properties.getMaxImageHeight()
                        || pixels > properties.getMaxImagePixels()) {
                    throw new FileValidationException("storage.file.dimensions_too_large");
                }
                BufferedImage decoded = reader.read(0);
                if (decoded == null) {
                    throw new FileValidationException("storage.file.invalid_image");
                }
                return decoded;
            } finally {
                reader.dispose();
            }
        } catch (ArithmeticException exception) {
            throw new FileValidationException("storage.file.dimensions_too_large");
        }
    }

    private String detectContentType(byte[] source) {
        if (source.length >= 8
                && (source[0] & 0xff) == 0x89 && source[1] == 0x50 && source[2] == 0x4e && source[3] == 0x47
                && source[4] == 0x0d && source[5] == 0x0a && source[6] == 0x1a && source[7] == 0x0a) {
            return "image/png";
        }
        if (source.length >= 3
                && (source[0] & 0xff) == 0xff && (source[1] & 0xff) == 0xd8 && (source[2] & 0xff) == 0xff) {
            return "image/jpeg";
        }
        if (source.length >= 16
                && source[0] == 'R' && source[1] == 'I' && source[2] == 'F' && source[3] == 'F'
                && source[8] == 'W' && source[9] == 'E' && source[10] == 'B' && source[11] == 'P'
                && source[12] == 'V' && source[13] == 'P' && source[14] == '8'
                && (source[15] == ' ' || source[15] == 'L' || source[15] == 'X')) {
            return "image/webp";
        }
        throw new FileValidationException("storage.file.invalid_type");
    }

    private String sanitizeAndValidateFileName(String originalFileName, String detectedType) {
        if (!StringUtils.hasText(originalFileName)) {
            throw new FileValidationException("storage.file.name_required");
        }
        String normalized = originalFileName.replace('\\', '/');
        normalized = normalized.substring(normalized.lastIndexOf('/') + 1)
                .replaceAll("[\\p{Cntrl}]", "").trim();
        if (normalized.isEmpty() || normalized.length() > 255) {
            throw new FileValidationException("storage.file.invalid_name");
        }
        String[] segments = normalized.split("\\.");
        if (segments.length != 2 || segments[0].isBlank()) {
            throw new FileValidationException("storage.file.double_extension");
        }
        String extension = segments[1].toLowerCase(Locale.ROOT);
        if (!ALLOWED_EXTENSIONS.get(detectedType).contains(extension)) {
            throw new FileValidationException("storage.file.extension_mismatch");
        }
        return normalized;
    }

    private void assertBucketExists(MinioClient minio, String bucket) {
        if (!StringUtils.hasText(bucket)) {
            throw new StorageException("storage.bucket.not_configured");
        }
        try {
            if (!minio.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
                throw new StorageException("storage.bucket.not_found");
            }
        } catch (StorageException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new StorageException("storage.connection.failed", exception);
        }
    }

    private String randomObjectKey(String objectPrefix) {
        String prefix = objectPrefix == null ? "" : objectPrefix.replace('\\', '/').replaceAll("^/+|/+$", "");
        if (!StringUtils.hasText(prefix) || prefix.contains("..")) {
            throw new StorageException("storage.object_prefix.invalid");
        }
        return prefix + "/" + UUID.randomUUID() + ".png";
    }

    private String publicUrl(String bucket, String objectKey) {
        String baseUrl = StringUtils.hasText(properties.getPublicBaseUrl())
                ? properties.getPublicBaseUrl() : properties.getEndpoint();
        if (!StringUtils.hasText(baseUrl)) {
            throw new StorageException("storage.public_url.not_configured");
        }
        return stripTrailingSlash(baseUrl) + "/" + bucket + "/" + objectKey;
    }

    private String presignedUrl(String bucket, String objectKey) {
        String signingEndpoint = StringUtils.hasText(properties.getPrivateDownloadBaseUrl())
                ? properties.getPrivateDownloadBaseUrl() : properties.getEndpoint();
        try {
            return client(signingEndpoint).getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Http.Method.GET)
                    .bucket(bucket)
                    .object(objectKey)
                    .expiry(properties.getPresignedExpirationMinutes(), TimeUnit.MINUTES)
                    .extraQueryParams(Map.of(
                            "response-cache-control", "no-store",
                            "response-content-type", SANITIZED_CONTENT_TYPE,
                            "response-content-disposition", "inline"))
                    .build());
        } catch (Exception exception) {
            throw new StorageException("storage.url.failed", exception);
        }
    }

    private MinioClient client(String endpoint) {
        if (!properties.isEnabled()) {
            throw new StorageException("storage.disabled");
        }
        if (!StringUtils.hasText(endpoint)
                || !StringUtils.hasText(properties.getAccessKey())
                || !StringUtils.hasText(properties.getSecretKey())) {
            throw new StorageException("storage.credentials.not_configured");
        }
        return clientFactory.apply(endpoint);
    }

    private String normalizeContentType(String contentType) {
        return contentType == null ? "" : contentType.split(";", 2)[0].trim().toLowerCase(Locale.ROOT);
    }

    private String sha256(byte[] bytes) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available", exception);
        }
    }

    private String stripTrailingSlash(String value) {
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }

    private record SanitizedImage(byte[] bytes, String originalFileName) {
    }
}
