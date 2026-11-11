package com.tecmarket.backend.appointment.controller;

import com.tecmarket.backend.appointment.dto.AppointmentResponse;
import com.tecmarket.backend.appointment.dto.CreateAppointmentRequest;
import com.tecmarket.backend.appointment.dto.UpdateAppointmentRequest;
import com.tecmarket.backend.appointment.dto.UpdateAppointmentStatusRequest;
import com.tecmarket.backend.appointment.service.AppointmentService;
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
@Tag(name = "Appointments", description = "Endpoints for managing appointments by business.")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/businesses/{businessId}/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final MessageService messageService;

    public AppointmentController(AppointmentService appointmentService, MessageService messageService) {
        this.appointmentService = appointmentService;
        this.messageService = messageService;
    }

    @Operation(summary = "List appointments by business", description = "Returns active appointments for a business.")
    @PreAuthorize("hasAuthority('APPOINTMENT_VIEW')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<AppointmentResponse>>> findAll(
            @PathVariable Integer businessId,
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size,
            @AuthenticationPrincipal Jwt jwt) {
        PageResponse<AppointmentResponse> response = appointmentService.findAllByBusiness(
                businessId, page, size, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("appointment.list.success"), response));
    }

    @Operation(summary = "Find appointment by id", description = "Returns one appointment by id and business.")
    @PreAuthorize("hasAuthority('APPOINTMENT_VIEW')")
    @GetMapping("/{appointmentId}")
    public ResponseEntity<ApiResponse<AppointmentResponse>> findById(
            @PathVariable Integer businessId,
            @PathVariable Integer appointmentId,
            @AuthenticationPrincipal Jwt jwt) {
        AppointmentResponse response = appointmentService.findById(businessId, appointmentId, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("appointment.find.success"), response));
    }

    @Operation(summary = "Create appointment", description = "Creates an appointment inside a business.")
    @PreAuthorize("hasAuthority('APPOINTMENT_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<AppointmentResponse>> create(
            @PathVariable Integer businessId,
            @Valid @RequestBody CreateAppointmentRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        AppointmentResponse response = appointmentService.create(businessId, request, jwt.getSubject());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(messageService.get("appointment.create.success"), response));
    }

    @Operation(summary = "Update appointment", description = "Updates an appointment inside a business.")
    @PreAuthorize("hasAuthority('APPOINTMENT_UPDATE')")
    @PutMapping("/{appointmentId}")
    public ResponseEntity<ApiResponse<AppointmentResponse>> update(
            @PathVariable Integer businessId,
            @PathVariable Integer appointmentId,
            @Valid @RequestBody UpdateAppointmentRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        AppointmentResponse response = appointmentService.update(businessId, appointmentId, request, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("appointment.update.success"), response));
    }

    @Operation(summary = "Disable appointment", description = "Soft-deletes an appointment.")
    @PreAuthorize("hasAuthority('APPOINTMENT_UPDATE')")
    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Integer businessId,
            @PathVariable Integer appointmentId,
            @AuthenticationPrincipal Jwt jwt) {
        appointmentService.delete(businessId, appointmentId, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("appointment.delete.success"), null));
    }

    @Operation(summary = "Change appointment status", description = "Updates only the status of an appointment.")
    @PreAuthorize("hasAuthority('APPOINTMENT_UPDATE')")
    @PatchMapping("/{appointmentId}/status")
    public ResponseEntity<ApiResponse<AppointmentResponse>> updateStatus(
            @PathVariable Integer businessId,
            @PathVariable Integer appointmentId,
            @Valid @RequestBody UpdateAppointmentStatusRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        AppointmentResponse response = appointmentService.updateStatus(businessId, appointmentId, request, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("appointment.status.update.success"), response));
    }
}
