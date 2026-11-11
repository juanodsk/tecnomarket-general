package com.tecmarket.backend.business.mapper;

import com.tecmarket.backend.business.dto.BusinessBranchResponse;
import com.tecmarket.backend.business.entity.BusinessBranch;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BusinessBranchMapper {

    default BusinessBranchResponse toResponse(BusinessBranch branch) {
        return new BusinessBranchResponse(
                branch.getId(),
                branch.getBusiness().getId(),
                branch.getBusiness().getName(),
                branch.getBranchNumber(),
                branch.getName(),
                branch.getCity().getCountry().getId(),
                branch.getCity().getCountry().getName(),
                branch.getCity().getId(),
                branch.getCity().getName(),
                branch.getAddress(),
                branch.getNeighborhood(),
                branch.getPhone(),
                branch.getLatitude(),
                branch.getLongitude(),
                branch.getServiceRadiusKm(),
                branch.getPrimaryBranch(),
                branch.getActive(),
                branch.getCreatedAt(),
                branch.getUpdatedAt());
    }
}

