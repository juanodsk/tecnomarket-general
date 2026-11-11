package com.tecmarket.backend.permission.repository;

import com.tecmarket.backend.permission.entity.PermissionScope;
import com.tecmarket.backend.permission.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Integer> {

  boolean existsByRoleCodeAndPermissionCode(String roleCode, String permissionCode);

  List<RolePermission> findByRoleCodeAndActiveTrue(String roleCode);

  List<RolePermission> findByRoleId(Integer roleId);

  List<RolePermission> findByRoleIdAndActiveTrue(Integer roleId);

  Optional<RolePermission> findByRoleIdAndPermissionId(Integer roleId, Integer permissionId);

  @Query("""
      select distinct p.code
      from RolePermission rp
      join rp.permission p
      join rp.role r
      where r.code = :roleCode
        and r.active = true
        and p.active = true
        and rp.active = true
      order by p.code
      """)
  List<String> findActivePermissionCodesByRoleCode(String roleCode);

  @Query("""
      select distinct p.code
      from RolePermission rp
      join rp.permission p
      join rp.role r
      where r.code = :roleCode
        and r.active = true
        and r.deletedAt is null
        and p.active = true
        and p.deletedAt is null
        and p.scope = :scope
        and rp.active = true
        and rp.deletedAt is null
      order by p.code
      """)
  List<String> findActivePermissionCodesByRoleCodeAndScope(String roleCode, PermissionScope scope);
}
