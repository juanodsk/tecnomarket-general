package com.tecmarket.backend.storage.service;

import com.tecmarket.backend.storage.config.MinioStorageProperties;
import com.tecmarket.backend.storage.entity.StorageDeletionStatus;
import com.tecmarket.backend.storage.entity.StorageObjectDeletionTask;
import com.tecmarket.backend.storage.repository.StorageObjectDeletionTaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StorageDeletionProcessor {

    private static final Logger log = LoggerFactory.getLogger(StorageDeletionProcessor.class);

    private final StorageObjectDeletionTaskRepository repository;
    private final MinioStorageService minioStorageService;
    private final MinioStorageProperties properties;

    public StorageDeletionProcessor(
            StorageObjectDeletionTaskRepository repository,
            MinioStorageService minioStorageService,
            MinioStorageProperties properties) {
        this.repository = repository;
        this.minioStorageService = minioStorageService;
        this.properties = properties;
    }

    @Transactional
    public int processDueTasks() {
        if (!properties.isEnabled()) {
            return 0;
        }
        List<StorageObjectDeletionTask> tasks = repository.findDueForUpdate(
                StorageDeletionStatus.PENDING,
                LocalDateTime.now(),
                PageRequest.of(0, properties.getDeletionBatchSize()));
        for (StorageObjectDeletionTask task : tasks) {
            process(task);
        }
        return tasks.size();
    }

    private void process(StorageObjectDeletionTask task) {
        try {
            minioStorageService.delete(task.getBucketName(), task.getObjectKey());
            task.setStatus(StorageDeletionStatus.COMPLETED);
            task.setCompletedAt(LocalDateTime.now());
            task.setLastError(null);
        } catch (RuntimeException exception) {
            int attempts = task.getAttemptCount() + 1;
            task.setAttemptCount(attempts);
            task.setLastError(truncate(exception.getMessage(), 1000));
            if (attempts >= properties.getDeletionMaxAttempts()) {
                task.setStatus(StorageDeletionStatus.FAILED);
                log.error("Storage object deletion permanently failed: taskId={}, bucket={}, key={}",
                        task.getId(), task.getBucketName(), task.getObjectKey(), exception);
            } else {
                long backoffMinutes = Math.min(360, 1L << Math.min(attempts, 8));
                task.setScheduledAt(LocalDateTime.now().plusMinutes(backoffMinutes));
                log.warn("Storage object deletion will retry: taskId={}, attempt={}", task.getId(), attempts);
            }
        }
    }

    private String truncate(String value, int maxLength) {
        if (value == null) {
            return "storage.delete.failed";
        }
        return value.length() <= maxLength ? value : value.substring(0, maxLength);
    }
}
