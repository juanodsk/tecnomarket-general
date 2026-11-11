package com.tecmarket.backend.business.controller;

import com.tecmarket.backend.business.dto.BusinessContextResponse;
import com.tecmarket.backend.business.dto.BusinessResponse;
import com.tecmarket.backend.business.dto.CreateBusinessCatalogRequest;
import com.tecmarket.backend.business.dto.UpdateBusinessCatalogRequest;
import com.tecmarket.backend.business.service.BusinessContextService;
import com.tecmarket.backend.business.service.BusinessService;
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

import java.util.List;

@RestController
@Tag(name = "Businesses", description = "Endpoints for managing businesses.")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/businesses")
public class BusinessController {

    private final BusinessService businessService;
    private final BusinessContextService businessContextService;
    private final MessageService messageService;

    public BusinessController(
            BusinessService businessService,
            BusinessContextService businessContextService,
            MessageService messageService) {
        this.businessService = businessService;
        this.businessContextService = businessContextService;
        this.messageService = messageService;
    }

    @Operation(summary = "List businesses", description = "Returns all active businesses.")
    @PreAuthorize("hasAuthority('BUSINESS_VIEW')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<BusinessResponse>>> findAll(
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size,
            @AuthenticationPrincipal Jwt jwt) {
        PageResponse<BusinessResponse> response = businessService.findAll(page, size, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("business.list.success"), response));
    }

    @Operation(summary = "List my businesses", description = "Returns active businesses linked to the authenticated user.")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<BusinessResponse>>> findMyBusinesses(@AuthenticationPrincipal Jwt jwt) {
        List<BusinessResponse> response = businessService.findMyBusinesses(jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("business.list.success"), response));
    }

    @Operation(
            summary = "Get my context in a business",
            description = "Returns the authenticated user's effective business role, permissions and accessible branches.")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{businessId}/me/context")
    public ResponseEntity<ApiResponse<BusinessContextResponse>> findMyBusinessContext(
            @PathVariable Integer businessId,
            @AuthenticationPrincipal Jwt jwt) {
        BusinessContextResponse response = businessContextService.findForAuthenticatedUser(
                businessId,
                jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("business.context.find.success"), response));
    }

    @Operation(summary = "Find business by ID", description = "Returns a business by its identifier.")
    @PreAuthorize("hasAuthority('BUSINESS_VIEW')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BusinessResponse>> findById(
            @PathVariable Integer id,
            @AuthenticationPrincipal Jwt jwt) {
        BusinessResponse response = businessService.findById(id, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("business.find.success"), response));
    }

    @Operation(summary = "Create business", description = "Creates a business record from admin catalog.")
    @PreAuthorize("hasAuthority('BUSINESS_UPDATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<BusinessResponse>> create(
            @Valid @RequestBody CreateBusinessCatalogRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        BusinessResponse response = businessService.create(request, jwt.getSubject());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(messageService.get("business.create.success"), response));
    }

    @Operation(summary = "Update business", description = "Updates business metadata.")
    @PreAuthorize("hasAuthority('BUSINESS_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BusinessResponse>> update(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateBusinessCatalogRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        BusinessResponse response = businessService.update(id, request, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("business.update.success"), response));
    }

    @Operation(summary = "Upload business logo", description = "Uploads and replaces the public logo stored in MinIO.")
    @PreAuthorize("hasAuthority('BUSINESS_UPDATE')")
    @PostMapping(value = "/{id}/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BusinessResponse>> uploadLogo(
            @PathVariable Integer id,
            @RequestPart("file") MultipartFile file,
            @AuthenticationPrincipal Jwt jwt) {
        BusinessResponse response = businessService.uploadLogo(id, file, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("business.logo.update.success"), response));
    }

    @Operation(summary = "Upload business cover", description = "Uploads and replaces the public cover image stored in MinIO.")
    @PreAuthorize("hasAuthority('BUSINESS_UPDATE')")
    @PostMapping(value = "/{id}/cover", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BusinessResponse>> uploadCover(
            @PathVariable Integer id,
            @RequestPart("file") MultipartFile file,
            @AuthenticationPrincipal Jwt jwt) {
        BusinessResponse response = businessService.uploadCover(id, file, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("business.cover.update.success"), response));
    }

    @Operation(summary = "Disable business", description = "Soft-deletes a business by marking it inactive.")
    @PreAuthorize("hasAuthority('BUSINESS_UPDATE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Integer id,
            @AuthenticationPrincipal Jwt jwt) {
        businessService.delete(id, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("business.delete.success"), null));
    }
}
