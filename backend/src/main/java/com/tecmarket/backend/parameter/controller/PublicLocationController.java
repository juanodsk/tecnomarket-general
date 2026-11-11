package com.tecmarket.backend.parameter.controller;

import com.tecmarket.backend.common.i18n.MessageService;
import com.tecmarket.backend.common.response.ApiResponse;
import com.tecmarket.backend.parameter.dto.CityResponse;
import com.tecmarket.backend.parameter.dto.CountryResponse;
import com.tecmarket.backend.parameter.dto.DepartmentResponse;
import com.tecmarket.backend.parameter.service.CityParameterService;
import com.tecmarket.backend.parameter.service.CountryParameterService;
import com.tecmarket.backend.parameter.service.DepartmentParameterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Public locations", description = "Public endpoints for location selectors.")
@RequestMapping("/api/v1/public/locations")
public class PublicLocationController {

    private final CountryParameterService countryParameterService;
    private final DepartmentParameterService departmentParameterService;
    private final CityParameterService cityParameterService;
    private final MessageService messageService;

    public PublicLocationController(
            CountryParameterService countryParameterService,
            DepartmentParameterService departmentParameterService,
            CityParameterService cityParameterService,
            MessageService messageService) {
        this.countryParameterService = countryParameterService;
        this.departmentParameterService = departmentParameterService;
        this.cityParameterService = cityParameterService;
        this.messageService = messageService;
    }

    @Operation(summary = "List public countries", description = "Returns active countries for public forms.")
    @GetMapping("/countries")
    public ResponseEntity<ApiResponse<List<CountryResponse>>> findCountries() {
        List<CountryResponse> response = countryParameterService.findAll();

        return ResponseEntity.ok(ApiResponse.success(messageService.get("country.list.success"), response));
    }

    @Operation(summary = "List public departments by country", description = "Returns active departments for public forms.")
    @GetMapping("/countries/{countryId}/departments")
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> findDepartmentsByCountry(@PathVariable Integer countryId) {
        List<DepartmentResponse> response = departmentParameterService.findByCountryId(countryId);

        return ResponseEntity.ok(ApiResponse.success(messageService.get("department.list.success"), response));
    }

    @Operation(summary = "List public cities by department", description = "Returns active cities for public forms.")
    @GetMapping("/departments/{departmentId}/cities")
    public ResponseEntity<ApiResponse<List<CityResponse>>> findCitiesByDepartment(@PathVariable Integer departmentId) {
        List<CityResponse> response = cityParameterService.findByDepartmentId(departmentId);

        return ResponseEntity.ok(ApiResponse.success(messageService.get("city.list.success"), response));
    }
}
