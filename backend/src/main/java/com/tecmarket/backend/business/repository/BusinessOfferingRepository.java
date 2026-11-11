package com.tecmarket.backend.business.repository;

import com.tecmarket.backend.business.entity.BusinessOffering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BusinessOfferingRepository extends JpaRepository<BusinessOffering, Integer> {

    List<BusinessOffering> findByBusinessIdAndDeletedAtIsNullOrderByNameAsc(Integer businessId);

    Page<BusinessOffering> findByBusinessIdAndDeletedAtIsNullOrderByNameAsc(Integer businessId, Pageable pageable);

    Optional<BusinessOffering> findByIdAndBusinessIdAndDeletedAtIsNull(Integer id, Integer businessId);

    boolean existsByBusinessIdAndServiceCategoryIdAndDeviceTypeIdAndNameAndDeletedAtIsNull(
            Integer businessId,
            Integer serviceCategoryId,
            Integer deviceTypeId,
            String name);
}
