package com.tecmarket.backend.parameter.mapper;

import com.tecmarket.backend.parameter.dto.UserParameterResponse;
import com.tecmarket.backend.user.entity.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserParameterMapper {

    @Mapping(target = "documentTypeCode", source = "documentType.code")
    @Mapping(target = "documentTypeName", source = "documentType.name")
    @Mapping(target = "countryId", source = "country.id")
    @Mapping(target = "countryName", source = "country.name")
    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "departmentName", source = "department.name")
    @Mapping(target = "cityId", source = "city.id")
    @Mapping(target = "cityName", source = "city.name")
    UserParameterResponse toResponse(AppUser appUser);
}
