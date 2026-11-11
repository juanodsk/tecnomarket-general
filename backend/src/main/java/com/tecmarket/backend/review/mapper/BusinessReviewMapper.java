package com.tecmarket.backend.review.mapper;

import com.tecmarket.backend.review.dto.BusinessReviewResponse;
import com.tecmarket.backend.review.dto.PublicBusinessDetailResponse;
import com.tecmarket.backend.review.dto.PublicBusinessReviewResponse;
import com.tecmarket.backend.review.dto.PublicBusinessResponse;
import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.review.entity.BusinessReview;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BusinessReviewMapper {

    default BusinessReviewResponse toResponse(BusinessReview entity) {
        return new BusinessReviewResponse(
                entity.getId(),
                entity.getBusiness().getId(),
                entity.getCustomer().getId(),
                entity.getCustomer().getName(),
                entity.getCustomer().getSurname(),
                entity.getCustomer().getEmail(),
                entity.getRepairOrder().getId(),
                entity.getRepairOrder().getOrderNumber(),
                entity.getRating(),
                entity.getComment(),
                entity.getCreatedAt());
    }

    default PublicBusinessReviewResponse toPublicResponse(BusinessReview entity) {
        String name = entity.getCustomer().getName();
        String surname = entity.getCustomer().getSurname();
        String displayName = anonymizeName(name, surname);
        return new PublicBusinessReviewResponse(
                entity.getRating(),
                entity.getComment(),
                displayName,
                entity.getCreatedAt());
    }

    private static String anonymizeName(String name, String surname) {
        String safeName = name == null || name.isBlank() ? "Cliente" : name.trim();
        if (surname == null || surname.isBlank()) {
            return safeName;
        }
        return safeName + " " + Character.toUpperCase(surname.trim().charAt(0)) + ".";
    }

    default PublicBusinessResponse toPublicBusinessResponse(Business business) {
        return new PublicBusinessResponse(
                business.getId(),
                business.getName(),
                business.getDescription(),
                business.getCity().getId(),
                business.getCity().getName(),
                business.getLogo(),
                business.getCoverImage(),
                business.getRatingAverage(),
                business.getRatingCount());
    }

    default PublicBusinessDetailResponse toPublicBusinessDetailResponse(Business business) {
        return new PublicBusinessDetailResponse(
                business.getId(),
                business.getName(),
                business.getLegalName(),
                business.getDescription(),
                business.getCity().getId(),
                business.getCity().getName(),
                business.getAddress(),
                business.getPhone(),
                business.getEmail(),
                business.getLogo(),
                business.getCoverImage(),
                business.getRatingAverage(),
                business.getRatingCount());
    }
}

