package com.tecmarket.backend.business.security;

import com.tecmarket.backend.business.mapper.BusinessOfferingMapper;
import com.tecmarket.backend.business.repository.BusinessOfferingRepository;
import com.tecmarket.backend.business.repository.BusinessRepository;
import com.tecmarket.backend.business.repository.BusinessUserRepository;
import com.tecmarket.backend.business.service.BusinessCatalogLookupService;
import com.tecmarket.backend.business.service.BusinessOfferingService;
import com.tecmarket.backend.repairorder.mapper.BusinessQualityTestMapper;
import com.tecmarket.backend.repairorder.mapper.CustomerDeviceMapper;
import com.tecmarket.backend.repairorder.repository.BusinessQualityTestRepository;
import com.tecmarket.backend.repairorder.repository.CustomerDeviceRepository;
import com.tecmarket.backend.repairorder.service.BusinessQualityTestService;
import com.tecmarket.backend.repairorder.service.CustomerDeviceService;
import com.tecmarket.backend.storage.config.MinioStorageProperties;
import com.tecmarket.backend.storage.service.MinioStorageService;
import com.tecmarket.backend.user.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class TenantScopedServicesAuthorizationTest {

    private static final Integer BUSINESS_ID = 9;
    private static final String SUBJECT = "7";

    @Mock private BusinessAccessGuard businessAccessGuard;
    @Mock private BusinessRepository businessRepository;
    @Mock private BusinessCatalogLookupService businessCatalogLookupService;

    @Mock private BusinessOfferingRepository businessOfferingRepository;
    @Mock private BusinessOfferingMapper businessOfferingMapper;
    @Mock private MinioStorageService minioStorageService;
    @Mock private MinioStorageProperties minioStorageProperties;

    @Mock private BusinessQualityTestRepository businessQualityTestRepository;
    @Mock private BusinessQualityTestMapper businessQualityTestMapper;

    @Mock private CustomerDeviceRepository customerDeviceRepository;
    @Mock private AppUserRepository appUserRepository;
    @Mock private BusinessUserRepository businessUserRepository;
    @Mock private CustomerDeviceMapper customerDeviceMapper;

    @InjectMocks private BusinessOfferingService businessOfferingService;
    @InjectMocks private BusinessQualityTestService businessQualityTestService;
    @InjectMocks private CustomerDeviceService customerDeviceService;

    @Test
    void businessOfferingOperationsRejectBeforeReadingOrMutatingAnotherBusiness() {
        deny("BUSINESS_SERVICE_VIEW");
        deny("BUSINESS_SERVICE_CREATE");
        deny("BUSINESS_SERVICE_UPDATE");

        assertAll(
                () -> assertThrows(AccessDeniedException.class,
                        () -> businessOfferingService.findAllByBusiness(BUSINESS_ID, 0, 20, SUBJECT)),
                () -> assertThrows(AccessDeniedException.class,
                        () -> businessOfferingService.findById(BUSINESS_ID, 1, SUBJECT)),
                () -> assertThrows(AccessDeniedException.class,
                        () -> businessOfferingService.create(BUSINESS_ID, null, SUBJECT)),
                () -> assertThrows(AccessDeniedException.class,
                        () -> businessOfferingService.update(BUSINESS_ID, 1, null, SUBJECT)),
                () -> assertThrows(AccessDeniedException.class,
                        () -> businessOfferingService.delete(BUSINESS_ID, 1, SUBJECT)),
                () -> assertThrows(AccessDeniedException.class,
                        () -> businessOfferingService.uploadImage(BUSINESS_ID, 1, null, SUBJECT)));

        verify(businessAccessGuard, times(2))
                .assertHasBusinessPermission(BUSINESS_ID, SUBJECT, "BUSINESS_SERVICE_VIEW");
        verify(businessAccessGuard).assertHasBusinessPermission(
                BUSINESS_ID, SUBJECT, "BUSINESS_SERVICE_CREATE");
        verify(businessAccessGuard, times(3))
                .assertHasBusinessPermission(BUSINESS_ID, SUBJECT, "BUSINESS_SERVICE_UPDATE");
        verifyNoInteractions(businessOfferingRepository, minioStorageService);
    }

    @Test
    void businessQualityTestOperationsRejectBeforeReadingOrMutatingAnotherBusiness() {
        deny("BUSINESS_QUALITY_TEST_VIEW");
        deny("BUSINESS_QUALITY_TEST_CREATE");
        deny("BUSINESS_QUALITY_TEST_UPDATE");

        assertAll(
                () -> assertThrows(AccessDeniedException.class,
                        () -> businessQualityTestService.findAllByBusiness(BUSINESS_ID, 0, 20, SUBJECT)),
                () -> assertThrows(AccessDeniedException.class,
                        () -> businessQualityTestService.findById(BUSINESS_ID, 1, SUBJECT)),
                () -> assertThrows(AccessDeniedException.class,
                        () -> businessQualityTestService.create(BUSINESS_ID, null, SUBJECT)),
                () -> assertThrows(AccessDeniedException.class,
                        () -> businessQualityTestService.update(BUSINESS_ID, 1, null, SUBJECT)),
                () -> assertThrows(AccessDeniedException.class,
                        () -> businessQualityTestService.delete(BUSINESS_ID, 1, SUBJECT)));

        verify(businessAccessGuard, times(2))
                .assertHasBusinessPermission(BUSINESS_ID, SUBJECT, "BUSINESS_QUALITY_TEST_VIEW");
        verify(businessAccessGuard).assertHasBusinessPermission(
                BUSINESS_ID, SUBJECT, "BUSINESS_QUALITY_TEST_CREATE");
        verify(businessAccessGuard, times(2))
                .assertHasBusinessPermission(BUSINESS_ID, SUBJECT, "BUSINESS_QUALITY_TEST_UPDATE");
        verifyNoInteractions(businessQualityTestRepository);
    }

    @Test
    void customerDeviceOperationsRejectBeforeExposingOrMutatingAnotherBusinessData() {
        deny("CUSTOMER_DEVICE_VIEW");
        deny("CUSTOMER_DEVICE_CREATE");
        deny("CUSTOMER_DEVICE_UPDATE");

        assertAll(
                () -> assertThrows(AccessDeniedException.class,
                        () -> customerDeviceService.findAllByBusiness(BUSINESS_ID, 0, 20, SUBJECT)),
                () -> assertThrows(AccessDeniedException.class,
                        () -> customerDeviceService.findById(BUSINESS_ID, 1, SUBJECT)),
                () -> assertThrows(AccessDeniedException.class,
                        () -> customerDeviceService.create(BUSINESS_ID, null, SUBJECT)),
                () -> assertThrows(AccessDeniedException.class,
                        () -> customerDeviceService.update(BUSINESS_ID, 1, null, SUBJECT)),
                () -> assertThrows(AccessDeniedException.class,
                        () -> customerDeviceService.delete(BUSINESS_ID, 1, SUBJECT)));

        verify(businessAccessGuard, times(2))
                .assertHasBusinessPermission(BUSINESS_ID, SUBJECT, "CUSTOMER_DEVICE_VIEW");
        verify(businessAccessGuard).assertHasBusinessPermission(
                BUSINESS_ID, SUBJECT, "CUSTOMER_DEVICE_CREATE");
        verify(businessAccessGuard, times(2))
                .assertHasBusinessPermission(BUSINESS_ID, SUBJECT, "CUSTOMER_DEVICE_UPDATE");
        verifyNoInteractions(customerDeviceRepository);
    }

    private void deny(String permission) {
        doThrow(new AccessDeniedException("auth.forbidden"))
                .when(businessAccessGuard)
                .assertHasBusinessPermission(BUSINESS_ID, SUBJECT, permission);
    }
}
