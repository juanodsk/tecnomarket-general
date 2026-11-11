package com.tecmarket.backend.repairorder.service;

import com.tecmarket.backend.business.dto.*;
import com.tecmarket.backend.business.entity.*;
import com.tecmarket.backend.business.repository.*;
import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.tecmarket.backend.repairorder.dto.CommissionSettlementResponse;
import com.tecmarket.backend.repairorder.dto.CreateCommissionSettlementRequest;
import com.tecmarket.backend.repairorder.dto.EligibleCommissionOrderResponse;
import com.tecmarket.backend.repairorder.dto.TechnicianCommissionSummaryResponse;
import com.tecmarket.backend.repairorder.dto.UpdateCommissionSettlementStatusRequest;
import com.tecmarket.backend.repairorder.entity.CommissionSettlement;
import com.tecmarket.backend.repairorder.entity.CommissionSettlementStatus;
import com.tecmarket.backend.repairorder.entity.RepairOrder;
import com.tecmarket.backend.repairorder.entity.RepairOrderCommission;
import com.tecmarket.backend.repairorder.entity.RepairOrderCommissionStatus;
import com.tecmarket.backend.repairorder.repository.CommissionSettlementRepository;
import com.tecmarket.backend.repairorder.repository.RepairOrderCommissionRepository;
import com.tecmarket.backend.repairorder.repository.RepairOrderPartRepository;
import com.tecmarket.backend.repairorder.repository.RepairOrderRepository;
@Service
public class CommissionSettlementService {

    private static final String TECHNICIAN_ROLE_CODE = "TECHNICIAN";
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100.00");

    private final CommissionSettlementRepository settlementRepository;
    private final RepairOrderCommissionRepository commissionRepository;
    private final RepairOrderRepository repairOrderRepository;
    private final RepairOrderPartRepository repairOrderPartRepository;
    private final BusinessUserRepository businessUserRepository;
    private final BusinessAccessGuard businessAccessGuard;
    private final RepairOrderCommissionService commissionService;

    public CommissionSettlementService(
            CommissionSettlementRepository settlementRepository,
            RepairOrderCommissionRepository commissionRepository,
            RepairOrderRepository repairOrderRepository,
            RepairOrderPartRepository repairOrderPartRepository,
            BusinessUserRepository businessUserRepository,
            BusinessAccessGuard businessAccessGuard,
            RepairOrderCommissionService commissionService) {
        this.settlementRepository = settlementRepository;
        this.commissionRepository = commissionRepository;
        this.repairOrderRepository = repairOrderRepository;
        this.repairOrderPartRepository = repairOrderPartRepository;
        this.businessUserRepository = businessUserRepository;
        this.businessAccessGuard = businessAccessGuard;
        this.commissionService = commissionService;
    }

