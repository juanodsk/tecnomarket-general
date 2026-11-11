package com.tecmarket.backend.quotation.repository;

import com.tecmarket.backend.quotation.entity.QuotationItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuotationItemRepository extends JpaRepository<QuotationItem, Integer> {

    List<QuotationItem> findByQuotationIdAndQuotationBusinessIdAndDeletedAtIsNullOrderByCreatedAtAscIdAsc(
            Integer quotationId,
            Integer businessId);

    @Query("""
            SELECT item
            FROM QuotationItem item
            WHERE item.quotation.id IN :quotationIds
              AND item.quotation.business.id = :businessId
              AND item.active = true
              AND item.deletedAt IS NULL
            ORDER BY item.quotation.id ASC, item.createdAt ASC, item.id ASC
            """)
    List<QuotationItem> findActiveByQuotationIdsAndBusinessId(
            @Param("quotationIds") List<Integer> quotationIds,
            @Param("businessId") Integer businessId);
}
