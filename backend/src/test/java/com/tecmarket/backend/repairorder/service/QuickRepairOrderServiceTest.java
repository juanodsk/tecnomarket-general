package com.tecmarket.backend.repairorder.service;

import com.tecmarket.backend.auth.service.CustomerAccountService;
import com.tecmarket.backend.auth.service.PasswordResetService;
import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.business.security.BusinessBranchAccessGuard;
import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.util.IdempotencySupport;
import com.tecmarket.backend.repairorder.dto.CreateQuickRepairOrderRequest;
import com.tecmarket.backend.repairorder.dto.CreateRepairOrderRequest;
import com.tecmarket.backend.repairorder.dto.CustomerDeviceResponse;
import com.tecmarket.backend.repairorder.dto.QuickRepairOrderCustomerRequest;
import com.tecmarket.backend.repairorder.dto.QuickRepairOrderDetailsRequest;
import com.tecmarket.backend.repairorder.dto.QuickRepairOrderDeviceRequest;
import com.tecmarket.backend.repairorder.dto.QuickRepairOrderResponse;
import com.tecmarket.backend.repairorder.dto.RepairOrderResponse;
import com.tecmarket.backend.repairorder.entity.CustomerDevice;
import com.tecmarket.backend.repairorder.entity.RepairOrder;
import com.tecmarket.backend.repairorder.mapper.CustomerDeviceMapper;
import com.tecmarket.backend.repairorder.mapper.RepairOrderMapper;
import com.tecmarket.backend.repairorder.repository.RepairOrderRepository;
import com.tecmarket.backend.user.entity.AppUser;
import com.tecmarket.backend.user.entity.DocumentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuickRepairOrderServiceTest {

    private static final Integer BUSINESS_ID = 10;
    private static final String SUBJECT = "30";
    private static final String IDEMPOTENCY_KEY = "quick-order-0001";

    @Mock private RepairOrderRepository repairOrderRepository;
    @Mock private RepairOrderMapper repairOrderMapper;
    @Mock private CustomerDeviceMapper customerDeviceMapper;
    @Mock private CustomerAccountService customerAccountService;
    @Mock private CustomerDeviceService customerDeviceService;
    @Mock private RepairOrderService repairOrderService;
    @Mock private PasswordResetService passwordResetService;
    @Mock private BusinessAccessGuard businessAccessGuard;
    @Mock private BusinessBranchAccessGuard businessBranchAccessGuard;

    private QuickRepairOrderService service;

    @BeforeEach
    void setUp() {
        service = new QuickRepairOrderService(
                repairOrderRepository,
                repairOrderMapper,
                customerDeviceMapper,
                customerAccountService,
                customerDeviceService,
                repairOrderService,
                passwordResetService,
                businessAccessGuard,
                businessBranchAccessGuard);
    }

    @Test
    void createsCustomerDeviceOrderAndInvitationInOneFlow() {
        CreateQuickRepairOrderRequest request = request();
        AppUser customer = customer(80);
        CustomerDeviceResponse device = org.mockito.Mockito.mock(CustomerDeviceResponse.class);
        RepairOrderResponse order = org.mockito.Mockito.mock(RepairOrderResponse.class);

        when(device.id()).thenReturn(90);
        when(order.orderNumber()).thenReturn("OS-010-01-000001");
        when(repairOrderRepository.findByBusinessIdAndIdempotencyKeyAndDeletedAtIsNull(
                BUSINESS_ID, IDEMPOTENCY_KEY)).thenReturn(Optional.empty());
        when(customerAccountService.resolveOrCreate(org.mockito.ArgumentMatchers.any()))
                .thenReturn(new CustomerAccountService.CustomerAccountResolution(customer, true));
        when(customerDeviceService.resolveOrCreateForQuickOrder(
                BUSINESS_ID, customer.getId(), request.device(), SUBJECT)).thenReturn(device);
        when(repairOrderService.createFromQuickOrder(
                org.mockito.ArgumentMatchers.eq(BUSINESS_ID),
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.eq(SUBJECT),
                org.mockito.ArgumentMatchers.eq(IDEMPOTENCY_KEY),
                org.mockito.ArgumentMatchers.anyString())).thenReturn(order);

        QuickRepairOrderResponse response = service.create(
                BUSINESS_ID, request, SUBJECT, IDEMPOTENCY_KEY);

        assertThat(response.device()).isSameAs(device);
        assertThat(response.repairOrder()).isSameAs(order);
        assertThat(response.customer().id()).isEqualTo(customer.getId());
        verify(businessAccessGuard).assertHasBusinessPermission(
                BUSINESS_ID, SUBJECT, "CUSTOMER_DEVICE_CREATE");
        verify(businessAccessGuard).assertHasBusinessPermission(
                BUSINESS_ID, SUBJECT, "REPAIR_ORDER_CREATE");
        verify(passwordResetService).inviteNewAccount(customer, "OS-010-01-000001");

        ArgumentCaptor<CreateRepairOrderRequest> orderRequest = ArgumentCaptor.forClass(CreateRepairOrderRequest.class);
        verify(repairOrderService).createFromQuickOrder(
                org.mockito.ArgumentMatchers.eq(BUSINESS_ID),
                orderRequest.capture(),
                org.mockito.ArgumentMatchers.eq(SUBJECT),
                org.mockito.ArgumentMatchers.eq(IDEMPOTENCY_KEY),
                org.mockito.ArgumentMatchers.anyString());
        assertThat(orderRequest.getValue().customerId()).isEqualTo(customer.getId());
        assertThat(orderRequest.getValue().customerDeviceId()).isEqualTo(90);
        assertThat(orderRequest.getValue().appointmentId()).isNull();
    }

    @Test
    void idempotentReplayReturnsExistingAggregateWithoutCreatingAnything() {
        CreateQuickRepairOrderRequest request = request();
        String fingerprint = IdempotencySupport.fingerprint(
                SUBJECT, "QUICK_REPAIR_ORDER\n" + request);
        RepairOrder existing = org.mockito.Mockito.mock(RepairOrder.class);
        AppUser customer = customer(80);
        CustomerDevice customerDevice = org.mockito.Mockito.mock(CustomerDevice.class);
        CustomerDeviceResponse deviceResponse = org.mockito.Mockito.mock(CustomerDeviceResponse.class);
        RepairOrderResponse orderResponse = org.mockito.Mockito.mock(RepairOrderResponse.class);

        when(existing.getIdempotencyFingerprint()).thenReturn(fingerprint);
        when(existing.getCustomer()).thenReturn(customer);
        when(existing.getCustomerDevice()).thenReturn(customerDevice);
        when(repairOrderRepository.findByBusinessIdAndIdempotencyKeyAndDeletedAtIsNull(
                BUSINESS_ID, IDEMPOTENCY_KEY)).thenReturn(Optional.of(existing));
        when(customerDeviceMapper.toResponse(customerDevice)).thenReturn(deviceResponse);
        when(repairOrderMapper.toResponse(existing)).thenReturn(orderResponse);

        QuickRepairOrderResponse response = service.create(
                BUSINESS_ID, request, SUBJECT, IDEMPOTENCY_KEY);

        assertThat(response.device()).isSameAs(deviceResponse);
        assertThat(response.repairOrder()).isSameAs(orderResponse);
        verifyNoInteractions(customerAccountService, customerDeviceService, repairOrderService, passwordResetService);
    }

    @Test
    void rejectsReusedIdempotencyKeyWithDifferentPayload() {
        RepairOrder existing = org.mockito.Mockito.mock(RepairOrder.class);
        when(existing.getIdempotencyFingerprint()).thenReturn("different-fingerprint");
        when(repairOrderRepository.findByBusinessIdAndIdempotencyKeyAndDeletedAtIsNull(
                BUSINESS_ID, IDEMPOTENCY_KEY)).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> service.create(BUSINESS_ID, request(), SUBJECT, IDEMPOTENCY_KEY))
                .isInstanceOf(ConflictException.class);

        verify(passwordResetService, never()).inviteNewAccount(
                org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.anyString());
    }

    private CreateQuickRepairOrderRequest request() {
        return new CreateQuickRepairOrderRequest(
                new QuickRepairOrderCustomerRequest(
                        "CC", "123456789", "Laura", "Gomez", "laura@example.com",
                        "Calle 10 #20-30", "+573001112233", 1, 2, 3),
                new QuickRepairOrderDeviceRequest(
                        11, 12, "iPhone 13 Pro", "SN-1", "351756051523999", "Azul", null),
                new QuickRepairOrderDetailsRequest(
                        21, 22, 23, "No enciende", null, new BigDecimal("150000.00"), null, null));
    }

    private AppUser customer(Integer id) {
        DocumentType documentType = new DocumentType();
        documentType.setCode("CC");
        AppUser customer = new AppUser();
        customer.setId(id);
        customer.setDocumentType(documentType);
        customer.setDocumentNumber("123456789");
        customer.setName("Laura");
        customer.setSurname("Gomez");
        customer.setEmail("laura@example.com");
        customer.setPhone("+573001112233");
        customer.setEmailVerified(false);
        customer.setActive(true);
        return customer;
    }
}
