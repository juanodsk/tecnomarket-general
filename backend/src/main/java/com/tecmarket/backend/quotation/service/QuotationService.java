package com.tecmarket.backend.quotation.service;

import com.tecmarket.backend.quotation.dto.CreateQuotationItemRequest;
import com.tecmarket.backend.quotation.dto.CreateQuotationRequest;
import com.tecmarket.backend.quotation.dto.QuotationItemResponse;
import com.tecmarket.backend.quotation.dto.QuotationResponse;
import com.tecmarket.backend.quotation.dto.UpdateQuotationStatusRequest;
import com.tecmarket.backend.business.entity.BusinessCatalogItem;
import com.tecmarket.backend.business.entity.BusinessCatalogType;
import com.tecmarket.backend.quotation.entity.Quotation;
import com.tecmarket.backend.quotation.entity.QuotationItem;
import com.tecmarket.backend.repairorder.entity.RepairOrder;
import com.tecmarket.backend.quotation.repository.QuotationItemRepository;
import com.tecmarket.backend.quotation.repository.QuotationRepository;
import com.tecmarket.backend.repairorder.repository.RepairOrderRepository;
import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.business.security.BusinessBranchAccessGuard;
import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.common.pagination.PageRequestFactory;
import com.tecmarket.backend.common.pagination.PageResponse;
import com.tecmarket.backend.repairorder.service.RepairOrderStateMachine;
import com.tecmarket.backend.repairorder.service.RepairOrderTimelineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.tecmarket.backend.business.service.BusinessCatalogLookupService;
@Service
public class QuotationService {

    private static final String INITIAL_QUOTATION_STATUS_CODE = "DRAFT";

    private final QuotationRepository quotationRepository;
    private final QuotationItemRepository quotationItemRepository;
    private final RepairOrderRepository repairOrderRepository;
    private final BusinessCatalogLookupService businessCatalogLookupService;
    private final BusinessAccessGuard businessAccessGuard;
    private final BusinessBranchAccessGuard businessBranchAccessGuard;
    private final RepairOrderTimelineService repairOrderTimelineService;

    public QuotationService(
            QuotationRepository quotationRepository,
            QuotationItemRepository quotationItemRepository,
            RepairOrderRepository repairOrderRepository,
            BusinessCatalogLookupService businessCatalogLookupService,
            BusinessAccessGuard businessAccessGuard,
            BusinessBranchAccessGuard businessBranchAccessGuard,
            RepairOrderTimelineService repairOrderTimelineService) {
        this.quotationRepository = quotationRepository;
        this.quotationItemRepository = quotationItemRepository;
        this.repairOrderRepository = repairOrderRepository;
        this.businessCatalogLookupService = businessCatalogLookupService;
        this.businessAccessGuard = businessAccessGuard;
        this.businessBranchAccessGuard = businessBranchAccessGuard;
        this.repairOrderTimelineService = repairOrderTimelineService;
    }

    @Transactional
    public QuotationResponse create(Integer businessId, CreateQuotationRequest request, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "QUOTATION_CREATE");
        RepairOrder repairOrder = findActiveRepairOrderForUpdate(businessId, request.repairOrderId());
        assertBranchAccess(repairOrder, subject);
        assertRepairOrderMutable(repairOrder);
        if (request.expiresAt() != null && !request.expiresAt().isAfter(LocalDateTime.now())) {
            throw new ConflictException("quotation.expiration.invalid");
        }
        BusinessCatalogItem initialStatus = findInitialStatus(businessId);

