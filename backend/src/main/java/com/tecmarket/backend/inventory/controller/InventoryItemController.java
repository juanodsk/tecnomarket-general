package com.tecmarket.backend.inventory.controller;

import com.tecmarket.backend.inventory.dto.CreateInventoryItemRequest;
import com.tecmarket.backend.inventory.dto.InventoryItemResponse;
import com.tecmarket.backend.inventory.dto.UpdateInventoryItemRequest;
import com.tecmarket.backend.inventory.service.InventoryItemService;
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
@Tag(name = "Inventory Items", description = "Endpoints for managing business inventory items.")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/businesses/{businessId}/inventory-items")
public class InventoryItemController {

    private final InventoryItemService inventoryItemService;
    private final MessageService messageService;

    public InventoryItemController(InventoryItemService inventoryItemService, MessageService messageService) {
        this.inventoryItemService = inventoryItemService;
        this.messageService = messageService;
    }

    @Operation(summary = "List inventory items", description = "Returns active inventory items by business.")
    @PreAuthorize("hasAuthority('INVENTORY_VIEW')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<InventoryItemResponse>>> findAll(
            @PathVariable Integer businessId,
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size,
            @AuthenticationPrincipal Jwt jwt) {
        PageResponse<InventoryItemResponse> response = inventoryItemService.findAllByBusiness(
                businessId, page, size, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("inventory_item.list.success"), response));
    }

    @Operation(summary = "Find inventory item by id", description = "Returns one inventory item by id.")
    @PreAuthorize("hasAuthority('INVENTORY_VIEW')")
    @GetMapping("/{inventoryItemId}")
    public ResponseEntity<ApiResponse<InventoryItemResponse>> findById(
            @PathVariable Integer businessId,
            @PathVariable Integer inventoryItemId,
            @AuthenticationPrincipal Jwt jwt) {
        InventoryItemResponse response = inventoryItemService.findById(businessId, inventoryItemId, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("inventory_item.find.success"), response));
    }

    @Operation(summary = "Create inventory item", description = "Creates an inventory item inside the business.")
    @PreAuthorize("hasAuthority('INVENTORY_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<InventoryItemResponse>> create(
            @PathVariable Integer businessId,
            @Valid @RequestBody CreateInventoryItemRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        InventoryItemResponse response = inventoryItemService.create(businessId, request, jwt.getSubject());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(messageService.get("inventory_item.create.success"), response));
    }

    @Operation(summary = "Update inventory item", description = "Updates inventory item fields.")
    @PreAuthorize("hasAuthority('INVENTORY_UPDATE')")
    @PutMapping("/{inventoryItemId}")
    public ResponseEntity<ApiResponse<InventoryItemResponse>> update(
            @PathVariable Integer businessId,
            @PathVariable Integer inventoryItemId,
            @Valid @RequestBody UpdateInventoryItemRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        InventoryItemResponse response = inventoryItemService.update(
                businessId,
                inventoryItemId,
                request,
                jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("inventory_item.update.success"), response));
    }

    @Operation(summary = "Disable inventory item", description = "Soft deletes an inventory item.")
    @PreAuthorize("hasAuthority('INVENTORY_DELETE')")
    @DeleteMapping("/{inventoryItemId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Integer businessId,
            @PathVariable Integer inventoryItemId,
            @AuthenticationPrincipal Jwt jwt) {
        inventoryItemService.delete(businessId, inventoryItemId, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("inventory_item.delete.success"), null));
    }
}
