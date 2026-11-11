package com.tecmarket.backend.business.security;

import com.tecmarket.backend.business.repository.BusinessUserRepository;
import com.tecmarket.backend.user.repository.UserRoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BusinessAccessGuardTest {

    @Mock
    private BusinessUserRepository businessUserRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @InjectMocks
    private BusinessAccessGuard businessAccessGuard;

    @Test
    void assertCanManageBusinessUsersAllowsActiveBusinessAdminForSameBusiness() {
        when(businessUserRepository.existsAnyActiveRoleCodeForBusiness(
                10,
                5,
                List.of("BUSINESS_OWNER", "BUSINESS_ADMIN")))
                .thenReturn(true);

        assertDoesNotThrow(() -> businessAccessGuard.assertCanManageBusinessUsers(10, "5"));

        verify(businessUserRepository).existsAnyActiveRoleCodeForBusiness(
                10,
                5,
                List.of("BUSINESS_OWNER", "BUSINESS_ADMIN"));
        verifyNoInteractions(userRoleRepository);
    }

    @Test
    void assertCanManageBusinessUsersAllowsActiveBusinessOwnerForSameBusiness() {
        when(businessUserRepository.existsAnyActiveRoleCodeForBusiness(
                10,
                5,
                List.of("BUSINESS_OWNER", "BUSINESS_ADMIN")))
                .thenReturn(true);

        assertDoesNotThrow(() -> businessAccessGuard.assertCanManageBusinessUsers(10, "5"));

        verify(businessUserRepository).existsAnyActiveRoleCodeForBusiness(
                10,
                5,
                List.of("BUSINESS_OWNER", "BUSINESS_ADMIN"));
        verifyNoInteractions(userRoleRepository);
    }

    @Test
    void assertCanManageBusinessUsersRejectsUserWithoutManagerRoleInRequestedBusiness() {
        when(businessUserRepository.existsAnyActiveRoleCodeForBusiness(
                10,
                5,
                List.of("BUSINESS_OWNER", "BUSINESS_ADMIN")))
                .thenReturn(false);

        assertThrows(
                AccessDeniedException.class,
                () -> businessAccessGuard.assertCanManageBusinessUsers(10, "5"));

        verify(businessUserRepository).existsAnyActiveRoleCodeForBusiness(
                10,
                5,
                List.of("BUSINESS_OWNER", "BUSINESS_ADMIN"));
        verifyNoInteractions(userRoleRepository);
    }

    @Test
    void assertHasBusinessPermissionRejectsPermissionInheritedFromAnotherBusiness() {
        when(userRoleRepository.findActiveRoleCodesByUserId(5)).thenReturn(List.of());
        when(businessUserRepository.existsActivePermissionForBusiness(
                20,
                5,
                List.of("INVENTORY_UPDATE")))
                .thenReturn(false);

        assertThrows(
                AccessDeniedException.class,
                () -> businessAccessGuard.assertHasBusinessPermission(20, "5", "INVENTORY_UPDATE"));

        verify(userRoleRepository).findActiveRoleCodesByUserId(5);
        verify(businessUserRepository).existsActivePermissionForBusiness(
                20,
                5,
                List.of("INVENTORY_UPDATE"));
        verifyNoMoreInteractions(userRoleRepository, businessUserRepository);
    }

    @Test
    void assertHasBusinessPermissionAllowsPermissionInRequestedBusiness() {
        when(userRoleRepository.findActiveRoleCodesByUserId(5)).thenReturn(List.of());
        when(businessUserRepository.existsActivePermissionForBusiness(
                10,
                5,
                List.of("INVENTORY_VIEW")))
                .thenReturn(true);

        assertDoesNotThrow(
                () -> businessAccessGuard.assertHasBusinessPermission(10, "5", "INVENTORY_VIEW"));

        verify(businessUserRepository).existsActivePermissionForBusiness(
                10,
                5,
                List.of("INVENTORY_VIEW"));
    }

    @Test
    void assertPlatformAdminRejectsBusinessRoleHolder() {
        when(userRoleRepository.findActiveRoleCodesByUserId(5)).thenReturn(List.of("CUSTOMER"));

        assertThrows(
                AccessDeniedException.class,
                () -> businessAccessGuard.assertPlatformAdmin("5"));

        verifyNoInteractions(businessUserRepository);
    }
}
