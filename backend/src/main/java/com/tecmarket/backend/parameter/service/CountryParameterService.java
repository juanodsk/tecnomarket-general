package com.tecmarket.backend.parameter.service;

import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.parameter.dto.CountryResponse;
import com.tecmarket.backend.parameter.dto.CreateCountryRequest;
import com.tecmarket.backend.parameter.dto.UpdateCountryRequest;
import com.tecmarket.backend.parameter.entity.Country;
import com.tecmarket.backend.parameter.mapper.CountryParameterMapper;
import com.tecmarket.backend.parameter.repository.CountryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CountryParameterService {

    private final CountryRepository countryRepository;
    private final CountryParameterMapper countryParameterMapper;

    public CountryParameterService(
            CountryRepository countryRepository,
            CountryParameterMapper countryParameterMapper) {
        this.countryRepository = countryRepository;
        this.countryParameterMapper = countryParameterMapper;
    }

    @Transactional(readOnly = true)
    public List<CountryResponse> findAll() {
        return countryRepository.findAllByDeletedAtIsNullOrderByNameAsc()
                .stream()
                .map(countryParameterMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CountryResponse findById(Integer id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("country.not_found"));

        return countryParameterMapper.toResponse(country);
    }

    @Transactional
    public CountryResponse create(CreateCountryRequest request) {
        String code = request.code().trim().toUpperCase();

        if (countryRepository.existsByCodeAndDeletedAtIsNull(code)) {
            throw new ConflictException("country.code.exists");
        }

        Country country = new Country();
        country.setCode(code);
        country.setName(request.name().trim());
        country.setActive(true);

        Country savedCountry = countryRepository.save(country);

        return countryParameterMapper.toResponse(savedCountry);
    }

    @Transactional
    public CountryResponse update(Integer id, UpdateCountryRequest request) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("country.not_found"));

        country.setName(request.name().trim());

        if (request.active() != null) {
            country.setActive(request.active());
        }

        return countryParameterMapper.toResponse(country);
    }

    @Transactional
    public void delete(Integer id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("country.not_found"));

        country.setActive(false);
        country.setDeletedAt(LocalDateTime.now());
    }
}
