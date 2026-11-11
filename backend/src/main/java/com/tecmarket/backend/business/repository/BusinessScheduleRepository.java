package com.tecmarket.backend.business.repository;

import com.tecmarket.backend.business.entity.BusinessSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BusinessScheduleRepository extends JpaRepository<BusinessSchedule, Integer> {

    List<BusinessSchedule> findByBusinessIdAndDeletedAtIsNullOrderByDayOfWeekAscSlotOrderAscIdAsc(Integer businessId);

    Page<BusinessSchedule> findByBusinessIdAndBranchIdInAndDeletedAtIsNullOrderByDayOfWeekAscSlotOrderAscIdAsc(
            Integer businessId, List<Integer> branchIds, Pageable pageable);

    List<BusinessSchedule> findByBusinessIdAndDayOfWeekAndDeletedAtIsNullOrderByOpenTimeAsc(Integer businessId,
            Integer dayOfWeek);

    List<BusinessSchedule> findByBranchIdAndDayOfWeekAndDeletedAtIsNullOrderByOpenTimeAsc(Integer branchId,
            Integer dayOfWeek);

    Optional<BusinessSchedule> findByIdAndBusinessIdAndDeletedAtIsNull(Integer id, Integer businessId);

    boolean existsByBusinessIdAndDayOfWeekAndSlotOrderAndDeletedAtIsNull(
            Integer businessId,
            Integer dayOfWeek,
            Integer slotOrder);

    boolean existsByBranchIdAndDayOfWeekAndSlotOrderAndDeletedAtIsNull(
            Integer branchId,
            Integer dayOfWeek,
            Integer slotOrder);

    boolean existsByBusinessIdAndDayOfWeekAndSlotOrderAndDeletedAtIsNullAndIdNot(
            Integer businessId,
            Integer dayOfWeek,
            Integer slotOrder,
            Integer id);

    boolean existsByBranchIdAndDayOfWeekAndSlotOrderAndDeletedAtIsNullAndIdNot(
            Integer branchId,
            Integer dayOfWeek,
            Integer slotOrder,
            Integer id);
}
