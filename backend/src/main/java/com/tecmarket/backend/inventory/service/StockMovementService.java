package com.tecmarket.backend.inventory.service;

import com.tecmarket.backend.inventory.dto.CreateStockMovementRequest;
import com.tecmarket.backend.inventory.dto.StockMovementResponse;
import com.tecmarket.backend.inventory.entity.StockMovement;
import com.tecmarket.backend.inventory.repository.StockMovementRepository;
import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.business.security.BusinessBranchAccessGuard;
import com.tecmarket.backend.business.entity.BusinessUser;
import com.tecmarket.backend.business.repository.BusinessUserRepository;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.common.pagination.PageRequestFactory;
import com.tecmarket.backend.common.pagination.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.util.IdempotencySupport;

@Service
public class StockMovementService {

    private final StockMovementRepository stockMovementRepository;
    private final InventoryStockService inventoryStockService;
    private final BusinessAccessGuard businessAccessGuard;
    private final BusinessBranchAccessGuard businessBranchAccessGuard;
    private final BusinessUserRepository businessUserRepository;

    public StockMovementService(
            StockMovementRepository stockMovementRepository,
            InventoryStockService inventoryStockService,
            BusinessAccessGuard businessAccessGuard,
            BusinessBranchAccessGuard businessBranchAccessGuard,
            BusinessUserRepository businessUserRepository) {
        this.stockMovementRepository = stockMovementRepository;
        this.inventoryStockService = inventoryStockService;
        this.businessAccessGuard = businessAccessGuard;
        this.businessBranchAccessGuard = businessBranchAccessGuard;
        this.businessUserRepository = businessUserRepository;
    }

    @Transactional(readOnly = true)
    public PageResponse<StockMovementResponse> findAllByBusiness(
            Integer businessId, Integer inventoryItemId, Integer page, Integer size, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "INVENTORY_VIEW");

        List<Integer> branchIds = businessBranchAccessGuard.accessibleBranchIds(businessId, subject);
        Page<StockMovement> result = inventoryItemId == null
                ? stockMovementRepository.findByInventoryItemBusinessIdAndBranchIdInAndDeletedAtIsNullOrderByCreatedAtDescIdDesc(
                        businessId, branchIds, PageRequestFactory.of(page, size))
                : stockMovementRepository
                        .findByInventoryItemIdAndInventoryItemBusinessIdAndBranchIdInAndDeletedAtIsNullOrderByCreatedAtDescIdDesc(
                                inventoryItemId,
                                businessId,
                                branchIds,
                                PageRequestFactory.of(page, size));

        return PageResponse.from(result.map(this::toResponse));
    }

    @Transactional
    public StockMovementResponse create(Integer businessId, CreateStockMovementRequest request, String subject) {
        return create(businessId, request, subject, null);
    }

    @Transactional
    public StockMovementResponse create(
            Integer businessId,
            CreateStockMovementRequest request,
            String subject,
            String rawIdempotencyKey) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "INVENTORY_UPDATE");
        businessBranchAccessGuard.findAccessibleBranchForUpdate(businessId, request.branchId(), subject);
        String idempotencyKey = IdempotencySupport.normalize(rawIdempotencyKey);
        String fingerprint = IdempotencySupport.fingerprint(subject, request);
        if (idempotencyKey != null) {
            StockMovement existing = stockMovementRepository
                    .findByBranchIdAndIdempotencyKeyAndDeletedAtIsNull(request.branchId(), idempotencyKey)
                    .orElse(null);
            if (existing != null) {
                if (!fingerprint.equals(existing.getIdempotencyFingerprint())) {
                    throw new ConflictException("idempotency.key.reused");
                }
                return toResponse(existing);
            }
        }
        BusinessUser actor = findActor(businessId, subject);
        businessBranchAccessGuard.assertBusinessUserAssigned(businessId, request.branchId(), actor.getId());
        StockMovement saved = inventoryStockService.applyMovement(
                businessId,
                request.branchId(),
                request.inventoryItemId(),
                request.movementTypeId(),
                request.quantity(),
                actor.getId(),
                request.repairOrderId(),
                null,
                request.notes());
        saved.setIdempotencyKey(idempotencyKey);
        saved.setIdempotencyFingerprint(idempotencyKey == null ? null : fingerprint);
        return toResponse(saved);
    }

    private BusinessUser findActor(Integer businessId, String subject) {
        Integer userId;
        try {
            userId = Integer.valueOf(subject);
        } catch (NumberFormatException exception) {
            throw new BadCredentialsException("auth.token.invalid");
        }
        return businessUserRepository
                .findByBusinessIdAndUserIdAndActiveTrueAndDeletedAtIsNull(businessId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("business_user.not_found"));
    }

    private StockMovementResponse toResponse(StockMovement movement) {
        return new StockMovementResponse(
                movement.getId(),
                movement.getBranch().getId(),
                movement.getBranch().getName(),
                movement.getInventoryItem().getId(),
                movement.getInventoryItem().getSku(),
                movement.getInventoryItem().getName(),
                movement.getMovementType().getId(),
                movement.getMovementType().getCode(),
                movement.getMovementType().getName(),
                movement.getQuantity(),
                movement.getBusinessUser().getId(),
                movement.getBusinessUser().getUser().getName() + " "
                        + movement.getBusinessUser().getUser().getSurname(),
                movement.getRepairOrder() != null ? movement.getRepairOrder().getId() : null,
                movement.getSaleItem() != null ? movement.getSaleItem().getId() : null,
                movement.getNotes(),
                movement.getCreatedAt());
    }
}
