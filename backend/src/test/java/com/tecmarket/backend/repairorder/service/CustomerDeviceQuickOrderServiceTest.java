package com.tecmarket.backend.repairorder.service;

import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.business.entity.BusinessCatalogItem;
import com.tecmarket.backend.business.entity.BusinessCatalogType;
import com.tecmarket.backend.business.entity.BusinessUser;
import com.tecmarket.backend.business.repository.BusinessRepository;
import com.tecmarket.backend.business.repository.BusinessUserRepository;
import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.business.service.BusinessCatalogLookupService;
import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.repairorder.dto.CustomerDeviceResponse;
import com.tecmarket.backend.repairorder.dto.QuickRepairOrderDeviceRequest;
import com.tecmarket.backend.repairorder.entity.CustomerDevice;
import com.tecmarket.backend.repairorder.mapper.CustomerDeviceMapper;
import com.tecmarket.backend.repairorder.repository.CustomerDeviceRepository;
import com.tecmarket.backend.user.entity.AppUser;
import com.tecmarket.backend.user.repository.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerDeviceQuickOrderServiceTest {

    private static final Integer BUSINESS_ID = 10;
    private static final Integer CUSTOMER_ID = 80;
    private static final String SUBJECT = "30";

    @Mock private CustomerDeviceRepository customerDeviceRepository;
    @Mock private BusinessRepository businessRepository;
    @Mock private AppUserRepository appUserRepository;
    @Mock private BusinessCatalogLookupService businessCatalogLookupService;
    @Mock private BusinessUserRepository businessUserRepository;
    @Mock private CustomerDeviceMapper customerDeviceMapper;
    @Mock private BusinessAccessGuard businessAccessGuard;

    private CustomerDeviceService service;
    private Business business;
    private AppUser customer;
    private BusinessCatalogItem deviceType;
    private BusinessCatalogItem brand;
    private BusinessUser actor;

    @BeforeEach
    void setUp() {
        service = new CustomerDeviceService(
                customerDeviceRepository,
                businessRepository,
                appUserRepository,
                businessCatalogLookupService,
                businessUserRepository,
                customerDeviceMapper,
                businessAccessGuard);
        business = new Business();
        business.setId(BUSINESS_ID);
        customer = new AppUser();
        customer.setId(CUSTOMER_ID);
        customer.setActive(true);
        deviceType = new BusinessCatalogItem();
        deviceType.setId(11);
        brand = new BusinessCatalogItem();
        brand.setId(12);
        actor = new BusinessUser();
        actor.setId(40);
    }

    @Test
    void reusesDeviceWhenImeiBelongsToSameCustomerAndDetailsMatch() {
        stubReferences();
        CustomerDevice existing = existingDevice(customer);
        CustomerDeviceResponse response = org.mockito.Mockito.mock(CustomerDeviceResponse.class);
        when(customerDeviceRepository.findByBusinessIdAndImeiAndActiveTrueAndDeletedAtIsNullOrderByIdAsc(
                BUSINESS_ID, "351756051523999")).thenReturn(List.of(existing));
        when(customerDeviceMapper.toResponse(existing)).thenReturn(response);

        CustomerDeviceResponse result = service.resolveOrCreateForQuickOrder(
                BUSINESS_ID, CUSTOMER_ID, request(), SUBJECT);

        assertThat(result).isSameAs(response);
        verify(customerDeviceRepository, never()).save(any());
    }

    @Test
    void rejectsImeiThatBelongsToAnotherCustomerInBusiness() {
        stubReferences();
        AppUser anotherCustomer = new AppUser();
        anotherCustomer.setId(81);
        CustomerDevice existing = existingDevice(anotherCustomer);
        when(customerDeviceRepository.findByBusinessIdAndImeiAndActiveTrueAndDeletedAtIsNullOrderByIdAsc(
                BUSINESS_ID, "351756051523999")).thenReturn(List.of(existing));

        assertThatThrownBy(() -> service.resolveOrCreateForQuickOrder(
                BUSINESS_ID, CUSTOMER_ID, request(), SUBJECT))
                .isInstanceOf(ConflictException.class);

        verify(customerDeviceRepository, never()).save(any());
    }

    @Test
    void createsDeviceWithRegistrarDerivedFromJwtSubject() {
        stubReferences();
        CustomerDeviceResponse response = org.mockito.Mockito.mock(CustomerDeviceResponse.class);
        when(customerDeviceRepository.findByBusinessIdAndImeiAndActiveTrueAndDeletedAtIsNullOrderByIdAsc(
                BUSINESS_ID, "351756051523999")).thenReturn(List.of());
        when(customerDeviceRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(customerDeviceMapper.toResponse(any())).thenReturn(response);

        CustomerDeviceResponse result = service.resolveOrCreateForQuickOrder(
                BUSINESS_ID, CUSTOMER_ID, request(), SUBJECT);

        assertThat(result).isSameAs(response);
        ArgumentCaptor<CustomerDevice> saved = ArgumentCaptor.forClass(CustomerDevice.class);
        verify(customerDeviceRepository).save(saved.capture());
        assertThat(saved.getValue().getRegisteredByBusinessUser()).isSameAs(actor);
        assertThat(saved.getValue().getCustomer()).isSameAs(customer);
    }

    private void stubReferences() {
        when(businessRepository.findByIdAndDeletedAtIsNull(BUSINESS_ID)).thenReturn(Optional.of(business));
        when(appUserRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(businessCatalogLookupService.findActive(
                BUSINESS_ID, 11, BusinessCatalogType.DEVICE_TYPE)).thenReturn(deviceType);
        when(businessCatalogLookupService.findActive(
                BUSINESS_ID, 12, BusinessCatalogType.BRAND)).thenReturn(brand);
        when(businessUserRepository.findByBusinessIdAndUserIdAndActiveTrueAndDeletedAtIsNull(
                BUSINESS_ID, 30)).thenReturn(Optional.of(actor));
    }

    private QuickRepairOrderDeviceRequest request() {
        return new QuickRepairOrderDeviceRequest(
                11, 12, "iPhone 13 Pro", "SN-1", "351756051523999", "Azul", null);
    }

    private CustomerDevice existingDevice(AppUser owner) {
        CustomerDevice existing = new CustomerDevice();
        existing.setId(90);
        existing.setBusiness(business);
        existing.setCustomer(owner);
        existing.setDeviceType(deviceType);
        existing.setBrand(brand);
        existing.setModel("iPhone 13 Pro");
        existing.setImei("351756051523999");
        existing.setRegisteredByBusinessUser(actor);
        existing.setActive(true);
        return existing;
    }
}
