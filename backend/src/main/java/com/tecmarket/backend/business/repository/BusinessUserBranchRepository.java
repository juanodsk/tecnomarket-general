package com.tecmarket.backend.business.repository;

import com.tecmarket.backend.business.entity.BusinessUserBranch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusinessUserBranchRepository extends JpaRepository<BusinessUserBranch, Integer> {

    boolean existsByBusinessUserBusinessIdAndBusinessUserUserIdAndBranchIdAndActiveTrueAndDeletedAtIsNull(
            Integer businessId, Integer userId, Integer branchId);

    boolean existsByBusinessUserIdAndBranchIdAndActiveTrueAndDeletedAtIsNull(Integer businessUserId, Integer branchId);

    List<BusinessUserBranch> findByBusinessUserIdAndDeletedAtIsNullOrderByBranchBranchNumberAsc(Integer businessUserId);

    List<BusinessUserBranch> findByBusinessUserBusinessIdAndBusinessUserUserIdAndActiveTrueAndDeletedAtIsNullOrderByBranchBranchNumberAsc(
            Integer businessId, Integer userId);
}
