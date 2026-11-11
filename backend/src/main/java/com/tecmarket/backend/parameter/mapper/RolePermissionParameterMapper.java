package com.tecmarket.backend.parameter.mapper;

import com.tecmarket.backend.parameter.dto.RolePermissionOptionResponse;
import com.tecmarket.backend.parameter.dto.RoleAssignedPermissionResponse;
import com.tecmarket.backend.permission.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RolePermissionParameterMapper {

    @Mapping(target = "permissionId", source = "permission.id")
    @Mapping(target = "moduleId", source = "permission.appModule.id")
    @Mapping(target = "moduleCode", source = "permission.appModule.code")
    @Mapping(target = "moduleName", source = "permission.appModule.name")
    @Mapping(target = "scope", expression = "java(permission.getScope().name())")
    @Mapping(target = "assigned", source = "assigned")
    RolePermissionOptionResponse toResponse(Permission permission, Boolean assigned);

    @Mapping(target = "permissionId", source = "permission.id")
    @Mapping(target = "moduleId", source = "permission.appModule.id")
    @Mapping(target = "moduleCode", source = "permission.appModule.code")
    @Mapping(target = "moduleName", source = "permission.appModule.name")
    @Mapping(target = "scope", expression = "java(permission.getScope().name())")
    RoleAssignedPermissionResponse toAssignedResponse(Permission permission);
}
