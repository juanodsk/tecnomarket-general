package com.tecmarket.backend.subscription.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import com.tecmarket.backend.common.i18n.MessageService;
import com.tecmarket.backend.common.response.ApiResponse;
import com.tecmarket.backend.subscription.dto.CreateSubscriptionPlanRequest;
import com.tecmarket.backend.subscription.dto.SubscriptionPlanResponse;
import com.tecmarket.backend.subscription.dto.UpdateSubscriptionPlanRequest;
import com.tecmarket.backend.subscription.service.SubscriptionPlanService;
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
@Tag(name = "Subscription Plans", description = "Endpoints for managing SaaS subscription plans.")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/subscription-plans")
public class SubscriptionPlanController {

    private final SubscriptionPlanService subscriptionPlanService;
    private final MessageService messageService;

    public SubscriptionPlanController(SubscriptionPlanService subscriptionPlanService, MessageService messageService) {
        this.subscriptionPlanService = subscriptionPlanService;
        this.messageService = messageService;
    }

    @Operation(summary = "List subscription plans", description = "Returns active and inactive non-deleted SaaS plans.")
    @PreAuthorize("hasAuthority('SUBSCRIPTION_VIEW')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<SubscriptionPlanResponse>>> findAll(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(ApiResponse.success(
                messageService.get("subscription_plan.list.success"),
                subscriptionPlanService.findAll(jwt.getSubject())));
    }

    @Operation(summary = "Find subscription plan by ID", description = "Returns one SaaS plan by ID.")
    @PreAuthorize("hasAuthority('SUBSCRIPTION_VIEW')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SubscriptionPlanResponse>> findById(
            @PathVariable Integer id,
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(ApiResponse.success(
                messageService.get("subscription_plan.find.success"),
                subscriptionPlanService.findById(id, jwt.getSubject())));
    }

    @Operation(summary = "Create subscription plan", description = "Creates a SaaS plan.")
    @PreAuthorize("hasAuthority('SUBSCRIPTION_MANAGE')")
    @PostMapping
    public ResponseEntity<ApiResponse<SubscriptionPlanResponse>> create(
            @Valid @RequestBody CreateSubscriptionPlanRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        messageService.get("subscription_plan.create.success"),
                        subscriptionPlanService.create(request, jwt.getSubject())));
    }

    @Operation(summary = "Update subscription plan", description = "Updates a SaaS plan.")
    @PreAuthorize("hasAuthority('SUBSCRIPTION_MANAGE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SubscriptionPlanResponse>> update(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateSubscriptionPlanRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(ApiResponse.success(
                messageService.get("subscription_plan.update.success"),
                subscriptionPlanService.update(id, request, jwt.getSubject())));
    }

    @Operation(summary = "Disable subscription plan", description = "Soft-deletes a SaaS plan.")
    @PreAuthorize("hasAuthority('SUBSCRIPTION_MANAGE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Integer id,
            @AuthenticationPrincipal Jwt jwt) {
        subscriptionPlanService.delete(id, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("subscription_plan.delete.success"), null));
    }
}
