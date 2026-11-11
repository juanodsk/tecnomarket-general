package com.tecmarket.backend.review.controller;

import com.tecmarket.backend.review.dto.BusinessReviewResponse;
import com.tecmarket.backend.review.dto.CreateBusinessReviewRequest;
import com.tecmarket.backend.review.service.BusinessReviewService;
import com.tecmarket.backend.common.i18n.MessageService;
import com.tecmarket.backend.common.response.ApiResponse;
import com.tecmarket.backend.common.pagination.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Business Reviews", description = "Endpoints for creating and listing business reviews.")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/businesses/{businessId}/reviews")
public class BusinessReviewController {

    private final BusinessReviewService businessReviewService;
    private final MessageService messageService;

    public BusinessReviewController(BusinessReviewService businessReviewService, MessageService messageService) {
        this.businessReviewService = businessReviewService;
        this.messageService = messageService;
    }

    @Operation(summary = "Create business review", description = "Creates one review per repair order by the same customer.")
    @PreAuthorize("hasAuthority('REVIEW_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<BusinessReviewResponse>> create(
            @PathVariable Integer businessId,
            @Valid @RequestBody CreateBusinessReviewRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        BusinessReviewResponse response = businessReviewService.create(businessId, request, jwt.getSubject());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(messageService.get("business_review.create.success"), response));
    }

    @Operation(summary = "List business reviews", description = "Lists reviews for a business in internal/private context.")
    @PreAuthorize("hasAuthority('REVIEW_VIEW')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<BusinessReviewResponse>>> findByBusiness(
            @PathVariable Integer businessId,
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size,
            @AuthenticationPrincipal Jwt jwt) {
        PageResponse<BusinessReviewResponse> response = businessReviewService.findByBusiness(
                businessId, page, size, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("business_review.list.success"), response));
    }
}
