package com.tecmarket.backend.business.controller;

import com.tecmarket.backend.business.dto.BusinessRequestResponse;
import com.tecmarket.backend.business.dto.CreateBusinessRequest;
import com.tecmarket.backend.business.dto.RejectBusinessRequest;
import com.tecmarket.backend.business.service.BusinessRequestService;
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
@Tag(name = "Business Requests", description = "Endpoints for business creation requests.")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/business/requests")
public class BusinessRequestController {

    private final BusinessRequestService businessRequestService;
    private final MessageService messageService;

    public BusinessRequestController(
            BusinessRequestService businessRequestService,
            MessageService messageService) {
        this.businessRequestService = businessRequestService;
        this.messageService = messageService;
    }

    @Operation(summary = "Create business request", description = "Creates a business creation request for the authenticated user.")
    @PreAuthorize("hasAuthority('BUSINESS_REQUEST_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<BusinessRequestResponse>> create(
            @Valid @RequestBody CreateBusinessRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        BusinessRequestResponse response = businessRequestService.create(request, jwt.getSubject());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(messageService.get("business_request.create.success"), response));
    }

    @Operation(summary = "List my business requests", description = "Returns business requests created by the authenticated user.")
    @PreAuthorize("hasAuthority('BUSINESS_REQUEST_VIEW_OWN')")
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<PageResponse<BusinessRequestResponse>>> findMyRequests(
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size,
            @AuthenticationPrincipal Jwt jwt) {
        PageResponse<BusinessRequestResponse> response = businessRequestService.findMyRequests(
                page, size, jwt.getSubject());

        return ResponseEntity.ok(ApiResponse.success(messageService.get("business_request.list_my.success"), response));
    }

    @Operation(summary = "List pending business requests", description = "Returns all pending requests for admin review.")
    @PreAuthorize("hasAuthority('BUSINESS_REQUEST_REVIEW')")
    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<PageResponse<BusinessRequestResponse>>> findPendingRequests(
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size,
            @AuthenticationPrincipal Jwt jwt) {
        PageResponse<BusinessRequestResponse> response = businessRequestService.findPendingRequests(
                page, size, jwt.getSubject());

        return ResponseEntity
                .ok(ApiResponse.success(messageService.get("business_request.list_pending.success"), response));
    }

    @Operation(summary = "Approve business request", description = "Approves a pending request and creates business + owner relation.")
    @PreAuthorize("hasAuthority('BUSINESS_REQUEST_REVIEW')")
    @PutMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<BusinessRequestResponse>> approve(
            @PathVariable Integer id,
            @AuthenticationPrincipal Jwt jwt) {
        BusinessRequestResponse response = businessRequestService.approve(id, jwt.getSubject());

        return ResponseEntity.ok(ApiResponse.success(messageService.get("business_request.approve.success"), response));
    }

    @Operation(summary = "Reject business request", description = "Rejects a pending request with a reason.")
    @PreAuthorize("hasAuthority('BUSINESS_REQUEST_REVIEW')")
    @PutMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<BusinessRequestResponse>> reject(
            @PathVariable Integer id,
            @Valid @RequestBody RejectBusinessRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        BusinessRequestResponse response = businessRequestService.reject(id, request, jwt.getSubject());

        return ResponseEntity.ok(ApiResponse.success(messageService.get("business_request.reject.success"), response));
    }
}
