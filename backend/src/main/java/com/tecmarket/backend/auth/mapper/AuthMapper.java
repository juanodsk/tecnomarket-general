/*
 * TecMarket Backend
 * Module: Auth
 */

package com.tecmarket.backend.auth.mapper;

import com.tecmarket.backend.auth.dto.AuthUserResponse;
import com.tecmarket.backend.user.entity.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(target = "documentTypeCode", source = "documentType.code")
    @Mapping(target = "countryId", source = "country.id")
    @Mapping(target = "countryName", source = "country.name")
    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "departmentName", source = "department.name")
    @Mapping(target = "cityId", source = "city.id")
    @Mapping(target = "cityName", source = "city.name")
    AuthUserResponse toResponse(AppUser appUser);
}
