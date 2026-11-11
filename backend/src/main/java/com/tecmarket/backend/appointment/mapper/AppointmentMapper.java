package com.tecmarket.backend.appointment.mapper;

import com.tecmarket.backend.appointment.dto.AppointmentResponse;
import com.tecmarket.backend.appointment.entity.Appointment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    default AppointmentResponse toResponse(Appointment entity) {
        return new AppointmentResponse(
                entity.getId(),
                entity.getBusiness().getId(),
                entity.getBranch().getId(),
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
                entity.getBusinessService().getId(),
                entity.getBusinessService().getName(),
                entity.getStatus().getId(),
                entity.getStatus().getCode(),
                entity.getStatus().getName(),
                entity.getScheduledAt(),
                entity.getProblemDescription(),
                entity.getActive());
    }
}

