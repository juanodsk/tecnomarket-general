package com.tecmarket.backend.parameter.service;

import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.parameter.dto.CreateRoleRequest;
import com.tecmarket.backend.parameter.dto.RoleResponse;
import com.tecmarket.backend.parameter.dto.UpdateRoleRequest;
import com.tecmarket.backend.parameter.mapper.RoleParameterMapper;
import com.tecmarket.backend.permission.entity.Role;
import com.tecmarket.backend.permission.repository.RoleRepository;
import com.tecmarket.backend.auth.service.SecurityVersionService;
import com.tecmarket.backend.business.repository.BusinessUserRepository;
import com.tecmarket.backend.user.repository.UserRoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.HashSet;
import java.util.Objects;

@Service
public class RoleParameterService {

    private final RoleRepository roleRepository;
    private final RoleParameterMapper roleParameterMapper;
    private final UserRoleRepository userRoleRepository;
    private final BusinessUserRepository businessUserRepository;
    private final SecurityVersionService securityVersionService;

    public RoleParameterService(
            RoleRepository roleRepository,
            RoleParameterMapper roleParameterMapper,
            UserRoleRepository userRoleRepository,
            BusinessUserRepository businessUserRepository,
            SecurityVersionService securityVersionService) {
        this.roleRepository = roleRepository;
        this.roleParameterMapper = roleParameterMapper;
        this.userRoleRepository = userRoleRepository;
        this.businessUserRepository = businessUserRepository;
        this.securityVersionService = securityVersionService;
    }

    @Transactional(readOnly = true)
    public List<RoleResponse> findAll() {
        return roleRepository.findAllByDeletedAtIsNullOrderByNameAsc()
                .stream()
                .map(roleParameterMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public RoleResponse findById(Integer id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("role.not_found"));

        return roleParameterMapper.toResponse(role);
    }

    @Transactional
    public RoleResponse create(CreateRoleRequest request) {
        String code = request.code().trim().toUpperCase();

        if (roleRepository.existsByCodeAndDeletedAtIsNull(code)) {
            throw new ConflictException("role.code.exists");
        }

        Role role = new Role();
        role.setCode(code);
        role.setName(request.name().trim());
        role.setDescription(request.description().trim());
        role.setScope(request.scope().trim().toUpperCase());
        role.setActive(true);

        Role savedRole = roleRepository.save(role);

        return roleParameterMapper.toResponse(savedRole);
    }

    @Transactional
    public RoleResponse update(Integer id, UpdateRoleRequest request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("role.not_found"));

        String previousScope = role.getScope();
        Boolean previousActive = role.getActive();

        role.setName(request.name().trim());
        role.setDescription(request.description().trim());
        role.setScope(request.scope().trim().toUpperCase());

        if (request.active() != null) {
            role.setActive(request.active());
        }

        if (!Objects.equals(previousScope, role.getScope())
                || !Objects.equals(previousActive, role.getActive())) {
            invalidateUsersAssignedToRole(role.getId());
        }

        return roleParameterMapper.toResponse(role);
    }

    @Transactional
    public void delete(Integer id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("role.not_found"));

        boolean wasActive = Boolean.TRUE.equals(role.getActive());
        role.setActive(false);
        role.setDeletedAt(LocalDateTime.now());
        if (wasActive) {
            invalidateUsersAssignedToRole(role.getId());
        }
    }

    private void invalidateUsersAssignedToRole(Integer roleId) {
        HashSet<Integer> affectedUserIds = new HashSet<>(userRoleRepository.findActiveUserIdsByRoleId(roleId));
        affectedUserIds.addAll(businessUserRepository.findActiveUserIdsByRoleId(roleId));
        securityVersionService.invalidateUsers(affectedUserIds);
    }
}
