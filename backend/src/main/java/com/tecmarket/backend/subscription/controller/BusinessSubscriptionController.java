package com.tecmarket.backend.subscription.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import com.tecmarket.backend.common.i18n.MessageService;
import com.tecmarket.backend.common.response.ApiResponse;
import com.tecmarket.backend.subscription.dto.BusinessSubscriptionResponse;
import com.tecmarket.backend.subscription.dto.CreateBusinessSubscriptionRequest;
import com.tecmarket.backend.subscription.dto.SubscriptionUsageResponse;
import com.tecmarket.backend.subscription.dto.UpdateBusinessSubscriptionRequest;
import com.tecmarket.backend.subscription.service.BusinessSubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@ConditionalOnProperty(prefix = "app.features", name = "subscriptions-enabled", havingValue = "true")
@Tag(name = "Business Subscriptions", description = "Endpoints for managing subscriptions assigned to businesses.")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1")
public class BusinessSubscriptionController {

    private final BusinessSubscriptionService businessSubscriptionService;
    private final MessageService messageService;

    public BusinessSubscriptionController(
            BusinessSubscriptionService businessSubscriptionService,
            MessageService messageService) {
        this.businessSubscriptionService = businessSubscriptionService;
        this.messageService = messageService;
    }

    @Operation(summary = "List business subscriptions", description = "Returns all business subscriptions.")
    @PreAuthorize("hasAuthority('SUBSCRIPTION_VIEW')")
    @GetMapping("/business-subscriptions")
    public ResponseEntity<ApiResponse<List<BusinessSubscriptionResponse>>> findAll(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(ApiResponse.success(
                messageService.get("business_subscription.list.success"),
                businessSubscriptionService.findAll(jwt.getSubject())));
    }

    @Operation(summary = "Find business subscription by ID", description = "Returns one business subscription.")
    @PreAuthorize("hasAuthority('SUBSCRIPTION_VIEW')")
    @GetMapping("/business-subscriptions/{id}")
    public ResponseEntity<ApiResponse<BusinessSubscriptionResponse>> findById(
            @PathVariable Integer id,
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(ApiResponse.success(
                messageService.get("business_subscription.find.success"),
                businessSubscriptionService.findById(id, jwt.getSubject())));
    }

    @Operation(summary = "Find active business subscription", description = "Returns the active subscription for a business.")
    @PreAuthorize("hasAuthority('SUBSCRIPTION_VIEW')")
    @GetMapping("/businesses/{businessId}/subscription")
    public ResponseEntity<ApiResponse<BusinessSubscriptionResponse>> findActiveByBusiness(
            @PathVariable Integer businessId,
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(ApiResponse.success(
                messageService.get("business_subscription.find.success"),
                businessSubscriptionService.findActiveByBusiness(businessId, jwt.getSubject())));
    }

    @Operation(summary = "Create business subscription", description = "Assigns a SaaS subscription to a business.")
    @PreAuthorize("hasAuthority('SUBSCRIPTION_MANAGE')")
    @PostMapping("/businesses/{businessId}/subscription")
    public ResponseEntity<ApiResponse<BusinessSubscriptionResponse>> create(
            @PathVariable Integer businessId,
            @Valid @RequestBody CreateBusinessSubscriptionRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        messageService.get("business_subscription.create.success"),
                        businessSubscriptionService.create(businessId, request, jwt.getSubject())));
    }

    @Operation(summary = "Update business subscription", description = "Updates a business subscription.")
    @PreAuthorize("hasAuthority('SUBSCRIPTION_MANAGE')")
    @PutMapping("/business-subscriptions/{id}")
    public ResponseEntity<ApiResponse<BusinessSubscriptionResponse>> update(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateBusinessSubscriptionRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(ApiResponse.success(
                messageService.get("business_subscription.update.success"),
                businessSubscriptionService.update(id, request, jwt.getSubject())));
    }

    @Operation(summary = "Cancel business subscription", description = "Cancels and soft-deletes a business subscription.")
    @PreAuthorize("hasAuthority('SUBSCRIPTION_MANAGE')")
    @DeleteMapping("/business-subscriptions/{id}")
    public ResponseEntity<ApiResponse<Void>> cancel(
            @PathVariable Integer id,
            @AuthenticationPrincipal Jwt jwt) {
        businessSubscriptionService.cancel(id, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("business_subscription.cancel.success"), null));
    }

    @Operation(summary = "Find subscription usage", description = "Returns usage counters for a business subscription.")
    @PreAuthorize("hasAuthority('SUBSCRIPTION_VIEW')")
    @GetMapping("/business-subscriptions/{id}/usage")
    public ResponseEntity<ApiResponse<SubscriptionUsageResponse>> findUsage(
            @PathVariable Integer id,
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(ApiResponse.success(
                messageService.get("subscription_usage.find.success"),
                businessSubscriptionService.findUsage(id, jwt.getSubject())));
    }

    @Operation(summary = "Recalculate subscription usage", description = "Recalculates users/orders usage for a business subscription.")
    @PreAuthorize("hasAuthority('SUBSCRIPTION_MANAGE')")
    @PostMapping("/business-subscriptions/{id}/usage/recalculate")
    public ResponseEntity<ApiResponse<SubscriptionUsageResponse>> recalculateUsage(
            @PathVariable Integer id,
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(ApiResponse.success(
                messageService.get("subscription_usage.recalculate.success"),
                businessSubscriptionService.recalculateUsage(id, jwt.getSubject())));
    }
}
