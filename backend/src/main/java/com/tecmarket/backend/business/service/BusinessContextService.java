package com.tecmarket.backend.business.service;

import com.tecmarket.backend.business.dto.BusinessContextBranchResponse;
import com.tecmarket.backend.business.dto.BusinessContextResponse;
import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.business.entity.BusinessBranch;
import com.tecmarket.backend.business.entity.BusinessUser;
import com.tecmarket.backend.business.repository.BusinessBranchRepository;
import com.tecmarket.backend.business.repository.BusinessRepository;
import com.tecmarket.backend.business.repository.BusinessUserRepository;
import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.business.security.BusinessBranchAccessGuard;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.permission.entity.PermissionScope;
import com.tecmarket.backend.permission.service.PermissionService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BusinessContextService {

    private static final String PLATFORM_ADMIN_ROLE_CODE = "PLATFORM_ADMIN";
    private static final String BUSINESS_SCOPE = "BUSINESS";

    private final BusinessRepository businessRepository;
    private final BusinessUserRepository businessUserRepository;
    private final BusinessBranchRepository businessBranchRepository;
    private final BusinessAccessGuard businessAccessGuard;
    private final BusinessBranchAccessGuard businessBranchAccessGuard;
    private final PermissionService permissionService;

    public BusinessContextService(
            BusinessRepository businessRepository,
            BusinessUserRepository businessUserRepository,
            BusinessBranchRepository businessBranchRepository,
            BusinessAccessGuard businessAccessGuard,
            BusinessBranchAccessGuard businessBranchAccessGuard,
            PermissionService permissionService) {
        this.businessRepository = businessRepository;
        this.businessUserRepository = businessUserRepository;
        this.businessBranchRepository = businessBranchRepository;
        this.businessAccessGuard = businessAccessGuard;
        this.businessBranchAccessGuard = businessBranchAccessGuard;
        this.permissionService = permissionService;
    }

    @Transactional(readOnly = true)
    public BusinessContextResponse findForAuthenticatedUser(Integer businessId, String subject) {
        Integer userId = parseUserId(subject);

        // Authorize first so an unrelated user cannot use the endpoint to discover
        // whether a tenant or one of its branches exists.
        List<Integer> accessibleBranchIds = businessBranchAccessGuard.accessibleBranchIds(businessId, subject);
        Business business = businessRepository.findByIdAndDeletedAtIsNull(businessId)
                .filter(value -> Boolean.TRUE.equals(value.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("business.not_found"));

        boolean platformAdmin = businessAccessGuard.isPlatformAdmin(userId);
        BusinessUser membership = platformAdmin ? null : findActiveMembership(businessId, userId);
        String permissionRoleCode = platformAdmin ? PLATFORM_ADMIN_ROLE_CODE : membership.getRole().getCode();
        List<String> permissions = permissionService.findActivePermissionCodesByRoleCodeAndScope(
                permissionRoleCode,
                PermissionScope.BUSINESS);

        Set<Integer> accessibleBranchIdSet = new HashSet<>(accessibleBranchIds);
        List<BusinessContextBranchResponse> branches = businessBranchRepository
                .findByBusinessIdAndDeletedAtIsNullOrderByPrimaryBranchDescNameAscIdAsc(businessId)
                .stream()
                .filter(branch -> Boolean.TRUE.equals(branch.getActive()))
                .filter(branch -> accessibleBranchIdSet.contains(branch.getId()))
                .map(this::toBranchResponse)
                .toList();

        return new BusinessContextResponse(
                userId,
                business.getId(),
                business.getName(),
                business.getLogo(),
                platformAdmin,
                membership == null ? null : membership.getId(),
                membership == null ? null : membership.getRole().getId(),
                membership == null ? null : membership.getRole().getCode(),
                membership == null ? null : membership.getRole().getName(),
                List.copyOf(permissions),
                branches);
    }

    private BusinessUser findActiveMembership(Integer businessId, Integer userId) {
        return businessUserRepository
                .findByBusinessIdAndUserIdAndActiveTrueAndDeletedAtIsNull(businessId, userId)
                .filter(this::hasActiveBusinessRole)
                .orElseThrow(() -> new AccessDeniedException("auth.forbidden"));
    }

    private boolean hasActiveBusinessRole(BusinessUser membership) {
        return membership.getRole() != null
                && Boolean.TRUE.equals(membership.getRole().getActive())
                && membership.getRole().getDeletedAt() == null
                && BUSINESS_SCOPE.equalsIgnoreCase(membership.getRole().getScope());
    }

    private BusinessContextBranchResponse toBranchResponse(BusinessBranch branch) {
        return new BusinessContextBranchResponse(
                branch.getId(),
                branch.getBranchNumber(),
                branch.getName(),
                branch.getCity().getId(),
                branch.getCity().getName(),
                branch.getAddress(),
                branch.getPrimaryBranch());
    }

    private Integer parseUserId(String subject) {
        try {
            return Integer.valueOf(subject);
        } catch (NumberFormatException exception) {
            throw new BadCredentialsException("auth.token.invalid");
        }
    }
}
