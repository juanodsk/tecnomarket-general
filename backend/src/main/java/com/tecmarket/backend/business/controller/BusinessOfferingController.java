package com.tecmarket.backend.business.controller;

import com.tecmarket.backend.business.dto.BusinessOfferingResponse;
import com.tecmarket.backend.business.dto.CreateBusinessOfferingRequest;
import com.tecmarket.backend.business.dto.UpdateBusinessOfferingRequest;
import com.tecmarket.backend.business.service.BusinessOfferingService;
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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Tag(name = "Business Services", description = "Endpoints for managing services offered by a business.")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/businesses/{businessId}/services")
public class BusinessOfferingController {

        private final BusinessOfferingService businessOfferingService;
        private final MessageService messageService;

        public BusinessOfferingController(BusinessOfferingService businessOfferingService,
                        MessageService messageService) {
                this.businessOfferingService = businessOfferingService;
                this.messageService = messageService;
        }

        @Operation(summary = "List business services", description = "Returns service configurations for a business.")
        @PreAuthorize("hasAuthority('BUSINESS_SERVICE_VIEW')")
        @GetMapping
        public ResponseEntity<ApiResponse<PageResponse<BusinessOfferingResponse>>> findAll(
                        @PathVariable Integer businessId,
                        @RequestParam(defaultValue = "0") @Min(0) Integer page,
                        @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size,
                        @AuthenticationPrincipal Jwt jwt) {
                PageResponse<BusinessOfferingResponse> response = businessOfferingService.findAllByBusiness(
                                businessId,
                                page,
                                size,
                                jwt.getSubject());
                return ResponseEntity
                                .ok(ApiResponse.success(messageService.get("business_service.list.success"), response));
        }

        @Operation(summary = "Find business service by ID", description = "Returns one service configuration by id.")
        @PreAuthorize("hasAuthority('BUSINESS_SERVICE_VIEW')")
        @GetMapping("/{offeringId}")
        public ResponseEntity<ApiResponse<BusinessOfferingResponse>> findById(
                        @PathVariable Integer businessId,
                        @PathVariable Integer offeringId,
                        @AuthenticationPrincipal Jwt jwt) {
                BusinessOfferingResponse response = businessOfferingService.findById(
                                businessId,
                                offeringId,
                                jwt.getSubject());
                return ResponseEntity
                                .ok(ApiResponse.success(messageService.get("business_service.find.success"), response));
        }

        @Operation(summary = "Create business service", description = "Creates a service offered by the business.")
        @PreAuthorize("hasAuthority('BUSINESS_SERVICE_CREATE')")
        @PostMapping
        public ResponseEntity<ApiResponse<BusinessOfferingResponse>> create(
                        @PathVariable Integer businessId,
                        @Valid @RequestBody CreateBusinessOfferingRequest request,
                        @AuthenticationPrincipal Jwt jwt) {
                BusinessOfferingResponse response = businessOfferingService.create(
                                businessId,
                                request,
                                jwt.getSubject());
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(ApiResponse.success(messageService.get("business_service.create.success"),
                                                response));
        }

        @Operation(summary = "Update business service", description = "Updates service data for the business.")
        @PreAuthorize("hasAuthority('BUSINESS_SERVICE_UPDATE')")
        @PutMapping("/{offeringId}")
        public ResponseEntity<ApiResponse<BusinessOfferingResponse>> update(
                        @PathVariable Integer businessId,
                        @PathVariable Integer offeringId,
                        @Valid @RequestBody UpdateBusinessOfferingRequest request,
                        @AuthenticationPrincipal Jwt jwt) {
                BusinessOfferingResponse response = businessOfferingService.update(
                                businessId,
                                offeringId,
                                request,
                                jwt.getSubject());
                return ResponseEntity.ok(
                                ApiResponse.success(messageService.get("business_service.update.success"), response));
        }

        @Operation(summary = "Upload service image", description = "Uploads and replaces the public image for a business service.")
        @PreAuthorize("hasAuthority('BUSINESS_SERVICE_UPDATE')")
        @PostMapping(value = "/{offeringId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<ApiResponse<BusinessOfferingResponse>> uploadImage(
                        @PathVariable Integer businessId,
                        @PathVariable Integer offeringId,
                        @RequestPart("file") MultipartFile file,
                        @AuthenticationPrincipal Jwt jwt) {
                BusinessOfferingResponse response = businessOfferingService.uploadImage(
                                businessId,
                                offeringId,
                                file,
                                jwt.getSubject());
                return ResponseEntity.ok(
                                ApiResponse.success(messageService.get("business_service.image.update.success"), response));
        }

        @Operation(summary = "Disable business service", description = "Soft-deletes the business service.")
        @PreAuthorize("hasAuthority('BUSINESS_SERVICE_UPDATE')")
        @DeleteMapping("/{offeringId}")
        public ResponseEntity<ApiResponse<Void>> delete(
                        @PathVariable Integer businessId,
                        @PathVariable Integer offeringId,
                        @AuthenticationPrincipal Jwt jwt) {
                businessOfferingService.delete(businessId, offeringId, jwt.getSubject());
                return ResponseEntity
                                .ok(ApiResponse.success(messageService.get("business_service.delete.success"), null));
        }
}
