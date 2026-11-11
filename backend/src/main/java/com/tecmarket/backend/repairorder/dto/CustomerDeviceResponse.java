package com.tecmarket.backend.repairorder.dto;

import java.time.LocalDateTime;

public record CustomerDeviceResponse(
        Integer id,
        Integer businessId,
        Integer customerId,
        String customerDocumentTypeCode,
        String customerDocumentNumber,
        String customerName,
        String customerSurname,
        String customerEmail,
        Integer deviceTypeId,
        String deviceTypeCode,
        String deviceTypeName,
        Integer brandId,
        String brandName,
        String model,
        String serialNumber,
        String imei,
        Integer registeredByBusinessUserId,
        String registeredByBusinessUserName,
        String color,
        LocalDateTime registeredAt,
        Boolean active) {
}
