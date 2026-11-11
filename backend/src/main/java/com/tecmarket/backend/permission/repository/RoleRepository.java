package com.tecmarket.backend.permission.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tecmarket.backend.permission.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByCode(String code);

    Optional<Role> findByCodeAndActiveTrue(String code);

    boolean existsByCode(String code);
    boolean existsByCodeAndDeletedAtIsNull(String code);

    List<Role> findAllByActiveTrueOrderByNameAsc();
    List<Role> findAllByDeletedAtIsNullOrderByNameAsc();

    List<Role> findByIdInAndActiveTrue(List<Integer> ids);
    List<Role> findAllByActiveTrueAndScopeOrderByNameAsc(String scope);
    List<Role> findByIdInAndActiveTrueAndScope(List<Integer> ids, String scope);

}
