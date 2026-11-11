package com.tecmarket.backend.business.service;

import com.tecmarket.backend.auth.service.SecurityVersionService;
import com.tecmarket.backend.business.dto.UpdateBusinessUserRequest;
import com.tecmarket.backend.business.entity.BusinessBranch;
import com.tecmarket.backend.business.entity.BusinessUser;
import com.tecmarket.backend.business.entity.BusinessUserBranch;
import com.tecmarket.backend.business.mapper.BusinessUserMapper;
import com.tecmarket.backend.business.repository.BusinessBranchRepository;
import com.tecmarket.backend.business.repository.BusinessRepository;
import com.tecmarket.backend.business.repository.BusinessUserBranchRepository;
import com.tecmarket.backend.business.repository.BusinessUserRepository;
import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.business.security.BusinessBranchAccessGuard;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.permission.entity.Role;
import com.tecmarket.backend.permission.repository.RoleRepository;
import com.tecmarket.backend.user.entity.AppUser;
import com.tecmarket.backend.user.repository.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BusinessUserServiceBranchAssignmentTest {

    private static final Integer BUSINESS_ID = 10;
    private static final Integer BUSINESS_USER_ID = 20;
    private static final String SUBJECT = "30";

    @Mock private BusinessRepository businessRepository;
    @Mock private BusinessUserRepository businessUserRepository;
    @Mock private AppUserRepository appUserRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private BusinessUserMapper businessUserMapper;
    @Mock private BusinessAccessGuard businessAccessGuard;
    @Mock private BusinessBranchRepository businessBranchRepository;
    @Mock private BusinessUserBranchRepository businessUserBranchRepository;
    @Mock private BusinessBranchAccessGuard businessBranchAccessGuard;
    @Mock private SecurityVersionService securityVersionService;

    private BusinessUserService service;

    @BeforeEach
    void setUp() {
        service = new BusinessUserService(
                businessRepository,
                businessUserRepository,
                appUserRepository,
                roleRepository,
                businessUserMapper,
                businessAccessGuard,
                businessBranchRepository,
                businessUserBranchRepository,
                businessBranchAccessGuard,
                securityVersionService);
    }

    @Test
    void updateKeepsSharedAssignmentRemovesMissingAndCreatesOnlyNewBranch() {
        Role currentRole = role(40, "RECEPTIONIST");
        Role requestedRole = role(41, "TECHNICIAN");
        BusinessUser businessUser = businessUser(currentRole);
        BusinessBranch branchA = branch(101);
        BusinessBranch branchB = branch(102);
        BusinessBranch branchC = branch(103);
        BusinessUserBranch assignmentA = assignment(businessUser, branchA);
        BusinessUserBranch assignmentB = assignment(businessUser, branchB);

        when(businessUserRepository.findByIdAndBusinessIdAndDeletedAtIsNull(BUSINESS_USER_ID, BUSINESS_ID))
                .thenReturn(Optional.of(businessUser));
        when(roleRepository.findById(requestedRole.getId())).thenReturn(Optional.of(requestedRole));
        when(businessBranchRepository.findByIdAndBusinessIdAndDeletedAtIsNull(branchB.getId(), BUSINESS_ID))
                .thenReturn(Optional.of(branchB));
        when(businessBranchRepository.findByIdAndBusinessIdAndDeletedAtIsNull(branchC.getId(), BUSINESS_ID))
                .thenReturn(Optional.of(branchC));
        when(businessUserBranchRepository
                .findByBusinessUserIdAndDeletedAtIsNullOrderByBranchBranchNumberAsc(BUSINESS_USER_ID))
                .thenReturn(List.of(assignmentA, assignmentB));

        service.update(
                BUSINESS_ID,
                BUSINESS_USER_ID,
                new UpdateBusinessUserRequest(
                        requestedRole.getId(),
                        List.of(branchB.getId(), branchC.getId()),
                        true,
                        new BigDecimal("15.00")),
                SUBJECT);

        assertThat(assignmentA.getActive()).isFalse();
        assertThat(assignmentA.getDeletedAt()).isNotNull();
        assertThat(assignmentB.getActive()).isTrue();
        assertThat(assignmentB.getDeletedAt()).isNull();

        ArgumentCaptor<BusinessUserBranch> insertedAssignment = ArgumentCaptor.forClass(BusinessUserBranch.class);
        verify(businessUserBranchRepository).save(insertedAssignment.capture());
        assertThat(insertedAssignment.getValue().getBusinessUser()).isSameAs(businessUser);
        assertThat(insertedAssignment.getValue().getBranch()).isSameAs(branchC);
        assertThat(insertedAssignment.getValue().getActive()).isTrue();
        verify(securityVersionService).invalidateUser(businessUser.getUser());
    }

    @Test
    void updateWithSameBranchesDoesNotInsertDuplicateAssignments() {
        Role role = role(40, "RECEPTIONIST");
        BusinessUser businessUser = businessUser(role);
        BusinessBranch branchA = branch(101);
        BusinessBranch branchB = branch(102);
        BusinessUserBranch assignmentA = assignment(businessUser, branchA);
        BusinessUserBranch assignmentB = assignment(businessUser, branchB);

        when(businessUserRepository.findByIdAndBusinessIdAndDeletedAtIsNull(BUSINESS_USER_ID, BUSINESS_ID))
                .thenReturn(Optional.of(businessUser));
        when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
        when(businessBranchRepository.findByIdAndBusinessIdAndDeletedAtIsNull(branchA.getId(), BUSINESS_ID))
                .thenReturn(Optional.of(branchA));
        when(businessBranchRepository.findByIdAndBusinessIdAndDeletedAtIsNull(branchB.getId(), BUSINESS_ID))
                .thenReturn(Optional.of(branchB));
        when(businessUserBranchRepository
                .findByBusinessUserIdAndDeletedAtIsNullOrderByBranchBranchNumberAsc(BUSINESS_USER_ID))
                .thenReturn(List.of(assignmentA, assignmentB));

        service.update(
                BUSINESS_ID,
                BUSINESS_USER_ID,
                new UpdateBusinessUserRequest(
                        role.getId(),
                        List.of(branchA.getId(), branchA.getId(), branchB.getId()),
                        true,
                        BigDecimal.ZERO),
                SUBJECT);

        verify(businessUserBranchRepository, never()).save(any());
        assertThat(assignmentA.getActive()).isTrue();
        assertThat(assignmentA.getDeletedAt()).isNull();
        assertThat(assignmentB.getActive()).isTrue();
        assertThat(assignmentB.getDeletedAt()).isNull();
        verify(securityVersionService, never()).invalidateUser(any(AppUser.class));
    }

    @Test
    void updateRejectsBranchOutsideBusinessBeforeChangingAssignments() {
        Role role = role(40, "RECEPTIONIST");
        BusinessUser businessUser = businessUser(role);

        when(businessUserRepository.findByIdAndBusinessIdAndDeletedAtIsNull(BUSINESS_USER_ID, BUSINESS_ID))
                .thenReturn(Optional.of(businessUser));
        when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
        when(businessBranchRepository.findByIdAndBusinessIdAndDeletedAtIsNull(999, BUSINESS_ID))
                .thenReturn(Optional.empty());

        UpdateBusinessUserRequest request = new UpdateBusinessUserRequest(
                role.getId(),
                List.of(999),
                true,
                BigDecimal.ZERO);

        assertThatThrownBy(() -> service.update(BUSINESS_ID, BUSINESS_USER_ID, request, SUBJECT))
                .isInstanceOf(ResourceNotFoundException.class);

        verifyNoInteractions(businessUserBranchRepository);
    }

    private BusinessUser businessUser(Role role) {
        AppUser user = new AppUser();
        user.setId(50);

        BusinessUser businessUser = new BusinessUser();
        businessUser.setId(BUSINESS_USER_ID);
        businessUser.setUser(user);
        businessUser.setRole(role);
        businessUser.setActive(true);
        businessUser.setCommissionPercentage(BigDecimal.ZERO);
        return businessUser;
    }

    private Role role(Integer id, String code) {
        Role role = new Role();
        role.setId(id);
        role.setCode(code);
        role.setName(code);
        role.setDescription(code);
        role.setScope("BUSINESS");
        role.setActive(true);
        return role;
    }

    private BusinessBranch branch(Integer id) {
        BusinessBranch branch = new BusinessBranch();
        branch.setId(id);
        branch.setBranchNumber(id);
        branch.setActive(true);
        return branch;
    }

    private BusinessUserBranch assignment(BusinessUser businessUser, BusinessBranch branch) {
        BusinessUserBranch assignment = new BusinessUserBranch();
        assignment.setBusinessUser(businessUser);
        assignment.setBranch(branch);
        assignment.setActive(true);
        return assignment;
    }
}
