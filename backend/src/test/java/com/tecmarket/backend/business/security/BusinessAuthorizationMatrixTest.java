package com.tecmarket.backend.business.security;

import com.tecmarket.backend.business.entity.BusinessBranch;
import com.tecmarket.backend.business.repository.BusinessBranchRepository;
import com.tecmarket.backend.business.repository.BusinessUserBranchRepository;
import com.tecmarket.backend.business.repository.BusinessUserRepository;
import com.tecmarket.backend.user.repository.UserRoleRepository;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BusinessAuthorizationMatrixTest {

    private static final Integer BUSINESS_ID = 10;
    private static final Integer BRANCH_ID = 20;
    private static final Integer USER_ID = 5;

    @ParameterizedTest(name = "{0}")
    @MethodSource("permissionCases")
    void enforcesPermissionInsideTheRequestedTenant(PermissionCase testCase) {
        BusinessUserRepository businessUsers = mock(BusinessUserRepository.class);
        UserRoleRepository globalRoles = mock(UserRoleRepository.class);
        BusinessAccessGuard guard = new BusinessAccessGuard(businessUsers, globalRoles);

        when(globalRoles.findActiveRoleCodesByUserId(USER_ID))
                .thenReturn(testCase.platformAdmin() ? List.of("PLATFORM_ADMIN") : List.of());
        if (!testCase.platformAdmin()) {
            when(businessUsers.existsActivePermissionForBusiness(
                    BUSINESS_ID, USER_ID, List.of("REPAIR_ORDER_UPDATE")))
                    .thenReturn(testCase.permissionInRequestedTenant());
        }

        if (testCase.allowed()) {
            assertThatCode(() -> guard.assertHasBusinessPermission(
                    BUSINESS_ID, USER_ID.toString(), "REPAIR_ORDER_UPDATE"))
                    .doesNotThrowAnyException();
        } else {
            assertThatThrownBy(() -> guard.assertHasBusinessPermission(
                    BUSINESS_ID, USER_ID.toString(), "REPAIR_ORDER_UPDATE"))
                    .isInstanceOf(AccessDeniedException.class);
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("branchCases")
    void enforcesRoleAndAssignmentAtBranchBoundary(BranchCase testCase) {
        BusinessAccessGuard businessAccess = mock(BusinessAccessGuard.class);
        BusinessBranchRepository branches = mock(BusinessBranchRepository.class);
        BusinessUserRepository businessUsers = mock(BusinessUserRepository.class);
        BusinessUserBranchRepository assignments = mock(BusinessUserBranchRepository.class);
        BusinessBranchAccessGuard guard = new BusinessBranchAccessGuard(
                businessAccess, branches, businessUsers, assignments);

        BusinessBranch branch = new BusinessBranch();
        branch.setId(BRANCH_ID);
        branch.setActive(true);
        when(branches.findByIdAndBusinessIdAndDeletedAtIsNull(BRANCH_ID, BUSINESS_ID))
                .thenReturn(Optional.of(branch));
        when(businessAccess.isPlatformAdmin(USER_ID)).thenReturn(testCase.platformAdmin());
        if (!testCase.platformAdmin()) {
            when(businessUsers.existsAnyActiveRoleCodeForBusiness(
                    BUSINESS_ID, USER_ID, List.of("BUSINESS_OWNER", "BUSINESS_ADMIN")))
                    .thenReturn(testCase.allBranchRole());
        }
        if (!testCase.platformAdmin() && !testCase.allBranchRole()) {
            when(assignments
                    .existsByBusinessUserBusinessIdAndBusinessUserUserIdAndBranchIdAndActiveTrueAndDeletedAtIsNull(
                            BUSINESS_ID, USER_ID, BRANCH_ID))
                    .thenReturn(testCase.assignedToBranch());
        }

        if (testCase.allowed()) {
            assertThatCode(() -> guard.findAccessibleBranch(BUSINESS_ID, BRANCH_ID, USER_ID.toString()))
                    .doesNotThrowAnyException();
        } else {
            assertThatThrownBy(() -> guard.findAccessibleBranch(BUSINESS_ID, BRANCH_ID, USER_ID.toString()))
                    .isInstanceOf(AccessDeniedException.class);
        }
    }

    private Stream<PermissionCase> permissionCases() {
        return Stream.of(
                new PermissionCase("platform admin", true, false, true),
                new PermissionCase("business role with tenant permission", false, true, true),
                new PermissionCase("permission from another tenant", false, false, false));
    }

    private Stream<BranchCase> branchCases() {
        return Stream.of(
                new BranchCase("platform admin can access every branch", true, false, false, true),
                new BranchCase("business owner can access every branch", false, true, false, true),
                new BranchCase("business admin can access every branch", false, true, false, true),
                new BranchCase("employee assigned to branch", false, false, true, true),
                new BranchCase("employee from another branch", false, false, false, false));
    }

    private record PermissionCase(
            String label,
            boolean platformAdmin,
            boolean permissionInRequestedTenant,
            boolean allowed) {

        @Override
        public String toString() {
            return label;
        }
    }

    private record BranchCase(
            String label,
            boolean platformAdmin,
            boolean allBranchRole,
            boolean assignedToBranch,
            boolean allowed) {

        @Override
        public String toString() {
            return label;
        }
    }
}
