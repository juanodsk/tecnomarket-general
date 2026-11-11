package com.tecmarket.backend.user.dto;

public record CreateInvitedCustomerRequest(
        String documentTypeCode,
        String documentNumber,
        String name,
        String surname,
        String email,
        String address,
        String phone,
        Integer countryId,
        Integer departmentId,
        Integer cityId) {
}
