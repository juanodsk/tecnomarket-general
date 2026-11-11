package com.tecmarket.backend.seed;

import com.tecmarket.backend.parameter.repository.CityRepository;
import com.tecmarket.backend.parameter.repository.CountryRepository;
import com.tecmarket.backend.parameter.repository.DepartmentRepository;
import com.tecmarket.backend.permission.entity.Role;
import com.tecmarket.backend.permission.repository.AppModuleRepository;
import com.tecmarket.backend.permission.repository.PermissionRepository;
import com.tecmarket.backend.permission.repository.RolePermissionRepository;
import com.tecmarket.backend.permission.repository.RoleRepository;
import com.tecmarket.backend.user.entity.AppUser;
import com.tecmarket.backend.user.repository.AppUserRepository;
import com.tecmarket.backend.user.repository.DocumentTypeRepository;
import com.tecmarket.backend.user.repository.UserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeneralDataSeederTest {

    @Mock private DocumentTypeRepository documentTypeRepository;
    @Mock private AppUserRepository appUserRepository;
    @Mock private UserRoleRepository userRoleRepository;
    @Mock private AppModuleRepository appModuleRepository;
    @Mock private PermissionRepository permissionRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private RolePermissionRepository rolePermissionRepository;
    @Mock private CountryRepository countryRepository;
    @Mock private DepartmentRepository departmentRepository;
    @Mock private CityRepository cityRepository;
    @Mock private PasswordEncoder passwordEncoder;

    private GeneralDataSeeder seeder;

    @BeforeEach
    void setUp() {
        seeder = new GeneralDataSeeder(
                documentTypeRepository,
                appUserRepository,
                userRoleRepository,
                appModuleRepository,
                permissionRepository,
                roleRepository,
                rolePermissionRepository,
                countryRepository,
                departmentRepository,
                cityRepository,
                passwordEncoder);
    }

    @Test
    void disabledSeederDoesNotAccessPersistence() {
        ReflectionTestUtils.setField(seeder, "enabled", false);

        seeder.run(null);

        verifyNoInteractions(
                documentTypeRepository,
                appUserRepository,
                userRoleRepository,
                appModuleRepository,
                permissionRepository,
                roleRepository,
                rolePermissionRepository,
                countryRepository,
                departmentRepository,
                cityRepository,
                passwordEncoder);
    }

    @Test
    void existingPlatformAdminPasswordIsNeverOverwritten() {
        configurePlatformAdmin("a-strong-random-password");
        AppUser existingAdmin = new AppUser();
        existingAdmin.setId(12);
        existingAdmin.setEmail("admin@example.com");
        existingAdmin.setPasswordHash("existing-password-hash");

        Role platformAdminRole = new Role();
        platformAdminRole.setId(3);
        platformAdminRole.setCode("PLATFORM_ADMIN");

        when(appUserRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(existingAdmin));
        when(roleRepository.findByCode("PLATFORM_ADMIN")).thenReturn(Optional.of(platformAdminRole));
        when(userRoleRepository.findByUserIdAndRoleCode(12, "PLATFORM_ADMIN")).thenReturn(Optional.empty());

        ReflectionTestUtils.invokeMethod(seeder, "seedPlatformAdmin");

        verify(passwordEncoder, never()).encode(anyString());
        verify(appUserRepository, never()).save(any(AppUser.class));
        verify(userRoleRepository).save(any());
    }

    @Test
    void enabledPlatformAdminSeedRejectsMissingPassword() {
        configurePlatformAdmin("");

        assertThrows(
                IllegalStateException.class,
                () -> ReflectionTestUtils.invokeMethod(seeder, "seedPlatformAdmin"));

        verifyNoInteractions(appUserRepository, passwordEncoder);
    }

    private void configurePlatformAdmin(String password) {
        ReflectionTestUtils.setField(seeder, "platformAdminEmail", "admin@example.com");
        ReflectionTestUtils.setField(seeder, "platformAdminPassword", password);
        ReflectionTestUtils.setField(seeder, "platformAdminDocumentTypeCode", "CC");
        ReflectionTestUtils.setField(seeder, "platformAdminDocumentNumber", "1000000001");
        ReflectionTestUtils.setField(seeder, "platformAdminName", "Platform");
        ReflectionTestUtils.setField(seeder, "platformAdminSurname", "Admin");
        ReflectionTestUtils.setField(seeder, "platformAdminAddress", "Calle 1");
        ReflectionTestUtils.setField(seeder, "platformAdminPhone", "3000000000");
        ReflectionTestUtils.setField(seeder, "platformAdminCity", "Medellin");
    }
}
