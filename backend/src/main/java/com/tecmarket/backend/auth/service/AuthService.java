/*
 * TecMarket Backend
 * Module: Auth
 */

package com.tecmarket.backend.auth.service;

import com.tecmarket.backend.auth.dto.AuthUserResponse;
import com.tecmarket.backend.auth.dto.AuthUserPermissionsResponse;
import com.tecmarket.backend.auth.dto.LoginRequest;
import com.tecmarket.backend.auth.dto.LoginResponse;
import com.tecmarket.backend.auth.dto.AuthSessionResponse;
import com.tecmarket.backend.auth.dto.RegisterRequest;
import com.tecmarket.backend.auth.mapper.AuthMapper;
import com.tecmarket.backend.auth.security.JwtService;
import com.tecmarket.backend.business.repository.BusinessUserRepository;
import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.parameter.entity.City;
import com.tecmarket.backend.parameter.entity.Country;
import com.tecmarket.backend.parameter.entity.Department;
import com.tecmarket.backend.parameter.service.LocationService;
import com.tecmarket.backend.permission.entity.Role;
import com.tecmarket.backend.permission.service.PermissionService;
import com.tecmarket.backend.user.entity.AppUser;
import com.tecmarket.backend.user.entity.DocumentType;
import com.tecmarket.backend.user.entity.UserRole;
import com.tecmarket.backend.user.repository.UserRoleRepository;
import com.tecmarket.backend.user.service.UserService;

