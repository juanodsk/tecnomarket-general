package com.tecmarket.backend.parameter.dto;

import java.time.LocalDateTime;

public record UserParameterResponse(
        Integer id,
        String documentTypeCode,
        String documentTypeName,
        String documentNumber,
        String name,
        String surname,
        String email,
        String phone,
        String address,
        Integer countryId,
        String countryName,
        Integer departmentId,
        String departmentName,
        Integer cityId,
        String cityName,
        Boolean active,
        Boolean emailVerified,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