        Quotation quotation = new Quotation();
        quotation.setRepairOrder(repairOrder);
        quotation.setBusiness(repairOrder.getBusiness());
        quotation.setCustomer(repairOrder.getCustomer());
        quotation.setQuotationStatus(initialStatus);
        quotation.setNotes(normalizeNullable(request.notes()));
        quotation.setExpiresAt(request.expiresAt());
        quotation.setTotalAmount(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        quotation.setActive(true);
        quotation.setDeletedAt(null);

        Quotation savedQuotation = quotationRepository.save(quotation);
        List<QuotationItem> items = request.items().stream()
                .map(value -> buildItem(savedQuotation, value))
                .toList();
        quotationItemRepository.saveAll(items);

        BigDecimal total = items.stream()
                .map(QuotationItem::getTotalPrice)
                .reduce(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        savedQuotation.setTotalAmount(total);
        repairOrderTimelineService.recordInternalEvent(
                repairOrder,
                subject,
                "Cotización creada (ID " + savedQuotation.getId() + ") por " + total.toPlainString()
                        + " con estado " + initialStatus.getCode() + ".");

        return toResponse(savedQuotation, items);
    }

    @Transactional(readOnly = true)
    public QuotationResponse findById(Integer businessId, Integer quotationId, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "QUOTATION_VIEW");
        Quotation quotation = findActiveQuotation(businessId, quotationId);
        assertBranchAccess(quotation.getRepairOrder(), subject);
        List<QuotationItem> items = quotationItemRepository
                .findByQuotationIdAndQuotationBusinessIdAndDeletedAtIsNullOrderByCreatedAtAscIdAsc(quotationId, businessId);
        return toResponse(quotation, items);
    }

    @Transactional(readOnly = true)
    public PageResponse<QuotationResponse> findAllByRepairOrder(
            Integer businessId,
            Integer repairOrderId,
            Integer page,
            Integer size,
            String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "QUOTATION_VIEW");
        RepairOrder repairOrder = findActiveRepairOrder(businessId, repairOrderId);
        assertBranchAccess(repairOrder, subject);

        var quotations = quotationRepository
                .findByBusinessIdAndRepairOrderIdAndActiveTrueAndDeletedAtIsNullOrderByCreatedAtDescIdDesc(
                        businessId,
                        repairOrderId,
                        PageRequestFactory.of(page, size));
        List<Integer> quotationIds = quotations.getContent().stream()
                .map(Quotation::getId)
                .toList();
        Map<Integer, List<QuotationItem>> itemsByQuotationId = quotationIds.isEmpty()
                ? Map.of()
                : quotationItemRepository.findActiveByQuotationIdsAndBusinessId(quotationIds, businessId).stream()
                        .collect(Collectors.groupingBy(item -> item.getQuotation().getId()));

