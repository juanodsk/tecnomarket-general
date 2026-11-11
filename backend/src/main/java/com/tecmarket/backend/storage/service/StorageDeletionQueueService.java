package com.tecmarket.backend.storage.service;

import com.tecmarket.backend.storage.entity.StorageDeletionStatus;
import com.tecmarket.backend.storage.entity.StorageObjectDeletionTask;
import com.tecmarket.backend.storage.repository.StorageObjectDeletionTaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
public class StorageDeletionQueueService {

    private final StorageObjectDeletionTaskRepository repository;

    public StorageDeletionQueueService(StorageObjectDeletionTaskRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void enqueue(
            String bucket,
            String objectKey,
            String reason,
            String sourceType,
            Integer sourceId,
            LocalDateTime scheduledAt) {
        if (!StringUtils.hasText(bucket) || !StringUtils.hasText(objectKey)) {
            return;
        }
        if (repository.existsByBucketNameAndObjectKeyAndStatus(bucket, objectKey, StorageDeletionStatus.PENDING)) {
            return;
        }
        StorageObjectDeletionTask task = new StorageObjectDeletionTask();
        task.setBucketName(bucket);
        task.setObjectKey(objectKey);
        task.setReason(requireText(reason, "reason"));
        task.setSourceType(requireText(sourceType, "sourceType"));
        task.setSourceId(sourceId);
        task.setScheduledAt(scheduledAt != null ? scheduledAt : LocalDateTime.now());
        repository.save(task);
    }

    private String requireText(String value, String field) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException(field + " is required");
        }
        return value.trim();
    }
}
