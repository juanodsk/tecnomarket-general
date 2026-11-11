/*
 * TecMarket Backend
 * Module: User
 */

package com.tecmarket.backend.user.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tecmarket.backend.user.entity.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findByEmailIgnoreCase(String email);
    Optional<AppUser> findByEmailIgnoreCaseAndActiveTrueAndDeletedAtIsNull(String email);

    Optional<AppUser> findByDocumentTypeCodeAndDocumentNumber(String documentTypeCode, String documentNumber);

    boolean existsByEmail(String email);

    boolean existsByDocumentTypeCodeAndDocumentNumber(String documentTypeCode, String documentNumber);

    List<AppUser> findByActiveTrueAndDeletedAtIsNullOrderByIdAsc();

    Page<AppUser> findByActiveTrueAndDeletedAtIsNullOrderByIdAsc(Pageable pageable);

}
