package com.tecmarket.backend.repairorder.repository;

import com.tecmarket.backend.repairorder.entity.BusinessQualityTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BusinessQualityTestRepository extends JpaRepository<BusinessQualityTest, Integer> {

    List<BusinessQualityTest> findByBusinessIdAndDeletedAtIsNullOrderByDeviceTypeIdAscSortOrderAscNameAsc(Integer businessId);

    Page<BusinessQualityTest> findByBusinessIdAndDeletedAtIsNullOrderByDeviceTypeIdAscSortOrderAscNameAsc(
            Integer businessId, Pageable pageable);

    Optional<BusinessQualityTest> findByIdAndBusinessIdAndDeletedAtIsNull(Integer id, Integer businessId);

    boolean existsByBusinessIdAndDeviceTypeIdAndCodeAndDeletedAtIsNull(Integer businessId, Integer deviceTypeId, String code);

    boolean existsByBusinessIdAndDeviceTypeIdAndCodeAndDeletedAtIsNullAndIdNot(
            Integer businessId,
            Integer deviceTypeId,
            String code,
            Integer id);
}
