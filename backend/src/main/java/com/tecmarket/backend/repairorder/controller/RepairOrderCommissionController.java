package com.tecmarket.backend.repairorder.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import com.tecmarket.backend.repairorder.dto.RepairOrderCommissionResponse;
import com.tecmarket.backend.repairorder.entity.RepairOrderCommissionStatus;
import com.tecmarket.backend.repairorder.service.RepairOrderCommissionService;
import com.tecmarket.backend.common.i18n.MessageService;
import com.tecmarket.backend.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.tecmarket.backend.business.entity.Business;
@RestController
@ConditionalOnProperty(prefix = "app.features", name = "commissions-enabled", havingValue = "true")
@Tag(name = "Repair Order Commissions", description = "Endpoints for managing technician repair commissions.")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/businesses/{businessId}")
public class RepairOrderCommissionController {

    private final RepairOrderCommissionService commissionService;
    private final MessageService messageService;

    public RepairOrderCommissionController(
            RepairOrderCommissionService commissionService,
            MessageService messageService) {
        this.commissionService = commissionService;
        this.messageService = messageService;
    }

    @Operation(summary = "List repair commissions", description = "Returns historical commissions generated for the business.")
    @PreAuthorize("hasAnyAuthority('BUSINESS_COMMISSION_VIEW','BUSINESS_COMMISSION_MANAGE')")
    @GetMapping("/commissions")
    public ResponseEntity<ApiResponse<List<RepairOrderCommissionResponse>>> findAll(
            @PathVariable Integer businessId,
            @RequestParam(required = false) Integer businessUserId,
            @RequestParam(required = false) RepairOrderCommissionStatus status,
            @AuthenticationPrincipal Jwt jwt) {
        List<RepairOrderCommissionResponse> response = commissionService.findAll(
                businessId, businessUserId, status, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("repair_order.commission.list.success"), response));
    }

    @Operation(summary = "Find repair commission", description = "Returns a commission by its identifier.")
    @PreAuthorize("hasAnyAuthority('BUSINESS_COMMISSION_VIEW','BUSINESS_COMMISSION_MANAGE')")
    @GetMapping("/commissions/{commissionId}")
    public ResponseEntity<ApiResponse<RepairOrderCommissionResponse>> findById(
            @PathVariable Integer businessId,
            @PathVariable Integer commissionId,
            @AuthenticationPrincipal Jwt jwt) {
        RepairOrderCommissionResponse response = commissionService.findById(
                businessId, commissionId, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("repair_order.commission.find.success"), response));
    }

    @Operation(summary = "Find commission by repair order", description = "Returns the historical commission generated when the order was included in a settlement.")
    @PreAuthorize("hasAnyAuthority('BUSINESS_COMMISSION_VIEW','BUSINESS_COMMISSION_MANAGE')")
    @GetMapping("/repair-orders/{repairOrderId}/commission")
    public ResponseEntity<ApiResponse<RepairOrderCommissionResponse>> findByRepairOrder(
            @PathVariable Integer businessId,
            @PathVariable Integer repairOrderId,
            @AuthenticationPrincipal Jwt jwt) {
        RepairOrderCommissionResponse response = commissionService.findByRepairOrder(
                businessId, repairOrderId, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("repair_order.commission.find.success"), response));
    }

}
