package com.tecmarket.backend.business.repository;

import com.tecmarket.backend.business.entity.BusinessBranch;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BusinessBranchRepository extends JpaRepository<BusinessBranch, Integer> {

    List<BusinessBranch> findByBusinessIdAndDeletedAtIsNullOrderByPrimaryBranchDescNameAscIdAsc(Integer businessId);

    Optional<BusinessBranch> findByIdAndBusinessIdAndDeletedAtIsNull(Integer id, Integer businessId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT branch
            FROM BusinessBranch branch
            WHERE branch.id = :id
              AND branch.business.id = :businessId
              AND branch.deletedAt IS NULL
            """)
    Optional<BusinessBranch> findForUpdateByIdAndBusinessId(
            @Param("id") Integer id,
            @Param("businessId") Integer businessId);

    Optional<BusinessBranch> findByBusinessIdAndPrimaryBranchTrueAndDeletedAtIsNull(Integer businessId);

    boolean existsByBusinessIdAndNameIgnoreCaseAndDeletedAtIsNull(Integer businessId, String name);

    boolean existsByBusinessIdAndNameIgnoreCaseAndDeletedAtIsNullAndIdNot(Integer businessId, String name, Integer id);

    long countByBusinessIdAndActiveTrueAndDeletedAtIsNull(Integer businessId);

    Optional<BusinessBranch> findFirstByBusinessIdAndDeletedAtIsNullOrderByBranchNumberDesc(Integer businessId);
}