import java.util.List;
import java.util.UUID;
import java.util.TreeSet;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private static final String CUSTOMER_ROLE_CODE = "CUSTOMER";

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthMapper authMapper;
    private final UserRoleRepository userRoleRepository;
    private final PermissionService permissionService;
    private final BusinessUserRepository businessUserRepository;
    private final EmailVerificationService emailVerificationService;
    private final LocationService locationService;
    private final RefreshTokenService refreshTokenService;
    private final SecurityVersionService securityVersionService;

    public AuthService(
            UserService userService,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthMapper authMapper,
            UserRoleRepository userRoleRepository,
            PermissionService permissionService,
            BusinessUserRepository businessUserRepository,
            EmailVerificationService emailVerificationService,
            LocationService locationService,
            RefreshTokenService refreshTokenService,
            SecurityVersionService securityVersionService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authMapper = authMapper;
        this.userRoleRepository = userRoleRepository;
        this.permissionService = permissionService;
        this.businessUserRepository = businessUserRepository;
        this.emailVerificationService = emailVerificationService;
        this.locationService = locationService;
        this.refreshTokenService = refreshTokenService;
        this.securityVersionService = securityVersionService;
    }

    @Transactional
    public AuthUserResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();
        String documentNumber = request.documentNumber().trim();

        if (userService.existsByEmail(email)) {
            throw new ConflictException("auth.email.exists");
        }

        if (userService.existsByDocumentTypeCodeAndDocumentNumber(request.documentTypeCode(), documentNumber)) {
            throw new ConflictException("auth.document.exists");
        }

        DocumentType documentType = userService.findDocumentTypeByCode(request.documentTypeCode())
                .orElseThrow(() -> new ResourceNotFoundException("document_code.not_found"));
        Country country = locationService.findActiveCountry(request.countryId())
                .orElseThrow(() -> new ResourceNotFoundException("country.not_found"));
        Department department = locationService.findActiveDepartment(request.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("department.not_found"));
        City city = locationService.findActiveCity(request.cityId())
                .orElseThrow(() -> new ResourceNotFoundException("city.not_found"));

        locationService.validateLocationHierarchy(country, department, city);

        AppUser user = new AppUser();
        user.setDocumentType(documentType);
        user.setDocumentNumber(documentNumber);
        user.setName(request.name().trim());
        user.setSurname(request.surname().trim());
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setAddress(request.address().trim());
        user.setPhone(request.phone().trim());
        user.setCountry(country);
        user.setDepartment(department);
        user.setCity(city);
        user.setEmailVerified(false);
        user.setEmailVerifiedAt(null);

        AppUser savedUser = userService.save(user);
        assignCustomerRole(savedUser);
        emailVerificationService.createAndSendVerification(savedUser);
        return authMapper.toResponse(savedUser);
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        return login(request, null, null);
    }

    @Transactional
    public LoginResponse login(LoginRequest request, String clientDescription, String ipAddress) {
        String email = request.email().trim().toLowerCase();

        AppUser user = userService.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("auth.login.invalid"));

        if (!Boolean.TRUE.equals(user.getActive())
                || !passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("auth.login.invalid");
        }

        if (Boolean.FALSE.equals(user.getEmailVerified())) {
            throw new BadCredentialsException("auth.email.not_verified");
        }

        List<String> permissions = findEffectivePermissions(user.getId());

        RefreshTokenService.IssuedSession session = refreshTokenService.issueSession(
                user,
                clientDescription,
                ipAddress);
        String accessToken = jwtService.generateAccessToken(user, permissions, session.familyId());

        return new LoginResponse(accessToken, session.refreshToken(), authMapper.toResponse(user));
    }

    @Transactional(readOnly = true)
    public AuthUserResponse getAuthenticatedUser(String subject) {
        AppUser user = findActiveUserBySubject(subject);
        return authMapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    public AuthUserPermissionsResponse getAuthenticatedUserPermissions(String subject) {
        AppUser user = findActiveUserBySubject(subject);

        List<String> roles = findEffectiveRoles(user.getId());
        List<String> permissions = findEffectivePermissions(user.getId());

        return new AuthUserPermissionsResponse(user.getId(), roles, permissions);
    }

    @Transactional
    public void verifyEmail(String token) {
        emailVerificationService.verifyEmailToken(token);
    }

    @Transactional(noRollbackFor = BadCredentialsException.class)
    public LoginResponse refreshAccessToken(String refreshTokenStr) {
        RefreshTokenService.RefreshSession session = refreshTokenService.rotate(refreshTokenStr);
        AppUser user = session.user();

        List<String> permissions = findEffectivePermissions(user.getId());
        String accessToken = jwtService.generateAccessToken(user, permissions, session.familyId());

        return new LoginResponse(accessToken, session.refreshToken(), authMapper.toResponse(user));
    }

    @Transactional
    public void logout(String refreshTokenStr, String subject) {
        AppUser user = findActiveUserBySubject(subject);
        refreshTokenService.revoke(refreshTokenStr, user.getId());
    }

    @Transactional(readOnly = true)
    public List<AuthSessionResponse> findSessions(String subject) {
        AppUser user = findActiveUserBySubject(subject);
        return refreshTokenService.findActiveSessions(user.getId());
    }

    @Transactional
    public void revokeSession(UUID sessionId, String subject) {
        AppUser user = findActiveUserBySubject(subject);
        refreshTokenService.revokeSession(sessionId, user.getId());
    }

    @Transactional
    public void changePassword(String subject, String currentPassword, String newPassword) {
        AppUser user = findActiveUserBySubject(subject);
        if (!passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
            throw new BadCredentialsException("auth.password.current.invalid");
        }
        if (passwordEncoder.matches(newPassword, user.getPasswordHash())) {
            throw new ConflictException("auth.password.same");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        securityVersionService.invalidateUser(user);
    }

    @Transactional
    public void revokeAllSessions(String subject) {
        AppUser user = findActiveUserBySubject(subject);
        securityVersionService.invalidateUser(user);
    }

    private AppUser findActiveUserBySubject(String subject) {
        Integer userId;
        try {
            userId = Integer.valueOf(subject);
        } catch (NumberFormatException exception) {
            throw new BadCredentialsException("auth.token.invalid");
        }

        return userService.findActiveUserById(userId)
                .orElseThrow(() -> new BadCredentialsException("auth.token.invalid"));
    }

    private List<String> findEffectiveRoles(Integer userId) {
        TreeSet<String> roleCodes = new TreeSet<>();
        roleCodes.addAll(userRoleRepository.findActiveRoleCodesByUserId(userId));
        roleCodes.addAll(businessUserRepository.findActiveRoleCodesByUserId(userId));
        return roleCodes.stream().toList();
    }

    private List<String> findEffectivePermissions(Integer userId) {
        TreeSet<String> permissionCodes = new TreeSet<>();
        permissionCodes.addAll(userRoleRepository.findActivePermissionCodesByUserId(userId));
        permissionCodes.addAll(businessUserRepository.findActivePermissionCodesByUserId(userId));
        return permissionCodes.stream().toList();
    }

    private void assignCustomerRole(AppUser user) {
        Role customerRole = permissionService.findRoleByCodeAndActive(CUSTOMER_ROLE_CODE)
                .orElseThrow(() -> new ResourceNotFoundException("role.not_found"));
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(customerRole);
        userRole.setActive(true);
        userRoleRepository.save(userRole);
    }
}
