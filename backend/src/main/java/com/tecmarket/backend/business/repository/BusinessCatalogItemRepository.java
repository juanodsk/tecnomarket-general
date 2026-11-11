package com.tecmarket.backend.business.repository;

import com.tecmarket.backend.business.entity.BusinessCatalogItem;
import com.tecmarket.backend.business.entity.BusinessCatalogType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BusinessCatalogItemRepository extends JpaRepository<BusinessCatalogItem, Integer> {

    List<BusinessCatalogItem> findByBusinessIdAndDeletedAtIsNull(Integer businessId);

    List<BusinessCatalogItem> findByBusinessIdAndCatalogTypeAndDeletedAtIsNullOrderBySortOrderAscNameAscIdAsc(
            Integer businessId,
            BusinessCatalogType catalogType);

    Optional<BusinessCatalogItem> findByIdAndBusinessIdAndCatalogTypeAndDeletedAtIsNull(
            Integer id,
            Integer businessId,
            BusinessCatalogType catalogType);

    Optional<BusinessCatalogItem> findByBusinessIdAndCatalogTypeAndCodeAndDeletedAtIsNull(
            Integer businessId,
            BusinessCatalogType catalogType,
            String code);

    boolean existsByBusinessIdAndCatalogTypeAndCodeAndDeletedAtIsNull(
            Integer businessId,
            BusinessCatalogType catalogType,
            String code);

    boolean existsByBusinessIdAndCatalogTypeAndCodeAndDeletedAtIsNullAndIdNot(
            Integer businessId,
            BusinessCatalogType catalogType,
            String code,
            Integer id);
}
