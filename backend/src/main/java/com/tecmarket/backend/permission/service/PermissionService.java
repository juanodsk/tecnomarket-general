package com.tecmarket.backend.permission.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tecmarket.backend.permission.entity.Role;
import com.tecmarket.backend.permission.entity.PermissionScope;
import com.tecmarket.backend.permission.repository.RolePermissionRepository;
import com.tecmarket.backend.permission.repository.RoleRepository;

@Service
public class PermissionService {

    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;

    public PermissionService(RoleRepository roleRepository, RolePermissionRepository rolePermissionRepository) {
        this.roleRepository = roleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Role> findRoleByCodeAndActive(String code) {
        return roleRepository.findByCodeAndActiveTrue(code);
    }

    @Transactional(readOnly = true)
    public List<String> findActivePermissionCodesByRoleCode(String code) {
        return rolePermissionRepository.findActivePermissionCodesByRoleCode(code);
    }

    @Transactional(readOnly = true)
    public List<String> findActivePermissionCodesByRoleCodeAndScope(String code, PermissionScope scope) {
        return rolePermissionRepository.findActivePermissionCodesByRoleCodeAndScope(code, scope);
    }
}
