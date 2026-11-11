package com.tecmarket.backend.repairorder.repository;

import com.tecmarket.backend.repairorder.entity.RepairOrderPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface RepairOrderPartRepository extends JpaRepository<RepairOrderPart, Integer> {

    Page<RepairOrderPart> findByRepairOrderIdAndRepairOrderBusinessIdAndDeletedAtIsNullOrderByCreatedAtDescIdDesc(
            Integer repairOrderId,
            Integer businessId,
            Pageable pageable);

    Optional<RepairOrderPart> findByIdAndRepairOrderIdAndRepairOrderBusinessIdAndDeletedAtIsNull(
            Integer id,
            Integer repairOrderId,
            Integer businessId);

    Optional<RepairOrderPart> findByRepairOrderIdAndIdempotencyKey(
            Integer repairOrderId,
            String idempotencyKey);

    @Query("""
            SELECT COALESCE(SUM(part.totalPrice), 0)
            FROM RepairOrderPart part
            WHERE part.repairOrder.id = :repairOrderId
              AND part.repairOrder.business.id = :businessId
              AND part.active = true
              AND part.deletedAt IS NULL
            """)
    BigDecimal sumActivePartsTotal(Integer repairOrderId, Integer businessId);
}
