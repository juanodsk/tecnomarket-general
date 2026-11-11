package com.tecmarket.backend.repairorder.dto;

import com.tecmarket.backend.repairorder.entity.RepairOrderQualityCheckResult;

public record QualityChecklistItemResponse(
        Integer qualityCheckId,
        Integer businessQualityTestId,
        String qualityTestCode,
        String qualityTestName,
        Boolean required,
        RepairOrderQualityCheckResult result,
        String notes) {
}
