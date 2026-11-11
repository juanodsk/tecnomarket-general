package com.tecmarket.backend.parameter.service;

import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.parameter.dto.CreateDepartmentRequest;
import com.tecmarket.backend.parameter.dto.DepartmentResponse;
import com.tecmarket.backend.parameter.dto.UpdateDepartmentRequest;
import com.tecmarket.backend.parameter.entity.Country;
import com.tecmarket.backend.parameter.entity.Department;
import com.tecmarket.backend.parameter.mapper.DepartmentParameterMapper;
import com.tecmarket.backend.parameter.repository.CountryRepository;
import com.tecmarket.backend.parameter.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DepartmentParameterService {

    private final DepartmentRepository departmentRepository;
    private final CountryRepository countryRepository;
    private final DepartmentParameterMapper departmentParameterMapper;

    public DepartmentParameterService(
            DepartmentRepository departmentRepository,
            CountryRepository countryRepository,
            DepartmentParameterMapper departmentParameterMapper) {
        this.departmentRepository = departmentRepository;
        this.countryRepository = countryRepository;
        this.departmentParameterMapper = departmentParameterMapper;
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponse> findAll() {
        return departmentRepository.findAllByDeletedAtIsNullOrderByNameAsc()
                .stream()
                .map(departmentParameterMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DepartmentResponse findById(Integer id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("department.not_found"));

        return departmentParameterMapper.toResponse(department);
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponse> findByCountryId(Integer countryId) {
        findActiveCountry(countryId);

        return departmentRepository.findByCountryIdAndDeletedAtIsNullOrderByNameAsc(countryId)
                .stream()
                .map(departmentParameterMapper::toResponse)
                .toList();
    }

    @Transactional
    public DepartmentResponse create(CreateDepartmentRequest request) {
        Country country = findActiveCountry(request.countryId());
        String code = request.code().trim().toUpperCase();
        String name = request.name().trim();

        if (departmentRepository.existsByCountryIdAndCodeIgnoreCaseAndDeletedAtIsNull(country.getId(), code)) {
            throw new ConflictException("department.code.exists");
        }

        if (departmentRepository.existsByCountryIdAndNameIgnoreCaseAndDeletedAtIsNull(country.getId(), name)) {
            throw new ConflictException("department.name.exists");
        }

        Department department = new Department();
        department.setCountry(country);
        department.setCode(code);
        department.setName(name);
        department.setActive(true);

        return departmentParameterMapper.toResponse(departmentRepository.save(department));
    }

    @Transactional
    public DepartmentResponse update(Integer id, UpdateDepartmentRequest request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("department.not_found"));
        Country country = findActiveCountry(request.countryId());
        String code = request.code().trim().toUpperCase();
        String name = request.name().trim();

        if (departmentRepository.existsByCountryIdAndCodeIgnoreCaseAndDeletedAtIsNullAndIdNot(country.getId(), code,
                id)) {
            throw new ConflictException("department.code.exists");
        }

        if (departmentRepository.existsByCountryIdAndNameIgnoreCaseAndDeletedAtIsNullAndIdNot(country.getId(), name,
                id)) {
            throw new ConflictException("department.name.exists");
        }

        department.setCountry(country);
        department.setCode(code);
        department.setName(name);

        if (request.active() != null) {
            department.setActive(request.active());
        }

        return departmentParameterMapper.toResponse(department);
    }

    @Transactional
    public void delete(Integer id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("department.not_found"));

        department.setActive(false);
        department.setDeletedAt(LocalDateTime.now());
    }

    private Country findActiveCountry(Integer countryId) {
        return countryRepository.findById(countryId)
                .filter(country -> Boolean.TRUE.equals(country.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("country.not_found"));
    }
}
