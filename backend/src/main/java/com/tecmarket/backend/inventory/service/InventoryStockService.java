package com.tecmarket.backend.inventory.service;

import com.tecmarket.backend.business.entity.*;
import com.tecmarket.backend.business.repository.BusinessUserRepository;
import com.tecmarket.backend.inventory.repository.InventoryItemRepository;
import com.tecmarket.backend.repairorder.repository.RepairOrderRepository;
import com.tecmarket.backend.inventory.repository.StockMovementRepository;
import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collection;

import com.tecmarket.backend.business.service.BusinessCatalogLookupService;
import com.tecmarket.backend.inventory.entity.InventoryItem;
import com.tecmarket.backend.inventory.entity.StockMovement;
import com.tecmarket.backend.repairorder.entity.RepairOrder;
import com.tecmarket.backend.sale.entity.Sale;
import com.tecmarket.backend.sale.entity.SaleItem;
@Service
public class InventoryStockService {

    private static final String MOVEMENT_IN = "IN";
    private static final String MOVEMENT_OUT = "OUT";
    private static final String MOVEMENT_ADJUSTMENT = "ADJUSTMENT";
    private static final String MOVEMENT_RETURN = "RETURN";

    private final InventoryItemRepository inventoryItemRepository;
    private final BusinessCatalogLookupService businessCatalogLookupService;
    private final BusinessUserRepository businessUserRepository;
    private final RepairOrderRepository repairOrderRepository;
    private final StockMovementRepository stockMovementRepository;

    public InventoryStockService(
            InventoryItemRepository inventoryItemRepository,
            BusinessCatalogLookupService businessCatalogLookupService,
            BusinessUserRepository businessUserRepository,
            RepairOrderRepository repairOrderRepository,
            StockMovementRepository stockMovementRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
        this.businessCatalogLookupService = businessCatalogLookupService;
        this.businessUserRepository = businessUserRepository;
        this.repairOrderRepository = repairOrderRepository;
        this.stockMovementRepository = stockMovementRepository;
    }

    @Transactional
    public StockMovement applyMovement(
            Integer businessId,
            Integer branchId,
            Integer inventoryItemId,
            Integer movementTypeId,
            Integer quantity,
            Integer businessUserId,
            Integer repairOrderId,
            SaleItem saleItem,
            String notes) {
        BusinessCatalogItem movementType = findActiveMovementType(businessId, movementTypeId);
        return applyMovementInternal(
                businessId,
                branchId,
                inventoryItemId,
                movementType,
                quantity,
                businessUserId,
                repairOrderId,
                saleItem,
                notes);
    }

    @Transactional
    public StockMovement applyMovementByCode(
            Integer businessId,
            Integer branchId,
            Integer inventoryItemId,
            String movementCode,
            Integer quantity,
            Integer businessUserId,
            Integer repairOrderId,
            SaleItem saleItem,
            String notes) {
        BusinessCatalogItem movementType = businessCatalogLookupService.findActiveByCode(
                businessId,
                BusinessCatalogType.MOVEMENT_TYPE,
                movementCode);

        return applyMovementInternal(
                businessId,
                branchId,
                inventoryItemId,
                movementType,
                quantity,
                businessUserId,
                repairOrderId,
                saleItem,
                notes);
    }

    @Transactional
    public void lockItemsForUpdate(Integer businessId, Collection<Integer> inventoryItemIds) {
        inventoryItemIds.stream()
                .distinct()
                .sorted()
                .forEach(inventoryItemId -> inventoryItemRepository
                        .findForUpdateByIdAndBusinessId(inventoryItemId, businessId)
                        .filter(item -> Boolean.TRUE.equals(item.getActive()))
                        .orElseThrow(() -> new ResourceNotFoundException("inventory_item.not_found")));
    }

    private StockMovement applyMovementInternal(
            Integer businessId,
            Integer branchId,
            Integer inventoryItemId,
            BusinessCatalogItem movementType,
            Integer quantity,
            Integer businessUserId,
            Integer repairOrderId,
            SaleItem saleItem,
            String notes) {
        InventoryItem inventoryItem = inventoryItemRepository.findForUpdateByIdAndBusinessId(inventoryItemId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("inventory_item.not_found"));
        if (!Boolean.TRUE.equals(inventoryItem.getActive())) {
            throw new ResourceNotFoundException("inventory_item.not_found");
        }
        if (!inventoryItem.getBranch().getId().equals(branchId)) {
            throw new ConflictException("inventory_item.branch_mismatch");
        }

        BusinessUser businessUser = businessUserRepository.findByIdAndBusinessIdAndDeletedAtIsNull(businessUserId, businessId)
                .filter(value -> Boolean.TRUE.equals(value.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("business_user.not_found"));

        RepairOrder repairOrder = null;
        if (repairOrderId != null) {
            repairOrder = repairOrderRepository.findByIdAndBusinessIdAndDeletedAtIsNull(repairOrderId, businessId)
                    .orElseThrow(() -> new ResourceNotFoundException("repair_order.not_found"));
            if (!repairOrder.getBranch().getId().equals(branchId)) {
                throw new ConflictException("repair_order.branch_mismatch");
            }
        }
        if (saleItem != null && !saleItem.getSale().getBranch().getId().equals(branchId)) {
            throw new ConflictException("sale.branch_mismatch");
        }

        Integer currentStock = inventoryItem.getQuantity();
        int delta = resolveDelta(movementType.getCode(), quantity);
        int nextStock = currentStock + delta;
        if (nextStock < 0) {
            throw new ConflictException("inventory.stock.insufficient");
        }
        inventoryItem.setQuantity(nextStock);

        StockMovement movement = new StockMovement();
        movement.setBranch(inventoryItem.getBranch());
        movement.setInventoryItem(inventoryItem);
        movement.setMovementType(movementType);
        movement.setQuantity(quantity);
        movement.setBusinessUser(businessUser);
        movement.setRepairOrder(repairOrder);
        movement.setSaleItem(saleItem);
        movement.setNotes(normalizeNullable(notes));
        movement.setActive(true);
        movement.setDeletedAt(null);
        return stockMovementRepository.save(movement);
    }

    private BusinessCatalogItem findActiveMovementType(Integer businessId, Integer movementTypeId) {
        return businessCatalogLookupService.findActive(
                businessId,
                movementTypeId,
                BusinessCatalogType.MOVEMENT_TYPE);
    }

    private int resolveDelta(String movementTypeCode, Integer quantity) {
        String normalized = movementTypeCode == null ? "" : movementTypeCode.trim().toUpperCase();
        return switch (normalized) {
            case MOVEMENT_OUT -> -quantity;
            case MOVEMENT_IN, MOVEMENT_RETURN, MOVEMENT_ADJUSTMENT -> quantity;
            default -> throw new ConflictException("stock_movement.type.unsupported");
        };
    }

    private String normalizeNullable(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }
}
