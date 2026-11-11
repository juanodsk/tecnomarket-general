package com.tecmarket.backend.business.security;

import com.tecmarket.backend.business.entity.BusinessBranch;
import com.tecmarket.backend.business.repository.BusinessBranchRepository;
import com.tecmarket.backend.business.repository.BusinessUserBranchRepository;
import com.tecmarket.backend.business.repository.BusinessUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BusinessBranchAccessGuardTest {

    @Mock
    private BusinessAccessGuard businessAccessGuard;
    @Mock
    private BusinessBranchRepository businessBranchRepository;
    @Mock
    private BusinessUserRepository businessUserRepository;
    @Mock
    private BusinessUserBranchRepository businessUserBranchRepository;
    @InjectMocks
    private BusinessBranchAccessGuard guard;

    @Test
    void allowsEmployeeAssignedToRequestedBranch() {
        BusinessBranch branch = activeBranch();
        when(businessBranchRepository.findByIdAndBusinessIdAndDeletedAtIsNull(20, 10)).thenReturn(Optional.of(branch));
        when(businessAccessGuard.isPlatformAdmin(5)).thenReturn(false);
        when(businessUserRepository.existsAnyActiveRoleCodeForBusiness(
                10, 5, List.of("BUSINESS_OWNER", "BUSINESS_ADMIN"))).thenReturn(false);
        when(businessUserBranchRepository
                .existsByBusinessUserBusinessIdAndBusinessUserUserIdAndBranchIdAndActiveTrueAndDeletedAtIsNull(
                        10, 5, 20)).thenReturn(true);

        assertDoesNotThrow(() -> guard.findAccessibleBranch(10, 20, "5"));
    }

    @Test
    void rejectsEmployeeNotAssignedToRequestedBranch() {
        BusinessBranch branch = activeBranch();
        when(businessBranchRepository.findByIdAndBusinessIdAndDeletedAtIsNull(20, 10)).thenReturn(Optional.of(branch));
        when(businessAccessGuard.isPlatformAdmin(5)).thenReturn(false);
        when(businessUserRepository.existsAnyActiveRoleCodeForBusiness(
                10, 5, List.of("BUSINESS_OWNER", "BUSINESS_ADMIN"))).thenReturn(false);
        when(businessUserBranchRepository
                .existsByBusinessUserBusinessIdAndBusinessUserUserIdAndBranchIdAndActiveTrueAndDeletedAtIsNull(
                        10, 5, 20)).thenReturn(false);

        assertThrows(AccessDeniedException.class, () -> guard.findAccessibleBranch(10, 20, "5"));
    }

    private BusinessBranch activeBranch() {
        BusinessBranch branch = new BusinessBranch();
        branch.setId(20);
        branch.setActive(true);
        return branch;
    }
}
