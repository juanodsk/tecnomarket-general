package com.tecmarket.backend.repairorder.service;

import com.tecmarket.backend.repairorder.dto.CreateRepairOrderRequest;
import com.tecmarket.backend.repairorder.dto.CustomerRepairOrderTrackingResponse;
import com.tecmarket.backend.repairorder.dto.CreateRepairOrderPartRequest;
import com.tecmarket.backend.repairorder.dto.RepairOrderAttachmentResponse;
import com.tecmarket.backend.repairorder.dto.RepairOrderHistoryResponse;
import com.tecmarket.backend.repairorder.dto.RepairOrderPartResponse;
import com.tecmarket.backend.repairorder.dto.RepairOrderResponse;
import com.tecmarket.backend.repairorder.dto.UpdateRepairOrderStatusRequest;
import com.tecmarket.backend.repairorder.dto.UpdateRepairOrderPartRequest;
import com.tecmarket.backend.repairorder.dto.UpdateRepairOrderFinancialsRequest;
import com.tecmarket.backend.repairorder.dto.UpdateRepairOrderDiagnosisRequest;
import com.tecmarket.backend.repairorder.dto.UpdateRepairOrderAssignmentRequest;
import com.tecmarket.backend.business.entity.*;
import com.tecmarket.backend.repairorder.mapper.RepairOrderMapper;
import com.tecmarket.backend.business.repository.*;
import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.business.security.BusinessBranchAccessGuard;
import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.exception.FileValidationException;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.common.util.IdempotencySupport;
import com.tecmarket.backend.common.pagination.PageRequestFactory;
import com.tecmarket.backend.common.pagination.PageResponse;
import com.tecmarket.backend.storage.dto.StoredObjectResponse;
import com.tecmarket.backend.storage.config.MinioStorageProperties;
import com.tecmarket.backend.storage.service.MinioStorageService;
import com.tecmarket.backend.storage.service.StorageDeletionQueueService;
import jakarta.persistence.criteria.Predicate;
import com.tecmarket.backend.user.entity.AppUser;
import com.tecmarket.backend.user.repository.AppUserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.math.RoundingMode;
import java.util.Optional;

import com.tecmarket.backend.appointment.entity.Appointment;
import com.tecmarket.backend.appointment.repository.AppointmentRepository;
import com.tecmarket.backend.business.service.BusinessCatalogLookupService;
import com.tecmarket.backend.inventory.entity.InventoryItem;
import com.tecmarket.backend.inventory.entity.StockMovement;
import com.tecmarket.backend.inventory.service.InventoryStockService;
import com.tecmarket.backend.repairorder.entity.CustomerDevice;
import com.tecmarket.backend.repairorder.entity.RepairOrder;
import com.tecmarket.backend.repairorder.entity.RepairOrderAttachment;
import com.tecmarket.backend.repairorder.entity.RepairOrderHistory;
import com.tecmarket.backend.repairorder.entity.RepairOrderHistoryVisibility;
import com.tecmarket.backend.repairorder.entity.RepairOrderPart;
import com.tecmarket.backend.repairorder.repository.CustomerDeviceRepository;
import com.tecmarket.backend.repairorder.repository.RepairOrderAttachmentRepository;
import com.tecmarket.backend.repairorder.repository.RepairOrderCommissionRepository;
import com.tecmarket.backend.repairorder.repository.RepairOrderHistoryRepository;
import com.tecmarket.backend.repairorder.repository.RepairOrderPartRepository;
import com.tecmarket.backend.repairorder.repository.RepairOrderRepository;
@Service
public class RepairOrderService {

    private static final String INITIAL_STATUS_CODE = "RECEIVED";
    private static final String DELIVERED_STATUS_CODE = "DELIVERED";
    private static final String CANCELLED_STATUS_CODE = "CANCELLED";
    private static final String STOCK_MOVEMENT_OUT = "OUT";
    private static final String STOCK_MOVEMENT_RETURN = "RETURN";
    private static final int ATTACHMENT_DESCRIPTION_MAX_LENGTH = 500;

    private final RepairOrderRepository repairOrderRepository;
    private final BusinessRepository businessRepository;
    private final AppUserRepository appUserRepository;
    private final CustomerDeviceRepository customerDeviceRepository;
    private final AppointmentRepository appointmentRepository;
    private final BusinessOfferingRepository businessOfferingRepository;
    private final BusinessCatalogLookupService businessCatalogLookupService;
    private final RepairOrderHistoryRepository repairOrderHistoryRepository;
    private final RepairOrderAttachmentRepository repairOrderAttachmentRepository;
    private final RepairOrderPartRepository repairOrderPartRepository;
    private final RepairOrderCommissionRepository repairOrderCommissionRepository;
    private final InventoryStockService inventoryStockService;
    private final BusinessUserRepository businessUserRepository;
    private final BusinessAccessGuard businessAccessGuard;
    private final BusinessBranchAccessGuard businessBranchAccessGuard;
    private final RepairOrderMapper repairOrderMapper;
    private final JdbcTemplate jdbcTemplate;
    private final MinioStorageService minioStorageService;
    private final StorageDeletionQueueService storageDeletionQueueService;
    private final MinioStorageProperties storageProperties;

