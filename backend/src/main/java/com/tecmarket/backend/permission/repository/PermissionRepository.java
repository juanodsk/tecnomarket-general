package com.tecmarket.backend.permission.repository;

import com.tecmarket.backend.permission.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    Optional<Permission> findByCode(String code);

    Optional<Permission> findByCodeAndActiveTrue(String code);

    boolean existsByCode(String code);
    boolean existsByCodeAndDeletedAtIsNull(String code);

    List<Permission> findAllByActiveTrueOrderByCodeAsc();
    List<Permission> findAllByDeletedAtIsNullOrderByCodeAsc();

    List<Permission> findByAppModuleCodeAndActiveTrueOrderByCodeAsc(String moduleCode);

    List<Permission> findByAppModuleIdAndActiveTrueOrderByCodeAsc(Integer moduleId);

    List<Permission> findByIdInAndActiveTrue(List<Integer> ids);

}
