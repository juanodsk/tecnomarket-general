package com.tecmarket.backend.business.repository;

import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.business.entity.BusinessUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BusinessUserRepository extends JpaRepository<BusinessUser, Integer> {

    List<BusinessUser> findByBusinessIdAndDeletedAtIsNullOrderByIdAsc(Integer businessId);

    Page<BusinessUser> findByBusinessIdAndDeletedAtIsNullOrderByIdAsc(Integer businessId, Pageable pageable);

    long countByBusinessIdAndActiveTrueAndDeletedAtIsNull(Integer businessId);

    Optional<BusinessUser> findByIdAndBusinessIdAndDeletedAtIsNull(Integer id, Integer businessId);

    Optional<BusinessUser> findByBusinessIdAndUserIdAndRoleId(Integer businessId, Integer userId, Integer roleId);

    List<BusinessUser> findByBusinessIdAndUserIdAndDeletedAtIsNullOrderByIdAsc(Integer businessId, Integer userId);

    boolean existsByBusinessIdAndUserIdAndActiveTrueAndDeletedAtIsNull(Integer businessId, Integer userId);

    Optional<BusinessUser> findByBusinessIdAndUserIdAndActiveTrueAndDeletedAtIsNull(Integer businessId, Integer userId);

    @Query("""
            SELECT bu
            FROM BusinessUser bu
            JOIN bu.role r
            JOIN bu.business b
            WHERE b.id = :businessId
              AND bu.active = true
              AND bu.deletedAt IS NULL
              AND r.active = true
              AND UPPER(r.code) = UPPER(:roleCode)
              AND b.active = true
              AND b.deletedAt IS NULL
            ORDER BY bu.user.name ASC, bu.user.surname ASC, bu.id ASC
            """)
    List<BusinessUser> findActiveByBusinessAndRoleCode(Integer businessId, String roleCode);

    @Query("""
            SELECT bu
            FROM BusinessUser bu
            JOIN bu.role r
            JOIN bu.user u
            JOIN BusinessUserBranch assignment ON assignment.businessUser = bu
            WHERE bu.business.id = :businessId
              AND assignment.branch.id = :branchId
              AND bu.active = true
              AND bu.deletedAt IS NULL
              AND assignment.active = true
              AND assignment.deletedAt IS NULL
              AND r.active = true
              AND UPPER(r.code) = UPPER(:roleCode)
            ORDER BY u.name ASC, u.surname ASC, bu.id ASC
            """)
    List<BusinessUser> findActiveByBusinessBranchAndRoleCode(Integer businessId, Integer branchId, String roleCode);

    @Query("""
            SELECT COUNT(bu) > 0
            FROM BusinessUser bu
            JOIN bu.role r
            JOIN bu.business b
            WHERE b.id = :businessId
              AND bu.user.id = :userId
              AND bu.active = true
              AND bu.deletedAt IS NULL
              AND r.active = true
              AND UPPER(r.code) IN :roleCodes
              AND b.active = true
              AND b.deletedAt IS NULL
            """)
    boolean existsAnyActiveRoleCodeForBusiness(Integer businessId, Integer userId, List<String> roleCodes);

    @Query("""
            SELECT COUNT(bu) > 0
            FROM BusinessUser bu
            JOIN bu.role r
            JOIN RolePermission rp ON rp.role = r
            JOIN rp.permission p
            JOIN bu.business b
            WHERE b.id = :businessId
              AND bu.user.id = :userId
              AND bu.active = true
              AND bu.deletedAt IS NULL
              AND r.active = true
              AND rp.active = true
              AND p.active = true
              AND p.code IN :permissionCodes
              AND b.active = true
              AND b.deletedAt IS NULL
            """)
    boolean existsActivePermissionForBusiness(Integer businessId, Integer userId, List<String> permissionCodes);

    @Query("""
            SELECT DISTINCT bu.business
            FROM BusinessUser bu
            JOIN bu.role r
            JOIN bu.business b
            WHERE bu.user.id = :userId
              AND bu.active = true
              AND bu.deletedAt IS NULL
              AND r.active = true
              AND b.active = true
              AND b.deletedAt IS NULL
            ORDER BY b.name ASC
            """)
    List<Business> findActiveBusinessesByUserId(Integer userId);

    @Query("""
            SELECT DISTINCT bu.business
            FROM BusinessUser bu
            JOIN bu.role r
            JOIN bu.business b
            WHERE bu.user.id = :userId
              AND bu.active = true
              AND bu.deletedAt IS NULL
              AND r.active = true
              AND b.active = true
              AND b.deletedAt IS NULL
            ORDER BY b.name ASC
            """)
    Page<Business> findActiveBusinessesByUserId(Integer userId, Pageable pageable);

    @Query("""
            SELECT DISTINCT r.code
            FROM BusinessUser bu
            JOIN bu.role r
            JOIN bu.business b
            WHERE bu.user.id = :userId
              AND bu.active = true
              AND bu.deletedAt IS NULL
              AND r.active = true
              AND b.active = true
              AND b.deletedAt IS NULL
            ORDER BY r.code
            """)
    List<String> findActiveRoleCodesByUserId(Integer userId);

    @Query("""
            SELECT DISTINCT p.code
            FROM BusinessUser bu
            JOIN bu.role r
            JOIN RolePermission rp ON rp.role = r
            JOIN rp.permission p
            JOIN bu.business b
            WHERE bu.user.id = :userId
              AND bu.active = true
              AND bu.deletedAt IS NULL
              AND r.active = true
              AND rp.active = true
              AND p.active = true
              AND b.active = true
              AND b.deletedAt IS NULL
            ORDER BY p.code
            """)
    List<String> findActivePermissionCodesByUserId(Integer userId);

    @Query("""
            SELECT DISTINCT bu.user.id
            FROM BusinessUser bu
            WHERE bu.role.id = :roleId
              AND bu.active = true
              AND bu.deletedAt IS NULL
              AND bu.user.active = true
            """)
    List<Integer> findActiveUserIdsByRoleId(Integer roleId);
}
