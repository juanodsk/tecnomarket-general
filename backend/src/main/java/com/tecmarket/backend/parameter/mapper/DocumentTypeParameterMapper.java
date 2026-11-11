package com.tecmarket.backend.parameter.mapper;

import com.tecmarket.backend.parameter.dto.DocumentTypeResponse;
import com.tecmarket.backend.user.entity.DocumentType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentTypeParameterMapper {

    DocumentTypeResponse toResponse(DocumentType documentType);
}
