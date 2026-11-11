package com.tecmarket.backend.parameter.mapper;

import com.tecmarket.backend.parameter.dto.RoleResponse;
import com.tecmarket.backend.permission.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleParameterMapper {

    RoleResponse toResponse(Role role);
}
