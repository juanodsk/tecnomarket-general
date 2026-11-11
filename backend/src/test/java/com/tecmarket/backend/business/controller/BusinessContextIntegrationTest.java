package com.tecmarket.backend.business.controller;

import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.business.entity.BusinessBranch;
import com.tecmarket.backend.business.entity.BusinessUser;
import com.tecmarket.backend.business.entity.BusinessUserBranch;
import com.tecmarket.backend.business.repository.BusinessBranchRepository;
import com.tecmarket.backend.business.repository.BusinessRepository;
import com.tecmarket.backend.business.repository.BusinessUserBranchRepository;
import com.tecmarket.backend.business.repository.BusinessUserRepository;
import com.tecmarket.backend.parameter.entity.City;
import com.tecmarket.backend.parameter.repository.CityRepository;
import com.tecmarket.backend.permission.entity.Role;
import com.tecmarket.backend.permission.repository.RoleRepository;
import com.tecmarket.backend.support.IntegrationTestBase;
import com.tecmarket.backend.user.entity.AppUser;
import com.tecmarket.backend.user.entity.DocumentType;
import com.tecmarket.backend.user.entity.UserRole;
import com.tecmarket.backend.user.repository.AppUserRepository;
import com.tecmarket.backend.user.repository.DocumentTypeRepository;
import com.tecmarket.backend.user.repository.UserRoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class BusinessContextIntegrationTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private BusinessBranchRepository businessBranchRepository;

    @Autowired
    private BusinessUserRepository businessUserRepository;

    @Autowired
    private BusinessUserBranchRepository businessUserBranchRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private CityRepository cityRepository;

    @Test
    void returnsPermissionsAndBranchesOnlyForTheSelectedBusiness() throws Exception {
        AppUser user = createActiveUser();
        Business businessA = createBusiness("Contexto A");
        Business businessB = createBusiness("Contexto B");
        BusinessBranch branchA1 = createBranch(businessA, 1, "A principal", true);
        createBranch(businessA, 2, "A restringida", false);
        BusinessBranch branchB1 = createBranch(businessB, 1, "B principal", true);

        BusinessUser technicianA = createMembership(businessA, user, "TECHNICIAN");
        assignBranch(technicianA, branchA1);
        BusinessUser sellerB = createMembership(businessB, user, "SELLER");
        assignBranch(sellerB, branchB1);

        mockMvc.perform(get("/api/v1/businesses/{businessId}/me/context", businessA.getId())
                        .with(jwt().jwt(builder -> builder.subject(user.getId().toString()))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.userId").value(user.getId()))
                .andExpect(jsonPath("$.data.businessId").value(businessA.getId()))
                .andExpect(jsonPath("$.data.businessUserId").value(technicianA.getId()))
                .andExpect(jsonPath("$.data.roleCode").value("TECHNICIAN"))
                .andExpect(jsonPath("$.data.permissions[?(@ == 'REPAIR_ORDER_UPDATE')]").exists())
                .andExpect(jsonPath("$.data.permissions[?(@ == 'SALE_CREATE')]").doesNotExist())
                .andExpect(jsonPath("$.data.branches.length()").value(1))
                .andExpect(jsonPath("$.data.branches[0].id").value(branchA1.getId()));

        mockMvc.perform(get("/api/v1/businesses/{businessId}/me/context", businessB.getId())
                        .with(jwt().jwt(builder -> builder.subject(user.getId().toString()))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.businessId").value(businessB.getId()))
                .andExpect(jsonPath("$.data.businessUserId").value(sellerB.getId()))
                .andExpect(jsonPath("$.data.roleCode").value("SELLER"))
                .andExpect(jsonPath("$.data.permissions[?(@ == 'SALE_CREATE')]").exists())
                .andExpect(jsonPath("$.data.permissions[?(@ == 'REPAIR_ORDER_UPDATE')]").doesNotExist())
                .andExpect(jsonPath("$.data.branches.length()").value(1))
                .andExpect(jsonPath("$.data.branches[0].id").value(branchB1.getId()));
    }

    @Test
    void rejectsAUserWithoutMembershipInTheSelectedBusiness() throws Exception {
        AppUser outsider = createActiveUser();
        Business business = createBusiness("Negocio ajeno");

        mockMvc.perform(get("/api/v1/businesses/{businessId}/me/context", business.getId())
                        .with(jwt().jwt(builder -> builder.subject(outsider.getId().toString()))))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void requiresAuthentication() throws Exception {
        mockMvc.perform(get("/api/v1/businesses/{businessId}/me/context", 1))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void platformAdminReceivesAllActiveBranchesWithoutFakeBusinessMembership() throws Exception {
        AppUser platformAdmin = createActiveUser();
        assignGlobalRole(platformAdmin, "PLATFORM_ADMIN");
        Business business = createBusiness("Negocio administrado");
        createBranch(business, 1, "Principal", true);
        createBranch(business, 2, "Secundaria", false);

        mockMvc.perform(get("/api/v1/businesses/{businessId}/me/context", business.getId())
                        .with(jwt().jwt(builder -> builder.subject(platformAdmin.getId().toString()))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.platformAdmin").value(true))
                .andExpect(jsonPath("$.data.businessUserId").doesNotExist())
                .andExpect(jsonPath("$.data.roleId").doesNotExist())
                .andExpect(jsonPath("$.data.roleCode").doesNotExist())
                .andExpect(jsonPath("$.data.permissions[?(@ == 'BUSINESS_USER_CREATE')]").exists())
                .andExpect(jsonPath("$.data.branches.length()").value(2));
    }

    private AppUser createActiveUser() {
        DocumentType documentType = documentTypeRepository.findByCode("CC").orElseThrow();
        String suffix = UUID.randomUUID().toString();

        AppUser user = new AppUser();
        user.setDocumentType(documentType);
        user.setDocumentNumber(suffix);
        user.setName("QA");
        user.setSurname("Context");
        user.setEmail("context+" + suffix + "@tecmarket.test");
        user.setPasswordHash("$2a$10$7EqJtq98hPqEX7fNZaFWoOHiXWyf1n4pRr6NfF5iU5zJfK6K7zN6K");
        user.setAddress("Calle QA");
        user.setPhone("+573001110000");
        user.setActive(true);
        user.setEmailVerified(true);
        return appUserRepository.save(user);
    }

    private Business createBusiness(String name) {
        DocumentType documentType = documentTypeRepository.findByCode("NIT").orElseThrow();
        City city = cityRepository.findAllByActiveTrueOrderByNameAsc().getFirst();
        String suffix = UUID.randomUUID().toString();

        Business business = new Business();
        business.setDocumentType(documentType);
        business.setDocumentNumber(suffix);
        business.setName(name + " " + suffix.substring(0, 8));
        business.setLegalName(name + " S.A.S.");
        business.setEmail("business+" + suffix + "@tecmarket.test");
        business.setPhone("+573002220000");
        business.setAddress("Carrera QA 1");
        business.setDescription("Negocio creado para validar contexto multitenant");
        business.setCity(city);
        business.setActive(true);
        return businessRepository.save(business);
    }

    private BusinessBranch createBranch(Business business, int branchNumber, String name, boolean primary) {
        BusinessBranch branch = new BusinessBranch();
        branch.setBusiness(business);
        branch.setBranchNumber(branchNumber);
        branch.setName(name);
        branch.setCity(business.getCity());
        branch.setAddress("Calle sede " + branchNumber);
        branch.setPrimaryBranch(primary);
        branch.setActive(true);
        return businessBranchRepository.save(branch);
    }

    private BusinessUser createMembership(Business business, AppUser user, String roleCode) {
        Role role = roleRepository.findByCode(roleCode).orElseThrow();

        BusinessUser membership = new BusinessUser();
        membership.setBusiness(business);
        membership.setUser(user);
        membership.setRole(role);
        membership.setCommissionPercentage(BigDecimal.ZERO);
        membership.setActive(true);
        return businessUserRepository.save(membership);
    }

    private void assignBranch(BusinessUser membership, BusinessBranch branch) {
        BusinessUserBranch assignment = new BusinessUserBranch();
        assignment.setBusinessUser(membership);
        assignment.setBranch(branch);
        assignment.setActive(true);
        businessUserBranchRepository.save(assignment);
    }

    private void assignGlobalRole(AppUser user, String roleCode) {
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(roleRepository.findByCode(roleCode).orElseThrow());
        userRole.setActive(true);
        userRoleRepository.save(userRole);
    }
}
