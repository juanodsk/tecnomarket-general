package com.tecmarket.backend.repairorder.repository;

import com.tecmarket.backend.repairorder.entity.RepairOrderCommission;
import com.tecmarket.backend.repairorder.entity.RepairOrderCommissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RepairOrderCommissionRepository extends JpaRepository<RepairOrderCommission, Integer> {

    Optional<RepairOrderCommission> findByIdAndBusinessIdAndDeletedAtIsNull(Integer id, Integer businessId);

    Optional<RepairOrderCommission> findByRepairOrderIdAndBusinessIdAndDeletedAtIsNull(
            Integer repairOrderId,
            Integer businessId);

    List<RepairOrderCommission> findByBusinessIdAndDeletedAtIsNullOrderByGeneratedAtDescIdDesc(Integer businessId);

    List<RepairOrderCommission> findByBusinessIdAndStatusAndDeletedAtIsNullOrderByGeneratedAtDescIdDesc(
            Integer businessId,
            RepairOrderCommissionStatus status);

    List<RepairOrderCommission> findByBusinessIdAndBusinessUserIdAndDeletedAtIsNullOrderByGeneratedAtDescIdDesc(
            Integer businessId,
            Integer businessUserId);

    List<RepairOrderCommission> findByBusinessIdAndBusinessUserIdAndStatusAndDeletedAtIsNullOrderByGeneratedAtDescIdDesc(
            Integer businessId,
            Integer businessUserId,
            RepairOrderCommissionStatus status);

    List<RepairOrderCommission> findBySettlementIdAndBusinessIdAndDeletedAtIsNullOrderByIdAsc(
            Integer settlementId,
            Integer businessId);
}
