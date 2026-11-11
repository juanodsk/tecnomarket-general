package com.tecmarket.backend.parameter.mapper;

import com.tecmarket.backend.parameter.dto.CityResponse;
import com.tecmarket.backend.parameter.entity.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CityParameterMapper {

    @Mapping(target = "countryId", source = "country.id")
    @Mapping(target = "countryCode", source = "country.code")
    @Mapping(target = "countryName", source = "country.name")
    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "departmentCode", source = "department.code")
    @Mapping(target = "departmentName", source = "department.name")
    CityResponse toResponse(City city);
}
