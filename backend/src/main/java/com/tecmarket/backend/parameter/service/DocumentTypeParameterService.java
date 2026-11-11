package com.tecmarket.backend.parameter.service;

import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.parameter.dto.CreateDocumentTypeRequest;
import com.tecmarket.backend.parameter.dto.DocumentTypeResponse;
import com.tecmarket.backend.parameter.dto.UpdateDocumentTypeRequest;
import com.tecmarket.backend.parameter.mapper.DocumentTypeParameterMapper;
import com.tecmarket.backend.user.entity.DocumentType;
import com.tecmarket.backend.user.repository.DocumentTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DocumentTypeParameterService {

    private final DocumentTypeRepository documentTypeRepository;
    private final DocumentTypeParameterMapper documentTypeParameterMapper;

    public DocumentTypeParameterService(
            DocumentTypeRepository documentTypeRepository,
            DocumentTypeParameterMapper documentTypeParameterMapper) {
        this.documentTypeRepository = documentTypeRepository;
        this.documentTypeParameterMapper = documentTypeParameterMapper;
    }

    @Transactional(readOnly = true)
    public List<DocumentTypeResponse> findAll() {
        return documentTypeRepository.findAllByDeletedAtIsNullOrderByNameAsc()
                .stream()
                .map(documentTypeParameterMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DocumentTypeResponse findById(Integer id) {
        DocumentType documentType = documentTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("document_type.not_found"));

        return documentTypeParameterMapper.toResponse(documentType);
    }

    @Transactional
    public DocumentTypeResponse create(CreateDocumentTypeRequest request) {
        String code = request.code().trim().toUpperCase();

        if (documentTypeRepository.existsByCodeAndDeletedAtIsNull(code)) {
            throw new ConflictException("document_type.code.exists");
        }

        DocumentType documentType = new DocumentType();
        documentType.setCode(code);
        documentType.setName(request.name().trim());
        documentType.setDescription(request.description().trim());
        documentType.setAppliesToPerson(request.appliesToPerson());
        documentType.setAppliesToCompany(request.appliesToCompany());
        documentType.setActive(true);

        DocumentType savedDocumentType = documentTypeRepository.save(documentType);

        return documentTypeParameterMapper.toResponse(savedDocumentType);
    }

    @Transactional
    public DocumentTypeResponse update(Integer id, UpdateDocumentTypeRequest request) {
        DocumentType documentType = documentTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("document_type.not_found"));

        documentType.setName(request.name().trim());
        documentType.setDescription(request.description().trim());
        documentType.setAppliesToPerson(request.appliesToPerson());
        documentType.setAppliesToCompany(request.appliesToCompany());

        if (request.active() != null) {
            documentType.setActive(request.active());
        }

        return documentTypeParameterMapper.toResponse(documentType);
    }

    @Transactional
    public void delete(Integer id) {
        DocumentType documentType = documentTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("document_type.not_found"));

        documentType.setActive(false);
        documentType.setDeletedAt(LocalDateTime.now());
    }
}
