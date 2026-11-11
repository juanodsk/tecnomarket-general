package com.tecmarket.backend.subscription.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Uso actual de una suscripcion.")
public record SubscriptionUsageResponse(
        @Schema(example = "1") Integer id,
        @Schema(example = "1") Integer businessSubscriptionId,
        @Schema(example = "4") Integer usersCount,
        @Schema(example = "120") Integer ordersCount,
        @Schema(example = "1") Integer branchesCount,
        @Schema(description = "Almacenamiento usado en bytes.", example = "0") Long storageUsed,
        LocalDateTime updatedAt) {
}
