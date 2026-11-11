package com.tecmarket.backend.parameter.controller;

import com.tecmarket.backend.common.i18n.MessageService;
import com.tecmarket.backend.common.response.ApiResponse;
import com.tecmarket.backend.parameter.dto.CreateDepartmentRequest;
import com.tecmarket.backend.parameter.dto.DepartmentResponse;
import com.tecmarket.backend.parameter.dto.UpdateDepartmentRequest;
import com.tecmarket.backend.parameter.service.DepartmentParameterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Tag(name = "Departments", description = "Endpoints for managing department parameters.")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/parameters")
public class DepartmentParameterController {

    private final DepartmentParameterService departmentParameterService;
    private final MessageService messageService;

    public DepartmentParameterController(
            DepartmentParameterService departmentParameterService,
            MessageService messageService) {
        this.departmentParameterService = departmentParameterService;
        this.messageService = messageService;
    }

    @Operation(summary = "List departments", description = "Returns all active departments.")
    @PreAuthorize("hasAnyAuthority('PARAMETER_VIEW','CATALOG_VIEW')")
    @GetMapping("/departments")
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> findAll() {
        List<DepartmentResponse> response = departmentParameterService.findAll();

        return ResponseEntity.ok(ApiResponse.success(messageService.get("department.list.success"), response));
    }

    @Operation(summary = "Find department by ID", description = "Returns a department by its identifier.")
    @PreAuthorize("hasAnyAuthority('PARAMETER_VIEW','CATALOG_VIEW')")
    @GetMapping("/departments/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponse>> findById(@PathVariable Integer id) {
        DepartmentResponse response = departmentParameterService.findById(id);

        return ResponseEntity.ok(ApiResponse.success(messageService.get("department.find.success"), response));
    }

    @Operation(summary = "List departments by country", description = "Returns active departments that belong to a country.")
    @PreAuthorize("hasAnyAuthority('PARAMETER_VIEW','CATALOG_VIEW')")
    @GetMapping("/countries/{countryId}/departments")
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> findByCountryId(@PathVariable Integer countryId) {
        List<DepartmentResponse> response = departmentParameterService.findByCountryId(countryId);

        return ResponseEntity.ok(ApiResponse.success(messageService.get("department.list.success"), response));
    }

    @Operation(summary = "Create department", description = "Creates a new department parameter.")
    @PreAuthorize("hasAuthority('PARAMETER_MANAGE')")
    @PostMapping("/departments")
    public ResponseEntity<ApiResponse<DepartmentResponse>> create(@Valid @RequestBody CreateDepartmentRequest request) {
        DepartmentResponse response = departmentParameterService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(messageService.get("department.create.success"), response));
    }

    @Operation(summary = "Update department", description = "Updates an existing department parameter.")
    @PreAuthorize("hasAuthority('PARAMETER_MANAGE')")
    @PutMapping("/departments/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponse>> update(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateDepartmentRequest request) {
        DepartmentResponse response = departmentParameterService.update(id, request);

        return ResponseEntity.ok(ApiResponse.success(messageService.get("department.update.success"), response));
    }

    @Operation(summary = "Disable department", description = "Soft-deletes a department by marking it inactive.")
    @PreAuthorize("hasAuthority('PARAMETER_MANAGE')")
    @DeleteMapping("/departments/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        departmentParameterService.delete(id);

        return ResponseEntity.ok(ApiResponse.success(messageService.get("department.delete.success"), null));
    }
}
