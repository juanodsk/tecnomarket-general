package com.tecmarket.backend.repairorder.controller;

import com.tecmarket.backend.repairorder.dto.QualityChecklistResponse;
import com.tecmarket.backend.repairorder.dto.UpsertQualityChecklistRequest;
import com.tecmarket.backend.repairorder.service.QualityChecklistService;
import com.tecmarket.backend.common.i18n.MessageService;
import com.tecmarket.backend.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Quality Checklists", description = "Checklist SI/NO/NO_APLICA para ingreso de dispositivos por ticket.")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/businesses/{businessId}/quality-checklists")
public class QualityChecklistController {

    private final QualityChecklistService qualityChecklistService;
    private final MessageService messageService;

    public QualityChecklistController(QualityChecklistService qualityChecklistService, MessageService messageService) {
        this.qualityChecklistService = qualityChecklistService;
        this.messageService = messageService;
    }

    @Operation(summary = "Consultar checklist por ticket", description = "Retorna el checklist capturado para un ticket de ingreso.")
    @PreAuthorize("hasAuthority('BUSINESS_QUALITY_CHECK_VIEW')")
    @GetMapping("/{ticketCode}")
    public ResponseEntity<ApiResponse<QualityChecklistResponse>> findByTicket(
            @PathVariable Integer businessId,
            @PathVariable String ticketCode,
            @AuthenticationPrincipal Jwt jwt) {
        QualityChecklistResponse response = qualityChecklistService.findByTicket(businessId, ticketCode, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("quality_checklist.find.success"), response));
    }

    @Operation(summary = "Guardar checklist por ticket", description = "Crea/actualiza el checklist completo (SI/NO/NO_APLICA) para un ticket de ingreso.")
    @PreAuthorize("hasAuthority('BUSINESS_QUALITY_CHECK_UPDATE')")
    @PutMapping("/{ticketCode}")
    public ResponseEntity<ApiResponse<QualityChecklistResponse>> upsertByTicket(
            @PathVariable Integer businessId,
            @PathVariable String ticketCode,
            @Valid @RequestBody UpsertQualityChecklistRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        QualityChecklistResponse response = qualityChecklistService.upsertByTicket(
                businessId, ticketCode, request, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("quality_checklist.upsert.success"), response));
    }

    @Operation(summary = "Eliminar checklist por ticket", description = "Desactiva el checklist de un ticket de ingreso.")
    @PreAuthorize("hasAuthority('BUSINESS_QUALITY_CHECK_UPDATE')")
    @DeleteMapping("/{ticketCode}")
    public ResponseEntity<ApiResponse<Void>> deleteByTicket(
            @PathVariable Integer businessId,
            @PathVariable String ticketCode,
            @AuthenticationPrincipal Jwt jwt) {
        qualityChecklistService.deleteByTicket(businessId, ticketCode, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("quality_checklist.delete.success"), null));
    }
}
