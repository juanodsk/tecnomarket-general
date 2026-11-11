package com.tecmarket.backend.storage.config;

import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Status;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MinioHealthIndicatorTest {

    @Test
    void reportsUpOnlyWhenEveryRequiredBucketIsReachable() throws Exception {
        MinioClient client = mock(MinioClient.class);
        when(client.bucketExists(any(BucketExistsArgs.class))).thenReturn(true);

        var indicator = new MinioHealthIndicator(properties(), client);

        assertThat(indicator.health().getStatus()).isEqualTo(Status.UP);
    }

    @Test
    void reportsDownWithoutLeakingStorageExceptionDetails() throws Exception {
        MinioClient client = mock(MinioClient.class);
        when(client.bucketExists(any(BucketExistsArgs.class))).thenThrow(new IllegalStateException("secret endpoint"));

        var health = new MinioHealthIndicator(properties(), client).health();

        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
        assertThat(health.getDetails()).isEmpty();
    }

    private MinioStorageProperties properties() {
        MinioStorageProperties properties = new MinioStorageProperties();
        properties.setBusinessAssetsBucket("assets");
        properties.setServiceImagesBucket("services");
        properties.setRepairEvidenceBucket("evidence");
        return properties;
    }
}
