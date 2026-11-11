package com.tecmarket.backend.repairorder.service;

import com.tecmarket.backend.repairorder.dto.RepairOrderCommissionResponse;
import com.tecmarket.backend.repairorder.entity.RepairOrderCommission;
import com.tecmarket.backend.repairorder.entity.RepairOrderCommissionStatus;
import com.tecmarket.backend.repairorder.repository.RepairOrderCommissionRepository;
import com.tecmarket.backend.repairorder.repository.RepairOrderRepository;
import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RepairOrderCommissionService {

    private final RepairOrderCommissionRepository commissionRepository;
    private final RepairOrderRepository repairOrderRepository;
    private final BusinessAccessGuard businessAccessGuard;

    public RepairOrderCommissionService(
            RepairOrderCommissionRepository commissionRepository,
            RepairOrderRepository repairOrderRepository,
            BusinessAccessGuard businessAccessGuard) {
        this.commissionRepository = commissionRepository;
        this.repairOrderRepository = repairOrderRepository;
        this.businessAccessGuard = businessAccessGuard;
    }

    @Transactional(readOnly = true)
    public List<RepairOrderCommissionResponse> findAll(
            Integer businessId,
            Integer businessUserId,
            RepairOrderCommissionStatus status,
            String subject) {
        assertCanView(businessId, subject);

        List<RepairOrderCommission> commissions;
        if (businessUserId != null && status != null) {
            commissions = commissionRepository
                    .findByBusinessIdAndBusinessUserIdAndStatusAndDeletedAtIsNullOrderByGeneratedAtDescIdDesc(
                            businessId, businessUserId, status);
        } else if (businessUserId != null) {
            commissions = commissionRepository
                    .findByBusinessIdAndBusinessUserIdAndDeletedAtIsNullOrderByGeneratedAtDescIdDesc(
                            businessId, businessUserId);
        } else if (status != null) {
            commissions = commissionRepository
                    .findByBusinessIdAndStatusAndDeletedAtIsNullOrderByGeneratedAtDescIdDesc(businessId, status);
        } else {
            commissions = commissionRepository
                    .findByBusinessIdAndDeletedAtIsNullOrderByGeneratedAtDescIdDesc(businessId);
        }

        return commissions.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public RepairOrderCommissionResponse findById(Integer businessId, Integer commissionId, String subject) {
        assertCanView(businessId, subject);
        return toResponse(findCommission(businessId, commissionId));
    }

    @Transactional(readOnly = true)
    public RepairOrderCommissionResponse findByRepairOrder(
            Integer businessId,
            Integer repairOrderId,
            String subject) {
        assertCanView(businessId, subject);
        repairOrderRepository.findByIdAndBusinessIdAndDeletedAtIsNull(repairOrderId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("repair_order.not_found"));

        RepairOrderCommission commission = commissionRepository
                .findByRepairOrderIdAndBusinessIdAndDeletedAtIsNull(repairOrderId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("repair_order.commission.not_found"));
        return toResponse(commission);
    }

    RepairOrderCommissionResponse toResponse(RepairOrderCommission entity) {
        return new RepairOrderCommissionResponse(
                entity.getId(),
                entity.getBusiness().getId(),
                entity.getSettlement() != null ? entity.getSettlement().getId() : null,
                entity.getSettlement() != null ? entity.getSettlement().getSettlementCode() : null,
                entity.getRepairOrder().getId(),
                entity.getRepairOrder().getOrderNumber(),
                entity.getBusinessUser().getId(),
                entity.getBusinessUser().getUser().getId(),
                entity.getBusinessUser().getUser().getName() + " "
                        + entity.getBusinessUser().getUser().getSurname(),
                entity.getBusinessUser().getUser().getEmail(),
                entity.getOrderTotal(),
                entity.getPartsTotal(),
                entity.getCommissionBase(),
                entity.getCommissionPercentage(),
                entity.getCommissionAmount(),
                entity.getStatus(),
                entity.getGeneratedAt(),
                entity.getApprovedAt(),
                entity.getPaidAt());
    }

    private void assertCanView(Integer businessId, String subject) {
        businessAccessGuard.assertHasBusinessPermission(
                businessId,
                subject,
                "BUSINESS_COMMISSION_VIEW",
                "BUSINESS_COMMISSION_MANAGE");
    }

    private RepairOrderCommission findCommission(Integer businessId, Integer commissionId) {
        return commissionRepository.findByIdAndBusinessIdAndDeletedAtIsNull(commissionId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("repair_order.commission.not_found"));
    }
}
