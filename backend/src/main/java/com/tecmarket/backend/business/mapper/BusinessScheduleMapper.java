package com.tecmarket.backend.business.mapper;

import com.tecmarket.backend.business.dto.BusinessScheduleResponse;
import com.tecmarket.backend.business.entity.BusinessSchedule;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BusinessScheduleMapper {

    default BusinessScheduleResponse toResponse(BusinessSchedule entity) {
        return new BusinessScheduleResponse(
                entity.getId(),
                entity.getBusiness().getId(),
                entity.getBranch().getId(),
                entity.getBranch().getName(),
                entity.getDayOfWeek(),
                entity.getOpenTime(),
                entity.getCloseTime(),
                entity.getSlotOrder(),
                entity.getActive());
    }
}

