package com.tecmarket.backend.repairorder.mapper;

import com.tecmarket.backend.repairorder.dto.CustomerDeviceResponse;
import com.tecmarket.backend.repairorder.entity.CustomerDevice;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerDeviceMapper {

    default CustomerDeviceResponse toResponse(CustomerDevice entity) {
        return new CustomerDeviceResponse(
                entity.getId(),
                entity.getBusiness().getId(),
                entity.getCustomer().getId(),
                entity.getCustomer().getDocumentType().getCode(),
                entity.getCustomer().getDocumentNumber(),
                entity.getCustomer().getName(),
                entity.getCustomer().getSurname(),
                entity.getCustomer().getEmail(),
                entity.getDeviceType().getId(),
                entity.getDeviceType().getCode(),
                entity.getDeviceType().getName(),
                entity.getBrand().getId(),
                entity.getBrand().getName(),
                entity.getModel(),
                entity.getSerialNumber(),
                entity.getImei(),
                entity.getRegisteredByBusinessUser().getId(),
                entity.getRegisteredByBusinessUser().getUser().getName() + " "
                        + entity.getRegisteredByBusinessUser().getUser().getSurname(),
                entity.getColor(),
                entity.getRegisteredAt(),
                entity.getActive());
    }
}

