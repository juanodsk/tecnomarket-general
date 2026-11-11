package com.tecmarket.backend.parameter.controller;

import com.tecmarket.backend.common.i18n.MessageService;
import com.tecmarket.backend.common.response.ApiResponse;
import com.tecmarket.backend.parameter.dto.AppModuleResponse;
import com.tecmarket.backend.parameter.dto.CreateAppModuleRequest;
import com.tecmarket.backend.parameter.dto.UpdateAppModuleRequest;
import com.tecmarket.backend.parameter.service.AppModuleParameterService;

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
@Tag(name = "App Modules", description = "Endpoints for managing application modules.")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/parameters/modules")
public class AppModuleParameterController {

    private final AppModuleParameterService appModuleParameterService;
    private final MessageService messageService;

    public AppModuleParameterController(
            AppModuleParameterService appModuleParameterService,
            MessageService messageService) {
        this.appModuleParameterService = appModuleParameterService;
        this.messageService = messageService;
    }

    @Operation(summary = "List application modules", description = "Returns all active modules used to group permissions.")
    @PreAuthorize("hasAuthority('PARAMETER_VIEW')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<AppModuleResponse>>> findAll() {
        List<AppModuleResponse> response = appModuleParameterService.findAll();

        return ResponseEntity.ok(ApiResponse.success(messageService.get("app_module.list.success"), response));
    }

    @Operation(summary = "Find application module by ID", description = "Returns an application module by its identifier.")
    @PreAuthorize("hasAuthority('PARAMETER_VIEW')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AppModuleResponse>> findById(@PathVariable Integer id) {
        AppModuleResponse response = appModuleParameterService.findById(id);

        return ResponseEntity.ok(ApiResponse.success(messageService.get("app_module.find.success"), response));
    }

    @Operation(summary = "Create application module", description = "Creates a module used to organize permissions.")
    @PreAuthorize("hasAuthority('PARAMETER_MANAGE')")
    @PostMapping
    public ResponseEntity<ApiResponse<AppModuleResponse>> create(@Valid @RequestBody CreateAppModuleRequest request) {
        AppModuleResponse response = appModuleParameterService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(messageService.get("app_module.create.success"), response));
    }

    @Operation(summary = "Update application module", description = "Updates module metadata and active status.")
    @PreAuthorize("hasAuthority('PARAMETER_MANAGE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AppModuleResponse>> update(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateAppModuleRequest request) {
        AppModuleResponse response = appModuleParameterService.update(id, request);

        return ResponseEntity.ok(ApiResponse.success(messageService.get("app_module.update.success"), response));
    }

    @Operation(summary = "Disable application module", description = "Soft-deletes a module by marking it inactive.")
    @PreAuthorize("hasAuthority('PARAMETER_MANAGE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        appModuleParameterService.delete(id);

        return ResponseEntity.ok(ApiResponse.success(messageService.get("app_module.delete.success"), null));
    }
}
