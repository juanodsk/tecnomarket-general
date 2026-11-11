package com.tecmarket.backend.parameter.controller;

import com.tecmarket.backend.common.i18n.MessageService;
import com.tecmarket.backend.common.response.ApiResponse;
import com.tecmarket.backend.parameter.dto.UpdateUserRolesRequest;
import com.tecmarket.backend.parameter.dto.UserRoleOptionResponse;
import com.tecmarket.backend.parameter.service.UserRoleParameterService;
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
@Tag(name = "User Roles", description = "Endpoints for assigning roles to users.")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/parameters/users/{userId}/roles")
public class UserRoleParameterController {

    private final UserRoleParameterService userRoleParameterService;
    private final MessageService messageService;

    public UserRoleParameterController(
            UserRoleParameterService userRoleParameterService,
            MessageService messageService) {
        this.userRoleParameterService = userRoleParameterService;
        this.messageService = messageService;
    }

    @Operation(summary = "List roles assigned to user", description = "Returns all active roles with an assigned flag for the selected user.")
    @PreAuthorize("hasAuthority('PARAMETER_VIEW')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserRoleOptionResponse>>> findRolesByUserId(
            @PathVariable Integer userId,
            @AuthenticationPrincipal Jwt jwt) {
        List<UserRoleOptionResponse> response = userRoleParameterService.findRolesByUserId(userId, jwt.getSubject());

        return ResponseEntity.ok(ApiResponse.success(
                messageService.get("user_role.list.success"),
                response));
    }

    @Operation(summary = "Replace user roles", description = "Replaces the roles assigned to a user with the provided role ID list.")
    @PreAuthorize("hasAuthority('PARAMETER_MANAGE')")
    @PutMapping
    public ResponseEntity<ApiResponse<List<UserRoleOptionResponse>>> updateRoles(
            @PathVariable Integer userId,
            @Valid @RequestBody UpdateUserRolesRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        List<UserRoleOptionResponse> response = userRoleParameterService.updateRoles(
                userId,
                request,
                jwt.getSubject());

        return ResponseEntity.ok(ApiResponse.success(
                messageService.get("user_role.update.success"),
                response));
    }
}
