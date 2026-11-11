package com.tecmarket.backend.business.repository;

import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.user.entity.DocumentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BusinessRepository extends JpaRepository<Business, Integer> {

    List<Business> findAllByDeletedAtIsNullOrderByNameAsc();

    Page<Business> findAllByDeletedAtIsNullOrderByNameAsc(Pageable pageable);

    Optional<Business> findByIdAndDeletedAtIsNull(Integer id);

    Optional<Business> findByIdAndActiveTrueAndDeletedAtIsNull(Integer id);

    boolean existsByDocumentTypeAndDocumentNumberAndDeletedAtIsNull(DocumentType documentType, String documentNumber);

    boolean existsByDocumentTypeAndDocumentNumberAndDeletedAtIsNullAndIdNot(
            DocumentType documentType,
            String documentNumber,
            Integer id);

    @Query("""
            SELECT b
            FROM Business b
            WHERE b.deletedAt IS NULL
              AND b.active = TRUE
              AND (:cityId IS NULL OR b.city.id = :cityId)
              AND (:minRating IS NULL OR COALESCE(b.ratingAverage, 0) >= :minRating)
            """)
    Page<Business> findPublicBusinesses(
            @Param("cityId") Integer cityId,
            @Param("minRating") BigDecimal minRating,
            Pageable pageable);

    @Query("""
            SELECT b
            FROM Business b
            WHERE b.deletedAt IS NULL
              AND b.active = TRUE
              AND (:cityId IS NULL OR b.city.id = :cityId)
              AND (:minRating IS NULL OR COALESCE(b.ratingAverage, 0) >= :minRating)
              AND (
                    LOWER(b.name) LIKE LOWER(CONCAT('%', :queryText, '%'))
                    OR LOWER(b.description) LIKE LOWER(CONCAT('%', :queryText, '%'))
                    OR LOWER(b.city.name) LIKE LOWER(CONCAT('%', :queryText, '%'))
                  )
            """)
    Page<Business> findPublicBusinessesByQuery(
            @Param("queryText") String queryText,
            @Param("cityId") Integer cityId,
            @Param("minRating") BigDecimal minRating,
            Pageable pageable);
}
