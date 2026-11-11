package com.tecmarket.backend.business.mapper;

import com.tecmarket.backend.business.dto.BusinessResponse;
import com.tecmarket.backend.business.entity.Business;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BusinessMapper {

    default BusinessResponse toResponse(Business business) {
        return new BusinessResponse(
                business.getId(),
                business.getDocumentType().getCode(),
                business.getDocumentNumber(),
                business.getName(),
                business.getLegalName(),
                business.getEmail(),
                business.getPhone(),
                business.getAddress(),
                business.getDescription(),
                business.getCity().getId(),
                business.getCity().getName(),
                business.getLogo(),
                business.getCoverImage(),
                business.getRatingAverage(),
                business.getRatingCount(),
                business.getActive());
    }
}

