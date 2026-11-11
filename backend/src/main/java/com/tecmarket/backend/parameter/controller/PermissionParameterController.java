package com.tecmarket.backend.parameter.controller;

import com.tecmarket.backend.common.i18n.MessageService;
import com.tecmarket.backend.common.response.ApiResponse;
import com.tecmarket.backend.parameter.dto.CreatePermissionRequest;
import com.tecmarket.backend.parameter.dto.PermissionResponse;
import com.tecmarket.backend.parameter.dto.UpdatePermissionRequest;
import com.tecmarket.backend.parameter.service.PermissionParameterService;
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
@Tag(name = "Permissions", description = "Endpoints for managing system permissions.")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/parameters")
public class PermissionParameterController {

    private final PermissionParameterService permissionParameterService;
    private final MessageService messageService;

    public PermissionParameterController(
            PermissionParameterService permissionParameterService,
            MessageService messageService) {
        this.permissionParameterService = permissionParameterService;
        this.messageService = messageService;
    }

    @Operation(summary = "List permissions", description = "Returns all active permissions in the system.")
    @PreAuthorize("hasAuthority('PARAMETER_VIEW')")
    @GetMapping("/permissions")
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> findAll() {
        List<PermissionResponse> response = permissionParameterService.findAll();

        return ResponseEntity.ok(ApiResponse.success(messageService.get("permission.list.success"), response));
    }

    @Operation(summary = "Find permission by ID", description = "Returns a permission by its identifier.")
    @PreAuthorize("hasAuthority('PARAMETER_VIEW')")
    @GetMapping("/permissions/{id}")
    public ResponseEntity<ApiResponse<PermissionResponse>> findById(@PathVariable Integer id) {
        PermissionResponse response = permissionParameterService.findById(id);

        return ResponseEntity.ok(ApiResponse.success(messageService.get("permission.find.success"), response));
    }

    @Operation(summary = "List permissions by module", description = "Returns all active permissions that belong to a module.")
    @PreAuthorize("hasAuthority('PARAMETER_VIEW')")
    @GetMapping("/modules/{moduleId}/permissions")
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> findByModuleId(@PathVariable Integer moduleId) {
        List<PermissionResponse> response = permissionParameterService.findByModuleId(moduleId);

        return ResponseEntity.ok(ApiResponse.success(messageService.get("permission.list.success"), response));
    }

    @Operation(summary = "Create permission", description = "Creates a permission and associates it to an application module.")
    @PreAuthorize("hasAuthority('PARAMETER_MANAGE')")
    @PostMapping("/permissions")
    public ResponseEntity<ApiResponse<PermissionResponse>> create(@Valid @RequestBody CreatePermissionRequest request) {
        PermissionResponse response = permissionParameterService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(messageService.get("permission.create.success"), response));
    }

    @Operation(summary = "Update permission", description = "Updates permission metadata, module association and active status.")
    @PreAuthorize("hasAuthority('PARAMETER_MANAGE')")
    @PutMapping("/permissions/{id}")
    public ResponseEntity<ApiResponse<PermissionResponse>> update(
            @PathVariable Integer id,
            @Valid @RequestBody UpdatePermissionRequest request) {
        PermissionResponse response = permissionParameterService.update(id, request);

        return ResponseEntity.ok(ApiResponse.success(messageService.get("permission.update.success"), response));
    }

    @Operation(summary = "Disable permission", description = "Soft-deletes a permission by marking it inactive.")
    @PreAuthorize("hasAuthority('PARAMETER_MANAGE')")
    @DeleteMapping("/permissions/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        permissionParameterService.delete(id);

        return ResponseEntity.ok(ApiResponse.success(messageService.get("permission.delete.success"), null));
    }
}
