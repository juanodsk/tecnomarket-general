package com.tecmarket.backend.parameter.controller;

import com.tecmarket.backend.common.i18n.MessageService;
import com.tecmarket.backend.common.response.ApiResponse;
import com.tecmarket.backend.parameter.dto.CreateDocumentTypeRequest;
import com.tecmarket.backend.parameter.dto.DocumentTypeResponse;
import com.tecmarket.backend.parameter.dto.UpdateDocumentTypeRequest;
import com.tecmarket.backend.parameter.service.DocumentTypeParameterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Document Types", description = "Endpoints for managing document type parameters.")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/parameters/document-types")
public class DocumentTypeParameterController {

    private final DocumentTypeParameterService documentTypeParameterService;
    private final MessageService messageService;

    public DocumentTypeParameterController(
            DocumentTypeParameterService documentTypeParameterService,
            MessageService messageService) {
        this.documentTypeParameterService = documentTypeParameterService;
        this.messageService = messageService;
    }

    @Operation(summary = "List document types", description = "Returns all active document types.")
    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<ApiResponse<List<DocumentTypeResponse>>> findAll() {
        List<DocumentTypeResponse> response = documentTypeParameterService.findAll();

        return ResponseEntity.ok(ApiResponse.success(messageService.get("document_type.list.success"), response));
    }

    @Operation(summary = "Find document type by ID", description = "Returns a document type by its identifier.")
    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DocumentTypeResponse>> findById(@PathVariable Integer id) {
        DocumentTypeResponse response = documentTypeParameterService.findById(id);

        return ResponseEntity.ok(ApiResponse.success(messageService.get("document_type.find.success"), response));
    }

    @Operation(summary = "Create document type", description = "Creates a new document type parameter.")
    @PreAuthorize("hasAuthority('PARAMETER_MANAGE')")
    @PostMapping
    public ResponseEntity<ApiResponse<DocumentTypeResponse>> create(
            @Valid @RequestBody CreateDocumentTypeRequest request) {
        DocumentTypeResponse response = documentTypeParameterService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(messageService.get("document_type.create.success"), response));
    }

    @Operation(summary = "Update document type", description = "Updates a document type parameter.")
    @PreAuthorize("hasAuthority('PARAMETER_MANAGE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DocumentTypeResponse>> update(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateDocumentTypeRequest request) {
        DocumentTypeResponse response = documentTypeParameterService.update(id, request);

        return ResponseEntity.ok(ApiResponse.success(messageService.get("document_type.update.success"), response));
    }

    @Operation(summary = "Disable document type", description = "Soft-deletes a document type by marking it inactive.")
    @PreAuthorize("hasAuthority('PARAMETER_MANAGE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        documentTypeParameterService.delete(id);

        return ResponseEntity.ok(ApiResponse.success(messageService.get("document_type.delete.success"), null));
    }
}

