package com.tecmarket.backend.storage.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Component
@Profile("prod")
public class ProductionStorageConfigurationValidator {

    private final MinioStorageProperties properties;

    public ProductionStorageConfigurationValidator(MinioStorageProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    void validate() {
        if (!properties.isEnabled()) {
            throw new IllegalStateException("MinIO must be enabled in production");
        }
        requireStorageEndpoint(properties.getEndpoint());
        requireSecret(properties.getAccessKey(), "app.storage.minio.access-key", 8);
        requireSecret(properties.getSecretKey(), "app.storage.minio.secret-key", 16);
        URI publicOrigin = requireHttpsOrigin(properties.getPublicBaseUrl(), "app.storage.minio.public-base-url");
        URI privateOrigin = requireHttpsOrigin(
                properties.getPrivateDownloadBaseUrl(),
                "app.storage.minio.private-download-base-url");
        if (origin(publicOrigin).equals(origin(privateOrigin))) {
            throw new IllegalStateException("Public assets and private evidence must use different origins");
        }
        List<String> buckets = List.of(
                requireText(properties.getBusinessAssetsBucket(), "business-assets-bucket"),
                requireText(properties.getServiceImagesBucket(), "service-images-bucket"),
                requireText(properties.getRepairEvidenceBucket(), "repair-evidence-bucket"));
        Set<String> uniqueBuckets = new HashSet<>(buckets);
        if (uniqueBuckets.size() != buckets.size()) {
            throw new IllegalStateException("Storage buckets must be different");
        }
        if (properties.isRepairEvidencePublic()) {
            throw new IllegalStateException("Repair evidence bucket cannot be public");
        }
        if (properties.getPresignedExpirationMinutes() < 1 || properties.getPresignedExpirationMinutes() > 15) {
            throw new IllegalStateException("Private evidence URLs must expire in 1 to 15 minutes");
        }
        if (properties.getMaxImageSizeBytes() <= 0 || properties.getMaxImageSizeBytes() > 10L * 1024 * 1024) {
            throw new IllegalStateException("Input images cannot exceed 10 MB");
        }
        if (properties.getMaxImageWidth() <= 0 || properties.getMaxImageHeight() <= 0
                || properties.getMaxImagePixels() <= 0 || properties.getMaxSanitizedImageSizeBytes() <= 0) {
            throw new IllegalStateException("Image limits must be positive");
        }
        if (properties.getEvidenceRetentionDays() < 1
                || properties.getDeletionBatchSize() < 1
                || properties.getDeletionMaxAttempts() < 1) {
            throw new IllegalStateException("Storage retention and cleanup limits must be positive");
        }
    }

    private String requireText(String value, String propertyName) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalStateException(propertyName + " is required in production");
        }
        return value.trim();
    }

    private URI requireHttpsOrigin(String value, String propertyName) {
        try {
            URI uri = URI.create(requireText(value, propertyName));
            if (!"https".equalsIgnoreCase(uri.getScheme()) || !StringUtils.hasText(uri.getHost())) {
                throw new IllegalStateException(propertyName + " must be an absolute HTTPS URL");
            }
            return uri;
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException(propertyName + " must be an absolute HTTPS URL", exception);
        }
    }

    private void requireStorageEndpoint(String value) {
        try {
            URI uri = URI.create(requireText(value, "app.storage.minio.endpoint"));
            boolean validScheme = "http".equalsIgnoreCase(uri.getScheme()) || "https".equalsIgnoreCase(uri.getScheme());
            if (!validScheme || !StringUtils.hasText(uri.getHost()) || uri.getUserInfo() != null
                    || uri.getQuery() != null || uri.getFragment() != null) {
                throw new IllegalStateException("app.storage.minio.endpoint must be an absolute HTTP(S) URL");
            }
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException("app.storage.minio.endpoint must be an absolute HTTP(S) URL", exception);
        }
    }

    private void requireSecret(String value, String propertyName, int minimumBytes) {
        String secret = requireText(value, propertyName);
        String normalized = secret.toLowerCase(Locale.ROOT);
        if (secret.getBytes(StandardCharsets.UTF_8).length < minimumBytes
                || normalized.contains("replace-with")
                || normalized.contains("reemplaza-con")
                || normalized.contains("change-me")) {
            throw new IllegalStateException(propertyName + " is missing, weak, or still contains an example value");
        }
    }

    private String origin(URI uri) {
        int port = uri.getPort();
        return uri.getScheme().toLowerCase(Locale.ROOT) + "://" + uri.getHost().toLowerCase(Locale.ROOT)
                + (port == -1 ? "" : ":" + port);
    }
}
