package com.tecmarket.backend.repairorder.mapper;

import com.tecmarket.backend.repairorder.dto.BusinessQualityTestResponse;
import com.tecmarket.backend.repairorder.entity.BusinessQualityTest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BusinessQualityTestMapper {

    default BusinessQualityTestResponse toResponse(BusinessQualityTest entity) {
        return new BusinessQualityTestResponse(
                entity.getId(),
                entity.getBusiness().getId(),
                entity.getDeviceType().getId(),
                entity.getDeviceType().getCode(),
                entity.getDeviceType().getName(),
                entity.getCode(),
                entity.getName(),
                entity.getDescription(),
                entity.getRequired(),
                entity.getSortOrder(),
                entity.getActive());
    }
}

