package com.tecmarket.backend.parameter.controller;

import com.tecmarket.backend.common.i18n.MessageService;
import com.tecmarket.backend.common.response.ApiResponse;
import com.tecmarket.backend.parameter.dto.CityResponse;
import com.tecmarket.backend.parameter.dto.CreateCityRequest;
import com.tecmarket.backend.parameter.dto.UpdateCityRequest;
import com.tecmarket.backend.parameter.service.CityParameterService;
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
@Tag(name = "Cities", description = "Endpoints for managing city parameters.")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/parameters")
public class CityParameterController {

    private final CityParameterService cityParameterService;
    private final MessageService messageService;

    public CityParameterController(
            CityParameterService cityParameterService,
            MessageService messageService) {
        this.cityParameterService = cityParameterService;
        this.messageService = messageService;
    }

    @Operation(summary = "List cities", description = "Returns all active cities.")
    @PreAuthorize("hasAnyAuthority('PARAMETER_VIEW','CATALOG_VIEW')")
    @GetMapping("/cities")
    public ResponseEntity<ApiResponse<List<CityResponse>>> findAll() {
        List<CityResponse> response = cityParameterService.findAll();

        return ResponseEntity.ok(ApiResponse.success(messageService.get("city.list.success"), response));
    }

    @Operation(summary = "Find city by ID", description = "Returns a city by its identifier.")
    @PreAuthorize("hasAnyAuthority('PARAMETER_VIEW','CATALOG_VIEW')")
    @GetMapping("/cities/{id}")
    public ResponseEntity<ApiResponse<CityResponse>> findById(@PathVariable Integer id) {
        CityResponse response = cityParameterService.findById(id);

        return ResponseEntity.ok(ApiResponse.success(messageService.get("city.find.success"), response));
    }

    @Operation(summary = "List cities by country", description = "Returns active cities that belong to a country.")
    @PreAuthorize("hasAnyAuthority('PARAMETER_VIEW','CATALOG_VIEW')")
    @GetMapping("/countries/{countryId}/cities")
    public ResponseEntity<ApiResponse<List<CityResponse>>> findByCountryId(@PathVariable Integer countryId) {
        List<CityResponse> response = cityParameterService.findByCountryId(countryId);

        return ResponseEntity.ok(ApiResponse.success(messageService.get("city.list.success"), response));
    }

    @Operation(summary = "List cities by department", description = "Returns active cities that belong to a department.")
    @PreAuthorize("hasAnyAuthority('PARAMETER_VIEW','CATALOG_VIEW')")
    @GetMapping("/departments/{departmentId}/cities")
    public ResponseEntity<ApiResponse<List<CityResponse>>> findByDepartmentId(@PathVariable Integer departmentId) {
        List<CityResponse> response = cityParameterService.findByDepartmentId(departmentId);

        return ResponseEntity.ok(ApiResponse.success(messageService.get("city.list.success"), response));
    }

    @Operation(summary = "Create city", description = "Creates a new city parameter.")
    @PreAuthorize("hasAuthority('PARAMETER_MANAGE')")
    @PostMapping("/cities")
    public ResponseEntity<ApiResponse<CityResponse>> create(@Valid @RequestBody CreateCityRequest request) {
        CityResponse response = cityParameterService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(messageService.get("city.create.success"), response));
    }

    @Operation(summary = "Update city", description = "Updates an existing city parameter.")
    @PreAuthorize("hasAuthority('PARAMETER_MANAGE')")
    @PutMapping("/cities/{id}")
    public ResponseEntity<ApiResponse<CityResponse>> update(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateCityRequest request) {
        CityResponse response = cityParameterService.update(id, request);

        return ResponseEntity.ok(ApiResponse.success(messageService.get("city.update.success"), response));
    }

    @Operation(summary = "Disable city", description = "Soft-deletes a city by marking it inactive.")
    @PreAuthorize("hasAuthority('PARAMETER_MANAGE')")
    @DeleteMapping("/cities/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        cityParameterService.delete(id);

        return ResponseEntity.ok(ApiResponse.success(messageService.get("city.delete.success"), null));
    }
}

