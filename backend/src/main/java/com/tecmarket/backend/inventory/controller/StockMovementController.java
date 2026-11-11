package com.tecmarket.backend.inventory.controller;

import com.tecmarket.backend.inventory.dto.CreateStockMovementRequest;
import com.tecmarket.backend.inventory.dto.StockMovementResponse;
import com.tecmarket.backend.inventory.service.StockMovementService;
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
@Tag(name = "Stock Movements", description = "Endpoints for stock in/out/adjustment movements.")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/businesses/{businessId}/stock-movements")
public class StockMovementController {

    private final StockMovementService stockMovementService;
    private final MessageService messageService;

    public StockMovementController(
            StockMovementService stockMovementService,
            MessageService messageService) {
        this.stockMovementService = stockMovementService;
        this.messageService = messageService;
    }

    @Operation(summary = "List stock movements", description = "Returns stock movements by business with optional inventory item filter.")
    @PreAuthorize("hasAuthority('INVENTORY_VIEW')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<StockMovementResponse>>> findAll(
            @PathVariable Integer businessId,
            @RequestParam(required = false) Integer inventoryItemId,
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size,
            @AuthenticationPrincipal Jwt jwt) {
        PageResponse<StockMovementResponse> response = stockMovementService.findAllByBusiness(
                businessId,
                inventoryItemId,
                page,
                size,
                jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("stock_movement.list.success"), response));
    }

    @Operation(summary = "Create stock movement", description = "Creates a stock movement and applies stock adjustment transactionally.")
    @PreAuthorize("hasAuthority('INVENTORY_UPDATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<StockMovementResponse>> create(
            @PathVariable Integer businessId,
            @Valid @RequestBody CreateStockMovementRequest request,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey,
            @AuthenticationPrincipal Jwt jwt) {
        StockMovementResponse response = stockMovementService.create(
                businessId, request, jwt.getSubject(), idempotencyKey);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(messageService.get("stock_movement.create.success"), response));
    }
}
