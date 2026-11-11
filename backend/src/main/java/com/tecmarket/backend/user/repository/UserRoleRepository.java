package com.tecmarket.backend.user.repository;

import com.tecmarket.backend.user.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

  boolean existsByUserIdAndRoleCode(Integer userId, String roleCode);

  Optional<UserRole> findByUserIdAndRoleCode(Integer userId, String roleCode);

  List<UserRole> findByUserId(Integer userId);

  List<UserRole> findByUserIdAndActiveTrue(Integer userId);

  @Query("""
      select distinct ur.user.id
      from UserRole ur
      where ur.role.id = :roleId
        and ur.active = true
        and ur.user.active = true
      """)
  List<Integer> findActiveUserIdsByRoleId(Integer roleId);

  @Query("""
      select distinct r.code
      from UserRole ur
      join ur.role r
      where ur.user.id = :userId
        and ur.active = true
        and r.active = true
      order by r.code
      """)
  List<String> findActiveRoleCodesByUserId(Integer userId);

  @Query("""
      select distinct p.code
      from UserRole ur
      join ur.role r
      join RolePermission rp on rp.role = r
      join rp.permission p
      where ur.user.id = :userId
        and ur.active = true
        and r.active = true
        and rp.active = true
        and p.active = true
      order by p.code
      """)
  List<String> findActivePermissionCodesByUserId(Integer userId);
}