    public RepairOrderService(
            RepairOrderRepository repairOrderRepository,
            BusinessRepository businessRepository,
            AppUserRepository appUserRepository,
            CustomerDeviceRepository customerDeviceRepository,
            AppointmentRepository appointmentRepository,
            BusinessOfferingRepository businessOfferingRepository,
            BusinessCatalogLookupService businessCatalogLookupService,
            RepairOrderHistoryRepository repairOrderHistoryRepository,
            RepairOrderAttachmentRepository repairOrderAttachmentRepository,
            RepairOrderPartRepository repairOrderPartRepository,
            RepairOrderCommissionRepository repairOrderCommissionRepository,
            InventoryStockService inventoryStockService,
            BusinessUserRepository businessUserRepository,
            BusinessAccessGuard businessAccessGuard,
            BusinessBranchAccessGuard businessBranchAccessGuard,
            RepairOrderMapper repairOrderMapper,
            JdbcTemplate jdbcTemplate,
            MinioStorageService minioStorageService,
            StorageDeletionQueueService storageDeletionQueueService,
            MinioStorageProperties storageProperties) {
        this.repairOrderRepository = repairOrderRepository;
        this.businessRepository = businessRepository;
        this.appUserRepository = appUserRepository;
        this.customerDeviceRepository = customerDeviceRepository;
        this.appointmentRepository = appointmentRepository;
        this.businessOfferingRepository = businessOfferingRepository;
        this.businessCatalogLookupService = businessCatalogLookupService;
        this.repairOrderHistoryRepository = repairOrderHistoryRepository;
        this.repairOrderAttachmentRepository = repairOrderAttachmentRepository;
        this.repairOrderPartRepository = repairOrderPartRepository;
        this.repairOrderCommissionRepository = repairOrderCommissionRepository;
        this.inventoryStockService = inventoryStockService;
        this.businessUserRepository = businessUserRepository;
        this.businessAccessGuard = businessAccessGuard;
        this.businessBranchAccessGuard = businessBranchAccessGuard;
        this.repairOrderMapper = repairOrderMapper;
        this.jdbcTemplate = jdbcTemplate;
        this.minioStorageService = minioStorageService;
        this.storageDeletionQueueService = storageDeletionQueueService;
        this.storageProperties = storageProperties;
    }

    @Transactional
    public RepairOrderResponse create(Integer businessId, CreateRepairOrderRequest request, String subject) {
        return create(businessId, request, subject, null);
    }

    @Transactional
    public RepairOrderResponse create(
            Integer businessId,
            CreateRepairOrderRequest request,
            String subject,
            String rawIdempotencyKey) {
        return createInternal(
                businessId,
                request,
                subject,
                rawIdempotencyKey,
                IdempotencySupport.fingerprint(subject, request));
    }

    RepairOrderResponse createFromQuickOrder(
            Integer businessId,
            CreateRepairOrderRequest request,
            String subject,
            String rawIdempotencyKey,
            String idempotencyFingerprint) {
        return createInternal(businessId, request, subject, rawIdempotencyKey, idempotencyFingerprint);
    }

