package com.tecmarket.backend.parameter.repository;

import com.tecmarket.backend.parameter.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Integer> {

    Optional<Country> findByCode(String code);

    Optional<Country> findByCodeAndActiveTrue(String code);

    boolean existsByCode(String code);
    boolean existsByCodeAndDeletedAtIsNull(String code);

    List<Country> findAllByActiveTrueOrderByNameAsc();
    List<Country> findAllByDeletedAtIsNullOrderByNameAsc();
}
