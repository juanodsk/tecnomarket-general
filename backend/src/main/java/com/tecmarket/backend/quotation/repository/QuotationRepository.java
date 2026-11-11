package com.tecmarket.backend.quotation.repository;

import com.tecmarket.backend.quotation.entity.Quotation;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QuotationRepository extends JpaRepository<Quotation, Integer> {

    Optional<Quotation> findByIdAndBusinessIdAndDeletedAtIsNull(Integer id, Integer businessId);

    Page<Quotation> findByBusinessIdAndRepairOrderIdAndActiveTrueAndDeletedAtIsNullOrderByCreatedAtDescIdDesc(
            Integer businessId,
            Integer repairOrderId,
            Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT quotation
            FROM Quotation quotation
            WHERE quotation.id = :id
              AND quotation.business.id = :businessId
              AND quotation.deletedAt IS NULL
            """)
    Optional<Quotation> findForUpdateByIdAndBusinessId(
            @Param("id") Integer id,
            @Param("businessId") Integer businessId);
}
