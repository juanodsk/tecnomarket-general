package com.tecmarket.backend.parameter.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.parameter.entity.City;
import com.tecmarket.backend.parameter.entity.Country;
import com.tecmarket.backend.parameter.entity.Department;
import com.tecmarket.backend.parameter.repository.CityRepository;
import com.tecmarket.backend.parameter.repository.CountryRepository;
import com.tecmarket.backend.parameter.repository.DepartmentRepository;

@Service
public class LocationService {

    private final CountryRepository countryRepository;
    private final DepartmentRepository departmentRepository;
    private final CityRepository cityRepository;

    public LocationService(CountryRepository countryRepository, DepartmentRepository departmentRepository, CityRepository cityRepository) {
        this.countryRepository = countryRepository;
        this.departmentRepository = departmentRepository;
        this.cityRepository = cityRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Country> findActiveCountry(Integer id) {
        return countryRepository.findById(id)
                .filter(activeCountry -> Boolean.TRUE.equals(activeCountry.getActive()));
    }

    @Transactional(readOnly = true)
    public Optional<Department> findActiveDepartment(Integer id) {
        return departmentRepository.findById(id)
                .filter(activeDepartment -> Boolean.TRUE.equals(activeDepartment.getActive()));
    }

    @Transactional(readOnly = true)
    public Optional<City> findActiveCity(Integer id) {
        return cityRepository.findById(id)
                .filter(activeCity -> Boolean.TRUE.equals(activeCity.getActive()));
    }

    public void validateLocationHierarchy(Country country, Department department, City city) {
        if (!department.getCountry().getId().equals(country.getId())
                || !city.getCountry().getId().equals(country.getId())
                || !city.getDepartment().getId().equals(department.getId())) {
            throw new ConflictException("location.hierarchy.invalid");
        }
    }
}
