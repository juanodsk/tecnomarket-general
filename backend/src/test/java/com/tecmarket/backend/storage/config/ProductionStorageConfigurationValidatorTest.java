package com.tecmarket.backend.storage.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductionStorageConfigurationValidatorTest {

    @Test
    void rejectsPublicEvidenceConfiguration() {
        MinioStorageProperties properties = validProperties();
        properties.setRepairEvidencePublic(true);

        assertThatThrownBy(() -> new ProductionStorageConfigurationValidator(properties).validate())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("cannot be public");
    }

    @Test
    void rejectsSharedOriginForPublicAndPrivateObjects() {
        MinioStorageProperties properties = validProperties();
        properties.setPrivateDownloadBaseUrl(properties.getPublicBaseUrl());

        assertThatThrownBy(() -> new ProductionStorageConfigurationValidator(properties).validate())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("different origins");
    }

    @Test
    void rejectsLongLivedEvidenceUrls() {
        MinioStorageProperties properties = validProperties();
        properties.setPresignedExpirationMinutes(30);

        assertThatThrownBy(() -> new ProductionStorageConfigurationValidator(properties).validate())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("1 to 15 minutes");
    }

    @Test
    void rejectsExampleStorageCredentials() {
        MinioStorageProperties properties = validProperties();
        properties.setSecretKey("replace-with-minio-secret-key");

        assertThatThrownBy(() -> new ProductionStorageConfigurationValidator(properties).validate())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("example value");
    }

    private MinioStorageProperties validProperties() {
        MinioStorageProperties properties = new MinioStorageProperties();
        properties.setEnabled(true);
        properties.setEndpoint("http://minio:9000");
        properties.setAccessKey("real-access-key");
        properties.setSecretKey("real-minio-secret-key-123");
        properties.setPublicBaseUrl("https://assets.example.com");
        properties.setPrivateDownloadBaseUrl("https://evidence.example.com");
        return properties;
    }
}
