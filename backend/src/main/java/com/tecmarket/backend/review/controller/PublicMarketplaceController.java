package com.tecmarket.backend.review.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import com.tecmarket.backend.review.dto.PublicBusinessDetailResponse;
import com.tecmarket.backend.review.dto.PublicBusinessResponse;
import com.tecmarket.backend.review.dto.PublicBusinessReviewResponse;
import com.tecmarket.backend.review.service.BusinessReviewService;
import com.tecmarket.backend.common.i18n.MessageService;
import com.tecmarket.backend.common.response.ApiResponse;
import com.tecmarket.backend.common.pagination.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
@RestController
@ConditionalOnProperty(prefix = "app.features", name = "marketplace-enabled", havingValue = "true")
@Tag(name = "Marketplace", description = "Public marketplace endpoints for business discovery and reviews.")
@RequestMapping("/api/v1/public/businesses")
public class PublicMarketplaceController {

    private final BusinessReviewService businessReviewService;
    private final MessageService messageService;

    public PublicMarketplaceController(BusinessReviewService businessReviewService, MessageService messageService) {
        this.businessReviewService = businessReviewService;
        this.messageService = messageService;
    }

    @Operation(summary = "Search public businesses", description = "Searches active businesses by text, city and minimum rating.")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<PublicBusinessResponse>>> findBusinesses(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Integer cityId,
            @RequestParam(required = false) @DecimalMin("0.0") @DecimalMax("5.0") BigDecimal minRating,
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size,
            @RequestParam(defaultValue = "ratingAverage") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        PageResponse<PublicBusinessResponse> response = businessReviewService.findPublicBusinesses(
                q,
                cityId,
                minRating,
                page,
                size,
                sortBy,
                sortDir);
        return ResponseEntity.ok(ApiResponse.success(messageService.get("marketplace.business.search.success"), response));
    }

    @Operation(summary = "Find public business detail", description = "Returns public detail for one active business.")
    @GetMapping("/{businessId}")
    public ResponseEntity<ApiResponse<PublicBusinessDetailResponse>> findBusinessById(@PathVariable Integer businessId) {
        PublicBusinessDetailResponse response = businessReviewService.findPublicBusinessById(businessId);
        return ResponseEntity.ok(ApiResponse.success(messageService.get("marketplace.business.find.success"), response));
    }

    @Operation(summary = "List public business reviews", description = "Returns visible reviews for one business.")
    @GetMapping("/{businessId}/reviews")
    public ResponseEntity<ApiResponse<PageResponse<PublicBusinessReviewResponse>>> findBusinessReviews(
            @PathVariable Integer businessId,
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size) {
        PageResponse<PublicBusinessReviewResponse> response = businessReviewService.findPublicByBusiness(
                businessId, page, size);
        return ResponseEntity.ok(ApiResponse.success(messageService.get("business_review.public.list.success"), response));
    }
}
