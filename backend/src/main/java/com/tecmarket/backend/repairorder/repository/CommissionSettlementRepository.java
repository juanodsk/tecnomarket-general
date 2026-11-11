package com.tecmarket.backend.repairorder.repository;

import com.tecmarket.backend.repairorder.entity.CommissionSettlement;
import com.tecmarket.backend.repairorder.entity.CommissionSettlementStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommissionSettlementRepository extends JpaRepository<CommissionSettlement, Integer> {

    Optional<CommissionSettlement> findByIdAndBusinessIdAndDeletedAtIsNull(Integer id, Integer businessId);

    List<CommissionSettlement> findByBusinessIdAndDeletedAtIsNullOrderByGeneratedAtDescIdDesc(Integer businessId);

    List<CommissionSettlement> findByBusinessIdAndBusinessUserIdAndDeletedAtIsNullOrderByGeneratedAtDescIdDesc(
            Integer businessId,
            Integer businessUserId);

    List<CommissionSettlement> findByBusinessIdAndStatusAndDeletedAtIsNullOrderByGeneratedAtDescIdDesc(
            Integer businessId,
            CommissionSettlementStatus status);

    List<CommissionSettlement> findByBusinessIdAndBusinessUserIdAndStatusAndDeletedAtIsNullOrderByGeneratedAtDescIdDesc(
            Integer businessId,
            Integer businessUserId,
            CommissionSettlementStatus status);
}
