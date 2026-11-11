package com.tecmarket.backend.storage.repository;

import com.tecmarket.backend.storage.entity.StorageDeletionStatus;
import com.tecmarket.backend.storage.entity.StorageObjectDeletionTask;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface StorageObjectDeletionTaskRepository extends JpaRepository<StorageObjectDeletionTask, Integer> {

    boolean existsByBucketNameAndObjectKeyAndStatus(String bucketName, String objectKey, StorageDeletionStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select task from StorageObjectDeletionTask task
            where task.status = :status and task.scheduledAt <= :now
            order by task.scheduledAt asc, task.id asc
            """)
    List<StorageObjectDeletionTask> findDueForUpdate(
            @Param("status") StorageDeletionStatus status,
            @Param("now") LocalDateTime now,
            Pageable pageable);
}
