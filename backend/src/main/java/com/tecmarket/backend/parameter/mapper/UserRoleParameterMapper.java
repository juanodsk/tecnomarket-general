package com.tecmarket.backend.parameter.mapper;

import com.tecmarket.backend.parameter.dto.UserRoleOptionResponse;
import com.tecmarket.backend.permission.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserRoleParameterMapper {

    @Mapping(target = "roleId", source = "role.id")
    @Mapping(target = "assigned", source = "assigned")
    UserRoleOptionResponse toResponse(Role role, Boolean assigned);
}
