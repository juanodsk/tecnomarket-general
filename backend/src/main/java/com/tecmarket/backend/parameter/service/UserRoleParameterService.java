package com.tecmarket.backend.parameter.service;

import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.parameter.dto.UpdateUserRolesRequest;
import com.tecmarket.backend.parameter.dto.UserRoleOptionResponse;
import com.tecmarket.backend.parameter.mapper.UserRoleParameterMapper;
import com.tecmarket.backend.permission.entity.Role;
import com.tecmarket.backend.permission.repository.RoleRepository;
import com.tecmarket.backend.user.entity.AppUser;
import com.tecmarket.backend.user.entity.UserRole;
import com.tecmarket.backend.user.repository.AppUserRepository;
import com.tecmarket.backend.user.repository.UserRoleRepository;
import com.tecmarket.backend.auth.service.SecurityVersionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Service
public class UserRoleParameterService {
    private static final String GLOBAL_SCOPE = "GLOBAL";

    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserRoleParameterMapper userRoleParameterMapper;
    private final SecurityVersionService securityVersionService;
    private final BusinessAccessGuard businessAccessGuard;

    public UserRoleParameterService(
            AppUserRepository appUserRepository,
            RoleRepository roleRepository,
            UserRoleRepository userRoleRepository,
            UserRoleParameterMapper userRoleParameterMapper,
            SecurityVersionService securityVersionService,
            BusinessAccessGuard businessAccessGuard) {
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.userRoleParameterMapper = userRoleParameterMapper;
        this.securityVersionService = securityVersionService;
        this.businessAccessGuard = businessAccessGuard;
    }

    @Transactional(readOnly = true)
    public List<UserRoleOptionResponse> findRolesByUserId(Integer userId, String subject) {
        businessAccessGuard.assertPlatformAdmin(subject);
        AppUser user = findActiveUser(userId);
        return mapRoleOptions(user);
    }

    @Transactional
    public List<UserRoleOptionResponse> updateRoles(
            Integer userId,
            UpdateUserRolesRequest request,
            String subject) {
        businessAccessGuard.assertPlatformAdmin(subject);
        AppUser user = findActiveUser(userId);

        List<Integer> requestedRoleIds = request.roleIds()
                .stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        List<Role> roles = requestedRoleIds.isEmpty()
                ? List.of()
                : roleRepository.findByIdInAndActiveTrueAndScope(requestedRoleIds, GLOBAL_SCOPE);

        if (roles.size() != requestedRoleIds.size()) {
            throw new ResourceNotFoundException("role.not_found");
        }

        Map<Integer, Role> rolesById = roles.stream()
                .collect(toMap(Role::getId, identity()));

        Set<Integer> requestedRoleIdSet = new HashSet<>(requestedRoleIds);

        List<UserRole> currentRelations = userRoleRepository.findByUserId(user.getId());
        Set<Integer> currentActiveRoleIds = currentRelations.stream()
                .filter(relation -> Boolean.TRUE.equals(relation.getActive()))
                .map(relation -> relation.getRole().getId())
                .collect(java.util.stream.Collectors.toSet());

        Map<Integer, UserRole> relationByRoleId = currentRelations.stream()
                .collect(toMap(
                        relation -> relation.getRole().getId(),
                        identity(),
                        (first, second) -> first));

        LocalDateTime now = LocalDateTime.now();

        for (UserRole relation : currentRelations) {
            Integer roleId = relation.getRole().getId();

            if (requestedRoleIdSet.contains(roleId)) {
                relation.setActive(true);
                relation.setDeletedAt(null);
            } else {
                relation.setActive(false);
                relation.setDeletedAt(now);
            }
        }

        for (Integer roleId : requestedRoleIdSet) {
            if (!relationByRoleId.containsKey(roleId)) {
                UserRole relation = new UserRole();
                relation.setUser(user);
                relation.setRole(rolesById.get(roleId));
                relation.setActive(true);
                currentRelations.add(relation);
            }
        }

        if (!currentRelations.isEmpty()) {
            userRoleRepository.saveAll(currentRelations);
        }

        if (!currentActiveRoleIds.equals(requestedRoleIdSet)) {
            securityVersionService.invalidateUser(user);
        }

        return mapRoleOptions(user);
    }

    private List<UserRoleOptionResponse> mapRoleOptions(AppUser user) {
        Set<Integer> assignedRoleIds = userRoleRepository.findByUserIdAndActiveTrue(user.getId())
                .stream()
                .map(userRole -> userRole.getRole().getId())
                .collect(java.util.stream.Collectors.toSet());

        return roleRepository.findAllByActiveTrueAndScopeOrderByNameAsc(GLOBAL_SCOPE)
                .stream()
                .map(role -> userRoleParameterMapper.toResponse(role, assignedRoleIds.contains(role.getId())))
                .toList();
    }

    private AppUser findActiveUser(Integer userId) {
        return appUserRepository.findById(userId)
                .filter(user -> Boolean.TRUE.equals(user.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("user.not_found"));
    }
}
