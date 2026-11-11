package com.tecmarket.backend.parameter.service;

import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.auth.service.SecurityVersionService;
import com.tecmarket.backend.business.repository.BusinessUserRepository;
import com.tecmarket.backend.user.repository.UserRoleRepository;
import com.tecmarket.backend.parameter.dto.RoleAssignedPermissionResponse;
import com.tecmarket.backend.parameter.dto.RolePermissionOptionResponse;
import com.tecmarket.backend.parameter.dto.UpdateRolePermissionsRequest;
import com.tecmarket.backend.parameter.mapper.RolePermissionParameterMapper;
import com.tecmarket.backend.permission.entity.Permission;
import com.tecmarket.backend.permission.entity.PermissionScope;
import com.tecmarket.backend.permission.entity.Role;
import com.tecmarket.backend.permission.entity.RolePermission;
import com.tecmarket.backend.permission.repository.PermissionRepository;
import com.tecmarket.backend.permission.repository.RolePermissionRepository;
import com.tecmarket.backend.permission.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Service
public class RolePermissionParameterService {

    private static final String BUSINESS_SCOPE = "BUSINESS";
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final RolePermissionParameterMapper rolePermissionParameterMapper;
    private final BusinessAccessGuard businessAccessGuard;
    private final UserRoleRepository userRoleRepository;
    private final BusinessUserRepository businessUserRepository;
    private final SecurityVersionService securityVersionService;

    public RolePermissionParameterService(
            RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            RolePermissionRepository rolePermissionRepository,
            RolePermissionParameterMapper rolePermissionParameterMapper,
            BusinessAccessGuard businessAccessGuard,
            UserRoleRepository userRoleRepository,
            BusinessUserRepository businessUserRepository,
            SecurityVersionService securityVersionService) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.rolePermissionParameterMapper = rolePermissionParameterMapper;
        this.businessAccessGuard = businessAccessGuard;
        this.userRoleRepository = userRoleRepository;
        this.businessUserRepository = businessUserRepository;
        this.securityVersionService = securityVersionService;
    }

    @Transactional(readOnly = true)
    public List<RolePermissionOptionResponse> findPermissionsByRoleId(Integer roleId, String subject) {
        businessAccessGuard.assertPlatformAdmin(subject);
        Role role = findActiveRole(roleId);

        Set<Integer> assignedPermissionIds = rolePermissionRepository.findByRoleIdAndActiveTrue(role.getId())
                .stream()
                .map(rolePermission -> rolePermission.getPermission().getId())
                .collect(java.util.stream.Collectors.toSet());

        Stream<Permission> permissions = permissionRepository.findAllByActiveTrueOrderByCodeAsc().stream();

        if (isBusinessScope(role)) {
            permissions = permissions.filter(this::isAllowedForBusinessScope);
        }

        return permissions
                .map(permission -> rolePermissionParameterMapper.toResponse(
                        permission,
                        assignedPermissionIds.contains(permission.getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RoleAssignedPermissionResponse> findAssignedPermissionsByRoleId(Integer roleId, String subject) {
        businessAccessGuard.assertPlatformAdmin(subject);
        Role role = findActiveRole(roleId);

        return rolePermissionRepository.findByRoleIdAndActiveTrue(role.getId())
                .stream()
                .map(RolePermission::getPermission)
                .filter(permission -> !isBusinessScope(role) || isAllowedForBusinessScope(permission))
                .distinct()
                .sorted(Comparator.comparing(Permission::getCode, String.CASE_INSENSITIVE_ORDER))
                .map(rolePermissionParameterMapper::toAssignedResponse)
                .toList();
    }

    @Transactional
    public List<RolePermissionOptionResponse> updatePermissions(
            Integer roleId,
            UpdateRolePermissionsRequest request,
            String subject) {
        businessAccessGuard.assertPlatformAdmin(subject);
        Role role = findActiveRole(roleId);

        List<Integer> requestedPermissionIds = request.permissionIds()
                .stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        List<Permission> permissions = requestedPermissionIds.isEmpty()
                ? List.of()
                : permissionRepository.findByIdInAndActiveTrue(requestedPermissionIds);

        if (permissions.size() != requestedPermissionIds.size()) {
            throw new ResourceNotFoundException("permission.not_found");
        }

        validateScopePermissions(role, permissions);

        Map<Integer, Permission> permissionsById = permissions.stream()
                .collect(toMap(Permission::getId, identity()));

        Set<Integer> requestedPermissionIdSet = new HashSet<>(requestedPermissionIds);

        List<RolePermission> currentRelations = rolePermissionRepository.findByRoleId(role.getId());
        Set<Integer> currentActivePermissionIds = currentRelations.stream()
                .filter(relation -> Boolean.TRUE.equals(relation.getActive()))
                .map(relation -> relation.getPermission().getId())
                .collect(java.util.stream.Collectors.toSet());

        Map<Integer, RolePermission> relationByPermissionId = currentRelations.stream()
                .collect(toMap(
                        relation -> relation.getPermission().getId(),
                        identity()));

        LocalDateTime now = LocalDateTime.now();

        for (RolePermission relation : currentRelations) {
            Integer permissionId = relation.getPermission().getId();

            if (requestedPermissionIdSet.contains(permissionId)) {
                relation.setActive(true);
                relation.setDeletedAt(null);
            } else {
                relation.setActive(false);
                relation.setDeletedAt(now);
            }
        }

        for (Integer permissionId : requestedPermissionIdSet) {
            if (!relationByPermissionId.containsKey(permissionId)) {
                RolePermission relation = new RolePermission();
                relation.setRole(role);
                relation.setPermission(permissionsById.get(permissionId));
                relation.setActive(true);
                currentRelations.add(relation);
            }
        }

        rolePermissionRepository.saveAll(currentRelations);

        if (!currentActivePermissionIds.equals(requestedPermissionIdSet)) {
            invalidateUsersAssignedToRole(role.getId());
        }

        return findPermissionsByRoleId(role.getId(), subject);
    }

    private Role findActiveRole(Integer roleId) {
        return roleRepository.findById(roleId)
                .filter(role -> Boolean.TRUE.equals(role.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("role.not_found"));
    }

    private void validateScopePermissions(Role role, List<Permission> permissions) {
        if (!isBusinessScope(role)) {
            return;
        }

        boolean hasInvalidPermission = permissions.stream()
                .anyMatch(permission -> !isAllowedForBusinessScope(permission));

        if (hasInvalidPermission) {
            throw new ConflictException("role_permission.scope.invalid");
        }
    }

    private boolean isBusinessScope(Role role) {
        return BUSINESS_SCOPE.equalsIgnoreCase(role.getScope());
    }

    private boolean isAllowedForBusinessScope(Permission permission) {
        return permission.getScope() == PermissionScope.BUSINESS;
    }

    private void invalidateUsersAssignedToRole(Integer roleId) {
        Set<Integer> affectedUserIds = new HashSet<>(userRoleRepository.findActiveUserIdsByRoleId(roleId));
        affectedUserIds.addAll(businessUserRepository.findActiveUserIdsByRoleId(roleId));
        securityVersionService.invalidateUsers(affectedUserIds);
    }
}
