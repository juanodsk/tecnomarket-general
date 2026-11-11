package com.tecmarket.backend.parameter.repository;

import com.tecmarket.backend.parameter.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Integer> {

    List<City> findAllByActiveTrueOrderByNameAsc();
    List<City> findAllByDeletedAtIsNullOrderByNameAsc();

    List<City> findByCountryIdAndActiveTrueOrderByNameAsc(Integer countryId);
    List<City> findByCountryIdAndDeletedAtIsNullOrderByNameAsc(Integer countryId);

    List<City> findByDepartmentIdAndActiveTrueOrderByNameAsc(Integer departmentId);
    List<City> findByDepartmentIdAndDeletedAtIsNullOrderByNameAsc(Integer departmentId);

    boolean existsByCountryIdAndNameIgnoreCase(Integer countryId, String name);
    boolean existsByCountryIdAndNameIgnoreCaseAndDeletedAtIsNull(Integer countryId, String name);

    boolean existsByCountryIdAndNameIgnoreCaseAndIdNot(Integer countryId, String name, Integer id);
    boolean existsByCountryIdAndNameIgnoreCaseAndDeletedAtIsNullAndIdNot(Integer countryId, String name, Integer id);

    boolean existsByDepartmentIdAndNameIgnoreCaseAndDeletedAtIsNull(Integer departmentId, String name);
    boolean existsByDepartmentIdAndNameIgnoreCaseAndDeletedAtIsNullAndIdNot(Integer departmentId, String name, Integer id);
}
