package com.tecmarket.backend.business.controller;

import com.tecmarket.backend.business.dto.BusinessScheduleResponse;
import com.tecmarket.backend.business.dto.CreateBusinessScheduleRequest;
import com.tecmarket.backend.business.dto.UpdateBusinessScheduleRequest;
import com.tecmarket.backend.business.service.BusinessScheduleService;
import com.tecmarket.backend.common.i18n.MessageService;
import com.tecmarket.backend.common.response.ApiResponse;
import com.tecmarket.backend.common.pagination.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

@RestController
@Tag(name = "Business Schedules", description = "Endpoints for managing schedules by business.")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/businesses/{businessId}/schedules")
public class BusinessScheduleController {

    private final BusinessScheduleService businessScheduleService;
    private final MessageService messageService;

    public BusinessScheduleController(
            BusinessScheduleService businessScheduleService,
            MessageService messageService) {
        this.businessScheduleService = businessScheduleService;
        this.messageService = messageService;
    }

    @Operation(summary = "List schedules by business", description = "Returns active schedules for a business.")
    @PreAuthorize("hasAuthority('BUSINESS_SCHEDULE_VIEW')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<BusinessScheduleResponse>>> findAll(
            @PathVariable Integer businessId,
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size,
            @AuthenticationPrincipal Jwt jwt) {
        PageResponse<BusinessScheduleResponse> response = businessScheduleService.findAllByBusiness(
                businessId, page, size, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("business_schedule.list.success"), response));
    }

    @Operation(summary = "Find schedule by id", description = "Returns one schedule by id.")
    @PreAuthorize("hasAuthority('BUSINESS_SCHEDULE_VIEW')")
    @GetMapping("/{scheduleId}")
    public ResponseEntity<ApiResponse<BusinessScheduleResponse>> findById(
            @PathVariable Integer businessId,
            @PathVariable Integer scheduleId,
            @AuthenticationPrincipal Jwt jwt) {
        BusinessScheduleResponse response = businessScheduleService.findById(businessId, scheduleId, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("business_schedule.find.success"), response));
    }

    @Operation(summary = "Create schedule", description = "Creates a business schedule slot.")
    @PreAuthorize("hasAuthority('BUSINESS_SCHEDULE_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<BusinessScheduleResponse>> create(
            @PathVariable Integer businessId,
            @Valid @RequestBody CreateBusinessScheduleRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        BusinessScheduleResponse response = businessScheduleService.create(businessId, request, jwt.getSubject());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(messageService.get("business_schedule.create.success"), response));
    }

    @Operation(summary = "Update schedule", description = "Updates a business schedule slot.")
    @PreAuthorize("hasAuthority('BUSINESS_SCHEDULE_UPDATE')")
    @PutMapping("/{scheduleId}")
    public ResponseEntity<ApiResponse<BusinessScheduleResponse>> update(
            @PathVariable Integer businessId,
            @PathVariable Integer scheduleId,
            @Valid @RequestBody UpdateBusinessScheduleRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        BusinessScheduleResponse response = businessScheduleService.update(businessId, scheduleId, request, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("business_schedule.update.success"), response));
    }

    @Operation(summary = "Delete schedule", description = "Soft deletes a business schedule slot.")
    @PreAuthorize("hasAuthority('BUSINESS_SCHEDULE_UPDATE')")
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Integer businessId,
            @PathVariable Integer scheduleId,
            @AuthenticationPrincipal Jwt jwt) {
        businessScheduleService.delete(businessId, scheduleId, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("business_schedule.delete.success"), null));
    }
}
