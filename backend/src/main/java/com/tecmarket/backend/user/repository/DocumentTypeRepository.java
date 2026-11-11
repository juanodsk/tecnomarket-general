/*
 * TecMarket Backend
 * Module: User
 */

package com.tecmarket.backend.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tecmarket.backend.user.entity.DocumentType;

public interface DocumentTypeRepository extends JpaRepository<DocumentType, Integer> {
    Optional<DocumentType> findByCode(String code);

    Optional<DocumentType> findByCodeAndActiveTrue(String code);

    boolean existsByCode(String code);
    boolean existsByCodeAndDeletedAtIsNull(String code);

    List<DocumentType> findAllByActiveTrueOrderByNameAsc();
    List<DocumentType> findAllByDeletedAtIsNullOrderByNameAsc();
}
