package com.tecmarket.backend.inventory.repository;

import com.tecmarket.backend.inventory.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StockMovementRepository extends JpaRepository<StockMovement, Integer> {

    List<StockMovement> findByInventoryItemBusinessIdAndDeletedAtIsNullOrderByCreatedAtDescIdDesc(Integer businessId);

    Page<StockMovement> findByInventoryItemBusinessIdAndBranchIdInAndDeletedAtIsNullOrderByCreatedAtDescIdDesc(
            Integer businessId, List<Integer> branchIds, Pageable pageable);

    List<StockMovement> findByInventoryItemIdAndInventoryItemBusinessIdAndDeletedAtIsNullOrderByCreatedAtDescIdDesc(
            Integer inventoryItemId,
            Integer businessId);

    Page<StockMovement> findByInventoryItemIdAndInventoryItemBusinessIdAndBranchIdInAndDeletedAtIsNullOrderByCreatedAtDescIdDesc(
            Integer inventoryItemId, Integer businessId, List<Integer> branchIds, Pageable pageable);

    Optional<StockMovement> findByBranchIdAndIdempotencyKeyAndDeletedAtIsNull(Integer branchId, String idempotencyKey);
}
