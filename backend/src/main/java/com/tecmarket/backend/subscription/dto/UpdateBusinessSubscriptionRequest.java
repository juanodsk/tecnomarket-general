package com.tecmarket.backend.subscription.dto;

import com.tecmarket.backend.subscription.entity.BillingCycle;
import com.tecmarket.backend.subscription.entity.PaymentProvider;
import com.tecmarket.backend.subscription.entity.SubscriptionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Schema(description = "Datos para actualizar la suscripcion de un negocio.")
public record UpdateBusinessSubscriptionRequest(
        @NotNull @Schema(example = "2") Integer planId,
        @NotNull @Schema(example = "ACTIVE") SubscriptionStatus status,
        @NotNull @Schema(example = "MONTHLY") BillingCycle billingCycle,
        @NotNull @Schema(example = "2026-06-04T00:00:00") LocalDateTime currentPeriodStart,
        @NotNull @Schema(example = "2026-07-04T00:00:00") LocalDateTime currentPeriodEnd,
        @Schema(example = "2026-06-19T00:00:00") LocalDateTime trialEndsAt,
        @NotNull @Schema(example = "false") Boolean cancelAtPeriodEnd,
        @NotNull @Schema(example = "MANUAL") PaymentProvider paymentProvider,
        @Size(max = 150) @Schema(example = "cus_demo_001") String providerCustomerId,
        @Size(max = 150) @Schema(example = "sub_demo_001") String providerSubscriptionId,
        @NotNull @Schema(example = "true") Boolean active) {
}
