package com.tecmarket.backend.repairorder.dto;

public record QuickRepairOrderCustomerResponse(
        Integer id,
        String documentTypeCode,
        String documentNumber,
        String name,
        String surname,
        String email,
        String phone,
        Boolean emailVerified) {
}
