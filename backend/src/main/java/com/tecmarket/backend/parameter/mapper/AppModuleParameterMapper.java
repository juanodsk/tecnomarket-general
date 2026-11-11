package com.tecmarket.backend.parameter.mapper;

import com.tecmarket.backend.parameter.dto.AppModuleResponse;
import com.tecmarket.backend.permission.entity.AppModule;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppModuleParameterMapper {

    AppModuleResponse toResponse(AppModule appModule);
}
