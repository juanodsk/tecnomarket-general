package com.tecmarket.backend.business.service;

import com.tecmarket.backend.business.dto.CreateBusinessCatalogRequest;
import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.business.entity.BusinessBranch;
import com.tecmarket.backend.business.entity.BusinessRequest;
import com.tecmarket.backend.business.entity.BusinessRequestStatus;
import com.tecmarket.backend.business.entity.BusinessUser;
import com.tecmarket.backend.business.mapper.BusinessMapper;
import com.tecmarket.backend.business.mapper.BusinessRequestMapper;
import com.tecmarket.backend.business.repository.BusinessRepository;
import com.tecmarket.backend.business.repository.BusinessRequestRepository;
import com.tecmarket.backend.business.repository.BusinessUserBranchRepository;
import com.tecmarket.backend.business.repository.BusinessUserRepository;
import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.auth.service.SecurityVersionService;
import com.tecmarket.backend.parameter.entity.City;
import com.tecmarket.backend.parameter.repository.CityRepository;
import com.tecmarket.backend.permission.entity.Role;
import com.tecmarket.backend.permission.repository.RoleRepository;
import com.tecmarket.backend.storage.config.MinioStorageProperties;
import com.tecmarket.backend.storage.service.MinioStorageService;
import com.tecmarket.backend.storage.service.StorageDeletionQueueService;
import com.tecmarket.backend.common.exception.FileValidationException;
import com.tecmarket.backend.user.entity.AppUser;
import com.tecmarket.backend.user.entity.DocumentType;
import com.tecmarket.backend.user.repository.AppUserRepository;
import com.tecmarket.backend.user.repository.DocumentTypeRepository;
import com.tecmarket.backend.user.repository.UserRoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BusinessCatalogInitializationWiringTest {

    @Mock private BusinessRepository businessRepository;
    @Mock private BusinessUserRepository businessUserRepository;
    @Mock private DocumentTypeRepository documentTypeRepository;
    @Mock private CityRepository cityRepository;
    @Mock private UserRoleRepository userRoleRepository;
    @Mock private BusinessAccessGuard businessAccessGuard;
    @Mock private BusinessMapper businessMapper;
    @Mock private BusinessBranchService businessBranchService;
    @Mock private BusinessCatalogInitializerService businessCatalogInitializerService;
    @Mock private MinioStorageService minioStorageService;
    @Mock private MinioStorageProperties minioStorageProperties;
    @Mock private StorageDeletionQueueService storageDeletionQueueService;

    @Mock private BusinessRequestRepository businessRequestRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private AppUserRepository appUserRepository;
    @Mock private BusinessRequestMapper businessRequestMapper;
    @Mock private BusinessUserBranchRepository businessUserBranchRepository;
    @Mock private SecurityVersionService securityVersionService;

    @Test
    void administrativeBusinessCreationInitializesCatalogs() {
        DocumentType documentType = new DocumentType();
        documentType.setCode("NIT");
        City city = activeCity(1);

        when(documentTypeRepository.findByCodeAndActiveTrue("NIT")).thenReturn(Optional.of(documentType));
        when(cityRepository.findById(1)).thenReturn(Optional.of(city));
        when(businessRepository.save(any(Business.class))).thenAnswer(invocation -> {
            Business business = invocation.getArgument(0);
            business.setId(9);
            return business;
        });

        BusinessService service = businessService();

        service.create(new CreateBusinessCatalogRequest(
                "NIT",
                "901234567-1",
                "Servicio Tecnico",
                "Servicio Tecnico S.A.S.",
                "contacto@tecmarket.test",
                "3001234567",
                "Calle 1",
                "Servicio tecnico de prueba",
                1,
                null,
                null), "99");

        verify(businessCatalogInitializerService).initialize(any(Business.class));
    }

    @Test
    void administrativeBusinessCreationRejectsExternalLogoUrl() {
        DocumentType documentType = new DocumentType();
        documentType.setCode("NIT");
        when(documentTypeRepository.findByCodeAndActiveTrue("NIT")).thenReturn(Optional.of(documentType));
        when(cityRepository.findById(1)).thenReturn(Optional.of(activeCity(1)));

        assertThatThrownBy(() -> businessService().create(new CreateBusinessCatalogRequest(
                "NIT",
                "901234567-2",
                "Servicio Tecnico",
                "Servicio Tecnico S.A.S.",
                "contacto@tecmarket.test",
                "3001234567",
                "Calle 1",
                "Servicio tecnico de prueba",
                1,
                "https://attacker.example/tracker.png",
                null), "99"))
                .isInstanceOf(FileValidationException.class)
                .hasMessage("storage.public_asset.upload_required");
    }

    @Test
    void approvedBusinessRequestInitializesCatalogs() {
        AppUser reviewer = activeUser(99, "admin@tecmarket.test");
        AppUser owner = activeUser(10, "owner@tecmarket.test");
        DocumentType documentType = new DocumentType();
        documentType.setCode("NIT");
        City city = activeCity(1);

        BusinessRequest request = new BusinessRequest();
        request.setId(4);
        request.setRequestedByUser(owner);
        request.setDocumentType(documentType);
        request.setDocumentNumber("901234567-1");
        request.setBusinessName("Servicio Tecnico");
        request.setLegalName("Servicio Tecnico S.A.S.");
        request.setEmail("contacto@tecmarket.test");
        request.setPhone("3001234567");
        request.setAddress("Calle 1");
        request.setCity(city);
        request.setStatus(BusinessRequestStatus.PENDING);
        request.setActive(true);

        Role ownerRole = new Role();
        ownerRole.setId(2);
        ownerRole.setCode("BUSINESS_OWNER");
        ownerRole.setActive(true);

        BusinessBranch branch = new BusinessBranch();
        branch.setId(3);

        when(appUserRepository.findById(99)).thenReturn(Optional.of(reviewer));
        when(businessRequestRepository.findByIdAndActiveTrue(4)).thenReturn(Optional.of(request));
        when(roleRepository.findByCodeAndActiveTrue("BUSINESS_OWNER")).thenReturn(Optional.of(ownerRole));
        when(businessRepository.save(any(Business.class))).thenAnswer(invocation -> {
            Business business = invocation.getArgument(0);
            business.setId(9);
            return business;
        });
        when(businessBranchService.createPrimaryBranchFromBusiness(any(Business.class))).thenReturn(branch);
        when(businessUserRepository.save(any(BusinessUser.class))).thenAnswer(invocation -> {
            BusinessUser businessUser = invocation.getArgument(0);
            businessUser.setId(5);
            return businessUser;
        });
        BusinessRequestService service = new BusinessRequestService(
                businessRequestRepository,
                businessRepository,
                businessUserRepository,
                roleRepository,
                appUserRepository,
                documentTypeRepository,
                cityRepository,
                businessRequestMapper,
                businessBranchService,
                businessUserBranchRepository,
                businessCatalogInitializerService,
                businessAccessGuard,
                securityVersionService);

        service.approve(4, "99");

        verify(businessCatalogInitializerService).initialize(any(Business.class));
    }

    private City activeCity(int id) {
        City city = new City();
        city.setId(id);
        city.setName("Medellin");
        city.setActive(true);
        return city;
    }

    private BusinessService businessService() {
        return new BusinessService(
                businessRepository,
                businessUserRepository,
                documentTypeRepository,
                cityRepository,
                userRoleRepository,
                businessAccessGuard,
                businessMapper,
                businessBranchService,
                businessCatalogInitializerService,
                minioStorageService,
                minioStorageProperties,
                storageDeletionQueueService);
    }

    private AppUser activeUser(int id, String email) {
        AppUser user = new AppUser();
        user.setId(id);
        user.setName("Usuario");
        user.setSurname("Prueba");
        user.setEmail(email);
        user.setActive(true);
        return user;
    }
}
