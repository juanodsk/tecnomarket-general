package com.tecmarket.backend.business.controller;

import com.tecmarket.backend.business.dto.BusinessUserResponse;
import com.tecmarket.backend.business.dto.BusinessRoleOptionResponse;
import com.tecmarket.backend.business.dto.BusinessTechnicianOptionResponse;
import com.tecmarket.backend.business.dto.BusinessUserCandidateResponse;
import com.tecmarket.backend.business.dto.CreateBusinessUserRequest;
import com.tecmarket.backend.business.dto.UpdateBusinessUserRequest;
import com.tecmarket.backend.business.service.BusinessUserService;
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

@RestController
@Tag(name = "Business Users", description = "Endpoints for managing users assigned to a business.")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/businesses/{businessId}/users")
public class BusinessUserController {

    private final BusinessUserService businessUserService;
    private final MessageService messageService;

    public BusinessUserController(BusinessUserService businessUserService, MessageService messageService) {
        this.businessUserService = businessUserService;
        this.messageService = messageService;
    }

    @Operation(summary = "List users by business", description = "Returns active user-role relations for a business.")
    @PreAuthorize("hasAuthority('BUSINESS_USER_VIEW')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<BusinessUserResponse>>> findAll(
            @PathVariable Integer businessId,
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size,
            @AuthenticationPrincipal Jwt jwt) {
        PageResponse<BusinessUserResponse> response = businessUserService.findAllByBusiness(
                businessId, page, size, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("business_user.list.success"), response));
    }

    @Operation(summary = "List assignable business roles", description = "Returns active BUSINESS scope roles available for assignment.")
    @PreAuthorize("hasAnyAuthority('BUSINESS_USER_VIEW','BUSINESS_USER_CREATE','BUSINESS_USER_UPDATE')")
    @GetMapping("/role-options")
    public ResponseEntity<ApiResponse<List<BusinessRoleOptionResponse>>> findRoleOptions(
            @PathVariable Integer businessId,
            @AuthenticationPrincipal Jwt jwt) {
        List<BusinessRoleOptionResponse> response = businessUserService.findAssignableRoles(businessId, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("business_user.role_options.list.success"), response));
    }

    @Operation(summary = "Find available user by email for business assignment", description = "Returns a single active user candidate using an exact email lookup.")
    @PreAuthorize("hasAuthority('BUSINESS_USER_CREATE')")
    @GetMapping("/available-users")
    public ResponseEntity<ApiResponse<List<BusinessUserCandidateResponse>>> findAvailableUsers(
            @PathVariable Integer businessId,
            @RequestParam(value = "email", required = false) String email,
            @AuthenticationPrincipal Jwt jwt) {
        List<BusinessUserCandidateResponse> response = businessUserService.findAvailableUsers(businessId, email, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("business_user.available_users.list.success"), response));
    }

    @Operation(summary = "List technicians for repair orders", description = "Returns active technicians associated to the business.")
    @PreAuthorize("hasAnyAuthority('REPAIR_ORDER_CREATE','REPAIR_ORDER_UPDATE')")
    @GetMapping("/technicians")
    public ResponseEntity<ApiResponse<List<BusinessTechnicianOptionResponse>>> findTechnicians(
            @PathVariable Integer businessId,
            @RequestParam(required = false) Integer branchId,
            @AuthenticationPrincipal Jwt jwt) {
        List<BusinessTechnicianOptionResponse> response = businessUserService.findTechniciansForRepairOrders(
                businessId,
                branchId,
                jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("business_user.list.success"), response));
    }

    @Operation(summary = "Assign user to business", description = "Creates a user-role assignment inside the business.")
    @PreAuthorize("hasAuthority('BUSINESS_USER_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<BusinessUserResponse>> create(
            @PathVariable Integer businessId,
            @Valid @RequestBody CreateBusinessUserRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        BusinessUserResponse response = businessUserService.create(businessId, request, jwt.getSubject());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(messageService.get("business_user.create.success"), response));
    }

    @Operation(summary = "Update business user assignment", description = "Updates role or active status for a business user relation.")
    @PreAuthorize("hasAuthority('BUSINESS_USER_UPDATE')")
    @PutMapping("/{businessUserId}")
    public ResponseEntity<ApiResponse<BusinessUserResponse>> update(
            @PathVariable Integer businessId,
            @PathVariable Integer businessUserId,
            @Valid @RequestBody UpdateBusinessUserRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        BusinessUserResponse response = businessUserService.update(businessId, businessUserId, request, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("business_user.update.success"), response));
    }

    @Operation(summary = "Disable business user assignment", description = "Soft-deletes the relation user-role in a business.")
    @PreAuthorize("hasAuthority('BUSINESS_USER_UPDATE')")
    @DeleteMapping("/{businessUserId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Integer businessId,
            @PathVariable Integer businessUserId,
            @AuthenticationPrincipal Jwt jwt) {
        businessUserService.delete(businessId, businessUserId, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("business_user.delete.success"), null));
    }
}
