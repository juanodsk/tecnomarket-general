package com.tecmarket.backend.parameter.service;

import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.parameter.dto.CityResponse;
import com.tecmarket.backend.parameter.dto.CreateCityRequest;
import com.tecmarket.backend.parameter.dto.UpdateCityRequest;
import com.tecmarket.backend.parameter.entity.City;
import com.tecmarket.backend.parameter.entity.Country;
import com.tecmarket.backend.parameter.entity.Department;
import com.tecmarket.backend.parameter.mapper.CityParameterMapper;
import com.tecmarket.backend.parameter.repository.CityRepository;
import com.tecmarket.backend.parameter.repository.CountryRepository;
import com.tecmarket.backend.parameter.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CityParameterService {

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final DepartmentRepository departmentRepository;
    private final CityParameterMapper cityParameterMapper;

    public CityParameterService(
            CityRepository cityRepository,
            CountryRepository countryRepository,
            DepartmentRepository departmentRepository,
            CityParameterMapper cityParameterMapper) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.departmentRepository = departmentRepository;
        this.cityParameterMapper = cityParameterMapper;
    }

    @Transactional(readOnly = true)
    public List<CityResponse> findAll() {
        return cityRepository.findAllByDeletedAtIsNullOrderByNameAsc()
                .stream()
                .map(cityParameterMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CityResponse findById(Integer id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("city.not_found"));

        return cityParameterMapper.toResponse(city);
    }

    @Transactional(readOnly = true)
    public List<CityResponse> findByCountryId(Integer countryId) {
        findActiveCountry(countryId);

        return cityRepository.findByCountryIdAndDeletedAtIsNullOrderByNameAsc(countryId)
                .stream()
                .map(cityParameterMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CityResponse> findByDepartmentId(Integer departmentId) {
        findActiveDepartment(departmentId);

        return cityRepository.findByDepartmentIdAndDeletedAtIsNullOrderByNameAsc(departmentId)
                .stream()
                .map(cityParameterMapper::toResponse)
                .toList();
    }

    @Transactional
    public CityResponse create(CreateCityRequest request) {
        Department department = findActiveDepartment(request.departmentId());
        String name = request.name().trim();

        if (cityRepository.existsByDepartmentIdAndNameIgnoreCaseAndDeletedAtIsNull(department.getId(), name)) {
            throw new ConflictException("city.name.exists");
        }

        City city = new City();
        city.setCountry(department.getCountry());
        city.setDepartment(department);
        city.setName(name);
        city.setActive(true);

        City savedCity = cityRepository.save(city);

        return cityParameterMapper.toResponse(savedCity);
    }

    @Transactional
    public CityResponse update(Integer id, UpdateCityRequest request) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("city.not_found"));

        Department department = findActiveDepartment(request.departmentId());
        String name = request.name().trim();

        if (cityRepository.existsByDepartmentIdAndNameIgnoreCaseAndDeletedAtIsNullAndIdNot(department.getId(), name, id)) {
            throw new ConflictException("city.name.exists");
        }

        city.setCountry(department.getCountry());
        city.setDepartment(department);
        city.setName(name);

        if (request.active() != null) {
            city.setActive(request.active());
        }

        return cityParameterMapper.toResponse(city);
    }

    @Transactional
    public void delete(Integer id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("city.not_found"));

        city.setActive(false);
        city.setDeletedAt(LocalDateTime.now());
    }

    private Country findActiveCountry(Integer countryId) {
        return countryRepository.findById(countryId)
                .filter(country -> Boolean.TRUE.equals(country.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("country.not_found"));
    }

    private Department findActiveDepartment(Integer departmentId) {
        return departmentRepository.findById(departmentId)
                .filter(department -> Boolean.TRUE.equals(department.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("department.not_found"));
    }
}
