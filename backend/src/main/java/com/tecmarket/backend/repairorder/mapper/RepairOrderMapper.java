package com.tecmarket.backend.repairorder.mapper;

import com.tecmarket.backend.repairorder.dto.RepairOrderResponse;
import com.tecmarket.backend.repairorder.entity.RepairOrder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RepairOrderMapper {

    default RepairOrderResponse toResponse(RepairOrder entity) {
        return new RepairOrderResponse(
                entity.getId(),
                entity.getOrderNumber(),
                entity.getOrderSequence(),
                entity.getBusiness().getId(),
                entity.getBranch().getId(),
                entity.getBranch().getBranchNumber(),
                entity.getBranch().getName(),
                entity.getCustomer().getId(),
                entity.getCustomer().getName(),
                entity.getCustomer().getSurname(),
                entity.getCustomer().getEmail(),
                entity.getCustomerDevice().getId(),
                entity.getCustomerDevice().getDeviceType().getName()
                        + " "
                        + entity.getCustomerDevice().getBrand().getName()
                        + " "
                        + entity.getCustomerDevice().getModel(),
                entity.getAppointment() != null ? entity.getAppointment().getId() : null,
                entity.getBusinessService().getId(),
                entity.getBusinessService().getName(),
                entity.getStatus().getId(),
                entity.getStatus().getCode(),
                entity.getStatus().getName(),
                entity.getReceivedByBusinessUser().getId(),
                entity.getReceivedByBusinessUser().getUser().getName() + " "
                        + entity.getReceivedByBusinessUser().getUser().getSurname(),
                entity.getAssignedBusinessUser().getId(),
                entity.getAssignedBusinessUser().getUser().getName() + " "
                        + entity.getAssignedBusinessUser().getUser().getSurname(),
                entity.getProblemDescription(),
                entity.getDiagnosis(),
                entity.getEstimatedPrice(),
                entity.getFinalPrice(),
                entity.getReceivedAt(),
                entity.getEstimatedDeliveryAt(),
                entity.getDeliveredAt(),
                entity.getActive());
    }
}

