package com.tecmarket.backend.quotation.controller;

import com.tecmarket.backend.quotation.dto.CreateQuotationRequest;
import com.tecmarket.backend.quotation.dto.QuotationResponse;
import com.tecmarket.backend.quotation.dto.UpdateQuotationStatusRequest;
import com.tecmarket.backend.quotation.service.QuotationService;
import com.tecmarket.backend.common.i18n.MessageService;
import com.tecmarket.backend.common.pagination.PageResponse;
import com.tecmarket.backend.common.response.ApiResponse;
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

import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.quotation.entity.Quotation;
@RestController
@Tag(name = "Quotations", description = "Endpoints for managing repair order quotations by business.")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/businesses/{businessId}")
public class QuotationController {

    private final QuotationService quotationService;
    private final MessageService messageService;

    public QuotationController(
            QuotationService quotationService,
            MessageService messageService) {
        this.quotationService = quotationService;
        this.messageService = messageService;
    }

    @Operation(summary = "Create quotation", description = "Creates a quotation for a repair order with detail items.")
    @PreAuthorize("hasAuthority('QUOTATION_CREATE')")
    @PostMapping("/quotations")
    public ResponseEntity<ApiResponse<QuotationResponse>> create(
            @PathVariable Integer businessId,
            @Valid @RequestBody CreateQuotationRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        QuotationResponse response = quotationService.create(businessId, request, jwt.getSubject());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(messageService.get("quotation.create.success"), response));
    }

    @Operation(summary = "Get quotation detail", description = "Returns quotation header and detail items.")
    @PreAuthorize("hasAuthority('QUOTATION_VIEW')")
    @GetMapping("/quotations/{quotationId}")
    public ResponseEntity<ApiResponse<QuotationResponse>> findById(
            @PathVariable Integer businessId,
            @PathVariable Integer quotationId,
            @AuthenticationPrincipal Jwt jwt) {
        QuotationResponse response = quotationService.findById(businessId, quotationId, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("quotation.find.success"), response));
    }

    @Operation(summary = "Change quotation status", description = "Updates only the status of an existing quotation.")
    @PreAuthorize("hasAuthority('QUOTATION_UPDATE')")
    @PatchMapping("/quotations/{quotationId}/status")
    public ResponseEntity<ApiResponse<QuotationResponse>> updateStatus(
            @PathVariable Integer businessId,
            @PathVariable Integer quotationId,
            @Valid @RequestBody UpdateQuotationStatusRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        QuotationResponse response = quotationService.updateStatus(
                businessId,
                quotationId,
                request,
                jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("quotation.status.update.success"), response));
    }

    @Operation(
            summary = "List quotations by repair order",
            description = "Returns active quotations and their items for an accessible repair order, newest first.")
    @PreAuthorize("hasAuthority('QUOTATION_VIEW')")
    @GetMapping("/repair-orders/{repairOrderId}/quotations")
    public ResponseEntity<ApiResponse<PageResponse<QuotationResponse>>> findAllByRepairOrder(
            @PathVariable Integer businessId,
            @PathVariable Integer repairOrderId,
            @RequestParam(name = "page", defaultValue = "0") @Min(0) Integer page,
            @RequestParam(name = "size", defaultValue = "20") @Min(1) @Max(100) Integer size,
            @AuthenticationPrincipal Jwt jwt) {
        PageResponse<QuotationResponse> response = quotationService.findAllByRepairOrder(
                businessId,
                repairOrderId,
                page,
                size,
                jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("quotation.list.success"), response));
    }
}
