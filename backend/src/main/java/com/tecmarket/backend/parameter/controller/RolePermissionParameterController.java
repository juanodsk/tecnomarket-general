package com.tecmarket.backend.parameter.controller;

import com.tecmarket.backend.common.i18n.MessageService;
import com.tecmarket.backend.common.response.ApiResponse;
import com.tecmarket.backend.parameter.dto.RoleAssignedPermissionResponse;
import com.tecmarket.backend.parameter.dto.RolePermissionOptionResponse;
import com.tecmarket.backend.parameter.dto.UpdateRolePermissionsRequest;
import com.tecmarket.backend.parameter.service.RolePermissionParameterService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Role Permissions", description = "Endpoints for assigning permissions to roles.")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/parameters/roles/{roleId}/permissions")
public class RolePermissionParameterController {

    private final RolePermissionParameterService rolePermissionParameterService;
    private final MessageService messageService;

    public RolePermissionParameterController(
            RolePermissionParameterService rolePermissionParameterService,
            MessageService messageService) {
        this.rolePermissionParameterService = rolePermissionParameterService;
        this.messageService = messageService;
    }

    @Operation(summary = "List permissions assigned to role", description = "Returns all active permissions with an assigned flag for the selected role.")
    @PreAuthorize("hasAuthority('PARAMETER_VIEW')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<RolePermissionOptionResponse>>> findPermissionsByRoleId(
            @PathVariable Integer roleId,
            @AuthenticationPrincipal Jwt jwt) {
        List<RolePermissionOptionResponse> response = rolePermissionParameterService
                .findPermissionsByRoleId(roleId, jwt.getSubject());

        return ResponseEntity.ok(ApiResponse.success(
                messageService.get("role_permission.list.success"),
                response));
    }

    @Operation(summary = "List only assigned permissions for role", description = "Returns the permissions currently assigned to a role.")
    @PreAuthorize("hasAuthority('PARAMETER_VIEW')")
    @GetMapping("/assigned")
    public ResponseEntity<ApiResponse<List<RoleAssignedPermissionResponse>>> findAssignedPermissionsByRoleId(
            @PathVariable Integer roleId,
            @AuthenticationPrincipal Jwt jwt) {
        List<RoleAssignedPermissionResponse> response = rolePermissionParameterService
                .findAssignedPermissionsByRoleId(roleId, jwt.getSubject());

        return ResponseEntity.ok(ApiResponse.success(
                messageService.get("role_permission.assigned.list.success"),
                response));
    }

    @Operation(summary = "Replace role permissions", description = "Replaces the permissions assigned to a role with the provided permission ID list.")
    @PreAuthorize("hasAuthority('PARAMETER_MANAGE')")
    @PutMapping
    public ResponseEntity<ApiResponse<List<RolePermissionOptionResponse>>> updatePermissions(
            @PathVariable Integer roleId,
            @Valid @RequestBody UpdateRolePermissionsRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        List<RolePermissionOptionResponse> response = rolePermissionParameterService
                .updatePermissions(roleId, request, jwt.getSubject());

        return ResponseEntity.ok(ApiResponse.success(
                messageService.get("role_permission.update.success"),
                response));
    }
}
