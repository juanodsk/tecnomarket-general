package com.tecmarket.backend.parameter.controller;

import com.tecmarket.backend.common.i18n.MessageService;
import com.tecmarket.backend.common.response.ApiResponse;
import com.tecmarket.backend.parameter.dto.CountryResponse;
import com.tecmarket.backend.parameter.dto.CreateCountryRequest;
import com.tecmarket.backend.parameter.dto.UpdateCountryRequest;
import com.tecmarket.backend.parameter.service.CountryParameterService;
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
@Tag(name = "Countries", description = "Endpoints for managing country parameters.")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/parameters/countries")
public class CountryParameterController {

    private final CountryParameterService countryParameterService;
    private final MessageService messageService;

    public CountryParameterController(
            CountryParameterService countryParameterService,
            MessageService messageService) {
        this.countryParameterService = countryParameterService;
        this.messageService = messageService;
    }

    @Operation(summary = "List countries", description = "Returns all active countries.")
    @PreAuthorize("hasAnyAuthority('PARAMETER_VIEW','CATALOG_VIEW')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CountryResponse>>> findAll() {
        List<CountryResponse> response = countryParameterService.findAll();

        return ResponseEntity.ok(ApiResponse.success(messageService.get("country.list.success"), response));
    }

    @Operation(summary = "Find country by ID", description = "Returns a country by its identifier.")
    @PreAuthorize("hasAnyAuthority('PARAMETER_VIEW','CATALOG_VIEW')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CountryResponse>> findById(@PathVariable Integer id) {
        CountryResponse response = countryParameterService.findById(id);

        return ResponseEntity.ok(ApiResponse.success(messageService.get("country.find.success"), response));
    }

    @Operation(summary = "Create country", description = "Creates a new country parameter.")
    @PreAuthorize("hasAuthority('PARAMETER_MANAGE')")
    @PostMapping
    public ResponseEntity<ApiResponse<CountryResponse>> create(@Valid @RequestBody CreateCountryRequest request) {
        CountryResponse response = countryParameterService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(messageService.get("country.create.success"), response));
    }

    @Operation(summary = "Update country", description = "Updates a country parameter.")
    @PreAuthorize("hasAuthority('PARAMETER_MANAGE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CountryResponse>> update(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateCountryRequest request) {
        CountryResponse response = countryParameterService.update(id, request);

        return ResponseEntity.ok(ApiResponse.success(messageService.get("country.update.success"), response));
    }

    @Operation(summary = "Disable country", description = "Soft-deletes a country by marking it inactive.")
    @PreAuthorize("hasAuthority('PARAMETER_MANAGE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        countryParameterService.delete(id);

        return ResponseEntity.ok(ApiResponse.success(messageService.get("country.delete.success"), null));
    }
}

