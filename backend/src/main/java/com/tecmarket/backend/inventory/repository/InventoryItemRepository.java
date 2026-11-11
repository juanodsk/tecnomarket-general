package com.tecmarket.backend.inventory.repository;

import com.tecmarket.backend.inventory.entity.InventoryItem;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Integer> {

    List<InventoryItem> findByBusinessIdAndDeletedAtIsNullOrderByNameAsc(Integer businessId);

    Page<InventoryItem> findByBusinessIdAndBranchIdInAndDeletedAtIsNullOrderByNameAsc(
            Integer businessId, List<Integer> branchIds, Pageable pageable);

    Optional<InventoryItem> findByIdAndBusinessIdAndDeletedAtIsNull(Integer id, Integer businessId);

    boolean existsByBusinessIdAndSkuAndDeletedAtIsNull(Integer businessId, String sku);

    boolean existsByBranchIdAndSkuAndDeletedAtIsNull(Integer branchId, String sku);

    boolean existsByBranchIdAndSkuAndDeletedAtIsNullAndIdNot(Integer branchId, String sku, Integer id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT i
            FROM InventoryItem i
            WHERE i.id = :id
              AND i.business.id = :businessId
              AND i.deletedAt IS NULL
            """)
    Optional<InventoryItem> findForUpdateByIdAndBusinessId(
            @Param("id") Integer id,
            @Param("businessId") Integer businessId);
}
