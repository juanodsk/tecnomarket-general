package com.tecmarket.backend.sale.repository;

import com.tecmarket.backend.sale.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SaleRepository extends JpaRepository<Sale, Integer> {

    List<Sale> findByBusinessIdAndDeletedAtIsNullOrderByCreatedAtDescIdDesc(Integer businessId);

    Page<Sale> findByBusinessIdAndBranchIdInAndDeletedAtIsNullOrderByCreatedAtDescIdDesc(
            Integer businessId, List<Integer> branchIds, Pageable pageable);

    Optional<Sale> findByIdAndBusinessIdAndDeletedAtIsNull(Integer id, Integer businessId);

    Optional<Sale> findByBusinessIdAndIdempotencyKeyAndDeletedAtIsNull(Integer businessId, String idempotencyKey);
}
