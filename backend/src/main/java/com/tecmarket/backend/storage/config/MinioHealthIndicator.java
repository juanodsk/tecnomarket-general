package com.tecmarket.backend.storage.config;

import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConditionalOnProperty(prefix = "app.storage.minio", name = "enabled", havingValue = "true")
public class MinioHealthIndicator implements HealthIndicator {

    private final MinioStorageProperties properties;
    private final MinioClient minioClient;

    @Autowired
    public MinioHealthIndicator(MinioStorageProperties properties) {
        this(properties, MinioClient.builder()
                .endpoint(properties.getEndpoint())
                .credentials(properties.getAccessKey(), properties.getSecretKey())
                .build());
    }

    MinioHealthIndicator(MinioStorageProperties properties, MinioClient minioClient) {
        this.properties = properties;
        this.minioClient = minioClient;
    }

    @Override
    public Health health() {
        try {
            for (String bucket : requiredBuckets()) {
                boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
                if (!exists) {
                    return Health.down().build();
                }
            }
            return Health.up().build();
        } catch (Exception exception) {
            return Health.down().build();
        }
    }

    private List<String> requiredBuckets() {
        return List.of(
                properties.getBusinessAssetsBucket(),
                properties.getServiceImagesBucket(),
                properties.getRepairEvidenceBucket());
    }
}