    private RepairOrderResponse createInternal(
            Integer businessId,
            CreateRepairOrderRequest request,
            String subject,
            String rawIdempotencyKey,
            String fingerprint) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "REPAIR_ORDER_CREATE");
        String idempotencyKey = IdempotencySupport.normalize(rawIdempotencyKey);
        BusinessBranch branch = businessBranchAccessGuard.findAccessibleBranchForUpdate(
                businessId, request.branchId(), subject);
        if (idempotencyKey != null) {
            RepairOrder existing = repairOrderRepository
                    .findByBusinessIdAndIdempotencyKeyAndDeletedAtIsNull(businessId, idempotencyKey)
                    .orElse(null);
            if (existing != null) {
                if (!fingerprint.equals(existing.getIdempotencyFingerprint())) {
                    throw new ConflictException("idempotency.key.reused");
                }
                businessBranchAccessGuard.findAccessibleBranch(businessId, existing.getBranch().getId(), subject);
                return repairOrderMapper.toResponse(existing);
            }
        }
        AppUser actor = findActiveUser(parseUserId(subject));
        Business business = findBusiness(businessId);
        BusinessUser actorBusinessUser = findActiveBusinessUserByUserId(businessId, actor.getId());
        AppUser customer = findActiveUser(request.customerId());
        CustomerDevice customerDevice = findActiveCustomerDevice(businessId, request.customerDeviceId());
        validateCustomerDeviceOwnership(customerDevice, customer);
        BusinessOffering businessService = findActiveBusinessService(businessId, request.businessServiceId());
        BusinessUser receivedBy = actorBusinessUser;
        BusinessUser assignedTo = findActiveTechnician(businessId, request.assignedBusinessUserId());
        businessBranchAccessGuard.assertBusinessUserAssigned(businessId, branch.getId(), receivedBy.getId());
        businessBranchAccessGuard.assertBusinessUserAssigned(businessId, branch.getId(), assignedTo.getId());
        Appointment appointment = findValidAppointment(businessId, request.appointmentId(), customer, customerDevice,
                businessService, branch);
        BusinessCatalogItem initialStatus = findInitialStatus(businessId);

        Integer sequence = nextSequenceForBranch(businessId, branch.getId());
        String orderNumber = formatOrderNumber(businessId, branch.getBranchNumber(), sequence);

        RepairOrder entity = new RepairOrder();
        entity.setOrderNumber(orderNumber);
        entity.setOrderSequence(sequence);
        entity.setBusiness(business);
        entity.setBranch(branch);
        entity.setCustomer(customer);
        entity.setCustomerDevice(customerDevice);
        entity.setAppointment(appointment);
        entity.setBusinessService(businessService);
        entity.setStatus(initialStatus);
        entity.setReceivedByBusinessUser(receivedBy);
        entity.setAssignedBusinessUser(assignedTo);
        entity.setProblemDescription(request.problemDescription().trim());
        entity.setDiagnosis(normalizeNullable(request.diagnosis()));
        entity.setEstimatedPrice(request.estimatedPrice());
        entity.setIdempotencyKey(idempotencyKey);
        entity.setIdempotencyFingerprint(idempotencyKey == null ? null : fingerprint);
        entity.setReceivedAt(request.receivedAt());
        entity.setEstimatedDeliveryAt(request.estimatedDeliveryAt());
        entity.setActive(true);

        RepairOrder saved = repairOrderRepository.save(entity);
        saveHistory(
                saved,
                saved.getStatus(),
                actor,
                "Orden creada con estado inicial " + saved.getStatus().getCode() + ".",
                RepairOrderHistoryVisibility.INTERNAL);
        return repairOrderMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public PageResponse<RepairOrderResponse> findAllByBusiness(
            Integer businessId,
            String subject,
            Integer branchId,
            String orderNumber,
            Integer orderSequence,
            Integer statusId,
            Integer customerId,
            String customer,
            LocalDateTime receivedFrom,
            LocalDateTime receivedTo,
            Integer page,
            Integer size,
            String sortBy,
            String sortDir) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "REPAIR_ORDER_VIEW");
        findBusiness(businessId);
        List<Integer> accessibleBranchIds = businessBranchAccessGuard.accessibleBranchIds(businessId, subject);
        if (branchId != null) {
            businessBranchAccessGuard.findAccessibleBranch(businessId, branchId, subject);
        }

        if (receivedFrom != null && receivedTo != null && receivedFrom.isAfter(receivedTo)) {
            throw new ConflictException("repair_order.received_at.invalid_range");
        }

        Specification<RepairOrder> spec = buildFilterSpecification(
                businessId,
                accessibleBranchIds,
                branchId,
                orderNumber,
                orderSequence,
                statusId,
                customerId,
                customer,
                receivedFrom,
                receivedTo);

        Pageable pageable = buildPageable(page, size, sortBy, sortDir);
        Page<RepairOrder> result = repairOrderRepository.findAll(spec, pageable);

        return PageResponse.from(result.map(repairOrderMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public CustomerRepairOrderTrackingResponse findCustomerOrder(String orderNumber, String subject) {
        Integer customerId = parseUserId(subject);
        RepairOrder repairOrder = repairOrderRepository
                .findByOrderNumberIgnoreCaseAndCustomerIdAndDeletedAtIsNull(orderNumber.trim(), customerId)
                .orElseThrow(() -> new ResourceNotFoundException("repair_order.not_found"));
        return new CustomerRepairOrderTrackingResponse(
                repairOrder.getOrderNumber(),
                repairOrder.getBusiness().getName(),
                repairOrder.getBranch().getName(),
                repairOrder.getCustomerDevice().getDeviceType().getName() + " "
                        + repairOrder.getCustomerDevice().getBrand().getName() + " "
                        + repairOrder.getCustomerDevice().getModel(),
                repairOrder.getBusinessService().getName(),
                repairOrder.getStatus().getCode(),
                repairOrder.getStatus().getName(),
                repairOrder.getReceivedAt(),
                repairOrder.getEstimatedDeliveryAt(),
                repairOrder.getDeliveredAt());
    }

    @Transactional
    public RepairOrderResponse updateStatus(
            Integer businessId,
            Integer repairOrderId,
            UpdateRepairOrderStatusRequest request,
            String subject) {
        AppUser actor = findActiveUser(parseUserId(subject));

        RepairOrder repairOrder = findActiveRepairOrderForUpdate(businessId, repairOrderId, subject);
        BusinessCatalogItem targetStatus = findActiveRepairOrderStatus(businessId, request.statusId());

        String currentCode = repairOrder.getStatus().getCode();
        String targetCode = targetStatus.getCode();

        String requiredPermission = isTerminalStatus(targetCode)
                ? "REPAIR_ORDER_CLOSE"
                : "REPAIR_ORDER_UPDATE";
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, requiredPermission);

        if (!currentCode.equalsIgnoreCase(targetCode)
                && !RepairOrderStateMachine.isTransitionAllowed(currentCode, targetCode)) {
            throw new ConflictException("repair_order.status.invalid_transition");
        }
        if (DELIVERED_STATUS_CODE.equalsIgnoreCase(targetCode) && repairOrder.getFinalPrice() == null) {
            throw new ConflictException("repair_order.final_price.required_for_delivery");
        }

        repairOrder.setStatus(targetStatus);

        if (DELIVERED_STATUS_CODE.equalsIgnoreCase(targetCode)) {
            if (repairOrder.getDeliveredAt() == null) {
                repairOrder.setDeliveredAt(LocalDateTime.now());
            }
        }
        if (!DELIVERED_STATUS_CODE.equalsIgnoreCase(targetCode)) {
            repairOrder.setDeliveredAt(null);
        }

        String historyComment = normalizeNullable(request.comment());
        if (historyComment == null && !currentCode.equalsIgnoreCase(targetCode)) {
            historyComment = "Estado cambiado de " + currentCode + " a " + targetCode + ".";
        }
        saveHistory(
                repairOrder,
                targetStatus,
                actor,
                historyComment,
                request.visibility() != null ? request.visibility() : RepairOrderHistoryVisibility.INTERNAL);

        return repairOrderMapper.toResponse(repairOrder);
    }

    @Transactional
    public RepairOrderResponse updateFinancials(
            Integer businessId,
            Integer repairOrderId,
            UpdateRepairOrderFinancialsRequest request,
            String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "REPAIR_ORDER_FINANCIAL_UPDATE");
        AppUser actor = findActiveUser(parseUserId(subject));
        RepairOrder repairOrder = findActiveRepairOrderForUpdate(businessId, repairOrderId, subject);
        assertOrderMutable(repairOrder);

        java.math.BigDecimal finalPrice = request.finalPrice().setScale(2, RoundingMode.HALF_UP);
        java.math.BigDecimal rawPartsTotal = repairOrderPartRepository.sumActivePartsTotal(repairOrderId, businessId);
        java.math.BigDecimal partsTotal = (rawPartsTotal == null ? java.math.BigDecimal.ZERO : rawPartsTotal)
                .setScale(2, RoundingMode.HALF_UP);
        if (finalPrice.compareTo(partsTotal) < 0) {
            throw new ConflictException("repair_order.final_price.less_than_parts");
        }

        java.math.BigDecimal previousPrice = repairOrder.getFinalPrice();
        repairOrder.setFinalPrice(finalPrice);
        saveHistory(
                repairOrder,
                repairOrder.getStatus(),
                actor,
                "Precio final actualizado de " + formatAmount(previousPrice) + " a " + formatAmount(finalPrice) + ".",
                RepairOrderHistoryVisibility.INTERNAL);
        return repairOrderMapper.toResponse(repairOrder);
    }

    @Transactional
    public RepairOrderResponse updateDiagnosis(
            Integer businessId,
            Integer repairOrderId,
            UpdateRepairOrderDiagnosisRequest request,
            String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "REPAIR_ORDER_UPDATE");
        AppUser actor = findActiveUser(parseUserId(subject));
        RepairOrder repairOrder = findActiveRepairOrderForUpdate(businessId, repairOrderId, subject);
        assertOrderMutable(repairOrder);

        repairOrder.setDiagnosis(request.diagnosis().trim());
        saveHistory(
                repairOrder,
                repairOrder.getStatus(),
                actor,
                "Diagnóstico técnico actualizado.",
                RepairOrderHistoryVisibility.INTERNAL);
        return repairOrderMapper.toResponse(repairOrder);
    }

    @Transactional
    public RepairOrderResponse updateAssignment(
            Integer businessId,
            Integer repairOrderId,
            UpdateRepairOrderAssignmentRequest request,
            String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "REPAIR_ORDER_UPDATE");
        AppUser actor = findActiveUser(parseUserId(subject));
        RepairOrder repairOrder = findActiveRepairOrderForUpdate(businessId, repairOrderId, subject);
        assertOrderMutable(repairOrder);

        BusinessUser previousAssignee = repairOrder.getAssignedBusinessUser();
        BusinessUser nextAssignee = findActiveTechnician(businessId, request.assignedBusinessUserId());
        businessBranchAccessGuard.assertBusinessUserAssigned(
                businessId, repairOrder.getBranch().getId(), nextAssignee.getId());
        repairOrder.setAssignedBusinessUser(nextAssignee);
        saveHistory(
                repairOrder,
                repairOrder.getStatus(),
                actor,
                "Técnico asignado cambiado de " + describeBusinessUser(previousAssignee)
                        + " a " + describeBusinessUser(nextAssignee) + ".",
                RepairOrderHistoryVisibility.INTERNAL);
        return repairOrderMapper.toResponse(repairOrder);
    }

    @Transactional(readOnly = true)
    public PageResponse<RepairOrderHistoryResponse> findHistoryByRepairOrder(
            Integer businessId,
            Integer repairOrderId,
            Integer page,
            Integer size,
            String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "REPAIR_ORDER_VIEW");
        findActiveRepairOrder(businessId, repairOrderId, subject);

        return PageResponse.from(repairOrderHistoryRepository
                .findByRepairOrderIdAndRepairOrderBusinessIdAndDeletedAtIsNullOrderByCreatedAtDescIdDesc(
                        repairOrderId,
                        businessId,
                        PageRequestFactory.of(page, size))
                .map(this::toHistoryResponse));
    }

    @Transactional(readOnly = true)
    public PageResponse<RepairOrderAttachmentResponse> findAttachmentsByRepairOrder(
            Integer businessId,
            Integer repairOrderId,
            Integer page,
            Integer size,
            String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "REPAIR_ORDER_VIEW");
        findActiveRepairOrder(businessId, repairOrderId, subject);

        return PageResponse.from(repairOrderAttachmentRepository
                .findByRepairOrderIdAndRepairOrderBusinessIdAndDeletedAtIsNullOrderByCreatedAtDescIdDesc(
                        repairOrderId,
                        businessId,
                        PageRequestFactory.of(page, size))
                .map(this::toAttachmentResponse));
    }

    @Transactional
    public RepairOrderAttachmentResponse uploadAttachment(
            Integer businessId,
            Integer repairOrderId,
            MultipartFile file,
            String description,
            String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "REPAIR_ORDER_UPLOAD_EVIDENCE");
        Integer userId = parseUserId(subject);
        RepairOrder repairOrder = findActiveRepairOrderForUpdate(businessId, repairOrderId, subject);
        assertOrderMutable(repairOrder);
        BusinessUser uploader = businessUserRepository
                .findByBusinessIdAndUserIdAndActiveTrueAndDeletedAtIsNull(businessId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("business_user.not_found"));
        String normalizedDescription = normalizeAttachmentDescription(description);

        StoredObjectResponse uploaded = minioStorageService.uploadRepairEvidence(
                "businesses/" + businessId + "/repair-orders/" + repairOrderId,
                file);
        minioStorageService.registerRollbackCleanup(uploaded);

        RepairOrderAttachment entity = new RepairOrderAttachment();
        entity.setRepairOrder(repairOrder);
        entity.setUploadedByBusinessUser(uploader);
        entity.setFileUrl(null);
        entity.setBucketName(uploaded.bucket());
        entity.setObjectKey(uploaded.objectKey());
        entity.setOriginalFileName(uploaded.originalFileName());
        entity.setContentType(uploaded.contentType());
        entity.setFileSize(uploaded.size());
        entity.setChecksumSha256(uploaded.checksumSha256());
        entity.setDescription(normalizedDescription);
        entity.setActive(true);
        entity.setDeletedAt(null);

        RepairOrderAttachment saved = repairOrderAttachmentRepository.save(entity);
        saveHistory(
                repairOrder,
                repairOrder.getStatus(),
                uploader.getUser(),
                "Evidencia fotográfica agregada (ID " + saved.getId() + ").",
                RepairOrderHistoryVisibility.INTERNAL);
        return toAttachmentResponse(saved);
    }

    @Transactional
    public void deleteAttachment(
            Integer businessId,
            Integer repairOrderId,
            Integer attachmentId,
            String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "REPAIR_ORDER_UPLOAD_EVIDENCE");
        RepairOrder repairOrder = findActiveRepairOrderForUpdate(businessId, repairOrderId, subject);
        assertOrderMutable(repairOrder);

        RepairOrderAttachment attachment = repairOrderAttachmentRepository
                .findByIdAndRepairOrderIdAndRepairOrderBusinessIdAndDeletedAtIsNull(
                        attachmentId,
                        repairOrderId,
                        businessId)
                .orElseThrow(() -> new ResourceNotFoundException("repair_order.attachment.not_found"));

        BusinessUser actor = businessUserRepository
                .findByBusinessIdAndUserIdAndActiveTrueAndDeletedAtIsNull(businessId, parseUserId(subject))
                .orElseThrow(() -> new ResourceNotFoundException("business_user.not_found"));
        LocalDateTime deletedAt = LocalDateTime.now();
        LocalDateTime retentionUntil = deletedAt.plusDays(storageProperties.getEvidenceRetentionDays());
        attachment.setActive(false);
        attachment.setDeletedByBusinessUser(actor);
        attachment.setDeletedAt(deletedAt);
        attachment.setRetentionUntil(retentionUntil);
        storageDeletionQueueService.enqueue(
                attachment.getBucketName(),
                attachment.getObjectKey(),
                "EVIDENCE_RETENTION_EXPIRED",
                "REPAIR_ORDER_ATTACHMENT",
                attachment.getId(),
                retentionUntil);
        saveHistory(
                repairOrder,
                repairOrder.getStatus(),
                actor.getUser(),
                "Evidencia fotográfica retirada (ID " + attachment.getId()
                        + "); conservación física hasta " + retentionUntil + ".",
                RepairOrderHistoryVisibility.INTERNAL);
    }

    @Transactional(readOnly = true)
    public PageResponse<RepairOrderPartResponse> findPartsByRepairOrder(
            Integer businessId,
            Integer repairOrderId,
            Integer page,
            Integer size,
            String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "REPAIR_ORDER_VIEW");
        findActiveRepairOrder(businessId, repairOrderId, subject);

        return PageResponse.from(repairOrderPartRepository
                .findByRepairOrderIdAndRepairOrderBusinessIdAndDeletedAtIsNullOrderByCreatedAtDescIdDesc(
                        repairOrderId,
                        businessId,
                        PageRequestFactory.of(page, size))
                .map(this::toPartResponse));
    }

    @Transactional
    public RepairOrderPartResponse createPart(
            Integer businessId,
            Integer repairOrderId,
            CreateRepairOrderPartRequest request,
            String subject) {
        return createPart(businessId, repairOrderId, request, subject, null);
    }

    @Transactional
    public RepairOrderPartResponse createPart(
            Integer businessId,
            Integer repairOrderId,
            CreateRepairOrderPartRequest request,
            String subject,
            String rawIdempotencyKey) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "REPAIR_ORDER_UPDATE");
        RepairOrder repairOrder = findActiveRepairOrderForUpdate(businessId, repairOrderId, subject);
        assertOrderMutable(repairOrder);
        String idempotencyKey = IdempotencySupport.normalize(rawIdempotencyKey);
        String fingerprint = IdempotencySupport.fingerprint(subject, request);
        if (idempotencyKey != null) {
            RepairOrderPart existing = repairOrderPartRepository
                    .findByRepairOrderIdAndIdempotencyKey(repairOrderId, idempotencyKey)
                    .orElse(null);
            if (existing != null) {
                if (existing.getDeletedAt() != null
                        || !fingerprint.equals(existing.getIdempotencyFingerprint())) {
                    throw new ConflictException("idempotency.key.reused");
                }
                return toPartResponse(existing);
            }
        }
        BusinessUser usedBy = findActiveBusinessUser(businessId, request.usedByBusinessUserId());
        validatePartUserBranch(repairOrder, usedBy);
        BusinessUser actor = findActiveBusinessUserByUserId(businessId, parseUserId(subject));

        StockMovement stockMovement = inventoryStockService.applyMovementByCode(
                businessId,
                repairOrder.getBranch().getId(),
                request.inventoryItemId(),
                STOCK_MOVEMENT_OUT,
                request.quantity(),
                actor.getId(),
                repairOrder.getId(),
                null,
                "Salida por repuesto agregado a la orden " + repairOrder.getOrderNumber());
        InventoryItem inventoryItem = stockMovement.getInventoryItem();

        RepairOrderPart entity = new RepairOrderPart();
        entity.setRepairOrder(repairOrder);
        entity.setInventoryItem(inventoryItem);
        entity.setQuantity(request.quantity());
        entity.setUnitPrice(request.unitPrice().setScale(2, RoundingMode.HALF_UP));
        entity.setTotalPrice(calculateTotalPrice(request.quantity(), request.unitPrice()));
        entity.setUsedByBusinessUser(usedBy);
        entity.setIdempotencyKey(idempotencyKey);
        entity.setIdempotencyFingerprint(idempotencyKey == null ? null : fingerprint);
        entity.setActive(true);
        entity.setDeletedAt(null);

        RepairOrderPart saved = repairOrderPartRepository.save(entity);
        saveHistory(
                repairOrder,
                repairOrder.getStatus(),
                actor.getUser(),
                "Repuesto agregado: " + inventoryItem.getSku() + " x" + request.quantity()
                        + " (ID " + saved.getId() + ").",
                RepairOrderHistoryVisibility.INTERNAL);
        return toPartResponse(saved);
    }

    @Transactional
    public RepairOrderPartResponse updatePart(
            Integer businessId,
            Integer repairOrderId,
            Integer partId,
            UpdateRepairOrderPartRequest request,
            String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "REPAIR_ORDER_UPDATE");
        RepairOrder repairOrder = findActiveRepairOrderForUpdate(businessId, repairOrderId, subject);
        assertOrderMutable(repairOrder);

        RepairOrderPart part = repairOrderPartRepository
                .findByIdAndRepairOrderIdAndRepairOrderBusinessIdAndDeletedAtIsNull(partId, repairOrderId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("repair_order.part.not_found"));

        BusinessUser usedBy = findActiveBusinessUser(businessId, request.usedByBusinessUserId());
        validatePartUserBranch(repairOrder, usedBy);
        BusinessUser actor = findActiveBusinessUserByUserId(businessId, parseUserId(subject));
        InventoryItem previousInventoryItem = part.getInventoryItem();
        int previousQuantity = part.getQuantity();
        java.math.BigDecimal previousUnitPrice = part.getUnitPrice();
        InventoryItem inventoryItem = previousInventoryItem;

        if (previousInventoryItem.getId().equals(request.inventoryItemId())) {
            int quantityDifference = request.quantity() - previousQuantity;
            if (quantityDifference != 0) {
                String movementCode = quantityDifference > 0 ? STOCK_MOVEMENT_OUT : STOCK_MOVEMENT_RETURN;
                StockMovement movement = inventoryStockService.applyMovementByCode(
                        businessId,
                        repairOrder.getBranch().getId(),
                        request.inventoryItemId(),
                        movementCode,
                        Math.abs(quantityDifference),
                        actor.getId(),
                        repairOrder.getId(),
                        null,
                        "Ajuste de repuesto en la orden " + repairOrder.getOrderNumber());
                inventoryItem = movement.getInventoryItem();
            }
        } else {
            inventoryStockService.lockItemsForUpdate(
                    businessId,
                    List.of(previousInventoryItem.getId(), request.inventoryItemId()));
            inventoryStockService.applyMovementByCode(
                    businessId,
                    repairOrder.getBranch().getId(),
                    previousInventoryItem.getId(),
                    STOCK_MOVEMENT_RETURN,
                    previousQuantity,
                    actor.getId(),
                    repairOrder.getId(),
                    null,
                    "Devolución por cambio de repuesto en la orden " + repairOrder.getOrderNumber());
            StockMovement movement = inventoryStockService.applyMovementByCode(
                    businessId,
                    repairOrder.getBranch().getId(),
                    request.inventoryItemId(),
                    STOCK_MOVEMENT_OUT,
                    request.quantity(),
                    actor.getId(),
                    repairOrder.getId(),
                    null,
                    "Salida por cambio de repuesto en la orden " + repairOrder.getOrderNumber());
            inventoryItem = movement.getInventoryItem();
        }

        part.setInventoryItem(inventoryItem);
        part.setQuantity(request.quantity());
        part.setUnitPrice(request.unitPrice().setScale(2, RoundingMode.HALF_UP));
        part.setTotalPrice(calculateTotalPrice(request.quantity(), request.unitPrice()));
        part.setUsedByBusinessUser(usedBy);

        saveHistory(
                repairOrder,
                repairOrder.getStatus(),
                actor.getUser(),
                "Repuesto actualizado (ID " + part.getId() + "): "
                        + previousInventoryItem.getSku() + " x" + previousQuantity + " a "
                        + inventoryItem.getSku() + " x" + request.quantity()
                        + "; precio unitario " + formatAmount(previousUnitPrice) + " a "
                        + formatAmount(part.getUnitPrice()) + ".",
                RepairOrderHistoryVisibility.INTERNAL);

        return toPartResponse(part);
    }

    @Transactional
    public void deletePart(
            Integer businessId,
            Integer repairOrderId,
            Integer partId,
            String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "REPAIR_ORDER_UPDATE");
        RepairOrder repairOrder = findActiveRepairOrderForUpdate(businessId, repairOrderId, subject);
        assertOrderMutable(repairOrder);

        RepairOrderPart part = repairOrderPartRepository
                .findByIdAndRepairOrderIdAndRepairOrderBusinessIdAndDeletedAtIsNull(partId, repairOrderId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("repair_order.part.not_found"));

        BusinessUser actor = findActiveBusinessUserByUserId(businessId, parseUserId(subject));
        inventoryStockService.applyMovementByCode(
                businessId,
                repairOrder.getBranch().getId(),
                part.getInventoryItem().getId(),
                STOCK_MOVEMENT_RETURN,
                part.getQuantity(),
                actor.getId(),
                repairOrder.getId(),
                null,
                "Devolución por eliminación de repuesto en la orden " + repairOrder.getOrderNumber());
        part.setActive(false);
        part.setDeletedAt(LocalDateTime.now());
        saveHistory(
                repairOrder,
                repairOrder.getStatus(),
                actor.getUser(),
                "Repuesto retirado: " + part.getInventoryItem().getSku() + " x" + part.getQuantity()
                        + " (ID " + part.getId() + ").",
                RepairOrderHistoryVisibility.INTERNAL);
    }

    private Business findBusiness(Integer businessId) {
        return businessRepository.findByIdAndDeletedAtIsNull(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("business.not_found"));
    }

    private RepairOrder findActiveRepairOrder(Integer businessId, Integer repairOrderId, String subject) {
        RepairOrder repairOrder = repairOrderRepository.findByIdAndBusinessIdAndDeletedAtIsNull(repairOrderId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("repair_order.not_found"));
        businessBranchAccessGuard.findAccessibleBranch(businessId, repairOrder.getBranch().getId(), subject);
        return repairOrder;
    }

    private RepairOrder findActiveRepairOrderForUpdate(Integer businessId, Integer repairOrderId, String subject) {
        RepairOrder repairOrder = repairOrderRepository.findForUpdateByIdAndBusinessId(repairOrderId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("repair_order.not_found"));
        businessBranchAccessGuard.findAccessibleBranch(businessId, repairOrder.getBranch().getId(), subject);
        return repairOrder;
    }

    private AppUser findActiveUser(Integer userId) {
        return appUserRepository.findById(userId)
                .filter(value -> Boolean.TRUE.equals(value.getActive()) && value.getDeletedAt() == null)
                .orElseThrow(() -> new ResourceNotFoundException("user.not_found"));
    }

    private CustomerDevice findActiveCustomerDevice(Integer businessId, Integer customerDeviceId) {
        return customerDeviceRepository.findByIdAndBusinessIdAndDeletedAtIsNull(customerDeviceId, businessId)
                .filter(value -> Boolean.TRUE.equals(value.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("customer_device.not_found"));
    }

    private BusinessOffering findActiveBusinessService(Integer businessId, Integer businessServiceId) {
        return businessOfferingRepository.findByIdAndBusinessIdAndDeletedAtIsNull(businessServiceId, businessId)
                .filter(value -> Boolean.TRUE.equals(value.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("business_service.not_found"));
    }

    private BusinessUser findActiveBusinessUser(Integer businessId, Integer businessUserId) {
        return businessUserRepository.findByIdAndBusinessIdAndDeletedAtIsNull(businessUserId, businessId)
                .filter(value -> Boolean.TRUE.equals(value.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("business_user.not_found"));
    }

    private BusinessUser findActiveTechnician(Integer businessId, Integer businessUserId) {
        BusinessUser businessUser = findActiveBusinessUser(businessId, businessUserId);
        if (!"TECHNICIAN".equalsIgnoreCase(businessUser.getRole().getCode())) {
            throw new ConflictException("repair_order.assigned_user.must_be_technician");
        }
        return businessUser;
    }

    private BusinessUser findActiveBusinessUserByUserId(Integer businessId, Integer userId) {
        return businessUserRepository.findByBusinessIdAndUserIdAndActiveTrueAndDeletedAtIsNull(businessId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("business_user.not_found"));
    }

    private void validatePartUserBranch(RepairOrder repairOrder, BusinessUser usedBy) {
        Integer branchId = repairOrder.getBranch().getId();
        businessBranchAccessGuard.assertBusinessUserAssigned(
                repairOrder.getBusiness().getId(), branchId, usedBy.getId());
    }

    private Appointment findValidAppointment(
            Integer businessId,
            Integer appointmentId,
            AppUser customer,
            CustomerDevice customerDevice,
            BusinessOffering businessService,
            BusinessBranch branch) {
        if (appointmentId == null) {
            return null;
        }

        Appointment appointment = appointmentRepository.findByIdAndBusinessIdAndDeletedAtIsNull(appointmentId, businessId)
                .filter(value -> Boolean.TRUE.equals(value.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("appointment.not_found"));

        if (!appointment.getCustomer().getId().equals(customer.getId())) {
            throw new ConflictException("repair_order.appointment.customer_mismatch");
        }

        if (!appointment.getCustomerDevice().getId().equals(customerDevice.getId())) {
            throw new ConflictException("repair_order.appointment.device_mismatch");
        }

        if (!appointment.getBusinessService().getId().equals(businessService.getId())) {
            throw new ConflictException("repair_order.appointment.service_mismatch");
        }
        if (!appointment.getBranch().getId().equals(branch.getId())) {
            throw new ConflictException("repair_order.appointment.branch_mismatch");
        }

        return appointment;
    }

    private BusinessCatalogItem findInitialStatus(Integer businessId) {
        return businessCatalogLookupService.findActiveByCode(
                businessId,
                BusinessCatalogType.REPAIR_ORDER_STATUS,
                INITIAL_STATUS_CODE);
    }

    private BusinessCatalogItem findActiveRepairOrderStatus(Integer businessId, Integer statusId) {
        return businessCatalogLookupService.findActive(
                businessId,
                statusId,
                BusinessCatalogType.REPAIR_ORDER_STATUS);
    }

    private void validateCustomerDeviceOwnership(CustomerDevice customerDevice, AppUser customer) {
        if (!customerDevice.getCustomer().getId().equals(customer.getId())) {
            throw new ConflictException("repair_order.customer_device_mismatch");
        }
    }

    private void assertOrderMutable(RepairOrder repairOrder) {
        String statusCode = repairOrder.getStatus().getCode();
        if (DELIVERED_STATUS_CODE.equalsIgnoreCase(statusCode)
                || CANCELLED_STATUS_CODE.equalsIgnoreCase(statusCode)) {
            throw new ConflictException("repair_order.terminal.cannot_modify");
        }
        if (repairOrderCommissionRepository
                .findByRepairOrderIdAndBusinessIdAndDeletedAtIsNull(
                        repairOrder.getId(), repairOrder.getBusiness().getId())
                .isPresent()) {
            throw new ConflictException("commission_settlement.order.already_liquidated");
        }
    }

    private Integer nextSequenceForBranch(Integer businessId, Integer branchId) {
        Number currentSequence = jdbcTemplate.queryForObject(
                """
                        WITH sequence_row AS (
                            INSERT INTO repair_order_sequence (business_id, branch_id, next_sequence, created_at, updated_at)
                            VALUES (?, ?, 2, NOW(), NOW())
                            ON CONFLICT (business_id, branch_id)
                            DO UPDATE SET next_sequence = repair_order_sequence.next_sequence + 1,
                                          updated_at = NOW()
                            RETURNING next_sequence - 1 AS current_sequence
                        )
                        SELECT current_sequence FROM sequence_row
                        """,
                Number.class,
                businessId,
                branchId);

        if (currentSequence == null) {
            throw new IllegalStateException("Could not generate repair order sequence.");
        }

        return currentSequence.intValue();
    }

    private String formatOrderNumber(Integer businessId, Integer branchNumber, Integer sequence) {
        return "OS-%03d-%02d-%06d".formatted(businessId, branchNumber, sequence);
    }

    private String normalizeNullable(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }

    private String normalizeAttachmentDescription(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }

        String normalized = value.trim();
        if (normalized.indexOf('\0') >= 0) {
            throw new FileValidationException("repair_order.attachment.description.invalid");
        }
        if (normalized.length() > ATTACHMENT_DESCRIPTION_MAX_LENGTH) {
            throw new FileValidationException("repair_order.attachment.description.too_long");
        }
        return normalized;
    }

    private Pageable buildPageable(Integer page, Integer size, String sortBy, String sortDir) {
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;
        String resolvedSortBy = resolveSortBy(sortBy);

        return PageRequestFactory.of(page, size, Sort.by(direction, resolvedSortBy));
    }

    private String resolveSortBy(String sortBy) {
        if (!StringUtils.hasText(sortBy)) {
            return "receivedAt";
        }

        return switch (sortBy.trim()) {
            case "id" -> "id";
            case "orderNumber" -> "orderNumber";
            case "orderSequence" -> "orderSequence";
            case "receivedAt" -> "receivedAt";
            case "estimatedDeliveryAt" -> "estimatedDeliveryAt";
            case "createdAt" -> "createdAt";
            case "statusName" -> "status.name";
            case "customerName" -> "customer.name";
            default -> "receivedAt";
        };
    }

    private Specification<RepairOrder> buildFilterSpecification(
            Integer businessId,
            List<Integer> accessibleBranchIds,
            Integer branchId,
            String orderNumber,
            Integer orderSequence,
            Integer statusId,
            Integer customerId,
            String customer,
            LocalDateTime receivedFrom,
            LocalDateTime receivedTo) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("business").get("id"), businessId));
            predicates.add(criteriaBuilder.isNull(root.get("deletedAt")));
            predicates.add(root.get("branch").get("id").in(accessibleBranchIds));

            if (branchId != null) {
                predicates.add(criteriaBuilder.equal(root.get("branch").get("id"), branchId));
            }

            if (StringUtils.hasText(orderNumber)) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("orderNumber")),
                        "%" + orderNumber.trim().toLowerCase() + "%"));
            }

            if (orderSequence != null) {
                predicates.add(criteriaBuilder.equal(root.get("orderSequence"), orderSequence));
            }

            if (statusId != null) {
                predicates.add(criteriaBuilder.equal(root.get("status").get("id"), statusId));
            }

            if (customerId != null) {
                predicates.add(criteriaBuilder.equal(root.get("customer").get("id"), customerId));
            }

            if (StringUtils.hasText(customer)) {
                String term = "%" + customer.trim().toLowerCase() + "%";
                Predicate byName = criteriaBuilder.like(criteriaBuilder.lower(root.get("customer").get("name")), term);
                Predicate bySurname = criteriaBuilder.like(criteriaBuilder.lower(root.get("customer").get("surname")),
                        term);
                Predicate byEmail = criteriaBuilder.like(criteriaBuilder.lower(root.get("customer").get("email")), term);
                Predicate byDocumentNumber = criteriaBuilder
                        .like(criteriaBuilder.lower(root.get("customer").get("documentNumber")), term);

                predicates.add(criteriaBuilder.or(byName, bySurname, byEmail, byDocumentNumber));
            }

            if (receivedFrom != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("receivedAt"), receivedFrom));
            }

            if (receivedTo != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("receivedAt"), receivedTo));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

    private RepairOrderHistoryResponse toHistoryResponse(RepairOrderHistory entity) {
        return new RepairOrderHistoryResponse(
                entity.getId(),
                entity.getRepairOrder().getId(),
                entity.getStatus().getId(),
                entity.getStatus().getCode(),
                entity.getStatus().getName(),
                entity.getCreatedByUser().getId(),
                entity.getCreatedByUser().getName() + " " + entity.getCreatedByUser().getSurname(),
                entity.getCreatedByUser().getEmail(),
                entity.getComment(),
                entity.getVisibility(),
                entity.getCreatedAt());
    }

    private RepairOrderAttachmentResponse toAttachmentResponse(RepairOrderAttachment entity) {
        return new RepairOrderAttachmentResponse(
                entity.getId(),
                entity.getRepairOrder().getId(),
                entity.getUploadedByBusinessUser().getId(),
                entity.getUploadedByBusinessUser().getUser().getEmail(),
                entity.getUploadedByBusinessUser().getUser().getName() + " "
                        + entity.getUploadedByBusinessUser().getUser().getSurname(),
                minioStorageService.resolveRepairEvidenceUrl(
                        entity.getBucketName(),
                        entity.getObjectKey()),
                entity.getOriginalFileName(),
                entity.getContentType(),
                entity.getFileSize(),
                entity.getChecksumSha256(),
                entity.getDescription(),
                entity.getCreatedAt());
    }

    private RepairOrderPartResponse toPartResponse(RepairOrderPart entity) {
        return new RepairOrderPartResponse(
                entity.getId(),
                entity.getRepairOrder().getId(),
                entity.getInventoryItem().getId(),
                entity.getInventoryItem().getName(),
                entity.getInventoryItem().getSku(),
                entity.getInventoryItem().getItemCategory().getCode(),
                entity.getInventoryItem().getItemCategory().getName(),
                entity.getQuantity(),
                entity.getUnitPrice(),
                entity.getTotalPrice(),
                entity.getUsedByBusinessUser().getId(),
                entity.getUsedByBusinessUser().getUser().getEmail(),
                entity.getUsedByBusinessUser().getUser().getName() + " "
                        + entity.getUsedByBusinessUser().getUser().getSurname(),
                entity.getCreatedAt());
    }

    private java.math.BigDecimal calculateTotalPrice(Integer quantity, java.math.BigDecimal unitPrice) {
        return unitPrice
                .setScale(2, RoundingMode.HALF_UP)
                .multiply(java.math.BigDecimal.valueOf(quantity.longValue()))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private String formatAmount(java.math.BigDecimal amount) {
        return amount == null ? "sin valor" : amount.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private String describeBusinessUser(BusinessUser businessUser) {
        if (businessUser == null) {
            return "sin asignación";
        }
        return businessUser.getUser().getName() + " " + businessUser.getUser().getSurname()
                + " (ID " + businessUser.getId() + ")";
    }

    private void saveHistory(
            RepairOrder repairOrder,
            BusinessCatalogItem status,
            AppUser createdByUser,
            String comment,
            RepairOrderHistoryVisibility visibility) {
        RepairOrderHistory history = new RepairOrderHistory();
        history.setRepairOrder(repairOrder);
        history.setStatus(status);
        history.setCreatedByUser(createdByUser);
        history.setComment(comment);
        history.setVisibility(visibility != null ? visibility : RepairOrderHistoryVisibility.INTERNAL);
        history.setActive(true);
        history.setDeletedAt(null);
        repairOrderHistoryRepository.save(history);
    }

    private boolean isTerminalStatus(String statusCode) {
        return RepairOrderStateMachine.isTerminal(statusCode);
    }

    private Integer parseUserId(String subject) {
        try {
            return Integer.valueOf(subject);
        } catch (NumberFormatException exception) {
            throw new BadCredentialsException("auth.token.invalid");
        }
    }
}
