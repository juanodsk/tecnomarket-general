package com.tecmarket.backend.storage.dto;

public record StoredObjectResponse(
        String bucket,
        String objectKey,
        String url,
        String originalFileName,
        String contentType,
        long size,
        String checksumSha256) {
}
