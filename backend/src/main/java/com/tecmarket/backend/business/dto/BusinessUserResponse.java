package com.tecmarket.backend.business.dto;

import java.math.BigDecimal;
import java.util.List;

public record BusinessUserResponse(
        Integer id,
        Integer businessId,
        Integer userId,
        String userName,
        String userSurname,
        String userEmail,
        Integer roleId,
        String roleCode,
        String roleName,
        List<Integer> branchIds,
        BigDecimal commissionPercentage,
        Boolean active) {
}
