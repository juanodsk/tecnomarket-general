package com.tecmarket.backend.repairorder.controller;

import com.tecmarket.backend.repairorder.dto.CreateCustomerDeviceRequest;
import com.tecmarket.backend.repairorder.dto.CustomerDeviceResponse;
import com.tecmarket.backend.repairorder.dto.UpdateCustomerDeviceRequest;
import com.tecmarket.backend.repairorder.service.CustomerDeviceService;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Customer Devices", description = "Endpoints for managing customer devices by business.")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/businesses/{businessId}/customer-devices")
public class CustomerDeviceController {

    private final CustomerDeviceService customerDeviceService;
    private final MessageService messageService;

    public CustomerDeviceController(CustomerDeviceService customerDeviceService, MessageService messageService) {
        this.customerDeviceService = customerDeviceService;
        this.messageService = messageService;
    }

    @Operation(summary = "List customer devices by business", description = "Returns active customer devices for a business.")
    @PreAuthorize("hasAuthority('CUSTOMER_DEVICE_VIEW')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CustomerDeviceResponse>>> findAll(
            @PathVariable Integer businessId,
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size,
            @AuthenticationPrincipal Jwt jwt) {
        PageResponse<CustomerDeviceResponse> response = customerDeviceService.findAllByBusiness(
                businessId,
                page,
                size,
                jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("customer_device.list.success"), response));
    }

    @Operation(summary = "Find customer device by id", description = "Returns one customer device by id and business.")
    @PreAuthorize("hasAuthority('CUSTOMER_DEVICE_VIEW')")
    @GetMapping("/{customerDeviceId}")
    public ResponseEntity<ApiResponse<CustomerDeviceResponse>> findById(
            @PathVariable Integer businessId,
            @PathVariable Integer customerDeviceId,
            @AuthenticationPrincipal Jwt jwt) {
        CustomerDeviceResponse response = customerDeviceService.findById(
                businessId,
                customerDeviceId,
                jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("customer_device.find.success"), response));
    }

    @Operation(summary = "Create customer device", description = "Registers a customer device inside a business.")
    @PreAuthorize("hasAuthority('CUSTOMER_DEVICE_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<CustomerDeviceResponse>> create(
            @PathVariable Integer businessId,
            @Valid @RequestBody CreateCustomerDeviceRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        CustomerDeviceResponse response = customerDeviceService.create(
                businessId,
                request,
                jwt.getSubject());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(messageService.get("customer_device.create.success"), response));
    }

    @Operation(summary = "Update customer device", description = "Updates a customer device inside a business.")
    @PreAuthorize("hasAuthority('CUSTOMER_DEVICE_UPDATE')")
    @PutMapping("/{customerDeviceId}")
    public ResponseEntity<ApiResponse<CustomerDeviceResponse>> update(
            @PathVariable Integer businessId,
            @PathVariable Integer customerDeviceId,
            @Valid @RequestBody UpdateCustomerDeviceRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        CustomerDeviceResponse response = customerDeviceService.update(
                businessId,
                customerDeviceId,
                request,
                jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("customer_device.update.success"), response));
    }

    @Operation(summary = "Disable customer device", description = "Soft-deletes a customer device.")
    @PreAuthorize("hasAuthority('CUSTOMER_DEVICE_UPDATE')")
    @DeleteMapping("/{customerDeviceId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Integer businessId,
            @PathVariable Integer customerDeviceId,
            @AuthenticationPrincipal Jwt jwt) {
        customerDeviceService.delete(businessId, customerDeviceId, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("customer_device.delete.success"), null));
    }
}
