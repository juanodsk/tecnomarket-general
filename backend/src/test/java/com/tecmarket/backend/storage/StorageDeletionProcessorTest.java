package com.tecmarket.backend.storage;

import com.tecmarket.backend.common.exception.StorageException;
import com.tecmarket.backend.storage.config.MinioStorageProperties;
import com.tecmarket.backend.storage.entity.StorageDeletionStatus;
import com.tecmarket.backend.storage.entity.StorageObjectDeletionTask;
import com.tecmarket.backend.storage.repository.StorageObjectDeletionTaskRepository;
import com.tecmarket.backend.storage.service.MinioStorageService;
import com.tecmarket.backend.storage.service.StorageDeletionProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StorageDeletionProcessorTest {

    @Mock private StorageObjectDeletionTaskRepository repository;
    @Mock private MinioStorageService minioStorageService;

    private MinioStorageProperties properties;
    private StorageDeletionProcessor processor;

    @BeforeEach
    void setUp() {
        properties = new MinioStorageProperties();
        properties.setEnabled(true);
        properties.setDeletionBatchSize(25);
        properties.setDeletionMaxAttempts(3);
        processor = new StorageDeletionProcessor(repository, minioStorageService, properties);
    }

    @Test
    void marksObjectAsCompletedOnlyAfterMinioDeletionSucceeds() {
        StorageObjectDeletionTask task = task();
        when(repository.findDueForUpdate(eq(StorageDeletionStatus.PENDING), any(), any()))
                .thenReturn(List.of(task));

        int processed = processor.processDueTasks();

        assertThat(processed).isEqualTo(1);
        assertThat(task.getStatus()).isEqualTo(StorageDeletionStatus.COMPLETED);
        assertThat(task.getCompletedAt()).isNotNull();
        verify(minioStorageService).delete("private-evidence", "businesses/1/proof.png");
    }

    @Test
    void retriesWithBackoffAndPreservesTaskWhenMinioFails() {
        StorageObjectDeletionTask task = task();
        LocalDateTime originalSchedule = task.getScheduledAt();
        when(repository.findDueForUpdate(eq(StorageDeletionStatus.PENDING), any(), any()))
                .thenReturn(List.of(task));
        doThrow(new StorageException("storage.delete.failed"))
                .when(minioStorageService).delete(task.getBucketName(), task.getObjectKey());

        processor.processDueTasks();

        assertThat(task.getStatus()).isEqualTo(StorageDeletionStatus.PENDING);
        assertThat(task.getAttemptCount()).isEqualTo(1);
        assertThat(task.getScheduledAt()).isAfter(originalSchedule);
        assertThat(task.getLastError()).isEqualTo("storage.delete.failed");
    }

    private StorageObjectDeletionTask task() {
        StorageObjectDeletionTask task = new StorageObjectDeletionTask();
        task.setId(7);
        task.setBucketName("private-evidence");
        task.setObjectKey("businesses/1/proof.png");
        task.setScheduledAt(LocalDateTime.now().minusMinutes(1));
        task.setStatus(StorageDeletionStatus.PENDING);
        task.setAttemptCount(0);
        return task;
    }
}
