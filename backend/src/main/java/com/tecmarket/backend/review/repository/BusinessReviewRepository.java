package com.tecmarket.backend.review.repository;

import com.tecmarket.backend.review.entity.BusinessReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface BusinessReviewRepository extends JpaRepository<BusinessReview, Integer> {

    boolean existsByRepairOrderIdAndDeletedAtIsNull(Integer repairOrderId);

    Page<BusinessReview> findByBusinessIdAndDeletedAtIsNullOrderByCreatedAtDescIdDesc(
            Integer businessId, Pageable pageable);

    Page<BusinessReview> findByBusinessIdAndActiveTrueAndDeletedAtIsNullOrderByCreatedAtDescIdDesc(
            Integer businessId, Pageable pageable);

    @Query("""
            SELECT COALESCE(AVG(br.rating), 0), COUNT(br)
            FROM BusinessReview br
            WHERE br.business.id = :businessId
              AND br.deletedAt IS NULL
              AND br.active = TRUE
            """)
    Object[] summarizeRatingByBusiness(@Param("businessId") Integer businessId);
}
