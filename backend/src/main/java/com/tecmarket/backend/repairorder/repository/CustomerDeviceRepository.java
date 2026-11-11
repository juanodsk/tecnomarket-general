package com.tecmarket.backend.repairorder.repository;

import com.tecmarket.backend.repairorder.entity.CustomerDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.List;

public interface CustomerDeviceRepository extends JpaRepository<CustomerDevice, Integer> {

    Page<CustomerDevice> findByBusinessIdAndDeletedAtIsNullOrderByRegisteredAtDescIdDesc(
            Integer businessId, Pageable pageable);

    Optional<CustomerDevice> findByIdAndBusinessIdAndDeletedAtIsNull(Integer id, Integer businessId);

    List<CustomerDevice> findByBusinessIdAndImeiAndActiveTrueAndDeletedAtIsNullOrderByIdAsc(
            Integer businessId,
            String imei);

    boolean existsByBusinessIdAndCustomerIdAndImeiAndDeletedAtIsNull(
            Integer businessId,
            Integer customerId,
            String imei);

    boolean existsByBusinessIdAndCustomerIdAndImeiAndDeletedAtIsNullAndIdNot(
            Integer businessId,
            Integer customerId,
            String imei,
            Integer id);
}