        return PageResponse.from(quotations.map(quotation -> toResponse(
                quotation,
                itemsByQuotationId.getOrDefault(quotation.getId(), List.of()))));
    }

    @Transactional
    public QuotationResponse updateStatus(
            Integer businessId,
            Integer quotationId,
            UpdateQuotationStatusRequest request,
            String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "QUOTATION_UPDATE");
        Quotation quotation = findActiveQuotationForUpdate(businessId, quotationId);
        RepairOrder repairOrder = findActiveRepairOrderForUpdate(businessId, quotation.getRepairOrder().getId());
        assertBranchAccess(quotation.getRepairOrder(), subject);
        assertRepairOrderMutable(repairOrder);
        BusinessCatalogItem status = findActiveStatus(businessId, request.statusId());

        String currentCode = quotation.getQuotationStatus().getCode();
        String targetCode = status.getCode();
        if (!QuotationStateMachine.isTransitionAllowed(currentCode, targetCode)) {
            throw new ConflictException("quotation.status.invalid_transition");
        }
        validateExpirationTransition(quotation, targetCode);

        quotation.setQuotationStatus(status);
        if (StringUtils.hasText(request.notes())) {
            quotation.setNotes(request.notes().trim());
        }

        List<QuotationItem> items = quotationItemRepository
                .findByQuotationIdAndQuotationBusinessIdAndDeletedAtIsNullOrderByCreatedAtAscIdAsc(quotationId, businessId);
        repairOrderTimelineService.recordInternalEvent(
                repairOrder,
                subject,
                "Cotización " + quotationId + " cambió de " + currentCode + " a " + targetCode + ".");
        return toResponse(quotation, items);
    }

    private RepairOrder findActiveRepairOrder(Integer businessId, Integer repairOrderId) {
        return repairOrderRepository.findByIdAndBusinessIdAndDeletedAtIsNull(repairOrderId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("repair_order.not_found"));
    }

    private RepairOrder findActiveRepairOrderForUpdate(Integer businessId, Integer repairOrderId) {
        return repairOrderRepository.findForUpdateByIdAndBusinessId(repairOrderId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("repair_order.not_found"));
    }

    private Quotation findActiveQuotation(Integer businessId, Integer quotationId) {
        return quotationRepository.findByIdAndBusinessIdAndDeletedAtIsNull(quotationId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("quotation.not_found"));
    }

    private Quotation findActiveQuotationForUpdate(Integer businessId, Integer quotationId) {
        return quotationRepository.findForUpdateByIdAndBusinessId(quotationId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("quotation.not_found"));
    }

    private void assertRepairOrderMutable(RepairOrder repairOrder) {
        if (RepairOrderStateMachine.isTerminal(repairOrder.getStatus().getCode())) {
            throw new ConflictException("repair_order.terminal.cannot_modify");
        }
    }

    private void validateExpirationTransition(Quotation quotation, String targetCode) {
        LocalDateTime expiresAt = quotation.getExpiresAt();
        LocalDateTime now = LocalDateTime.now();
        if ("EXPIRED".equalsIgnoreCase(targetCode)) {
            if (expiresAt == null || expiresAt.isAfter(now)) {
                throw new ConflictException("quotation.expiration.not_reached");
            }
            return;
        }
        if (expiresAt != null && !expiresAt.isAfter(now)
                && !"CANCELLED".equalsIgnoreCase(targetCode)) {
            throw new ConflictException("quotation.expired");
        }
    }

    private void assertBranchAccess(RepairOrder repairOrder, String subject) {
        businessBranchAccessGuard.findAccessibleBranch(
                repairOrder.getBusiness().getId(),
                repairOrder.getBranch().getId(),
                subject);
    }

    private BusinessCatalogItem findInitialStatus(Integer businessId) {
        return businessCatalogLookupService.findActiveByCode(
                businessId,
                BusinessCatalogType.QUOTATION_STATUS,
                INITIAL_QUOTATION_STATUS_CODE);
    }

    private BusinessCatalogItem findActiveStatus(Integer businessId, Integer statusId) {
        return businessCatalogLookupService.findActive(
                businessId,
                statusId,
                BusinessCatalogType.QUOTATION_STATUS);
    }

    private QuotationItem buildItem(Quotation quotation, CreateQuotationItemRequest request) {
        BigDecimal unitPrice = request.unitPrice().setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(request.quantity()))
                .setScale(2, RoundingMode.HALF_UP);

        QuotationItem item = new QuotationItem();
        item.setQuotation(quotation);
        item.setDescription(request.description().trim());
        item.setQuantity(request.quantity());
        item.setUnitPrice(unitPrice);
        item.setTotalPrice(totalPrice);
        item.setActive(true);
        item.setDeletedAt(null);
        return item;
    }

    private QuotationResponse toResponse(Quotation quotation, List<QuotationItem> items) {
        List<QuotationItemResponse> itemResponses = items.stream()
                .map(this::toItemResponse)
                .toList();

        return new QuotationResponse(
                quotation.getId(),
                quotation.getBusiness().getId(),
                quotation.getRepairOrder().getId(),
                quotation.getCustomer().getId(),
                quotation.getCustomer().getName() + " " + quotation.getCustomer().getSurname(),
                quotation.getQuotationStatus().getCode(),
                quotation.getQuotationStatus().getName(),
                quotation.getTotalAmount(),
                quotation.getNotes(),
                quotation.getExpiresAt(),
                quotation.getCreatedAt(),
                itemResponses);
    }

    private QuotationItemResponse toItemResponse(QuotationItem item) {
        return new QuotationItemResponse(
                item.getId(),
                item.getDescription(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getTotalPrice());
    }

    private String normalizeNullable(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }
}
