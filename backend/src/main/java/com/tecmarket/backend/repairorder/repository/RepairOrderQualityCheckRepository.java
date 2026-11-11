package com.tecmarket.backend.repairorder.repository;

import com.tecmarket.backend.repairorder.entity.RepairOrderQualityCheck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepairOrderQualityCheckRepository extends JpaRepository<RepairOrderQualityCheck, Integer> {

    List<RepairOrderQualityCheck> findByBusinessIdAndIntakeTicketCodeAndDeletedAtIsNullOrderByIdAsc(
            Integer businessId,
            String intakeTicketCode);

    List<RepairOrderQualityCheck> findByBusinessIdAndRepairOrderIdAndDeletedAtIsNullOrderByIdAsc(
            Integer businessId,
            Integer repairOrderId);
}
