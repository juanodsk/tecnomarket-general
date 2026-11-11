package com.tecmarket.backend.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tecmarket.backend.user.entity.AppUser;
import com.tecmarket.backend.user.entity.DocumentType;
import com.tecmarket.backend.user.repository.AppUserRepository;
import com.tecmarket.backend.user.repository.DocumentTypeRepository;

@Service
public class UserService {

    private final AppUserRepository appUserRepository;
    private final DocumentTypeRepository documentTypeRepository;

    public UserService(AppUserRepository appUserRepository, DocumentTypeRepository documentTypeRepository) {
        this.appUserRepository = appUserRepository;
        this.documentTypeRepository = documentTypeRepository;
    }

    @Transactional(readOnly = true)
    public Optional<AppUser> findByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<AppUser> findByEmailIgnoreCase(String email) {
        return appUserRepository.findByEmailIgnoreCase(email);
    }

    @Transactional(readOnly = true)
    public Optional<AppUser> findByDocument(String documentTypeCode, String documentNumber) {
        return appUserRepository.findByDocumentTypeCodeAndDocumentNumber(documentTypeCode, documentNumber);
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return appUserRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean existsByDocumentTypeCodeAndDocumentNumber(String documentTypeCode, String documentNumber) {
        return appUserRepository.existsByDocumentTypeCodeAndDocumentNumber(documentTypeCode, documentNumber);
    }

    @Transactional(readOnly = true)
    public Optional<AppUser> findActiveUserById(Integer userId) {
        return appUserRepository.findById(userId)
                .filter(appUser -> Boolean.TRUE.equals(appUser.getActive()));
    }

    @Transactional(readOnly = true)
    public Optional<DocumentType> findDocumentTypeByCode(String code) {
        return documentTypeRepository.findByCode(code);
    }

    @Transactional
    public AppUser save(AppUser user) {
        return appUserRepository.save(user);
    }
}