    @Transactional(readOnly = true)
    public List<TechnicianCommissionSummaryResponse> findTechnicianSummaries(
            Integer businessId,
            LocalDate periodStart,
            LocalDate periodEnd,
            String subject) {
        assertCanView(businessId, subject);
        validatePeriod(periodStart, periodEnd);

        return businessUserRepository.findActiveByBusinessAndRoleCode(businessId, TECHNICIAN_ROLE_CODE)
                .stream()
                .map(technician -> toTechnicianSummary(
                        technician,
                        findEligibleOrders(businessId, technician, periodStart, periodEnd)))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EligibleCommissionOrderResponse> findEligibleOrders(
            Integer businessId,
            Integer businessUserId,
            LocalDate periodStart,
            LocalDate periodEnd,
            String subject) {
        assertCanView(businessId, subject);
        validatePeriod(periodStart, periodEnd);
        BusinessUser technician = findTechnician(businessId, businessUserId);

        return findEligibleOrders(businessId, technician, periodStart, periodEnd)
                .stream()
                .map(order -> toEligibleResponse(order, technician))
                .toList();
    }

    @Transactional
    public CommissionSettlementResponse create(
            Integer businessId,
            CreateCommissionSettlementRequest request,
            String subject) {
        assertCanManage(businessId, subject);
        validatePeriod(request.periodStart(), request.periodEnd());
        BusinessUser technician = findTechnician(businessId, request.businessUserId());

        Set<Integer> requestedIds = new LinkedHashSet<>(request.repairOrderIds());
        if (requestedIds.size() != request.repairOrderIds().size()) {
            throw new ConflictException("commission_settlement.orders.duplicated");
        }

        Map<Integer, RepairOrder> eligibleById = findEligibleOrders(
                businessId,
                technician,
                request.periodStart(),
                request.periodEnd())
                .stream()
                .collect(Collectors.toMap(RepairOrder::getId, Function.identity()));

        if (!eligibleById.keySet().containsAll(requestedIds)) {
            throw new ConflictException("commission_settlement.orders.invalid");
        }

        CommissionSettlement settlement = new CommissionSettlement();
        settlement.setBusiness(technician.getBusiness());
        settlement.setBusinessUser(technician);
        settlement.setPeriodStart(request.periodStart());
        settlement.setPeriodEnd(request.periodEnd());
        settlement.setOrdersCount(requestedIds.size());
        settlement.setTotalAmount(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        settlement.setStatus(CommissionSettlementStatus.DRAFT);
        settlement.setNotes(normalizeNullable(request.notes()));
        settlement.setGeneratedAt(LocalDateTime.now());
        settlement.setActive(true);
        settlementRepository.saveAndFlush(settlement);
        settlement.setSettlementCode("LIQ-%03d-%06d".formatted(businessId, settlement.getId()));

        List<RepairOrderCommission> commissions = requestedIds.stream()
                .map(eligibleById::get)
                .map(order -> createCommissionSnapshot(order, technician, settlement))
                .toList();
        commissionRepository.saveAll(commissions);

        BigDecimal totalAmount = commissions.stream()
                .map(RepairOrderCommission::getCommissionAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        settlement.setTotalAmount(money(totalAmount));

        return toResponse(settlement, commissions);
    }

    @Transactional(readOnly = true)
    public List<CommissionSettlementResponse> findAll(
            Integer businessId,
            Integer businessUserId,
            CommissionSettlementStatus status,
            String subject) {
        assertCanView(businessId, subject);

        List<CommissionSettlement> settlements;
        if (businessUserId != null && status != null) {
            settlements = settlementRepository
                    .findByBusinessIdAndBusinessUserIdAndStatusAndDeletedAtIsNullOrderByGeneratedAtDescIdDesc(
                            businessId, businessUserId, status);
        } else if (businessUserId != null) {
            settlements = settlementRepository
                    .findByBusinessIdAndBusinessUserIdAndDeletedAtIsNullOrderByGeneratedAtDescIdDesc(
                            businessId, businessUserId);
        } else if (status != null) {
            settlements = settlementRepository
                    .findByBusinessIdAndStatusAndDeletedAtIsNullOrderByGeneratedAtDescIdDesc(businessId, status);
        } else {
            settlements = settlementRepository
                    .findByBusinessIdAndDeletedAtIsNullOrderByGeneratedAtDescIdDesc(businessId);
        }

        return settlements.stream()
                .map(settlement -> toResponse(
                        settlement,
                        commissionRepository.findBySettlementIdAndBusinessIdAndDeletedAtIsNullOrderByIdAsc(
                                settlement.getId(), businessId)))
                .toList();
    }

    @Transactional(readOnly = true)
    public CommissionSettlementResponse findById(Integer businessId, Integer settlementId, String subject) {
        assertCanView(businessId, subject);
        CommissionSettlement settlement = findSettlement(businessId, settlementId);
        return toResponse(
                settlement,
                commissionRepository.findBySettlementIdAndBusinessIdAndDeletedAtIsNullOrderByIdAsc(
                        settlementId, businessId));
    }

    @Transactional
    public CommissionSettlementResponse updateStatus(
            Integer businessId,
            Integer settlementId,
            UpdateCommissionSettlementStatusRequest request,
            String subject) {
        assertCanManage(businessId, subject);
        CommissionSettlement settlement = findSettlement(businessId, settlementId);
        validateTransition(settlement.getStatus(), request.status());

        LocalDateTime now = LocalDateTime.now();
        settlement.setStatus(request.status());
        if (request.status() == CommissionSettlementStatus.APPROVED) {
            settlement.setApprovedAt(now);
        } else if (request.status() == CommissionSettlementStatus.PAID) {
            settlement.setPaidAt(now);
        }

        List<RepairOrderCommission> commissions = commissionRepository
                .findBySettlementIdAndBusinessIdAndDeletedAtIsNullOrderByIdAsc(settlementId, businessId);
        for (RepairOrderCommission commission : commissions) {
            if (request.status() == CommissionSettlementStatus.APPROVED) {
                commission.setStatus(RepairOrderCommissionStatus.APPROVED);
                commission.setApprovedAt(now);
            } else if (request.status() == CommissionSettlementStatus.PAID) {
                commission.setStatus(RepairOrderCommissionStatus.PAID);
                commission.setPaidAt(now);
            }
        }

        return toResponse(settlement, commissions);
    }

    private List<RepairOrder> findEligibleOrders(
            Integer businessId,
            BusinessUser technician,
            LocalDate periodStart,
            LocalDate periodEnd) {
        return repairOrderRepository.findEligibleForCommissionSettlement(
                businessId,
                technician.getId(),
                periodStart.atStartOfDay(),
                periodEnd.plusDays(1).atStartOfDay());
    }

    private RepairOrderCommission createCommissionSnapshot(
            RepairOrder order,
            BusinessUser technician,
            CommissionSettlement settlement) {
        EligibleCommissionOrderResponse calculation = toEligibleResponse(order, technician);

        RepairOrderCommission commission = new RepairOrderCommission();
        commission.setBusiness(order.getBusiness());
        commission.setRepairOrder(order);
        commission.setBusinessUser(technician);
        commission.setSettlement(settlement);
        commission.setOrderTotal(calculation.orderTotal());
        commission.setPartsTotal(calculation.partsTotal());
        commission.setCommissionBase(calculation.commissionBase());
        commission.setCommissionPercentage(calculation.commissionPercentage());
        commission.setCommissionAmount(calculation.commissionAmount());
        commission.setStatus(RepairOrderCommissionStatus.PENDING);
        commission.setGeneratedAt(LocalDateTime.now());
        commission.setActive(true);
        return commission;
    }

    private EligibleCommissionOrderResponse toEligibleResponse(RepairOrder order, BusinessUser technician) {
        BigDecimal orderTotal = money(order.getFinalPrice());
        BigDecimal partsTotal = money(repairOrderPartRepository.sumActivePartsTotal(
                order.getId(), order.getBusiness().getId()));
        if (orderTotal.compareTo(partsTotal) < 0) {
            throw new ConflictException("repair_order.final_price.less_than_parts");
        }

        BigDecimal commissionBase = money(orderTotal.subtract(partsTotal));
        BigDecimal commissionPercentage = percentage(technician.getCommissionPercentage());
        BigDecimal commissionAmount = money(
                commissionBase.multiply(commissionPercentage).divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP));

        return new EligibleCommissionOrderResponse(
                order.getId(),
                order.getOrderNumber(),
                technician.getId(),
                buildFullName(technician),
                order.getReceivedAt(),
                orderTotal,
                partsTotal,
                commissionBase,
                commissionPercentage,
                commissionAmount);
    }

    private TechnicianCommissionSummaryResponse toTechnicianSummary(
            BusinessUser technician,
            List<RepairOrder> eligibleOrders) {
        List<EligibleCommissionOrderResponse> calculations = eligibleOrders.stream()
                .map(order -> toEligibleResponse(order, technician))
                .toList();
        BigDecimal totalCommissionBase = calculations.stream()
                .map(EligibleCommissionOrderResponse::commissionBase)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal estimatedCommissionAmount = calculations.stream()
                .map(EligibleCommissionOrderResponse::commissionAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new TechnicianCommissionSummaryResponse(
                technician.getId(),
                technician.getUser().getId(),
                buildFullName(technician),
                technician.getUser().getEmail(),
                percentage(technician.getCommissionPercentage()),
                calculations.size(),
                money(totalCommissionBase),
                money(estimatedCommissionAmount));
    }

    private CommissionSettlementResponse toResponse(
            CommissionSettlement settlement,
            List<RepairOrderCommission> commissions) {
        BusinessUser technician = settlement.getBusinessUser();
        return new CommissionSettlementResponse(
                settlement.getId(),
                settlement.getSettlementCode(),
                settlement.getBusiness().getId(),
                technician.getId(),
                technician.getUser().getId(),
                buildFullName(technician),
                technician.getUser().getEmail(),
                settlement.getPeriodStart(),
                settlement.getPeriodEnd(),
                settlement.getOrdersCount(),
                settlement.getTotalAmount(),
                settlement.getStatus(),
                settlement.getNotes(),
                settlement.getGeneratedAt(),
                settlement.getApprovedAt(),
                settlement.getPaidAt(),
                commissions.stream().map(commissionService::toResponse).toList());
    }

    private BusinessUser findTechnician(Integer businessId, Integer businessUserId) {
        BusinessUser technician = businessUserRepository
                .findByIdAndBusinessIdAndDeletedAtIsNull(businessUserId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("business_user.not_found"));
        if (!TECHNICIAN_ROLE_CODE.equalsIgnoreCase(technician.getRole().getCode())) {
            throw new ConflictException("repair_order.assigned_user.must_be_technician");
        }
        return technician;
    }

    private CommissionSettlement findSettlement(Integer businessId, Integer settlementId) {
        return settlementRepository.findByIdAndBusinessIdAndDeletedAtIsNull(settlementId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("commission_settlement.not_found"));
    }

    private void validatePeriod(LocalDate periodStart, LocalDate periodEnd) {
        if (periodEnd.isBefore(periodStart)) {
            throw new ConflictException("commission_settlement.period.invalid");
        }
    }

    private void validateTransition(CommissionSettlementStatus current, CommissionSettlementStatus target) {
        if (current == target) {
            return;
        }
        boolean allowed = switch (current) {
            case DRAFT -> target == CommissionSettlementStatus.APPROVED;
            case APPROVED -> target == CommissionSettlementStatus.PAID;
            case PAID -> false;
        };
        if (!allowed) {
            throw new ConflictException("commission_settlement.status.invalid_transition");
        }
    }

    private void assertCanView(Integer businessId, String subject) {
        businessAccessGuard.assertHasBusinessPermission(
                businessId,
                subject,
                "BUSINESS_COMMISSION_VIEW",
                "BUSINESS_COMMISSION_MANAGE");
    }

    private void assertCanManage(Integer businessId, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "BUSINESS_COMMISSION_MANAGE");
    }

    private String buildFullName(BusinessUser technician) {
        String name = technician.getUser().getName() == null ? "" : technician.getUser().getName().trim();
        String surname = technician.getUser().getSurname() == null ? "" : technician.getUser().getSurname().trim();
        String fullName = (name + " " + surname).trim();
        return fullName.isBlank() ? technician.getUser().getEmail() : fullName;
    }

    private String normalizeNullable(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private BigDecimal money(BigDecimal value) {
        return (value == null ? BigDecimal.ZERO : value).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal percentage(BigDecimal value) {
        return (value == null ? BigDecimal.ZERO : value).setScale(2, RoundingMode.HALF_UP);
    }
}
