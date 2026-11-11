package com.tecmarket.backend.repairorder.repository;

import com.tecmarket.backend.repairorder.entity.RepairOrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RepairOrderHistoryRepository extends JpaRepository<RepairOrderHistory, Integer> {

    Page<RepairOrderHistory> findByRepairOrderIdAndRepairOrderBusinessIdAndDeletedAtIsNullOrderByCreatedAtDescIdDesc(
            Integer repairOrderId,
            Integer businessId,
            Pageable pageable);
}
