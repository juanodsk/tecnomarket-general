package com.tecmarket.backend.parameter.service;

import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.parameter.dto.AppModuleResponse;
import com.tecmarket.backend.parameter.dto.CreateAppModuleRequest;
import com.tecmarket.backend.parameter.dto.UpdateAppModuleRequest;
import com.tecmarket.backend.parameter.mapper.AppModuleParameterMapper;
import com.tecmarket.backend.permission.entity.AppModule;
import com.tecmarket.backend.permission.repository.AppModuleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppModuleParameterService {

    private final AppModuleRepository appModuleRepository;
    private final AppModuleParameterMapper appModuleParameterMapper;

    public AppModuleParameterService(
            AppModuleRepository appModuleRepository,
            AppModuleParameterMapper appModuleParameterMapper) {
        this.appModuleRepository = appModuleRepository;
        this.appModuleParameterMapper = appModuleParameterMapper;
    }

    @Transactional(readOnly = true)
    public List<AppModuleResponse> findAll() {
        return appModuleRepository.findAllByDeletedAtIsNullOrderByNameAsc()
                .stream()
                .map(appModuleParameterMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AppModuleResponse findById(Integer id) {
        AppModule appModule = appModuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("app_module.not_found"));

        return appModuleParameterMapper.toResponse(appModule);
    }

    @Transactional
    public AppModuleResponse create(CreateAppModuleRequest request) {
        String code = request.code().trim().toUpperCase();

        if (appModuleRepository.existsByCodeAndDeletedAtIsNull(code)) {
            throw new ConflictException("app_module.code.exists");
        }

        AppModule appModule = new AppModule();
        appModule.setCode(code);
        appModule.setName(request.name().trim());
        appModule.setActive(true);

        AppModule savedAppModule = appModuleRepository.save(appModule);

        return appModuleParameterMapper.toResponse(savedAppModule);
    }

    @Transactional
    public AppModuleResponse update(Integer id, UpdateAppModuleRequest request) {
        AppModule appModule = appModuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("app_module.not_found"));

        appModule.setName(request.name().trim());

        if (request.active() != null) {
            appModule.setActive(request.active());
        }

        return appModuleParameterMapper.toResponse(appModule);
    }

    @Transactional
    public void delete(Integer id) {
        AppModule appModule = appModuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("app_module.not_found"));

        appModule.setActive(false);
        appModule.setDeletedAt(LocalDateTime.now());
    }
}
