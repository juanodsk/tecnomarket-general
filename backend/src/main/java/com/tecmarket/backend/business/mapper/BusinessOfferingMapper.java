package com.tecmarket.backend.business.mapper;

import com.tecmarket.backend.business.dto.BusinessOfferingResponse;
import com.tecmarket.backend.business.entity.BusinessOffering;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BusinessOfferingMapper {

    default BusinessOfferingResponse toResponse(BusinessOffering entity) {
        return new BusinessOfferingResponse(
                entity.getId(),
                entity.getBusiness().getId(),
                entity.getServiceCategory().getId(),
                entity.getServiceCategory().getCode(),
                entity.getServiceCategory().getName(),
                entity.getDeviceType().getId(),
                entity.getDeviceType().getCode(),
                entity.getDeviceType().getName(),
                entity.getName(),
                entity.getDescription(),
                entity.getBasePrice(),
                entity.getEstimatedTimeMinutes(),
                entity.getImageUrl(),
                entity.getActive());
    }
}

