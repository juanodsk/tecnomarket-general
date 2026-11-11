package com.tecmarket.backend.permission.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tecmarket.backend.permission.entity.AppModule;

public interface AppModuleRepository extends JpaRepository<AppModule, Integer> {

    Optional<AppModule> findByCode(String code);

    Optional<AppModule> findByCodeAndActiveTrue(String code);

    boolean existsByCode(String code);
    boolean existsByCodeAndDeletedAtIsNull(String code);

    List<AppModule> findAllByActiveTrueOrderByNameAsc();
    List<AppModule> findAllByDeletedAtIsNullOrderByNameAsc();

}
