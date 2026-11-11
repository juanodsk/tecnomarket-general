package com.tecmarket.backend.repairorder.service;

import com.tecmarket.backend.auth.service.CustomerAccountService;
import com.tecmarket.backend.auth.service.PasswordResetService;
import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.business.security.BusinessBranchAccessGuard;
import com.tecmarket.backend.common.exception.BadRequestException;
import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.util.IdempotencySupport;
import com.tecmarket.backend.repairorder.dto.CreateQuickRepairOrderRequest;
import com.tecmarket.backend.repairorder.dto.CreateRepairOrderRequest;
import com.tecmarket.backend.repairorder.dto.CustomerDeviceResponse;
import com.tecmarket.backend.repairorder.dto.QuickRepairOrderCustomerRequest;
import com.tecmarket.backend.repairorder.dto.QuickRepairOrderCustomerResponse;
import com.tecmarket.backend.repairorder.dto.QuickRepairOrderResponse;
import com.tecmarket.backend.repairorder.dto.RepairOrderResponse;
import com.tecmarket.backend.repairorder.entity.RepairOrder;
import com.tecmarket.backend.repairorder.mapper.CustomerDeviceMapper;
import com.tecmarket.backend.repairorder.mapper.RepairOrderMapper;
import com.tecmarket.backend.repairorder.repository.RepairOrderRepository;
import com.tecmarket.backend.user.dto.CreateInvitedCustomerRequest;
import com.tecmarket.backend.user.entity.AppUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuickRepairOrderService {

    private static final String IDEMPOTENCY_OPERATION = "QUICK_REPAIR_ORDER";

    private final RepairOrderRepository repairOrderRepository;
    private final RepairOrderMapper repairOrderMapper;
    private final CustomerDeviceMapper customerDeviceMapper;
    private final CustomerAccountService customerAccountService;
    private final CustomerDeviceService customerDeviceService;
    private final RepairOrderService repairOrderService;
    private final PasswordResetService passwordResetService;
    private final BusinessAccessGuard businessAccessGuard;
    private final BusinessBranchAccessGuard businessBranchAccessGuard;

    public QuickRepairOrderService(
            RepairOrderRepository repairOrderRepository,
            RepairOrderMapper repairOrderMapper,
            CustomerDeviceMapper customerDeviceMapper,
            CustomerAccountService customerAccountService,
            CustomerDeviceService customerDeviceService,
            RepairOrderService repairOrderService,
            PasswordResetService passwordResetService,
            BusinessAccessGuard businessAccessGuard,
            BusinessBranchAccessGuard businessBranchAccessGuard) {
        this.repairOrderRepository = repairOrderRepository;
        this.repairOrderMapper = repairOrderMapper;
        this.customerDeviceMapper = customerDeviceMapper;
        this.customerAccountService = customerAccountService;
        this.customerDeviceService = customerDeviceService;
        this.repairOrderService = repairOrderService;
        this.passwordResetService = passwordResetService;
        this.businessAccessGuard = businessAccessGuard;
        this.businessBranchAccessGuard = businessBranchAccessGuard;
    }

    @Transactional
    public QuickRepairOrderResponse create(
            Integer businessId,
            CreateQuickRepairOrderRequest request,
            String subject,
            String rawIdempotencyKey) {
        businessAccessGuard.assertHasBusinessPermission(
                businessId, subject, "CUSTOMER_DEVICE_CREATE");
        businessAccessGuard.assertHasBusinessPermission(
                businessId, subject, "REPAIR_ORDER_CREATE");

        String idempotencyKey = IdempotencySupport.normalize(rawIdempotencyKey);
        if (idempotencyKey == null) {
            throw new BadRequestException("idempotency.key.required");
        }
        String fingerprint = IdempotencySupport.fingerprint(
                subject, IDEMPOTENCY_OPERATION + "\n" + request);

        businessBranchAccessGuard.findAccessibleBranchForUpdate(
                businessId, request.order().branchId(), subject);
        RepairOrder existing = repairOrderRepository
                .findByBusinessIdAndIdempotencyKeyAndDeletedAtIsNull(businessId, idempotencyKey)
                .orElse(null);
        if (existing != null) {
            if (!fingerprint.equals(existing.getIdempotencyFingerprint())) {
                throw new ConflictException("idempotency.key.reused");
            }
            return toResponse(existing);
        }

        CustomerAccountService.CustomerAccountResolution customerResolution = customerAccountService
                .resolveOrCreate(toCustomerAccountRequest(request.customer()));
        AppUser customer = customerResolution.user();

        CustomerDeviceResponse device = customerDeviceService.resolveOrCreateForQuickOrder(
                businessId,
                customer.getId(),
                request.device(),
                subject);

        CreateRepairOrderRequest createOrderRequest = new CreateRepairOrderRequest(
                request.order().branchId(),
                customer.getId(),
                device.id(),
                null,
                request.order().businessServiceId(),
                request.order().assignedBusinessUserId(),
                request.order().problemDescription(),
                request.order().diagnosis(),
                request.order().estimatedPrice(),
                request.order().estimatedDeliveryAt(),
                request.order().receivedAt());
        RepairOrderResponse repairOrder = repairOrderService.createFromQuickOrder(
                businessId,
                createOrderRequest,
                subject,
                idempotencyKey,
                fingerprint);

        if (customerResolution.created()) {
            passwordResetService.inviteNewAccount(customer, repairOrder.orderNumber());
        }

        return new QuickRepairOrderResponse(toCustomerResponse(customer), device, repairOrder);
    }

    private QuickRepairOrderResponse toResponse(RepairOrder repairOrder) {
        return new QuickRepairOrderResponse(
                toCustomerResponse(repairOrder.getCustomer()),
                customerDeviceMapper.toResponse(repairOrder.getCustomerDevice()),
                repairOrderMapper.toResponse(repairOrder));
    }

    private CreateInvitedCustomerRequest toCustomerAccountRequest(QuickRepairOrderCustomerRequest customer) {
        return new CreateInvitedCustomerRequest(
                customer.documentTypeCode(),
                customer.documentNumber(),
                customer.name(),
                customer.surname(),
                customer.email(),
                customer.address(),
                customer.phone(),
                customer.countryId(),
                customer.departmentId(),
                customer.cityId());
    }

    private QuickRepairOrderCustomerResponse toCustomerResponse(AppUser customer) {
        return new QuickRepairOrderCustomerResponse(
                customer.getId(),
                customer.getDocumentType().getCode(),
                customer.getDocumentNumber(),
                customer.getName(),
                customer.getSurname(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getEmailVerified());
    }
}
