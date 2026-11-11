package com.tecmarket.backend.business.controller;

import com.tecmarket.backend.business.dto.BusinessBranchResponse;
import com.tecmarket.backend.business.dto.CreateBusinessBranchRequest;
import com.tecmarket.backend.business.dto.UpdateBusinessBranchRequest;
import com.tecmarket.backend.business.service.BusinessBranchService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Business Branches", description = "Endpoints for managing business branches or physical locations.")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/businesses/{businessId}/branches")
public class BusinessBranchController {

    private final BusinessBranchService businessBranchService;
    private final MessageService messageService;

    public BusinessBranchController(BusinessBranchService businessBranchService, MessageService messageService) {
        this.businessBranchService = businessBranchService;
        this.messageService = messageService;
    }

    @Operation(summary = "List business branches", description = "Returns active and inactive branches for a business.")
    @PreAuthorize("hasAuthority('BUSINESS_BRANCH_VIEW')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<BusinessBranchResponse>>> findAll(
            @PathVariable Integer businessId,
            @AuthenticationPrincipal Jwt jwt) {
        List<BusinessBranchResponse> response = businessBranchService.findAll(businessId, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("business_branch.list.success"), response));
    }

    @Operation(summary = "Find branch by id", description = "Returns a single branch of a business.")
    @PreAuthorize("hasAuthority('BUSINESS_BRANCH_VIEW')")
    @GetMapping("/{branchId}")
    public ResponseEntity<ApiResponse<BusinessBranchResponse>> findById(
            @PathVariable Integer businessId,
            @PathVariable Integer branchId,
            @AuthenticationPrincipal Jwt jwt) {
        BusinessBranchResponse response = businessBranchService.findById(businessId, branchId, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("business_branch.find.success"), response));
    }

    @Operation(summary = "Create branch", description = "Creates a business branch if the subscription allows it.")
    @PreAuthorize("hasAuthority('BUSINESS_BRANCH_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<BusinessBranchResponse>> create(
            @PathVariable Integer businessId,
            @Valid @RequestBody CreateBusinessBranchRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        BusinessBranchResponse response = businessBranchService.create(businessId, request, jwt.getSubject());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(messageService.get("business_branch.create.success"), response));
    }

    @Operation(summary = "Update branch", description = "Updates branch location and metadata.")
    @PreAuthorize("hasAuthority('BUSINESS_BRANCH_UPDATE')")
    @PutMapping("/{branchId}")
    public ResponseEntity<ApiResponse<BusinessBranchResponse>> update(
            @PathVariable Integer businessId,
            @PathVariable Integer branchId,
            @Valid @RequestBody UpdateBusinessBranchRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        BusinessBranchResponse response = businessBranchService.update(businessId, branchId, request, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("business_branch.update.success"), response));
    }

    @Operation(summary = "Delete branch", description = "Soft deletes a branch.")
    @PreAuthorize("hasAuthority('BUSINESS_BRANCH_UPDATE')")
    @DeleteMapping("/{branchId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Integer businessId,
            @PathVariable Integer branchId,
            @AuthenticationPrincipal Jwt jwt) {
        businessBranchService.delete(businessId, branchId, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("business_branch.delete.success"), null));
    }
}
