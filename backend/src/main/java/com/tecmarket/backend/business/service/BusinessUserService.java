package com.tecmarket.backend.business.service;

import com.tecmarket.backend.business.dto.BusinessUserResponse;
import com.tecmarket.backend.business.dto.BusinessRoleOptionResponse;
import com.tecmarket.backend.business.dto.BusinessTechnicianOptionResponse;
import com.tecmarket.backend.business.dto.BusinessUserCandidateResponse;
import com.tecmarket.backend.business.dto.CreateBusinessUserRequest;
import com.tecmarket.backend.business.dto.UpdateBusinessUserRequest;
import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.business.entity.BusinessUser;
import com.tecmarket.backend.business.entity.BusinessBranch;
import com.tecmarket.backend.business.entity.BusinessUserBranch;
import com.tecmarket.backend.business.mapper.BusinessUserMapper;
import com.tecmarket.backend.business.repository.BusinessRepository;
import com.tecmarket.backend.business.repository.BusinessUserRepository;
import com.tecmarket.backend.business.repository.BusinessBranchRepository;
import com.tecmarket.backend.business.repository.BusinessUserBranchRepository;
import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.business.security.BusinessBranchAccessGuard;
import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.common.pagination.PageRequestFactory;
import com.tecmarket.backend.common.pagination.PageResponse;
import com.tecmarket.backend.permission.entity.Role;
import com.tecmarket.backend.permission.repository.RoleRepository;
import com.tecmarket.backend.user.entity.AppUser;
import com.tecmarket.backend.user.repository.AppUserRepository;
import com.tecmarket.backend.auth.service.SecurityVersionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class BusinessUserService {

    private static final String BUSINESS_SCOPE = "BUSINESS";
    private static final String TECHNICIAN_ROLE_CODE = "TECHNICIAN";

    private final BusinessRepository businessRepository;
    private final BusinessUserRepository businessUserRepository;
    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final BusinessUserMapper businessUserMapper;
    private final BusinessAccessGuard businessAccessGuard;
    private final BusinessBranchRepository businessBranchRepository;
    private final BusinessUserBranchRepository businessUserBranchRepository;
    private final BusinessBranchAccessGuard businessBranchAccessGuard;
    private final SecurityVersionService securityVersionService;

    public BusinessUserService(
            BusinessRepository businessRepository,
            BusinessUserRepository businessUserRepository,
            AppUserRepository appUserRepository,
            RoleRepository roleRepository,
            BusinessUserMapper businessUserMapper,
            BusinessAccessGuard businessAccessGuard,
            BusinessBranchRepository businessBranchRepository,
            BusinessUserBranchRepository businessUserBranchRepository,
            BusinessBranchAccessGuard businessBranchAccessGuard,
            SecurityVersionService securityVersionService) {
        this.businessRepository = businessRepository;
        this.businessUserRepository = businessUserRepository;
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
        this.businessUserMapper = businessUserMapper;
        this.businessAccessGuard = businessAccessGuard;
        this.businessBranchRepository = businessBranchRepository;
        this.businessUserBranchRepository = businessUserBranchRepository;
        this.businessBranchAccessGuard = businessBranchAccessGuard;
        this.securityVersionService = securityVersionService;
    }

    @Transactional(readOnly = true)
    public PageResponse<BusinessUserResponse> findAllByBusiness(
            Integer businessId, Integer page, Integer size, String subject) {
        businessAccessGuard.assertCanManageBusinessUsers(businessId, subject);
        findBusiness(businessId);
        return PageResponse.from(businessUserRepository
                .findByBusinessIdAndDeletedAtIsNullOrderByIdAsc(
                        businessId, PageRequestFactory.of(page, size))
                .map(businessUserMapper::toResponse));
    }

    @Transactional
    public BusinessUserResponse create(Integer businessId, CreateBusinessUserRequest request, String subject) {
        businessAccessGuard.assertCanManageBusinessUsers(businessId, subject);
        Business business = findBusiness(businessId);
        AppUser user = findUser(request.userId());
        Role role = findBusinessRole(request.roleId());

        List<BusinessUser> existingRelations = businessUserRepository
                .findByBusinessIdAndUserIdAndDeletedAtIsNullOrderByIdAsc(businessId, request.userId());

        if (!existingRelations.isEmpty()) {
            BusinessUser relation = existingRelations.getFirst();
            if (Boolean.TRUE.equals(relation.getActive())) {
                throw new ConflictException("business_user.exists");
            }
            relation.setRole(role);
            relation.setCommissionPercentage(resolveCommissionPercentage(role, request.commissionPercentage(), relation.getCommissionPercentage()));
            relation.setActive(true);
            relation.setDeletedAt(null);
            replaceBranchAssignments(businessId, relation, request.branchIds());
            securityVersionService.invalidateUser(user);
            return businessUserMapper.toResponse(relation);
        }

        BusinessUser created = new BusinessUser();
        created.setBusiness(business);
        created.setUser(user);
        created.setRole(role);
        created.setCommissionPercentage(resolveCommissionPercentage(role, request.commissionPercentage(), null));
        created.setActive(true);

        BusinessUser saved = businessUserRepository.save(created);
        replaceBranchAssignments(businessId, saved, request.branchIds());
        securityVersionService.invalidateUser(user);
        return businessUserMapper.toResponse(saved);
    }

    @Transactional
    public BusinessUserResponse update(Integer businessId, Integer businessUserId, UpdateBusinessUserRequest request, String subject) {
        businessAccessGuard.assertCanManageBusinessUsers(businessId, subject);
        BusinessUser relation = businessUserRepository
                .findByIdAndBusinessIdAndDeletedAtIsNull(businessUserId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("business_user.not_found"));

        Role role = findBusinessRole(request.roleId());
        Integer previousRoleId = relation.getRole().getId();
        Boolean previousActive = relation.getActive();

        relation.setRole(role);
        relation.setCommissionPercentage(resolveCommissionPercentage(
                role,
                request.commissionPercentage(),
                relation.getCommissionPercentage()));
        replaceBranchAssignments(businessId, relation, request.branchIds());

        if (request.active() != null) {
            relation.setActive(request.active());
            relation.setDeletedAt(Boolean.FALSE.equals(request.active()) ? LocalDateTime.now() : null);
        }

        if (!Objects.equals(previousRoleId, relation.getRole().getId())
                || !Objects.equals(previousActive, relation.getActive())) {
            securityVersionService.invalidateUser(relation.getUser());
        }

        return businessUserMapper.toResponse(relation);
    }

    @Transactional
    public void delete(Integer businessId, Integer businessUserId, String subject) {
        businessAccessGuard.assertCanManageBusinessUsers(businessId, subject);
        BusinessUser relation = businessUserRepository
                .findByIdAndBusinessIdAndDeletedAtIsNull(businessUserId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("business_user.not_found"));

        relation.setActive(false);
        relation.setDeletedAt(LocalDateTime.now());
        securityVersionService.invalidateUser(relation.getUser());
    }

    @Transactional(readOnly = true)
    public List<BusinessRoleOptionResponse> findAssignableRoles(Integer businessId, String subject) {
        businessAccessGuard.assertCanManageBusinessUsers(businessId, subject);
        findBusiness(businessId);
        return roleRepository.findAllByActiveTrueAndScopeOrderByNameAsc(BUSINESS_SCOPE)
                .stream()
                .map(role -> new BusinessRoleOptionResponse(
                        role.getId(),
                        role.getCode(),
                        role.getName()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BusinessUserCandidateResponse> findAvailableUsers(Integer businessId, String email, String subject) {
        businessAccessGuard.assertCanManageBusinessUsers(businessId, subject);
        findBusiness(businessId);

        String normalizedEmail = email == null ? "" : email.trim();
        if (normalizedEmail.isBlank() || !isValidEmail(normalizedEmail)) {
            return List.of();
        }

        return appUserRepository.findByEmailIgnoreCaseAndActiveTrueAndDeletedAtIsNull(normalizedEmail)
                .stream()
                .map(user -> new BusinessUserCandidateResponse(
                        user.getId(),
                        buildFullName(user),
                        user.getEmail(),
                        businessUserRepository.existsByBusinessIdAndUserIdAndActiveTrueAndDeletedAtIsNull(
                                businessId,
                                user.getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BusinessTechnicianOptionResponse> findTechniciansForRepairOrders(
            Integer businessId, Integer branchId, String subject) {
        businessAccessGuard.assertHasBusinessPermission(
                businessId,
                subject,
                "REPAIR_ORDER_CREATE",
                "REPAIR_ORDER_UPDATE");
        findBusiness(businessId);
        if (branchId != null) {
            businessBranchAccessGuard.findAccessibleBranch(businessId, branchId, subject);
        }

        List<BusinessUser> technicians = branchId == null
                ? businessUserRepository.findActiveByBusinessAndRoleCode(businessId, TECHNICIAN_ROLE_CODE)
                : businessUserRepository.findActiveByBusinessBranchAndRoleCode(businessId, branchId, TECHNICIAN_ROLE_CODE);
        return technicians
                .stream()
                .map(relation -> new BusinessTechnicianOptionResponse(
                        relation.getId(),
                        relation.getUser().getId(),
                        relation.getUser().getName(),
                        relation.getUser().getSurname(),
                        relation.getUser().getEmail(),
                        relation.getRole().getCode(),
                        relation.getRole().getName(),
                        relation.getCommissionPercentage()))
                .toList();
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    }

    private Business findBusiness(Integer businessId) {
        return businessRepository.findByIdAndDeletedAtIsNull(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("business.not_found"));
    }

    private AppUser findUser(Integer userId) {
        return appUserRepository.findById(userId)
                .filter(user -> Boolean.TRUE.equals(user.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("user.not_found"));
    }

    private Role findBusinessRole(Integer roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("role.not_found"));

        if (!Boolean.TRUE.equals(role.getActive()) || !BUSINESS_SCOPE.equalsIgnoreCase(role.getScope())) {
            throw new ResourceNotFoundException("role.not_found");
        }
        return role;
    }

    private String buildFullName(AppUser user) {
        String name = user.getName() == null ? "" : user.getName().trim();
        String surname = user.getSurname() == null ? "" : user.getSurname().trim();
        String fullName = (name + " " + surname).trim();
        if (!fullName.isBlank()) {
            return fullName;
        }
        return user.getEmail();
    }

    private BigDecimal resolveCommissionPercentage(Role role, BigDecimal requested, BigDecimal current) {
        if (!TECHNICIAN_ROLE_CODE.equalsIgnoreCase(role.getCode())) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal resolved = requested != null ? requested : current;
        if (resolved == null) {
            resolved = BigDecimal.ZERO;
        }
        return resolved.setScale(2, RoundingMode.HALF_UP);
    }

    private void replaceBranchAssignments(Integer businessId, BusinessUser businessUser, List<Integer> branchIds) {
        Set<Integer> requestedBranchIds = new LinkedHashSet<>(branchIds);
        if (requestedBranchIds.isEmpty()) {
            throw new ConflictException("business_user.branch.required");
        }

        List<BusinessBranch> requestedBranches = requestedBranchIds.stream()
                .map(branchId -> businessBranchRepository.findByIdAndBusinessIdAndDeletedAtIsNull(branchId, businessId)
                        .filter(branch -> Boolean.TRUE.equals(branch.getActive()))
                        .orElseThrow(() -> new ResourceNotFoundException("business_branch.not_found")))
                .toList();

        List<BusinessUserBranch> currentAssignments = businessUserBranchRepository
                .findByBusinessUserIdAndDeletedAtIsNullOrderByBranchBranchNumberAsc(businessUser.getId());
        Set<Integer> currentlyAssignedBranchIds = new HashSet<>();
        LocalDateTime removedAt = LocalDateTime.now();

        currentAssignments.forEach(assignment -> {
            Integer assignedBranchId = assignment.getBranch().getId();
            currentlyAssignedBranchIds.add(assignedBranchId);

            if (requestedBranchIds.contains(assignedBranchId)) {
                assignment.setActive(true);
                assignment.setDeletedAt(null);
                return;
            }

            assignment.setActive(false);
            assignment.setDeletedAt(removedAt);
        });

        requestedBranches.stream()
                .filter(branch -> !currentlyAssignedBranchIds.contains(branch.getId()))
                .forEach(branch -> {
                    BusinessUserBranch assignment = new BusinessUserBranch();
                    assignment.setBusinessUser(businessUser);
                    assignment.setBranch(branch);
                    assignment.setActive(true);
                    businessUserBranchRepository.save(assignment);
                });
    }
}
