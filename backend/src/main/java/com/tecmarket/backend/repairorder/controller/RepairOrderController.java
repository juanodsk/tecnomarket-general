package com.tecmarket.backend.repairorder.controller;

import com.tecmarket.backend.repairorder.dto.CreateRepairOrderRequest;
import com.tecmarket.backend.repairorder.dto.CreateQuickRepairOrderRequest;
import com.tecmarket.backend.repairorder.dto.CreateRepairOrderPartRequest;
import com.tecmarket.backend.repairorder.dto.RepairOrderAttachmentResponse;
import com.tecmarket.backend.repairorder.dto.RepairOrderHistoryResponse;
import com.tecmarket.backend.repairorder.dto.RepairOrderPartResponse;
import com.tecmarket.backend.repairorder.dto.RepairOrderResponse;
import com.tecmarket.backend.repairorder.dto.QuickRepairOrderResponse;
import com.tecmarket.backend.repairorder.dto.QualityChecklistResponse;
import com.tecmarket.backend.repairorder.dto.UpsertQualityChecklistRequest;
import com.tecmarket.backend.repairorder.dto.UpdateRepairOrderPartRequest;
import com.tecmarket.backend.repairorder.dto.UpdateRepairOrderStatusRequest;
import com.tecmarket.backend.repairorder.dto.UpdateRepairOrderFinancialsRequest;
import com.tecmarket.backend.repairorder.dto.UpdateRepairOrderDiagnosisRequest;
import com.tecmarket.backend.repairorder.dto.UpdateRepairOrderAssignmentRequest;
import com.tecmarket.backend.repairorder.service.QualityChecklistService;
import com.tecmarket.backend.repairorder.service.RepairOrderService;
import com.tecmarket.backend.repairorder.service.QuickRepairOrderService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

import com.tecmarket.backend.business.entity.Business;
@RestController
@Tag(name = "Repair Orders", description = "Endpoints for creating and managing repair orders by business.")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/businesses/{businessId}/repair-orders")
public class RepairOrderController {

    private final RepairOrderService repairOrderService;
    private final QuickRepairOrderService quickRepairOrderService;
    private final QualityChecklistService qualityChecklistService;
    private final MessageService messageService;

    public RepairOrderController(
            RepairOrderService repairOrderService,
            QuickRepairOrderService quickRepairOrderService,
            QualityChecklistService qualityChecklistService,
            MessageService messageService) {
        this.repairOrderService = repairOrderService;
        this.quickRepairOrderService = quickRepairOrderService;
        this.qualityChecklistService = qualityChecklistService;
        this.messageService = messageService;
    }

