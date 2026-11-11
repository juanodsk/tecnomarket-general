package com.tecmarket.backend.business.mapper;

import com.tecmarket.backend.business.dto.BusinessRequestResponse;
import com.tecmarket.backend.business.entity.BusinessRequest;
import com.tecmarket.backend.user.entity.AppUser;
import org.mapstruct.Mapper;
import org.springframework.util.StringUtils;

@Mapper(componentModel = "spring")
public interface BusinessRequestMapper {

    default BusinessRequestResponse toResponse(BusinessRequest entity) {
        return new BusinessRequestResponse(
                entity.getId(),
                entity.getDocumentType().getCode(),
                entity.getDocumentNumber(),
                entity.getBusinessName(),
                entity.getLegalName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getCity().getId(),
                entity.getCity().getName(),
                entity.getAddress(),
                entity.getLegalFile(),
                entity.getStatus().name(),
                entity.getRequestedByUser().getId(),
                formatUserFullName(entity.getRequestedByUser()),
                entity.getReviewedByUser() != null ? entity.getReviewedByUser().getId() : null,
                formatUserFullName(entity.getReviewedByUser()),
                entity.getReviewedAt(),
                entity.getRejectionReason(),
                entity.getCreatedAt());
    }

    private String formatUserFullName(AppUser user) {
        if (user == null) {
            return null;
        }
        String name = user.getName();
        String surname = user.getSurname();
        if (!StringUtils.hasText(name) && !StringUtils.hasText(surname)) {
            return user.getEmail();
        }
        String fullName = ((name != null ? name : "") + " " + (surname != null ? surname : "")).trim();
        return StringUtils.hasText(fullName) ? fullName : user.getEmail();
    }
}

