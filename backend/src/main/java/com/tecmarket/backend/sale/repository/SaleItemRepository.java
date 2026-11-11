package com.tecmarket.backend.sale.repository;

import com.tecmarket.backend.sale.entity.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleItemRepository extends JpaRepository<SaleItem, Integer> {

    List<SaleItem> findBySaleIdAndSaleBusinessIdAndDeletedAtIsNullOrderByCreatedAtAscIdAsc(
            Integer saleId,
            Integer businessId);
}
