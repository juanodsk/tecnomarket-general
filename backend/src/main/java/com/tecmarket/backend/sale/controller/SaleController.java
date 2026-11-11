package com.tecmarket.backend.sale.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import com.tecmarket.backend.sale.dto.CreateSaleRequest;
import com.tecmarket.backend.sale.dto.SaleItemResponse;
import com.tecmarket.backend.sale.dto.SaleResponse;
import com.tecmarket.backend.sale.service.SaleService;
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

import java.util.List;

import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.sale.entity.Sale;
@RestController
@ConditionalOnProperty(prefix = "app.features", name = "direct-sales-enabled", havingValue = "true")
@Tag(name = "Sales", description = "Endpoints for direct sales and sale items.")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/businesses/{businessId}/sales")
public class SaleController {

    private final SaleService saleService;
    private final MessageService messageService;

    public SaleController(SaleService saleService, MessageService messageService) {
        this.saleService = saleService;
        this.messageService = messageService;
    }

    @Operation(summary = "List sales", description = "Returns sales by business.")
    @PreAuthorize("hasAuthority('SALE_VIEW')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<SaleResponse>>> findAll(
            @PathVariable Integer businessId,
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size,
            @AuthenticationPrincipal Jwt jwt) {
        PageResponse<SaleResponse> response = saleService.findAllByBusiness(
                businessId, page, size, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("sale.list.success"), response));
    }

    @Operation(summary = "Get sale detail", description = "Returns a sale and all its items.")
    @PreAuthorize("hasAuthority('SALE_VIEW')")
    @GetMapping("/{saleId}")
    public ResponseEntity<ApiResponse<SaleResponse>> findById(
            @PathVariable Integer businessId,
            @PathVariable Integer saleId,
            @AuthenticationPrincipal Jwt jwt) {
        SaleResponse response = saleService.findById(businessId, saleId, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("sale.find.success"), response));
    }

    @Operation(summary = "List sale items", description = "Returns only the item lines of a sale.")
    @PreAuthorize("hasAuthority('SALE_VIEW')")
    @GetMapping("/{saleId}/items")
    public ResponseEntity<ApiResponse<List<SaleItemResponse>>> findItems(
            @PathVariable Integer businessId,
            @PathVariable Integer saleId,
            @AuthenticationPrincipal Jwt jwt) {
        List<SaleItemResponse> response = saleService.findItems(businessId, saleId, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("sale_item.list.success"), response));
    }

    @Operation(summary = "Create sale", description = "Creates a sale with item lines and discounts stock transactionally.")
    @PreAuthorize("hasAuthority('SALE_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<SaleResponse>> create(
            @PathVariable Integer businessId,
            @Valid @RequestBody CreateSaleRequest request,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey,
            @AuthenticationPrincipal Jwt jwt) {
        SaleResponse response = saleService.create(businessId, request, jwt.getSubject(), idempotencyKey);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(messageService.get("sale.create.success"), response));
    }
}
