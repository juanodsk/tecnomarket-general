package com.tecmarket.backend.business.dto;

import java.math.BigDecimal;

public record BusinessTechnicianOptionResponse(
        Integer businessUserId,
        Integer userId,
        String userName,
        String userSurname,
        String userEmail,
        String roleCode,
        String roleName,
        BigDecimal commissionPercentage) {
}
