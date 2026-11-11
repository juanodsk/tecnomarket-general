package com.tecmarket.backend.repairorder.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import com.tecmarket.backend.business.dto.*;
import com.tecmarket.backend.repairorder.entity.CommissionSettlementStatus;
import com.tecmarket.backend.repairorder.service.CommissionSettlementService;
import com.tecmarket.backend.common.i18n.MessageService;
import com.tecmarket.backend.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.repairorder.dto.CommissionSettlementResponse;
import com.tecmarket.backend.repairorder.dto.CreateCommissionSettlementRequest;
import com.tecmarket.backend.repairorder.dto.EligibleCommissionOrderResponse;
import com.tecmarket.backend.repairorder.dto.TechnicianCommissionSummaryResponse;
import com.tecmarket.backend.repairorder.dto.UpdateCommissionSettlementStatusRequest;
@RestController
@ConditionalOnProperty(prefix = "app.features", name = "commissions-enabled", havingValue = "true")
@Tag(name = "Commission Settlements", description = "Liquidaciones agrupadas de comisiones por tecnico.")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/businesses/{businessId}/commission-settlements")
public class CommissionSettlementController {

    private final CommissionSettlementService settlementService;
    private final MessageService messageService;

    public CommissionSettlementController(
            CommissionSettlementService settlementService,
            MessageService messageService) {
        this.settlementService = settlementService;
        this.messageService = messageService;
    }

    @Operation(summary = "List technician commission summaries", description = "Returns eligible order totals grouped by technician for a period.")
    @PreAuthorize("hasAnyAuthority('BUSINESS_COMMISSION_VIEW','BUSINESS_COMMISSION_MANAGE')")
    @GetMapping("/technician-summaries")
    public ResponseEntity<ApiResponse<List<TechnicianCommissionSummaryResponse>>> findTechnicianSummaries(
            @PathVariable Integer businessId,
            @RequestParam LocalDate periodStart,
            @RequestParam LocalDate periodEnd,
            @AuthenticationPrincipal Jwt jwt) {
        List<TechnicianCommissionSummaryResponse> response = settlementService.findTechnicianSummaries(
                businessId, periodStart, periodEnd, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(
                messageService.get("commission_settlement.technician_summary.list.success"), response));
    }

    @Operation(summary = "List eligible orders", description = "Returns unliquidated orders for a technician and period.")
    @PreAuthorize("hasAnyAuthority('BUSINESS_COMMISSION_VIEW','BUSINESS_COMMISSION_MANAGE')")
    @GetMapping("/eligible-orders")
    public ResponseEntity<ApiResponse<List<EligibleCommissionOrderResponse>>> findEligibleOrders(
            @PathVariable Integer businessId,
            @RequestParam Integer businessUserId,
            @RequestParam LocalDate periodStart,
            @RequestParam LocalDate periodEnd,
            @AuthenticationPrincipal Jwt jwt) {
        List<EligibleCommissionOrderResponse> response = settlementService.findEligibleOrders(
                businessId, businessUserId, periodStart, periodEnd, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(
                messageService.get("commission_settlement.eligible.list.success"), response));
    }

    @Operation(summary = "Create settlement", description = "Creates a draft settlement from selected repair orders.")
    @PreAuthorize("hasAuthority('BUSINESS_COMMISSION_MANAGE')")
    @PostMapping
    public ResponseEntity<ApiResponse<CommissionSettlementResponse>> create(
            @PathVariable Integer businessId,
            @Valid @RequestBody CreateCommissionSettlementRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        CommissionSettlementResponse response = settlementService.create(businessId, request, jwt.getSubject());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(
                messageService.get("commission_settlement.create.success"), response));
    }

    @Operation(summary = "List settlements", description = "Returns commission settlements for the business.")
    @PreAuthorize("hasAnyAuthority('BUSINESS_COMMISSION_VIEW','BUSINESS_COMMISSION_MANAGE')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CommissionSettlementResponse>>> findAll(
            @PathVariable Integer businessId,
            @RequestParam(required = false) Integer businessUserId,
            @RequestParam(required = false) CommissionSettlementStatus status,
            @AuthenticationPrincipal Jwt jwt) {
        List<CommissionSettlementResponse> response = settlementService.findAll(
                businessId, businessUserId, status, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("commission_settlement.list.success"), response));
    }

    @Operation(summary = "Find settlement", description = "Returns a settlement and its selected orders.")
    @PreAuthorize("hasAnyAuthority('BUSINESS_COMMISSION_VIEW','BUSINESS_COMMISSION_MANAGE')")
    @GetMapping("/{settlementId}")
    public ResponseEntity<ApiResponse<CommissionSettlementResponse>> findById(
            @PathVariable Integer businessId,
            @PathVariable Integer settlementId,
            @AuthenticationPrincipal Jwt jwt) {
        CommissionSettlementResponse response = settlementService.findById(
                businessId, settlementId, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("commission_settlement.find.success"), response));
    }

    @Operation(summary = "Update settlement status", description = "Transitions a settlement from DRAFT to APPROVED and PAID.")
    @PreAuthorize("hasAuthority('BUSINESS_COMMISSION_MANAGE')")
    @PatchMapping("/{settlementId}/status")
    public ResponseEntity<ApiResponse<CommissionSettlementResponse>> updateStatus(
            @PathVariable Integer businessId,
            @PathVariable Integer settlementId,
            @Valid @RequestBody UpdateCommissionSettlementStatusRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        CommissionSettlementResponse response = settlementService.updateStatus(
                businessId, settlementId, request, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(
                messageService.get("commission_settlement.status.update.success"), response));
    }
}
