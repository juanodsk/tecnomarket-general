package com.tecmarket.backend.repairorder.controller;

import com.tecmarket.backend.repairorder.dto.CustomerRepairOrderTrackingResponse;
import com.tecmarket.backend.repairorder.service.RepairOrderService;
import com.tecmarket.backend.common.i18n.MessageService;
import com.tecmarket.backend.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "My Repair Orders", description = "Secure repair-order tracking for the authenticated customer.")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/me/repair-orders")
public class CustomerRepairOrderController {

    private final RepairOrderService repairOrderService;
    private final MessageService messageService;

    public CustomerRepairOrderController(RepairOrderService repairOrderService, MessageService messageService) {
        this.repairOrderService = repairOrderService;
        this.messageService = messageService;
    }

    @Operation(summary = "Track my repair order", description = "Finds an order by its public number only when it belongs to the authenticated customer.")
    @GetMapping("/{orderNumber}")
    public ResponseEntity<ApiResponse<CustomerRepairOrderTrackingResponse>> findMyOrder(
            @PathVariable String orderNumber,
            @AuthenticationPrincipal Jwt jwt) {
        CustomerRepairOrderTrackingResponse response = repairOrderService.findCustomerOrder(orderNumber, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("repair_order.find.success"), response));
    }
}
