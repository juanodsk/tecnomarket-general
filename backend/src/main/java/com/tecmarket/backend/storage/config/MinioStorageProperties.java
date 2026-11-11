package com.tecmarket.backend.storage.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.storage.minio")
public class MinioStorageProperties {

    private boolean enabled;
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String publicBaseUrl;
    private String privateDownloadBaseUrl;
    private String businessAssetsBucket = "business-assets";
    private String serviceImagesBucket = "service-images";
    private String repairEvidenceBucket = "repair-order-evidence";
    private boolean repairEvidencePublic;
    private long maxImageSizeBytes = 10 * 1024 * 1024;
    private int maxImageWidth = 8000;
    private int maxImageHeight = 8000;
    private long maxImagePixels = 25_000_000;
    private long maxSanitizedImageSizeBytes = 12 * 1024 * 1024;
    private int presignedExpirationMinutes = 10;
    private int evidenceRetentionDays = 90;
    private String deletionCleanupCron = "0 */5 * * * *";
    private int deletionBatchSize = 50;
    private int deletionMaxAttempts = 10;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getPublicBaseUrl() {
        return publicBaseUrl;
    }

    public void setPublicBaseUrl(String publicBaseUrl) {
        this.publicBaseUrl = publicBaseUrl;
    }

    public String getPrivateDownloadBaseUrl() {
        return privateDownloadBaseUrl;
    }

    public void setPrivateDownloadBaseUrl(String privateDownloadBaseUrl) {
        this.privateDownloadBaseUrl = privateDownloadBaseUrl;
    }

    public String getBusinessAssetsBucket() {
        return businessAssetsBucket;
    }

    public void setBusinessAssetsBucket(String businessAssetsBucket) {
        this.businessAssetsBucket = businessAssetsBucket;
    }

    public String getServiceImagesBucket() {
        return serviceImagesBucket;
    }

    public void setServiceImagesBucket(String serviceImagesBucket) {
        this.serviceImagesBucket = serviceImagesBucket;
    }

    public String getRepairEvidenceBucket() {
        return repairEvidenceBucket;
    }

    public void setRepairEvidenceBucket(String repairEvidenceBucket) {
        this.repairEvidenceBucket = repairEvidenceBucket;
    }

    public boolean isRepairEvidencePublic() {
        return repairEvidencePublic;
    }

    public void setRepairEvidencePublic(boolean repairEvidencePublic) {
        this.repairEvidencePublic = repairEvidencePublic;
    }

    public long getMaxImageSizeBytes() {
        return maxImageSizeBytes;
    }

    public void setMaxImageSizeBytes(long maxImageSizeBytes) {
        this.maxImageSizeBytes = maxImageSizeBytes;
    }

    public int getMaxImageWidth() {
        return maxImageWidth;
    }

    public void setMaxImageWidth(int maxImageWidth) {
        this.maxImageWidth = maxImageWidth;
    }

    public int getMaxImageHeight() {
        return maxImageHeight;
    }

    public void setMaxImageHeight(int maxImageHeight) {
        this.maxImageHeight = maxImageHeight;
    }

    public long getMaxImagePixels() {
        return maxImagePixels;
    }

    public void setMaxImagePixels(long maxImagePixels) {
        this.maxImagePixels = maxImagePixels;
    }

    public long getMaxSanitizedImageSizeBytes() {
        return maxSanitizedImageSizeBytes;
    }

    public void setMaxSanitizedImageSizeBytes(long maxSanitizedImageSizeBytes) {
        this.maxSanitizedImageSizeBytes = maxSanitizedImageSizeBytes;
    }

    public int getPresignedExpirationMinutes() {
        return presignedExpirationMinutes;
    }

    public void setPresignedExpirationMinutes(int presignedExpirationMinutes) {
        this.presignedExpirationMinutes = presignedExpirationMinutes;
    }

    public int getEvidenceRetentionDays() {
        return evidenceRetentionDays;
    }

    public void setEvidenceRetentionDays(int evidenceRetentionDays) {
        this.evidenceRetentionDays = evidenceRetentionDays;
    }

    public String getDeletionCleanupCron() {
        return deletionCleanupCron;
    }

    public void setDeletionCleanupCron(String deletionCleanupCron) {
        this.deletionCleanupCron = deletionCleanupCron;
    }

    public int getDeletionBatchSize() {
        return deletionBatchSize;
    }

    public void setDeletionBatchSize(int deletionBatchSize) {
        this.deletionBatchSize = deletionBatchSize;
    }

    public int getDeletionMaxAttempts() {
        return deletionMaxAttempts;
    }

    public void setDeletionMaxAttempts(int deletionMaxAttempts) {
        this.deletionMaxAttempts = deletionMaxAttempts;
    }
}
