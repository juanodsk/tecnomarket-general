package com.tecmarket.backend.parameter.controller;

import com.tecmarket.backend.common.i18n.MessageService;
import com.tecmarket.backend.common.response.ApiResponse;
import com.tecmarket.backend.parameter.dto.CreateRoleRequest;
import com.tecmarket.backend.parameter.dto.RoleResponse;
import com.tecmarket.backend.parameter.dto.UpdateRoleRequest;
import com.tecmarket.backend.parameter.service.RoleParameterService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Tag(name = "Roles", description = "Endpoints for managing user roles.")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/parameters/roles")
public class RoleParameterController {

    private final RoleParameterService roleParameterService;
    private final MessageService messageService;

    public RoleParameterController(
            RoleParameterService roleParameterService,
            MessageService messageService) {
        this.roleParameterService = roleParameterService;
        this.messageService = messageService;
    }

    @Operation(summary = "List roles", description = "Returns all active roles available in the system.")
    @PreAuthorize("hasAuthority('PARAMETER_VIEW')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponse>>> findAll() {
        List<RoleResponse> response = roleParameterService.findAll();

        return ResponseEntity.ok(ApiResponse.success(messageService.get("role.list.success"), response));
    }

    @Operation(summary = "Find role by ID", description = "Returns a role by its identifier.")
    @PreAuthorize("hasAuthority('PARAMETER_VIEW')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> findById(@PathVariable Integer id) {
        RoleResponse response = roleParameterService.findById(id);

        return ResponseEntity.ok(ApiResponse.success(messageService.get("role.find.success"), response));
    }

    @Operation(summary = "Create role", description = "Creates a role that can later receive permissions.")
    @PreAuthorize("hasAuthority('PARAMETER_MANAGE')")
    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> create(@Valid @RequestBody CreateRoleRequest request) {
        RoleResponse response = roleParameterService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(messageService.get("role.create.success"), response));
    }

    @Operation(summary = "Update role", description = "Updates role metadata and active status.")
    @PreAuthorize("hasAuthority('PARAMETER_MANAGE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> update(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateRoleRequest request) {
        RoleResponse response = roleParameterService.update(id, request);

        return ResponseEntity.ok(ApiResponse.success(messageService.get("role.update.success"), response));
    }

    @Operation(summary = "Disable role", description = "Soft-deletes a role by marking it inactive.")
    @PreAuthorize("hasAuthority('PARAMETER_MANAGE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        roleParameterService.delete(id);

        return ResponseEntity.ok(ApiResponse.success(messageService.get("role.delete.success"), null));
    }
}
