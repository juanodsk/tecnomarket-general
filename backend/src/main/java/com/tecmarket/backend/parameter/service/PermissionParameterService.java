package com.tecmarket.backend.parameter.service;

import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.parameter.dto.CreatePermissionRequest;
import com.tecmarket.backend.parameter.dto.PermissionResponse;
import com.tecmarket.backend.parameter.dto.UpdatePermissionRequest;
import com.tecmarket.backend.parameter.mapper.PermissionParameterMapper;
import com.tecmarket.backend.permission.entity.AppModule;
import com.tecmarket.backend.permission.entity.Permission;
import com.tecmarket.backend.permission.repository.AppModuleRepository;
import com.tecmarket.backend.permission.repository.PermissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PermissionParameterService {

    private final PermissionRepository permissionRepository;
    private final AppModuleRepository appModuleRepository;
    private final PermissionParameterMapper permissionParameterMapper;

    public PermissionParameterService(
            PermissionRepository permissionRepository,
            AppModuleRepository appModuleRepository,
            PermissionParameterMapper permissionParameterMapper) {
        this.permissionRepository = permissionRepository;
        this.appModuleRepository = appModuleRepository;
        this.permissionParameterMapper = permissionParameterMapper;
    }

    @Transactional(readOnly = true)
    public List<PermissionResponse> findAll() {
        return permissionRepository.findAllByDeletedAtIsNullOrderByCodeAsc()
                .stream()
                .map(permissionParameterMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PermissionResponse findById(Integer id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("permission.not_found"));

        return permissionParameterMapper.toResponse(permission);
    }

    @Transactional(readOnly = true)
    public List<PermissionResponse> findByModuleId(Integer moduleId) {
        if (!appModuleRepository.existsById(moduleId)) {
            throw new ResourceNotFoundException("app_module.not_found");
        }

        return permissionRepository.findByAppModuleIdAndActiveTrueOrderByCodeAsc(moduleId)
                .stream()
                .map(permissionParameterMapper::toResponse)
                .toList();
    }

    @Transactional
    public PermissionResponse create(CreatePermissionRequest request) {
        String code = request.code().trim().toUpperCase();

        if (permissionRepository.existsByCodeAndDeletedAtIsNull(code)) {
            throw new ConflictException("permission.code.exists");
        }

        AppModule appModule = appModuleRepository.findById(request.moduleId())
                .filter(module -> Boolean.TRUE.equals(module.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("app_module.not_found"));

        Permission permission = new Permission();
        permission.setAppModule(appModule);
        permission.setCode(code);
        permission.setName(request.name().trim());
        permission.setDescription(request.description().trim());
        permission.setActive(true);

        Permission savedPermission = permissionRepository.save(permission);

        return permissionParameterMapper.toResponse(savedPermission);
    }

    @Transactional
    public PermissionResponse update(Integer id, UpdatePermissionRequest request) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("permission.not_found"));

        AppModule appModule = appModuleRepository.findById(request.moduleId())
                .filter(module -> Boolean.TRUE.equals(module.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("app_module.not_found"));

        permission.setAppModule(appModule);
        permission.setName(request.name().trim());
        permission.setDescription(request.description().trim());

        if (request.active() != null) {
            permission.setActive(request.active());
        }

        return permissionParameterMapper.toResponse(permission);
    }

    @Transactional
    public void delete(Integer id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("permission.not_found"));

        permission.setActive(false);
        permission.setDeletedAt(LocalDateTime.now());
    }
}
