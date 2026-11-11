package com.tecmarket.backend.business.controller;

import com.tecmarket.backend.business.dto.BusinessCatalogItemResponse;
import com.tecmarket.backend.business.dto.CreateBusinessCatalogItemRequest;
import com.tecmarket.backend.business.dto.UpdateBusinessCatalogItemRequest;
import com.tecmarket.backend.business.service.BusinessCatalogService;
import com.tecmarket.backend.common.i18n.MessageService;
import com.tecmarket.backend.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Business Catalogs", description = "Endpoints for managing catalogs owned by each business.")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/businesses/{businessId}/catalogs/{catalogType}")
public class BusinessCatalogController {

    private final BusinessCatalogService businessCatalogService;
    private final MessageService messageService;

    public BusinessCatalogController(BusinessCatalogService businessCatalogService, MessageService messageService) {
        this.businessCatalogService = businessCatalogService;
        this.messageService = messageService;
    }

    @Operation(summary = "List business catalog items", description = "Returns catalog items configured by one business.")
    @PreAuthorize("hasAuthority('BUSINESS_CATALOG_VIEW')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<BusinessCatalogItemResponse>>> findAll(
            @PathVariable Integer businessId,
            @PathVariable String catalogType,
            @AuthenticationPrincipal Jwt jwt) {
        List<BusinessCatalogItemResponse> response = businessCatalogService.findAll(
                businessId,
                catalogType,
                jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("business_catalog.list.success"), response));
    }

    @Operation(summary = "Find business catalog item", description = "Returns one catalog item owned by one business.")
    @PreAuthorize("hasAuthority('BUSINESS_CATALOG_VIEW')")
    @GetMapping("/{itemId}")
    public ResponseEntity<ApiResponse<BusinessCatalogItemResponse>> findById(
            @PathVariable Integer businessId,
            @PathVariable String catalogType,
            @PathVariable Integer itemId,
            @AuthenticationPrincipal Jwt jwt) {
        BusinessCatalogItemResponse response = businessCatalogService.findById(
                businessId,
                catalogType,
                itemId,
                jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("business_catalog.find.success"), response));
    }

    @Operation(summary = "Create business catalog item", description = "Creates a catalog item owned by one business.")
    @PreAuthorize("hasAuthority('BUSINESS_CATALOG_MANAGE')")
    @PostMapping
    public ResponseEntity<ApiResponse<BusinessCatalogItemResponse>> create(
            @PathVariable Integer businessId,
            @PathVariable String catalogType,
            @Valid @RequestBody CreateBusinessCatalogItemRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        BusinessCatalogItemResponse response = businessCatalogService.create(
                businessId,
                catalogType,
                request,
                jwt.getSubject());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(messageService.get("business_catalog.create.success"), response));
    }

    @Operation(summary = "Update business catalog item", description = "Updates a catalog item owned by one business.")
    @PreAuthorize("hasAuthority('BUSINESS_CATALOG_MANAGE')")
    @PutMapping("/{itemId}")
    public ResponseEntity<ApiResponse<BusinessCatalogItemResponse>> update(
            @PathVariable Integer businessId,
            @PathVariable String catalogType,
            @PathVariable Integer itemId,
            @Valid @RequestBody UpdateBusinessCatalogItemRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        BusinessCatalogItemResponse response = businessCatalogService.update(
                businessId,
                catalogType,
                itemId,
                request,
                jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("business_catalog.update.success"), response));
    }

    @Operation(summary = "Disable business catalog item", description = "Soft deletes a catalog item owned by one business.")
    @PreAuthorize("hasAuthority('BUSINESS_CATALOG_MANAGE')")
    @DeleteMapping("/{itemId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Integer businessId,
            @PathVariable String catalogType,
            @PathVariable Integer itemId,
            @AuthenticationPrincipal Jwt jwt) {
        businessCatalogService.delete(businessId, catalogType, itemId, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("business_catalog.delete.success"), null));
    }
}
