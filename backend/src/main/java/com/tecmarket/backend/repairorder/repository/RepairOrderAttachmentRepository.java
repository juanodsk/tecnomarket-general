package com.tecmarket.backend.repairorder.repository;

import com.tecmarket.backend.repairorder.entity.RepairOrderAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RepairOrderAttachmentRepository extends JpaRepository<RepairOrderAttachment, Integer> {

    Page<RepairOrderAttachment> findByRepairOrderIdAndRepairOrderBusinessIdAndDeletedAtIsNullOrderByCreatedAtDescIdDesc(
            Integer repairOrderId,
            Integer businessId,
            Pageable pageable);

    Optional<RepairOrderAttachment> findByIdAndRepairOrderIdAndRepairOrderBusinessIdAndDeletedAtIsNull(
            Integer id,
            Integer repairOrderId,
            Integer businessId);
}