    @Operation(
            summary = "Create quick repair order",
            description = "Atomically resolves or creates the customer, resolves or creates the device, creates the repair order and sends a password invitation for new accounts.")
    @PreAuthorize("hasAuthority('CUSTOMER_DEVICE_CREATE') and hasAuthority('REPAIR_ORDER_CREATE')")
    @PostMapping("/quick")
    public ResponseEntity<ApiResponse<QuickRepairOrderResponse>> createQuick(
            @PathVariable Integer businessId,
            @Valid @RequestBody CreateQuickRepairOrderRequest request,
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @AuthenticationPrincipal Jwt jwt) {
        QuickRepairOrderResponse response = quickRepairOrderService.create(
                businessId, request, jwt.getSubject(), idempotencyKey);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(messageService.get("repair_order.quick.create.success"), response));
    }

    @Operation(summary = "Create repair order", description = "Creates a repair order with sequence OS-{business}-{branch}-{sequence}.")
    @PreAuthorize("hasAuthority('REPAIR_ORDER_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<RepairOrderResponse>> create(
            @PathVariable Integer businessId,
            @Valid @RequestBody CreateRepairOrderRequest request,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey,
            @AuthenticationPrincipal Jwt jwt) {
        RepairOrderResponse response = repairOrderService.create(
                businessId, request, jwt.getSubject(), idempotencyKey);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(messageService.get("repair_order.create.success"), response));
    }

    @Operation(summary = "List repair orders by business", description = "Returns repair orders with filters, pagination and sorting.")
    @PreAuthorize("hasAuthority('REPAIR_ORDER_VIEW')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<RepairOrderResponse>>> findAll(
            @PathVariable Integer businessId,
            @RequestParam(required = false) Integer branchId,
            @RequestParam(required = false) String orderNumber,
            @RequestParam(required = false) Integer orderSequence,
            @RequestParam(required = false) Integer statusId,
            @RequestParam(required = false) Integer customerId,
            @RequestParam(required = false) String customer,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime receivedFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime receivedTo,
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size,
            @RequestParam(defaultValue = "receivedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @AuthenticationPrincipal Jwt jwt) {
        PageResponse<RepairOrderResponse> response = repairOrderService.findAllByBusiness(
                businessId,
                jwt.getSubject(),
                branchId,
                orderNumber,
                orderSequence,
                statusId,
                customerId,
                customer,
                receivedFrom,
                receivedTo,
                page,
                size,
                sortBy,
                sortDir);

        return ResponseEntity.ok(ApiResponse.success(messageService.get("repair_order.list.success"), response));
    }

    @Operation(summary = "Change repair order status", description = "Updates only the status of a repair order.")
    @PreAuthorize("hasAnyAuthority('REPAIR_ORDER_UPDATE', 'REPAIR_ORDER_CLOSE')")
    @PatchMapping("/{repairOrderId}/status")
    public ResponseEntity<ApiResponse<RepairOrderResponse>> updateStatus(
            @PathVariable Integer businessId,
            @PathVariable Integer repairOrderId,
            @Valid @RequestBody UpdateRepairOrderStatusRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        RepairOrderResponse response = repairOrderService.updateStatus(
                businessId, repairOrderId, request, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("repair_order.status.update.success"), response));
    }

    @Operation(summary = "Update repair order final price", description = "Sets the total amount charged before delivering the order.")
    @PreAuthorize("hasAuthority('REPAIR_ORDER_FINANCIAL_UPDATE')")
    @PatchMapping("/{repairOrderId}/financials")
    public ResponseEntity<ApiResponse<RepairOrderResponse>> updateFinancials(
            @PathVariable Integer businessId,
            @PathVariable Integer repairOrderId,
            @Valid @RequestBody UpdateRepairOrderFinancialsRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        RepairOrderResponse response = repairOrderService.updateFinancials(
                businessId, repairOrderId, request, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("repair_order.financials.update.success"), response));
    }

    @Operation(summary = "Update repair order diagnosis", description = "Updates only the technical diagnosis and records an immutable timeline event.")
    @PreAuthorize("hasAuthority('REPAIR_ORDER_UPDATE')")
    @PatchMapping("/{repairOrderId}/diagnosis")
    public ResponseEntity<ApiResponse<RepairOrderResponse>> updateDiagnosis(
            @PathVariable Integer businessId,
            @PathVariable Integer repairOrderId,
            @Valid @RequestBody UpdateRepairOrderDiagnosisRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        RepairOrderResponse response = repairOrderService.updateDiagnosis(
                businessId, repairOrderId, request, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("repair_order.diagnosis.update.success"), response));
    }

    @Operation(summary = "Assign repair order technician", description = "Changes only the assigned technician and records an immutable timeline event.")
    @PreAuthorize("hasAuthority('REPAIR_ORDER_UPDATE')")
    @PatchMapping("/{repairOrderId}/assignment")
    public ResponseEntity<ApiResponse<RepairOrderResponse>> updateAssignment(
            @PathVariable Integer businessId,
            @PathVariable Integer repairOrderId,
            @Valid @RequestBody UpdateRepairOrderAssignmentRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        RepairOrderResponse response = repairOrderService.updateAssignment(
                businessId, repairOrderId, request, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("repair_order.assignment.update.success"), response));
    }

    @Operation(summary = "Get repair order history", description = "Returns timeline/history entries for a repair order.")
    @PreAuthorize("hasAuthority('REPAIR_ORDER_VIEW')")
    @GetMapping("/{repairOrderId}/history")
    public ResponseEntity<ApiResponse<PageResponse<RepairOrderHistoryResponse>>> findHistory(
            @PathVariable Integer businessId,
            @PathVariable Integer repairOrderId,
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size,
            @AuthenticationPrincipal Jwt jwt) {
        PageResponse<RepairOrderHistoryResponse> response = repairOrderService.findHistoryByRepairOrder(
                businessId,
                repairOrderId,
                page,
                size,
                jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("repair_order.history.list.success"), response));
    }

    @Operation(
            summary = "Get repair order quality checklist",
            description = "Returns SI/NO/NO_APLICA quality checklist linked to this repair order.")
    @PreAuthorize("hasAuthority('BUSINESS_QUALITY_CHECK_VIEW')")
    @GetMapping("/{repairOrderId}/quality-checklist")
    public ResponseEntity<ApiResponse<QualityChecklistResponse>> findQualityChecklist(
            @PathVariable Integer businessId,
            @PathVariable Integer repairOrderId,
            @AuthenticationPrincipal Jwt jwt) {
        QualityChecklistResponse response = qualityChecklistService.findByRepairOrder(
                businessId,
                repairOrderId,
                jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("quality_checklist.find.success"), response));
    }

    @Operation(
            summary = "Save repair order quality checklist",
            description = "Creates or updates SI/NO/NO_APLICA quality checklist for this repair order.")
    @PreAuthorize("hasAuthority('BUSINESS_QUALITY_CHECK_UPDATE')")
    @PutMapping("/{repairOrderId}/quality-checklist")
    public ResponseEntity<ApiResponse<QualityChecklistResponse>> upsertQualityChecklist(
            @PathVariable Integer businessId,
            @PathVariable Integer repairOrderId,
            @Valid @RequestBody UpsertQualityChecklistRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        QualityChecklistResponse response = qualityChecklistService.upsertByRepairOrder(
                businessId,
                repairOrderId,
                request,
                jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("quality_checklist.upsert.success"), response));
    }

    @Operation(
            summary = "Delete repair order quality checklist",
            description = "Soft deletes checklist rows linked to this repair order.")
    @PreAuthorize("hasAuthority('BUSINESS_QUALITY_CHECK_UPDATE')")
    @DeleteMapping("/{repairOrderId}/quality-checklist")
    public ResponseEntity<ApiResponse<Void>> deleteQualityChecklist(
            @PathVariable Integer businessId,
            @PathVariable Integer repairOrderId,
            @AuthenticationPrincipal Jwt jwt) {
        qualityChecklistService.deleteByRepairOrder(businessId, repairOrderId, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("quality_checklist.delete.success"), null));
    }

    @Operation(summary = "List repair order attachments", description = "Returns evidence/attachments linked to a repair order.")
    @PreAuthorize("hasAuthority('REPAIR_ORDER_VIEW')")
    @GetMapping("/{repairOrderId}/attachments")
    public ResponseEntity<ApiResponse<PageResponse<RepairOrderAttachmentResponse>>> findAttachments(
            @PathVariable Integer businessId,
            @PathVariable Integer repairOrderId,
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size,
            @AuthenticationPrincipal Jwt jwt) {
        PageResponse<RepairOrderAttachmentResponse> response = repairOrderService.findAttachmentsByRepairOrder(
                businessId,
                repairOrderId,
                page,
                size,
                jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("repair_order.attachment.list.success"), response));
    }

    @Operation(summary = "Upload repair order evidence", description = "Uploads an evidence image to MinIO and links it to the repair order.")
    @PreAuthorize("hasAuthority('REPAIR_ORDER_UPLOAD_EVIDENCE')")
    @PostMapping(value = "/{repairOrderId}/attachments/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<RepairOrderAttachmentResponse>> uploadAttachment(
            @PathVariable Integer businessId,
            @PathVariable Integer repairOrderId,
            @RequestPart("file") MultipartFile file,
            @RequestPart(value = "description", required = false) String description,
            @AuthenticationPrincipal Jwt jwt) {
        RepairOrderAttachmentResponse response = repairOrderService.uploadAttachment(
                businessId,
                repairOrderId,
                file,
                description,
                jwt.getSubject());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(messageService.get("repair_order.attachment.create.success"), response));
    }

    @Operation(summary = "Delete repair order attachment", description = "Soft deletes an evidence attachment from a repair order.")
    @PreAuthorize("hasAuthority('REPAIR_ORDER_UPLOAD_EVIDENCE')")
    @DeleteMapping("/{repairOrderId}/attachments/{attachmentId}")
    public ResponseEntity<ApiResponse<Void>> deleteAttachment(
            @PathVariable Integer businessId,
            @PathVariable Integer repairOrderId,
            @PathVariable Integer attachmentId,
            @AuthenticationPrincipal Jwt jwt) {
        repairOrderService.deleteAttachment(
                businessId,
                repairOrderId,
                attachmentId,
                jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("repair_order.attachment.delete.success"), null));
    }

    @Operation(summary = "List repair order parts", description = "Returns spare parts used in a repair order.")
    @PreAuthorize("hasAuthority('REPAIR_ORDER_VIEW')")
    @GetMapping("/{repairOrderId}/parts")
    public ResponseEntity<ApiResponse<PageResponse<RepairOrderPartResponse>>> findParts(
            @PathVariable Integer businessId,
            @PathVariable Integer repairOrderId,
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size,
            @AuthenticationPrincipal Jwt jwt) {
        PageResponse<RepairOrderPartResponse> response = repairOrderService.findPartsByRepairOrder(
                businessId,
                repairOrderId,
                page,
                size,
                jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("repair_order.part.list.success"), response));
    }

    @Operation(summary = "Create repair order part", description = "Adds a spare part used in a repair order.")
    @PreAuthorize("hasAuthority('REPAIR_ORDER_UPDATE')")
    @PostMapping("/{repairOrderId}/parts")
    public ResponseEntity<ApiResponse<RepairOrderPartResponse>> createPart(
            @PathVariable Integer businessId,
            @PathVariable Integer repairOrderId,
            @Valid @RequestBody CreateRepairOrderPartRequest request,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey,
            @AuthenticationPrincipal Jwt jwt) {
        RepairOrderPartResponse response = repairOrderService.createPart(
                businessId,
                repairOrderId,
                request,
                jwt.getSubject(),
                idempotencyKey);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(messageService.get("repair_order.part.create.success"), response));
    }

    @Operation(summary = "Update repair order part", description = "Updates quantity/pricing/user for a part in a repair order.")
    @PreAuthorize("hasAuthority('REPAIR_ORDER_UPDATE')")
    @PutMapping("/{repairOrderId}/parts/{partId}")
    public ResponseEntity<ApiResponse<RepairOrderPartResponse>> updatePart(
            @PathVariable Integer businessId,
            @PathVariable Integer repairOrderId,
            @PathVariable Integer partId,
            @Valid @RequestBody UpdateRepairOrderPartRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        RepairOrderPartResponse response = repairOrderService.updatePart(
                businessId,
                repairOrderId,
                partId,
                request,
                jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("repair_order.part.update.success"), response));
    }

    @Operation(summary = "Delete repair order part", description = "Soft deletes a spare part from a repair order.")
    @PreAuthorize("hasAuthority('REPAIR_ORDER_UPDATE')")
    @DeleteMapping("/{repairOrderId}/parts/{partId}")
    public ResponseEntity<ApiResponse<Void>> deletePart(
            @PathVariable Integer businessId,
            @PathVariable Integer repairOrderId,
            @PathVariable Integer partId,
            @AuthenticationPrincipal Jwt jwt) {
        repairOrderService.deletePart(
                businessId,
                repairOrderId,
                partId,
                jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("repair_order.part.delete.success"), null));
    }
}
