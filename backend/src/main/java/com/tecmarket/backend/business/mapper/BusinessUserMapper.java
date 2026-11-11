package com.tecmarket.backend.business.mapper;

import com.tecmarket.backend.business.dto.BusinessUserResponse;
import com.tecmarket.backend.business.entity.BusinessUser;
import com.tecmarket.backend.business.repository.BusinessUserBranchRepository;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class BusinessUserMapper {

    @Autowired
    protected BusinessUserBranchRepository businessUserBranchRepository;

    public BusinessUserResponse toResponse(BusinessUser entity) {
        return new BusinessUserResponse(
                entity.getId(),
                entity.getBusiness().getId(),
                entity.getUser().getId(),
                entity.getUser().getName(),
                entity.getUser().getSurname(),
                entity.getUser().getEmail(),
                entity.getRole().getId(),
                entity.getRole().getCode(),
                entity.getRole().getName(),
                businessUserBranchRepository.findByBusinessUserIdAndDeletedAtIsNullOrderByBranchBranchNumberAsc(entity.getId())
                        .stream()
                        .filter(assignment -> Boolean.TRUE.equals(assignment.getActive()))
                        .map(assignment -> assignment.getBranch().getId())
                        .toList(),
                entity.getCommissionPercentage(),
                entity.getActive());
    }
}
