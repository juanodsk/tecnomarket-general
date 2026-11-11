package com.tecmarket.backend.business.security;

import com.tecmarket.backend.business.repository.BusinessUserRepository;
import com.tecmarket.backend.user.repository.UserRoleRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
public class BusinessAccessGuard {

    private static final String PLATFORM_ADMIN_ROLE_CODE = "PLATFORM_ADMIN";
    private static final String BUSINESS_OWNER_ROLE_CODE = "BUSINESS_OWNER";
    private static final String BUSINESS_ADMIN_ROLE_CODE = "BUSINESS_ADMIN";
    private static final List<String> BUSINESS_USER_MANAGER_ROLE_CODES = List.of(
            BUSINESS_OWNER_ROLE_CODE,
            BUSINESS_ADMIN_ROLE_CODE);
    private final BusinessUserRepository businessUserRepository;
    private final UserRoleRepository userRoleRepository;

    public BusinessAccessGuard(
            BusinessUserRepository businessUserRepository,
            UserRoleRepository userRoleRepository) {
        this.businessUserRepository = businessUserRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Transactional(readOnly = true)
    public void assertCanAccessBusiness(Integer businessId, String subject) {
        Integer userId = parseUserId(subject);

        if (isPlatformAdmin(userId)) {
            return;
        }

        boolean belongsToBusiness = businessUserRepository
                .existsByBusinessIdAndUserIdAndActiveTrueAndDeletedAtIsNull(businessId, userId);
        if (!belongsToBusiness) {
            throw new AccessDeniedException("auth.forbidden");
        }
    }

    @Transactional(readOnly = true)
    public boolean isPlatformAdmin(Integer userId) {
        return userRoleRepository.findActiveRoleCodesByUserId(userId)
                .stream()
                .anyMatch(PLATFORM_ADMIN_ROLE_CODE::equals);
    }

    @Transactional(readOnly = true)
    public void assertPlatformAdmin(String subject) {
        Integer userId = parseUserId(subject);
        if (!isPlatformAdmin(userId)) {
            throw new AccessDeniedException("auth.forbidden");
        }
    }

    @Transactional(readOnly = true)
    public void assertCanManageBusinessUsers(Integer businessId, String subject) {
        Integer userId = parseUserId(subject);

        boolean canManageBusinessUsers = businessUserRepository.existsAnyActiveRoleCodeForBusiness(
                businessId,
                userId,
                BUSINESS_USER_MANAGER_ROLE_CODES);
        if (!canManageBusinessUsers) {
            throw new AccessDeniedException("auth.forbidden");
        }
    }

    @Transactional(readOnly = true)
    public void assertHasBusinessPermission(Integer businessId, String subject, String... permissionCodes) {
        if (!hasBusinessPermission(businessId, subject, permissionCodes)) {
            throw new AccessDeniedException("auth.forbidden");
        }
    }

    @Transactional(readOnly = true)
    public boolean hasBusinessPermission(Integer businessId, String subject, String... permissionCodes) {
        Integer userId = parseUserId(subject);

        boolean isPlatformAdmin = userRoleRepository.findActiveRoleCodesByUserId(userId)
                .stream()
                .anyMatch(PLATFORM_ADMIN_ROLE_CODE::equals);
        if (isPlatformAdmin) {
            return true;
        }

        List<String> codes = Arrays.stream(permissionCodes)
                .filter(code -> code != null && !code.isBlank())
                .distinct()
                .toList();

        return !codes.isEmpty()
                && businessUserRepository.existsActivePermissionForBusiness(businessId, userId, codes);
    }

    private Integer parseUserId(String subject) {
        try {
            return Integer.valueOf(subject);
        } catch (NumberFormatException exception) {
            throw new BadCredentialsException("auth.token.invalid");
        }
    }
}
