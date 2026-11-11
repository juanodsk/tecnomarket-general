package com.tecmarket.backend.business.service;

import com.tecmarket.backend.business.dto.BusinessScheduleResponse;
import com.tecmarket.backend.business.dto.CreateBusinessScheduleRequest;
import com.tecmarket.backend.business.dto.UpdateBusinessScheduleRequest;
import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.business.entity.BusinessSchedule;
import com.tecmarket.backend.business.entity.BusinessBranch;
import com.tecmarket.backend.business.mapper.BusinessScheduleMapper;
import com.tecmarket.backend.business.repository.BusinessRepository;
import com.tecmarket.backend.business.repository.BusinessScheduleRepository;
import com.tecmarket.backend.business.repository.BusinessBranchRepository;
import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.common.pagination.PageRequestFactory;
import com.tecmarket.backend.common.pagination.PageResponse;
import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.business.security.BusinessBranchAccessGuard;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Service
public class BusinessScheduleService {

    private final BusinessRepository businessRepository;
    private final BusinessScheduleRepository businessScheduleRepository;
    private final BusinessScheduleMapper businessScheduleMapper;
    private final BusinessBranchRepository businessBranchRepository;
    private final BusinessAccessGuard businessAccessGuard;
    private final BusinessBranchAccessGuard businessBranchAccessGuard;

    public BusinessScheduleService(
            BusinessRepository businessRepository,
            BusinessScheduleRepository businessScheduleRepository,
            BusinessScheduleMapper businessScheduleMapper,
            BusinessBranchRepository businessBranchRepository,
            BusinessAccessGuard businessAccessGuard,
            BusinessBranchAccessGuard businessBranchAccessGuard) {
        this.businessRepository = businessRepository;
        this.businessScheduleRepository = businessScheduleRepository;
        this.businessScheduleMapper = businessScheduleMapper;
        this.businessBranchRepository = businessBranchRepository;
        this.businessAccessGuard = businessAccessGuard;
        this.businessBranchAccessGuard = businessBranchAccessGuard;
    }

    @Transactional(readOnly = true)
    public PageResponse<BusinessScheduleResponse> findAllByBusiness(
            Integer businessId, Integer page, Integer size, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "BUSINESS_SCHEDULE_VIEW");
        findBusiness(businessId);
        return PageResponse.from(businessScheduleRepository
                .findByBusinessIdAndBranchIdInAndDeletedAtIsNullOrderByDayOfWeekAscSlotOrderAscIdAsc(
                        businessId,
                        businessBranchAccessGuard.accessibleBranchIds(businessId, subject),
                        PageRequestFactory.of(page, size))
                .map(businessScheduleMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public BusinessScheduleResponse findById(Integer businessId, Integer scheduleId, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "BUSINESS_SCHEDULE_VIEW");
        BusinessSchedule schedule = businessScheduleRepository
                .findByIdAndBusinessIdAndDeletedAtIsNull(scheduleId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("business_schedule.not_found"));
        businessBranchAccessGuard.findAccessibleBranch(businessId, schedule.getBranch().getId(), subject);
        return businessScheduleMapper.toResponse(schedule);
    }

    @Transactional
    public BusinessScheduleResponse create(Integer businessId, CreateBusinessScheduleRequest request, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "BUSINESS_SCHEDULE_CREATE");
        Business business = findBusiness(businessId);
        BusinessBranch branch = businessBranchAccessGuard.findAccessibleBranch(businessId, request.branchId(), subject);
        validateTimeRange(request.openTime(), request.closeTime());

        Integer slotOrder = resolveSlotOrderForCreate(branch.getId(), request.dayOfWeek(), request.slotOrder());
        validateNoOverlap(branch.getId(), request.dayOfWeek(), request.openTime(), request.closeTime(), null);

        BusinessSchedule schedule = new BusinessSchedule();
        schedule.setBusiness(business);
        schedule.setBranch(branch);
        schedule.setDayOfWeek(request.dayOfWeek());
        schedule.setOpenTime(request.openTime());
        schedule.setCloseTime(request.closeTime());
        schedule.setSlotOrder(slotOrder);
        schedule.setActive(true);

        return businessScheduleMapper.toResponse(businessScheduleRepository.save(schedule));
    }

    @Transactional
    public BusinessScheduleResponse update(Integer businessId, Integer scheduleId,
            UpdateBusinessScheduleRequest request, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "BUSINESS_SCHEDULE_UPDATE");
        BusinessSchedule schedule = businessScheduleRepository
                .findByIdAndBusinessIdAndDeletedAtIsNull(scheduleId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("business_schedule.not_found"));
        businessBranchAccessGuard.findAccessibleBranch(businessId, schedule.getBranch().getId(), subject);

