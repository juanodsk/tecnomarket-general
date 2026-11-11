package com.tecmarket.backend.parameter.mapper;

import com.tecmarket.backend.parameter.dto.PermissionResponse;
import com.tecmarket.backend.permission.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PermissionParameterMapper {

    @Mapping(target = "moduleId", source = "appModule.id")
    @Mapping(target = "moduleCode", source = "appModule.code")
    @Mapping(target = "moduleName", source = "appModule.name")
    PermissionResponse toResponse(Permission permission);
}
