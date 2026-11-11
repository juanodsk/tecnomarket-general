package com.tecmarket.backend.parameter.controller;

import com.tecmarket.backend.common.i18n.MessageService;
import com.tecmarket.backend.common.response.ApiResponse;
import com.tecmarket.backend.common.pagination.PageResponse;
import com.tecmarket.backend.parameter.dto.UserParameterResponse;
import com.tecmarket.backend.parameter.dto.UpdateUserStatusRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.tecmarket.backend.parameter.service.UserParameterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@Tag(name = "Users", description = "Endpoints for listing users in catalogs.")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/parameters/users")
public class UserParameterController {

    private final UserParameterService userParameterService;
    private final MessageService messageService;

    public UserParameterController(
            UserParameterService userParameterService,
            MessageService messageService) {
        this.userParameterService = userParameterService;
        this.messageService = messageService;
    }

    @Operation(summary = "List users", description = "Returns active users for catalogs and role assignment.")
    @PreAuthorize("hasAuthority('PARAMETER_VIEW')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<UserParameterResponse>>> findAll(
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size) {
        PageResponse<UserParameterResponse> response = userParameterService.findAll(page, size);

        return ResponseEntity.ok(ApiResponse.success(
                messageService.get("user.list.success"),
                response));
    }

    @Operation(summary = "Find user by ID", description = "Returns user basic data by identifier.")
    @PreAuthorize("hasAuthority('PARAMETER_VIEW')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserParameterResponse>> findById(@PathVariable Integer id) {
        UserParameterResponse response = userParameterService.findById(id);

        return ResponseEntity.ok(ApiResponse.success(
                messageService.get("user.find.success"),
                response));
    }

    @Operation(summary = "Update user status", description = "Activates or deactivates a user and invalidates all of their sessions.")
    @PreAuthorize("hasAuthority('PARAMETER_MANAGE')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<UserParameterResponse>> updateStatus(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateUserStatusRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(ApiResponse.success(
                messageService.get("user.status.update.success"),
                userParameterService.updateStatus(id, request, jwt.getSubject())));
    }

    @Operation(summary = "Revoke all user sessions", description = "Immediately invalidates all access and refresh tokens for a user.")
    @PreAuthorize("hasAuthority('PARAMETER_MANAGE')")
    @DeleteMapping("/{id}/sessions")
    public ResponseEntity<ApiResponse<Void>> revokeAllSessions(
            @PathVariable Integer id,
            @AuthenticationPrincipal Jwt jwt) {
        userParameterService.revokeAllSessions(id, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(
                messageService.get("user.sessions.revoke_all.success"),
                null));
    }
}
