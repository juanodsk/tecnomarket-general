package com.tecmarket.backend.business.security;

import com.tecmarket.backend.business.entity.BusinessBranch;
import com.tecmarket.backend.business.repository.BusinessBranchRepository;
import com.tecmarket.backend.business.repository.BusinessUserBranchRepository;
import com.tecmarket.backend.business.repository.BusinessUserRepository;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class BusinessBranchAccessGuard {

    private static final List<String> ALL_BRANCH_ROLE_CODES = List.of("BUSINESS_OWNER", "BUSINESS_ADMIN");

    private final BusinessAccessGuard businessAccessGuard;
    private final BusinessBranchRepository businessBranchRepository;
    private final BusinessUserRepository businessUserRepository;
    private final BusinessUserBranchRepository businessUserBranchRepository;

    public BusinessBranchAccessGuard(
            BusinessAccessGuard businessAccessGuard,
            BusinessBranchRepository businessBranchRepository,
            BusinessUserRepository businessUserRepository,
            BusinessUserBranchRepository businessUserBranchRepository) {
        this.businessAccessGuard = businessAccessGuard;
        this.businessBranchRepository = businessBranchRepository;
        this.businessUserRepository = businessUserRepository;
        this.businessUserBranchRepository = businessUserBranchRepository;
    }

    @Transactional(readOnly = true)
    public BusinessBranch findAccessibleBranch(Integer businessId, Integer branchId, String subject) {
        businessAccessGuard.assertCanAccessBusiness(businessId, subject);
        BusinessBranch branch = findActiveBranch(businessId, branchId);
        assertSubjectCanAccessBranch(businessId, branchId, subject);
        return branch;
    }

    @Transactional
    public BusinessBranch findAccessibleBranchForUpdate(Integer businessId, Integer branchId, String subject) {
        businessAccessGuard.assertCanAccessBusiness(businessId, subject);
        BusinessBranch branch = businessBranchRepository.findForUpdateByIdAndBusinessId(branchId, businessId)
                .filter(value -> Boolean.TRUE.equals(value.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("business_branch.not_found"));
        assertSubjectCanAccessBranch(businessId, branchId, subject);
        return branch;
    }

    private void assertSubjectCanAccessBranch(Integer businessId, Integer branchId, String subject) {
        Integer userId = parseUserId(subject);

        if (canAccessAllBranches(businessId, userId)) {
            return;
        }

        if (!businessUserBranchRepository
                .existsByBusinessUserBusinessIdAndBusinessUserUserIdAndBranchIdAndActiveTrueAndDeletedAtIsNull(
                        businessId, userId, branchId)) {
            throw new AccessDeniedException("auth.forbidden");
        }
    }

    @Transactional(readOnly = true)
    public List<Integer> accessibleBranchIds(Integer businessId, String subject) {
        businessAccessGuard.assertCanAccessBusiness(businessId, subject);
        Integer userId = parseUserId(subject);
        if (canAccessAllBranches(businessId, userId)) {
            return businessBranchRepository.findByBusinessIdAndDeletedAtIsNullOrderByPrimaryBranchDescNameAscIdAsc(businessId)
                    .stream()
                    .filter(branch -> Boolean.TRUE.equals(branch.getActive()))
                    .map(BusinessBranch::getId)
                    .toList();
        }
        return businessUserBranchRepository
                .findByBusinessUserBusinessIdAndBusinessUserUserIdAndActiveTrueAndDeletedAtIsNullOrderByBranchBranchNumberAsc(
                        businessId, userId)
                .stream()
                .map(assignment -> assignment.getBranch().getId())
                .toList();
    }

    @Transactional(readOnly = true)
    public BusinessBranch findActiveBranch(Integer businessId, Integer branchId) {
        return businessBranchRepository.findByIdAndBusinessIdAndDeletedAtIsNull(branchId, businessId)
                .filter(branch -> Boolean.TRUE.equals(branch.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("business_branch.not_found"));
    }

    @Transactional(readOnly = true)
    public void assertBusinessUserAssigned(Integer businessId, Integer branchId, Integer businessUserId) {
        boolean managesAllBranches = businessUserRepository.findByIdAndBusinessIdAndDeletedAtIsNull(businessUserId, businessId)
                .filter(user -> Boolean.TRUE.equals(user.getActive()))
                .map(user -> ALL_BRANCH_ROLE_CODES.contains(user.getRole().getCode().toUpperCase()))
                .orElse(false);
        if (managesAllBranches) {
            return;
        }
        if (!businessUserBranchRepository
                .existsByBusinessUserIdAndBranchIdAndActiveTrueAndDeletedAtIsNull(businessUserId, branchId)) {
            throw new AccessDeniedException("business_user.branch.not_assigned");
        }
    }

    private Integer parseUserId(String subject) {
        try {
            return Integer.valueOf(subject);
        } catch (NumberFormatException exception) {
            throw new BadCredentialsException("auth.token.invalid");
        }
    }

    private boolean canAccessAllBranches(Integer businessId, Integer userId) {
        return businessAccessGuard.isPlatformAdmin(userId)
                || businessUserRepository.existsAnyActiveRoleCodeForBusiness(businessId, userId, ALL_BRANCH_ROLE_CODES);
    }
}
