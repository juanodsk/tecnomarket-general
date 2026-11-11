package com.tecmarket.backend.parameter.mapper;

import com.tecmarket.backend.parameter.dto.DepartmentResponse;
import com.tecmarket.backend.parameter.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DepartmentParameterMapper {

    @Mapping(target = "countryId", source = "country.id")
    @Mapping(target = "countryCode", source = "country.code")
    @Mapping(target = "countryName", source = "country.name")
    DepartmentResponse toResponse(Department department);
}
