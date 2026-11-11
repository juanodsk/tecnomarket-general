package com.tecmarket.backend.repairorder.service;

import com.tecmarket.backend.repairorder.dto.QualityChecklistItemRequest;
import com.tecmarket.backend.repairorder.dto.QualityChecklistItemResponse;
import com.tecmarket.backend.repairorder.dto.QualityChecklistResponse;
import com.tecmarket.backend.repairorder.dto.UpsertQualityChecklistRequest;
import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.repairorder.entity.BusinessQualityTest;
import com.tecmarket.backend.repairorder.entity.RepairOrder;
import com.tecmarket.backend.repairorder.entity.RepairOrderQualityCheck;
import com.tecmarket.backend.repairorder.repository.BusinessQualityTestRepository;
import com.tecmarket.backend.business.repository.BusinessRepository;
import com.tecmarket.backend.repairorder.repository.RepairOrderRepository;
import com.tecmarket.backend.repairorder.repository.RepairOrderQualityCheckRepository;
import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.user.entity.AppUser;
import com.tecmarket.backend.user.repository.AppUserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class QualityChecklistService {

    private final BusinessRepository businessRepository;
    private final BusinessQualityTestRepository businessQualityTestRepository;
    private final RepairOrderRepository repairOrderRepository;
    private final RepairOrderQualityCheckRepository repairOrderQualityCheckRepository;
    private final AppUserRepository appUserRepository;
    private final BusinessAccessGuard businessAccessGuard;

    public QualityChecklistService(
            BusinessRepository businessRepository,
            BusinessQualityTestRepository businessQualityTestRepository,
            RepairOrderRepository repairOrderRepository,
            RepairOrderQualityCheckRepository repairOrderQualityCheckRepository,
            AppUserRepository appUserRepository,
            BusinessAccessGuard businessAccessGuard) {
        this.businessRepository = businessRepository;
        this.businessQualityTestRepository = businessQualityTestRepository;
        this.repairOrderRepository = repairOrderRepository;
        this.repairOrderQualityCheckRepository = repairOrderQualityCheckRepository;
        this.appUserRepository = appUserRepository;
        this.businessAccessGuard = businessAccessGuard;
    }

    @Transactional(readOnly = true)
    public QualityChecklistResponse findByTicket(Integer businessId, String ticketCode, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "BUSINESS_QUALITY_CHECK_VIEW");
        String normalizedTicketCode = normalizeTicketCode(ticketCode);
        List<RepairOrderQualityCheck> checks = repairOrderQualityCheckRepository
                .findByBusinessIdAndIntakeTicketCodeAndDeletedAtIsNullOrderByIdAsc(businessId, normalizedTicketCode);

        if (checks.isEmpty()) {
            throw new ResourceNotFoundException("quality_checklist.not_found");
        }

        return toResponse(businessId, normalizedTicketCode, checks);
    }

    @Transactional(readOnly = true)
    public QualityChecklistResponse findByRepairOrder(Integer businessId, Integer repairOrderId, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "BUSINESS_QUALITY_CHECK_VIEW");
        RepairOrder repairOrder = findActiveRepairOrder(businessId, repairOrderId);

        List<RepairOrderQualityCheck> checks = repairOrderQualityCheckRepository
                .findByBusinessIdAndRepairOrderIdAndDeletedAtIsNullOrderByIdAsc(businessId, repairOrderId);
        if (checks.isEmpty()) {
            throw new ResourceNotFoundException("quality_checklist.not_found");
        }

        return toResponse(businessId, repairOrder.getOrderNumber(), checks);
    }

    @Transactional
    public QualityChecklistResponse upsertByTicket(
            Integer businessId,
            String ticketCode,
            UpsertQualityChecklistRequest request,
            String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "BUSINESS_QUALITY_CHECK_UPDATE");
        Business business = businessRepository.findByIdAndDeletedAtIsNull(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("business.not_found"));

        AppUser checker = findActiveUserBySubject(subject);
        String normalizedTicketCode = normalizeTicketCode(ticketCode);
        Integer expectedDeviceTypeId = null;
        if (request.repairOrderId() != null) {
            RepairOrder repairOrder = findActiveRepairOrder(businessId, request.repairOrderId());
            expectedDeviceTypeId = repairOrder.getCustomerDevice().getDeviceType().getId();
            normalizedTicketCode = repairOrder.getOrderNumber();
        }

        return upsertInternal(
                businessId,
                normalizedTicketCode,
                request.repairOrderId(),
                expectedDeviceTypeId,
                request,
                checker,
                business);
    }

    @Transactional
    public QualityChecklistResponse upsertByRepairOrder(
            Integer businessId,
            Integer repairOrderId,
            UpsertQualityChecklistRequest request,
            String subject) {
        if (request.repairOrderId() != null && !repairOrderId.equals(request.repairOrderId())) {
            throw new ConflictException("quality_checklist.repair_order.mismatch");
        }

        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "BUSINESS_QUALITY_CHECK_UPDATE");
        Business business = businessRepository.findByIdAndDeletedAtIsNull(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("business.not_found"));

        AppUser checker = findActiveUserBySubject(subject);
        RepairOrder repairOrder = findActiveRepairOrder(businessId, repairOrderId);

        return upsertInternal(
                businessId,
                repairOrder.getOrderNumber(),
                repairOrderId,
                repairOrder.getCustomerDevice().getDeviceType().getId(),
                request,
                checker,
                business);
    }

    private QualityChecklistResponse upsertInternal(
            Integer businessId,
            String normalizedTicketCode,
            Integer repairOrderId,
            Integer expectedDeviceTypeId,
            UpsertQualityChecklistRequest request,
            AppUser checker,
            Business business) {
        LocalDateTime now = LocalDateTime.now();

        List<RepairOrderQualityCheck> existing = repairOrderQualityCheckRepository
                .findByBusinessIdAndIntakeTicketCodeAndDeletedAtIsNullOrderByIdAsc(
                        businessId,
                        normalizedTicketCode);

        Map<Integer, RepairOrderQualityCheck> existingByTestId = existing.stream()
                .collect(Collectors.toMap(
                        item -> item.getBusinessQualityTest().getId(),
                        Function.identity(),
                        (left, right) -> left,
                        LinkedHashMap::new));

        Set<Integer> requestedTestIds = request.items().stream()
                .map(QualityChecklistItemRequest::businessQualityTestId)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        List<BusinessQualityTest> tests = businessQualityTestRepository.findAllById(requestedTestIds);
        Map<Integer, BusinessQualityTest> testsById = tests.stream()
                .collect(Collectors.toMap(BusinessQualityTest::getId, Function.identity()));

        if (testsById.size() != requestedTestIds.size()) {
            throw new ResourceNotFoundException("business_quality_test.not_found");
        }

        for (BusinessQualityTest test : tests) {
            if (!test.getBusiness().getId().equals(businessId)
                    || !Boolean.TRUE.equals(test.getActive())
                    || test.getDeletedAt() != null) {
                throw new ResourceNotFoundException("business_quality_test.not_found");
            }
            if (expectedDeviceTypeId != null && !test.getDeviceType().getId().equals(expectedDeviceTypeId)) {
                throw new ConflictException("quality_checklist.device_type_mismatch");
            }
        }

        List<RepairOrderQualityCheck> toSave = new ArrayList<>();
        Set<Integer> keepActiveTestIds = new HashSet<>();

        for (QualityChecklistItemRequest item : request.items()) {
            BusinessQualityTest qualityTest = testsById.get(item.businessQualityTestId());
            RepairOrderQualityCheck row = existingByTestId.get(item.businessQualityTestId());

            if (row == null) {
                row = new RepairOrderQualityCheck();
                row.setBusiness(business);
                row.setBusinessQualityTest(qualityTest);
                row.setIntakeTicketCode(normalizedTicketCode);
            }

            row.setRepairOrderId(repairOrderId);
            row.setCheckedByUser(checker);
            row.setResult(item.result());
            row.setNotes(normalizeNullable(item.notes()));
            row.setCheckedAt(now);
            row.setActive(true);
            row.setDeletedAt(null);

            toSave.add(row);
            keepActiveTestIds.add(item.businessQualityTestId());
        }

        for (RepairOrderQualityCheck row : existing) {
            Integer testId = row.getBusinessQualityTest().getId();
            if (!keepActiveTestIds.contains(testId)) {
                row.setActive(false);
                row.setDeletedAt(now);
                toSave.add(row);
            }
        }

        repairOrderQualityCheckRepository.saveAll(toSave);

        List<RepairOrderQualityCheck> reloaded = repairOrderQualityCheckRepository
                .findByBusinessIdAndIntakeTicketCodeAndDeletedAtIsNullOrderByIdAsc(
                        businessId,
                        normalizedTicketCode);

        return toResponse(businessId, normalizedTicketCode, reloaded);
    }

    @Transactional
    public void deleteByTicket(Integer businessId, String ticketCode, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "BUSINESS_QUALITY_CHECK_UPDATE");
        String normalizedTicketCode = normalizeTicketCode(ticketCode);
        List<RepairOrderQualityCheck> existing = repairOrderQualityCheckRepository
                .findByBusinessIdAndIntakeTicketCodeAndDeletedAtIsNullOrderByIdAsc(businessId, normalizedTicketCode);

        if (existing.isEmpty()) {
            throw new ResourceNotFoundException("quality_checklist.not_found");
        }

        LocalDateTime now = LocalDateTime.now();
        for (RepairOrderQualityCheck row : existing) {
            row.setActive(false);
            row.setDeletedAt(now);
        }
    }

    @Transactional
    public void deleteByRepairOrder(Integer businessId, Integer repairOrderId, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "BUSINESS_QUALITY_CHECK_UPDATE");
        findActiveRepairOrder(businessId, repairOrderId);

        List<RepairOrderQualityCheck> existing = repairOrderQualityCheckRepository
                .findByBusinessIdAndRepairOrderIdAndDeletedAtIsNullOrderByIdAsc(businessId, repairOrderId);
        if (existing.isEmpty()) {
            throw new ResourceNotFoundException("quality_checklist.not_found");
        }

        LocalDateTime now = LocalDateTime.now();
        for (RepairOrderQualityCheck row : existing) {
            row.setActive(false);
            row.setDeletedAt(now);
        }
    }

    private QualityChecklistResponse toResponse(
            Integer businessId,
            String ticketCode,
            List<RepairOrderQualityCheck> checks) {
        RepairOrderQualityCheck head = checks.get(0);

        List<QualityChecklistItemResponse> items = checks.stream()
                .map(row -> new QualityChecklistItemResponse(
                        row.getId(),
                        row.getBusinessQualityTest().getId(),
                        row.getBusinessQualityTest().getCode(),
                        row.getBusinessQualityTest().getName(),
                        row.getBusinessQualityTest().getRequired(),
                        row.getResult(),
                        row.getNotes()))
                .toList();

        return new QualityChecklistResponse(
                businessId,
                ticketCode,
                head.getRepairOrderId(),
                head.getCheckedByUser().getId(),
                head.getCheckedByUser().getName() + " " + head.getCheckedByUser().getSurname(),
                head.getCheckedAt(),
                items);
    }

    private AppUser findActiveUserBySubject(String subject) {
        Integer userId;
        try {
            userId = Integer.valueOf(subject);
        } catch (NumberFormatException exception) {
            throw new BadCredentialsException("auth.token.invalid");
        }

        return appUserRepository.findById(userId)
                .filter(appUser -> Boolean.TRUE.equals(appUser.getActive()))
                .orElseThrow(() -> new BadCredentialsException("auth.token.invalid"));
    }

    private RepairOrder findActiveRepairOrder(Integer businessId, Integer repairOrderId) {
        return repairOrderRepository.findByIdAndBusinessIdAndDeletedAtIsNull(repairOrderId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("repair_order.not_found"));
    }

    private String normalizeTicketCode(String ticketCode) {
        if (!StringUtils.hasText(ticketCode)) {
            throw new ResourceNotFoundException("quality_checklist.ticket.invalid");
        }
        return ticketCode.trim().toUpperCase();
    }

    private String normalizeNullable(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }
}
