package com.tecmarket.backend.storage.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StorageDeletionScheduler {

    private final StorageDeletionProcessor processor;

    public StorageDeletionScheduler(StorageDeletionProcessor processor) {
        this.processor = processor;
    }

    @Scheduled(cron = "${app.storage.minio.deletion-cleanup-cron:0 */5 * * * *}")
    public void processDueTasks() {
        processor.processDueTasks();
    }
}
