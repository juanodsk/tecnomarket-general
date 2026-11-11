package com.tecmarket.backend.auth.service;

import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.parameter.entity.City;
import com.tecmarket.backend.parameter.entity.Country;
import com.tecmarket.backend.parameter.entity.Department;
import com.tecmarket.backend.parameter.service.LocationService;
import com.tecmarket.backend.permission.entity.PermissionScope;
import com.tecmarket.backend.permission.entity.Role;
import com.tecmarket.backend.permission.service.PermissionService;
import com.tecmarket.backend.user.dto.CreateInvitedCustomerRequest;
import com.tecmarket.backend.user.entity.AppUser;
import com.tecmarket.backend.user.entity.DocumentType;
import com.tecmarket.backend.user.entity.UserRole;
import com.tecmarket.backend.user.repository.UserRoleRepository;
import com.tecmarket.backend.user.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Locale;
import java.util.Optional;

@Service
public class CustomerAccountService {

    private static final String CUSTOMER_ROLE_CODE = "CUSTOMER";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final UserService userService;
    private final LocationService locationService;
    private final PermissionService permissionService;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityVersionService securityVersionService;

    public CustomerAccountService(
            UserService userService,
            LocationService locationService,
            PermissionService permissionService,
            UserRoleRepository userRoleRepository,
            PasswordEncoder passwordEncoder,
            SecurityVersionService securityVersionService) {
        this.userService = userService;
        this.locationService = locationService;
        this.permissionService = permissionService;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.securityVersionService = securityVersionService;
    }

    @Transactional
    public CustomerAccountResolution resolveOrCreate(CreateInvitedCustomerRequest request) {
        String documentTypeCode = request.documentTypeCode().trim().toUpperCase(Locale.ROOT);
        String documentNumber = request.documentNumber().trim();
        String email = request.email().trim().toLowerCase(Locale.ROOT);

        DocumentType documentType = userService.findDocumentTypeByCode(documentTypeCode)
                .orElseThrow(() -> new ResourceNotFoundException("document_code.not_found"));
        Country country = locationService.findActiveCountry(request.countryId())
                .orElseThrow(() -> new ResourceNotFoundException("country.not_found"));
        Department department = locationService.findActiveDepartment(request.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("department.not_found"));
        City city = locationService.findActiveCity(request.cityId())
                .orElseThrow(() -> new ResourceNotFoundException("city.not_found"));
        locationService.validateLocationHierarchy(country, department, city);

        Optional<AppUser> userByEmail = userService.findByEmailIgnoreCase(email);
        Optional<AppUser> userByDocument = userService.findByDocument(documentTypeCode, documentNumber);
        AppUser existing = reconcileIdentity(userByEmail, userByDocument);
        if (existing != null) {
            if (!Boolean.TRUE.equals(existing.getActive()) || existing.getDeletedAt() != null) {
                throw new ConflictException("quick_order.customer.inactive");
            }
            ensureCustomerRole(existing, true);
            return new CustomerAccountResolution(existing, false);
        }

        AppUser customer = new AppUser();
        customer.setDocumentType(documentType);
        customer.setDocumentNumber(documentNumber);
        customer.setName(request.name().trim());
        customer.setSurname(request.surname().trim());
        customer.setEmail(email);
        customer.setPasswordHash(passwordEncoder.encode(generateUnknownPassword()));
        customer.setAddress(request.address().trim());
        customer.setPhone(request.phone().trim());
        customer.setCountry(country);
        customer.setDepartment(department);
        customer.setCity(city);
        customer.setEmailVerified(false);
        customer.setEmailVerifiedAt(null);
        customer.setActive(true);

        AppUser saved = userService.save(customer);
        ensureCustomerRole(saved, false);
        return new CustomerAccountResolution(saved, true);
    }

    private AppUser reconcileIdentity(Optional<AppUser> userByEmail, Optional<AppUser> userByDocument) {
        if (userByEmail.isEmpty() && userByDocument.isEmpty()) {
            return null;
        }
        if (userByEmail.isPresent() && userByDocument.isPresent()
                && userByEmail.get().getId().equals(userByDocument.get().getId())) {
            return userByEmail.get();
        }
        throw new ConflictException("quick_order.customer.identity_conflict");
    }

    private void ensureCustomerRole(AppUser user, boolean invalidateExistingSessions) {
        Role customerRole = permissionService.findRoleByCodeAndActive(CUSTOMER_ROLE_CODE)
                .filter(role -> PermissionScope.GLOBAL.name().equalsIgnoreCase(role.getScope()))
                .orElseThrow(() -> new ResourceNotFoundException("role.not_found"));

        Optional<UserRole> existingRelation = userRoleRepository.findByUserIdAndRoleCode(
                user.getId(), CUSTOMER_ROLE_CODE);
        if (existingRelation.isPresent()) {
            UserRole relation = existingRelation.get();
            if (!Boolean.TRUE.equals(relation.getActive()) || relation.getDeletedAt() != null) {
                relation.setActive(true);
                relation.setDeletedAt(null);
                if (invalidateExistingSessions) {
                    securityVersionService.invalidateUser(user);
                }
            }
            return;
        }

        UserRole relation = new UserRole();
        relation.setUser(user);
        relation.setRole(customerRole);
        relation.setActive(true);
        userRoleRepository.save(relation);
        if (invalidateExistingSessions) {
            securityVersionService.invalidateUser(user);
        }
    }

    private String generateUnknownPassword() {
        byte[] bytes = new byte[48];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public record CustomerAccountResolution(AppUser user, boolean created) {
    }
}
