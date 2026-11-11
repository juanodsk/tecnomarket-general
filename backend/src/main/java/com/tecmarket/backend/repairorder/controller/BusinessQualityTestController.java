package com.tecmarket.backend.repairorder.controller;

import com.tecmarket.backend.repairorder.dto.BusinessQualityTestResponse;
import com.tecmarket.backend.repairorder.dto.CreateBusinessQualityTestRequest;
import com.tecmarket.backend.repairorder.dto.UpdateBusinessQualityTestRequest;
import com.tecmarket.backend.repairorder.service.BusinessQualityTestService;
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
@Tag(name = "Business Quality Tests", description = "Endpoints for managing configurable quality tests by business.")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/businesses/{businessId}/quality-tests")
public class BusinessQualityTestController {

    private final BusinessQualityTestService businessQualityTestService;
    private final MessageService messageService;

    public BusinessQualityTestController(BusinessQualityTestService businessQualityTestService,
            MessageService messageService) {
        this.businessQualityTestService = businessQualityTestService;
        this.messageService = messageService;
    }

    @Operation(summary = "List business quality tests", description = "Returns configurable quality tests for a business.")
    @PreAuthorize("hasAuthority('BUSINESS_QUALITY_TEST_VIEW')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<BusinessQualityTestResponse>>> findAll(
            @PathVariable Integer businessId,
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size,
            @AuthenticationPrincipal Jwt jwt) {
        PageResponse<BusinessQualityTestResponse> response = businessQualityTestService.findAllByBusiness(
                businessId,
                page,
                size,
                jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("business_quality_test.list.success"), response));
    }

    @Operation(summary = "Find business quality test by id", description = "Returns one quality test by id.")
    @PreAuthorize("hasAuthority('BUSINESS_QUALITY_TEST_VIEW')")
    @GetMapping("/{qualityTestId}")
    public ResponseEntity<ApiResponse<BusinessQualityTestResponse>> findById(
            @PathVariable Integer businessId,
            @PathVariable Integer qualityTestId,
            @AuthenticationPrincipal Jwt jwt) {
        BusinessQualityTestResponse response = businessQualityTestService.findById(
                businessId,
                qualityTestId,
                jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("business_quality_test.find.success"), response));
    }

    @Operation(summary = "Create business quality test", description = "Creates one configurable quality test for business.")
    @PreAuthorize("hasAuthority('BUSINESS_QUALITY_TEST_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<BusinessQualityTestResponse>> create(
            @PathVariable Integer businessId,
            @Valid @RequestBody CreateBusinessQualityTestRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        BusinessQualityTestResponse response = businessQualityTestService.create(
                businessId,
                request,
                jwt.getSubject());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(messageService.get("business_quality_test.create.success"), response));
    }

    @Operation(summary = "Update business quality test", description = "Updates one configurable quality test for business.")
    @PreAuthorize("hasAuthority('BUSINESS_QUALITY_TEST_UPDATE')")
    @PutMapping("/{qualityTestId}")
    public ResponseEntity<ApiResponse<BusinessQualityTestResponse>> update(
            @PathVariable Integer businessId,
            @PathVariable Integer qualityTestId,
            @Valid @RequestBody UpdateBusinessQualityTestRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        BusinessQualityTestResponse response = businessQualityTestService.update(
                businessId,
                qualityTestId,
                request,
                jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("business_quality_test.update.success"), response));
    }

    @Operation(summary = "Disable business quality test", description = "Soft-deletes one configurable quality test for business.")
    @PreAuthorize("hasAuthority('BUSINESS_QUALITY_TEST_UPDATE')")
    @DeleteMapping("/{qualityTestId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Integer businessId,
            @PathVariable Integer qualityTestId,
            @AuthenticationPrincipal Jwt jwt) {
        businessQualityTestService.delete(businessId, qualityTestId, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("business_quality_test.delete.success"), null));
    }
}
