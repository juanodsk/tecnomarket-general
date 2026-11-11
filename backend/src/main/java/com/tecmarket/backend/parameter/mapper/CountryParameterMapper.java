package com.tecmarket.backend.parameter.mapper;

import com.tecmarket.backend.parameter.dto.CountryResponse;
import com.tecmarket.backend.parameter.entity.Country;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CountryParameterMapper {

    CountryResponse toResponse(Country country);
}