        validateTimeRange(request.openTime(), request.closeTime());

        Integer slotOrder = resolveSlotOrderForUpdate(
                schedule.getBranch().getId(),
                request.dayOfWeek(),
                request.slotOrder(),
                scheduleId);

        validateNoOverlap(
                schedule.getBranch().getId(),
                request.dayOfWeek(),
                request.openTime(),
                request.closeTime(),
                scheduleId);

        schedule.setDayOfWeek(request.dayOfWeek());
        schedule.setOpenTime(request.openTime());
        schedule.setCloseTime(request.closeTime());
        schedule.setSlotOrder(slotOrder);

        if (request.active() != null) {
            schedule.setActive(request.active());
            schedule.setDeletedAt(Boolean.FALSE.equals(request.active()) ? LocalDateTime.now() : null);
        }

        return businessScheduleMapper.toResponse(schedule);
    }

    @Transactional
    public void delete(Integer businessId, Integer scheduleId, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "BUSINESS_SCHEDULE_UPDATE");
        BusinessSchedule schedule = businessScheduleRepository
                .findByIdAndBusinessIdAndDeletedAtIsNull(scheduleId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("business_schedule.not_found"));
        businessBranchAccessGuard.findAccessibleBranch(businessId, schedule.getBranch().getId(), subject);

        schedule.setActive(false);
        schedule.setDeletedAt(LocalDateTime.now());
    }

    private Business findBusiness(Integer businessId) {
        return businessRepository.findByIdAndDeletedAtIsNull(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("business.not_found"));
    }

    private BusinessBranch findActiveBranch(Integer businessId, Integer branchId) {
        return businessBranchRepository.findByIdAndBusinessIdAndDeletedAtIsNull(branchId, businessId)
                .filter(branch -> Boolean.TRUE.equals(branch.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("business_branch.not_found"));
    }

    private void validateTimeRange(LocalTime openTime, LocalTime closeTime) {
        if (!openTime.isBefore(closeTime)) {
            throw new ConflictException("business_schedule.invalid_time_range");
        }
    }

    private Integer resolveSlotOrderForCreate(Integer branchId, Integer dayOfWeek, Integer requestedSlotOrder) {
        if (requestedSlotOrder != null) {
            if (businessScheduleRepository.existsByBranchIdAndDayOfWeekAndSlotOrderAndDeletedAtIsNull(
                    branchId, dayOfWeek, requestedSlotOrder)) {
                throw new ConflictException("business_schedule.slot_order.exists");
            }
            return requestedSlotOrder;
        }

        return businessScheduleRepository
                .findByBranchIdAndDayOfWeekAndDeletedAtIsNullOrderByOpenTimeAsc(branchId, dayOfWeek)
                .stream()
                .map(BusinessSchedule::getSlotOrder)
                .filter(value -> value != null && value > 0)
                .max(Comparator.naturalOrder())
                .map(max -> max + 1)
                .orElse(1);
    }

    private Integer resolveSlotOrderForUpdate(
            Integer branchId,
            Integer dayOfWeek,
            Integer requestedSlotOrder,
            Integer currentScheduleId) {
        if (requestedSlotOrder == null) {
            return businessScheduleRepository.findById(currentScheduleId)
                    .filter(schedule -> schedule.getBranch().getId().equals(branchId) && schedule.getDeletedAt() == null)
                    .map(BusinessSchedule::getSlotOrder)
                    .orElse(1);
        }

        if (businessScheduleRepository.existsByBranchIdAndDayOfWeekAndSlotOrderAndDeletedAtIsNullAndIdNot(
                branchId, dayOfWeek, requestedSlotOrder, currentScheduleId)) {
            throw new ConflictException("business_schedule.slot_order.exists");
        }

        return requestedSlotOrder;
    }

    private void validateNoOverlap(
            Integer branchId,
            Integer dayOfWeek,
            LocalTime openTime,
            LocalTime closeTime,
            Integer ignoreScheduleId) {
        List<BusinessSchedule> existing = businessScheduleRepository
                .findByBranchIdAndDayOfWeekAndDeletedAtIsNullOrderByOpenTimeAsc(branchId, dayOfWeek);

        for (BusinessSchedule current : existing) {
            if (ignoreScheduleId != null && current.getId().equals(ignoreScheduleId)) {
                continue;
            }

            if (isOverlapping(openTime, closeTime, current.getOpenTime(), current.getCloseTime())) {
                throw new ConflictException("business_schedule.overlap");
            }
        }
    }

    private boolean isOverlapping(LocalTime startA, LocalTime endA, LocalTime startB, LocalTime endB) {
        return startA.isBefore(endB) && startB.isBefore(endA);
    }
}
