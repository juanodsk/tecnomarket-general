package com.tecmarket.backend.parameter.repository;

import com.tecmarket.backend.parameter.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    Optional<Department> findByCountryIdAndCodeIgnoreCase(Integer countryId, String code);

    List<Department> findAllByDeletedAtIsNullOrderByNameAsc();

    List<Department> findByCountryIdAndActiveTrueOrderByNameAsc(Integer countryId);

    List<Department> findByCountryIdAndDeletedAtIsNullOrderByNameAsc(Integer countryId);

    boolean existsByCountryIdAndCodeIgnoreCaseAndDeletedAtIsNull(Integer countryId, String code);

    boolean existsByCountryIdAndCodeIgnoreCaseAndDeletedAtIsNullAndIdNot(Integer countryId, String code, Integer id);

    boolean existsByCountryIdAndNameIgnoreCaseAndDeletedAtIsNull(Integer countryId, String name);

    boolean existsByCountryIdAndNameIgnoreCaseAndDeletedAtIsNullAndIdNot(Integer countryId, String name, Integer id);
}
