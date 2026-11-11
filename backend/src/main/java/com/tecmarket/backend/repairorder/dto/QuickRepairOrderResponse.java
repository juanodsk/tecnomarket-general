package com.tecmarket.backend.repairorder.dto;

public record QuickRepairOrderResponse(
        QuickRepairOrderCustomerResponse customer,
        CustomerDeviceResponse device,
        RepairOrderResponse repairOrder) {
}
