package com.tecmarket.backend.repairorder.repository;

import com.tecmarket.backend.repairorder.entity.RepairOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.tecmarket.backend.repairorder.entity.RepairOrderCommission;
public interface RepairOrderRepository extends JpaRepository<RepairOrder, Integer>, JpaSpecificationExecutor<RepairOrder> {

    Optional<RepairOrder> findByIdAndBusinessIdAndDeletedAtIsNull(Integer id, Integer businessId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT repairOrder
            FROM RepairOrder repairOrder
            WHERE repairOrder.id = :id
              AND repairOrder.business.id = :businessId
              AND repairOrder.deletedAt IS NULL
            """)
    Optional<RepairOrder> findForUpdateByIdAndBusinessId(
            @Param("id") Integer id,
            @Param("businessId") Integer businessId);

    Optional<RepairOrder> findByBusinessIdAndIdempotencyKeyAndDeletedAtIsNull(Integer businessId, String idempotencyKey);

    Optional<RepairOrder> findByOrderNumberIgnoreCaseAndCustomerIdAndDeletedAtIsNull(String orderNumber, Integer customerId);

    long countByBusinessIdAndDeletedAtIsNull(Integer businessId);

    @Query("""
            SELECT order
            FROM RepairOrder order
            WHERE order.business.id = :businessId
              AND order.assignedBusinessUser.id = :businessUserId
              AND order.finalPrice IS NOT NULL
              AND order.active = true
              AND order.deletedAt IS NULL
              AND order.receivedAt >= :periodStart
              AND order.receivedAt < :periodEndExclusive
              AND NOT EXISTS (
                    SELECT commission.id
                    FROM RepairOrderCommission commission
                    WHERE commission.repairOrder.id = order.id
                      AND commission.deletedAt IS NULL
              )
            ORDER BY order.receivedAt ASC, order.id ASC
            """)
    List<RepairOrder> findEligibleForCommissionSettlement(
            Integer businessId,
            Integer businessUserId,
            LocalDateTime periodStart,
            LocalDateTime periodEndExclusive);
}
