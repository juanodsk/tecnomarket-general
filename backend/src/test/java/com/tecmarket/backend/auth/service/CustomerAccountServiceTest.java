package com.tecmarket.backend.auth.service;

import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.parameter.entity.City;
import com.tecmarket.backend.parameter.entity.Country;
import com.tecmarket.backend.parameter.entity.Department;
import com.tecmarket.backend.parameter.service.LocationService;
import com.tecmarket.backend.permission.entity.Role;
import com.tecmarket.backend.permission.service.PermissionService;
import com.tecmarket.backend.user.dto.CreateInvitedCustomerRequest;
import com.tecmarket.backend.user.entity.AppUser;
import com.tecmarket.backend.user.entity.DocumentType;
import com.tecmarket.backend.user.entity.UserRole;
import com.tecmarket.backend.user.repository.UserRoleRepository;
import com.tecmarket.backend.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerAccountServiceTest {

    @Mock private UserService userService;
    @Mock private LocationService locationService;
    @Mock private PermissionService permissionService;
    @Mock private UserRoleRepository userRoleRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private SecurityVersionService securityVersionService;

    private CustomerAccountService service;
    private DocumentType documentType;
    private Country country;
    private Department department;
    private City city;
    private Role customerRole;

    @BeforeEach
    void setUp() {
        service = new CustomerAccountService(
                userService,
                locationService,
                permissionService,
                userRoleRepository,
                passwordEncoder,
                securityVersionService);
        documentType = new DocumentType();
        documentType.setCode("CC");
        country = new Country();
        country.setId(1);
        department = new Department();
        department.setId(2);
        city = new City();
        city.setId(3);
        customerRole = new Role();
        customerRole.setId(4);
        customerRole.setCode("CUSTOMER");
        customerRole.setScope("GLOBAL");
        customerRole.setActive(true);
    }

    @Test
    void createsCustomerWithUnknownPasswordAndGlobalCustomerRole() {
        stubReferenceData();
        when(userService.findByEmailIgnoreCase("laura@example.com")).thenReturn(Optional.empty());
        when(userService.findByDocument("CC", "123456789")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encoded-unknown-password");
        when(userService.save(any())).thenAnswer(invocation -> {
            AppUser saved = invocation.getArgument(0);
            saved.setId(80);
            return saved;
        });
        when(permissionService.findRoleByCodeAndActive("CUSTOMER")).thenReturn(Optional.of(customerRole));
        when(userRoleRepository.findByUserIdAndRoleCode(80, "CUSTOMER")).thenReturn(Optional.empty());

        CustomerAccountService.CustomerAccountResolution result = service.resolveOrCreate(request());

        assertThat(result.created()).isTrue();
        assertThat(result.user().getEmail()).isEqualTo("laura@example.com");
        assertThat(result.user().getPasswordHash()).isEqualTo("encoded-unknown-password");
        assertThat(result.user().getEmailVerified()).isFalse();
        ArgumentCaptor<UserRole> roleAssignment = ArgumentCaptor.forClass(UserRole.class);
        verify(userRoleRepository).save(roleAssignment.capture());
        assertThat(roleAssignment.getValue().getRole()).isSameAs(customerRole);
        verify(securityVersionService, never()).invalidateUser(any(AppUser.class));
    }

    @Test
    void rejectsWhenEmailAndDocumentResolveToDifferentAccounts() {
        stubReferenceData();
        AppUser byEmail = activeCustomer(80);
        AppUser byDocument = activeCustomer(81);
        when(userService.findByEmailIgnoreCase("laura@example.com")).thenReturn(Optional.of(byEmail));
        when(userService.findByDocument("CC", "123456789")).thenReturn(Optional.of(byDocument));

        assertThatThrownBy(() -> service.resolveOrCreate(request()))
                .isInstanceOf(ConflictException.class);

        verify(userService, never()).save(any());
    }

    @Test
    void reusesExistingCustomerAndReactivatesCustomerRole() {
        stubReferenceData();
        AppUser existing = activeCustomer(80);
        UserRole relation = new UserRole();
        relation.setUser(existing);
        relation.setRole(customerRole);
        relation.setActive(false);
        relation.setDeletedAt(LocalDateTime.now());
        when(userService.findByEmailIgnoreCase("laura@example.com")).thenReturn(Optional.of(existing));
        when(userService.findByDocument("CC", "123456789")).thenReturn(Optional.of(existing));
        when(permissionService.findRoleByCodeAndActive("CUSTOMER")).thenReturn(Optional.of(customerRole));
        when(userRoleRepository.findByUserIdAndRoleCode(80, "CUSTOMER")).thenReturn(Optional.of(relation));

        CustomerAccountService.CustomerAccountResolution result = service.resolveOrCreate(request());

        assertThat(result.created()).isFalse();
        assertThat(result.user()).isSameAs(existing);
        assertThat(relation.getActive()).isTrue();
        assertThat(relation.getDeletedAt()).isNull();
        verify(securityVersionService).invalidateUser(existing);
        verify(userService, never()).save(any());
    }

    private void stubReferenceData() {
        when(userService.findDocumentTypeByCode("CC")).thenReturn(Optional.of(documentType));
        when(locationService.findActiveCountry(1)).thenReturn(Optional.of(country));
        when(locationService.findActiveDepartment(2)).thenReturn(Optional.of(department));
        when(locationService.findActiveCity(3)).thenReturn(Optional.of(city));
    }

    private CreateInvitedCustomerRequest request() {
        return new CreateInvitedCustomerRequest(
                " cc ",
                " 123456789 ",
                " Laura ",
                " Gomez ",
                " LAURA@EXAMPLE.COM ",
                " Calle 10 #20-30 ",
                " +573001112233 ",
                1,
                2,
                3);
    }

    private AppUser activeCustomer(Integer id) {
        AppUser user = new AppUser();
        user.setId(id);
        user.setDocumentType(documentType);
        user.setDocumentNumber("123456789");
        user.setEmail("laura@example.com");
        user.setActive(true);
        return user;
    }
}
