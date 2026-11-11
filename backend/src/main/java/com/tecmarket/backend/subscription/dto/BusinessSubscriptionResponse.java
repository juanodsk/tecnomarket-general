package com.tecmarket.backend.subscription.dto;

import com.tecmarket.backend.subscription.entity.BillingCycle;
import com.tecmarket.backend.subscription.entity.PaymentProvider;
import com.tecmarket.backend.subscription.entity.SubscriptionStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Suscripcion contratada por un negocio.")
public record BusinessSubscriptionResponse(
        @Schema(example = "1") Integer id,
        @Schema(example = "1") Integer businessId,
        @Schema(example = "TecMarket Centro") String businessName,
        @Schema(example = "2") Integer planId,
        @Schema(example = "PRO") String planCode,
        @Schema(example = "Pro") String planName,
        @Schema(example = "ACTIVE") SubscriptionStatus status,
        @Schema(example = "MONTHLY") BillingCycle billingCycle,
        LocalDateTime currentPeriodStart,
        LocalDateTime currentPeriodEnd,
        LocalDateTime trialEndsAt,
        @Schema(example = "false") Boolean cancelAtPeriodEnd,
        @Schema(example = "MANUAL") PaymentProvider paymentProvider,
        @Schema(example = "cus_demo_001") String providerCustomerId,
        @Schema(example = "sub_demo_001") String providerSubscriptionId,
        @Schema(example = "true") Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
