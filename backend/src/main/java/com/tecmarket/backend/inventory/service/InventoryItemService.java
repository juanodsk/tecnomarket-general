package com.tecmarket.backend.inventory.service;

import com.tecmarket.backend.inventory.dto.CreateInventoryItemRequest;
import com.tecmarket.backend.inventory.dto.InventoryItemResponse;
import com.tecmarket.backend.inventory.dto.UpdateInventoryItemRequest;
import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.business.entity.BusinessCatalogItem;
import com.tecmarket.backend.business.entity.BusinessCatalogType;
import com.tecmarket.backend.inventory.entity.InventoryItem;
import com.tecmarket.backend.business.entity.BusinessBranch;
import com.tecmarket.backend.business.repository.BusinessRepository;
import com.tecmarket.backend.inventory.repository.InventoryItemRepository;
import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.business.security.BusinessBranchAccessGuard;
import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.common.pagination.PageRequestFactory;
import com.tecmarket.backend.common.pagination.PageResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.RoundingMode;
import java.time.LocalDateTime;
import com.tecmarket.backend.business.service.BusinessCatalogLookupService;
@Service
public class InventoryItemService {

    private final InventoryItemRepository inventoryItemRepository;
    private final BusinessRepository businessRepository;
    private final BusinessCatalogLookupService businessCatalogLookupService;
    private final BusinessAccessGuard businessAccessGuard;
    private final BusinessBranchAccessGuard businessBranchAccessGuard;

    public InventoryItemService(
            InventoryItemRepository inventoryItemRepository,
            BusinessRepository businessRepository,
            BusinessCatalogLookupService businessCatalogLookupService,
            BusinessAccessGuard businessAccessGuard,
            BusinessBranchAccessGuard businessBranchAccessGuard) {
        this.inventoryItemRepository = inventoryItemRepository;
        this.businessRepository = businessRepository;
        this.businessCatalogLookupService = businessCatalogLookupService;
        this.businessAccessGuard = businessAccessGuard;
        this.businessBranchAccessGuard = businessBranchAccessGuard;
    }

    @Transactional(readOnly = true)
    public PageResponse<InventoryItemResponse> findAllByBusiness(
            Integer businessId, Integer page, Integer size, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "INVENTORY_VIEW");
        findBusiness(businessId);
        boolean includeCost = businessAccessGuard.hasBusinessPermission(businessId, subject, "INVENTORY_COST_VIEW");
        return PageResponse.from(inventoryItemRepository
                .findByBusinessIdAndBranchIdInAndDeletedAtIsNullOrderByNameAsc(
                        businessId,
                        businessBranchAccessGuard.accessibleBranchIds(businessId, subject),
                        PageRequestFactory.of(page, size))
                .map(item -> toResponse(item, includeCost)));
    }

    @Transactional(readOnly = true)
    public InventoryItemResponse findById(Integer businessId, Integer inventoryItemId, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "INVENTORY_VIEW");
        InventoryItem inventoryItem = findInventoryItem(businessId, inventoryItemId);
        businessBranchAccessGuard.findAccessibleBranch(businessId, inventoryItem.getBranch().getId(), subject);
        return toResponse(inventoryItem,
                businessAccessGuard.hasBusinessPermission(businessId, subject, "INVENTORY_COST_VIEW"));
    }

    @Transactional
    public InventoryItemResponse create(Integer businessId, CreateInventoryItemRequest request, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "INVENTORY_CREATE");
        Business business = findBusiness(businessId);
        BusinessBranch branch = businessBranchAccessGuard.findAccessibleBranch(businessId, request.branchId(), subject);
        BusinessCatalogItem itemCategory = findActiveItemCategory(businessId, request.itemCategoryId());
        String sku = normalizeSku(request.sku());

        if (inventoryItemRepository.existsByBranchIdAndSkuAndDeletedAtIsNull(branch.getId(), sku)) {
            throw new ConflictException("inventory_item.sku.exists");
        }

        InventoryItem inventoryItem = new InventoryItem();
        inventoryItem.setBusiness(business);
        inventoryItem.setBranch(branch);
        inventoryItem.setItemCategory(itemCategory);
        inventoryItem.setName(request.name().trim());
        inventoryItem.setDescription(normalizeNullable(request.description()));
        inventoryItem.setSku(sku);
        inventoryItem.setQuantity(request.quantity());
        inventoryItem.setMinimumStock(request.minimumStock());
        inventoryItem.setUnitCost(request.unitCost().setScale(2, RoundingMode.HALF_UP));
        inventoryItem.setSalePrice(request.salePrice().setScale(2, RoundingMode.HALF_UP));
        inventoryItem.setActive(true);
        inventoryItem.setDeletedAt(null);

        return toResponse(inventoryItemRepository.save(inventoryItem),
                businessAccessGuard.hasBusinessPermission(businessId, subject, "INVENTORY_COST_VIEW"));
    }

    @Transactional
    public InventoryItemResponse update(
            Integer businessId,
            Integer inventoryItemId,
            UpdateInventoryItemRequest request,
            String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "INVENTORY_UPDATE");
        InventoryItem inventoryItem = findInventoryItem(businessId, inventoryItemId);
        businessBranchAccessGuard.findAccessibleBranch(businessId, inventoryItem.getBranch().getId(), subject);
        BusinessCatalogItem itemCategory = findActiveItemCategory(businessId, request.itemCategoryId());
        String sku = normalizeSku(request.sku());

        boolean skuChanged = !inventoryItem.getSku().equals(sku);
        if (skuChanged && inventoryItemRepository.existsByBranchIdAndSkuAndDeletedAtIsNullAndIdNot(
                inventoryItem.getBranch().getId(), sku, inventoryItemId)) {
            throw new ConflictException("inventory_item.sku.exists");
        }

        inventoryItem.setItemCategory(itemCategory);
        inventoryItem.setName(request.name().trim());
        inventoryItem.setDescription(normalizeNullable(request.description()));
        inventoryItem.setSku(sku);
        inventoryItem.setQuantity(request.quantity());
        inventoryItem.setMinimumStock(request.minimumStock());
        inventoryItem.setUnitCost(request.unitCost().setScale(2, RoundingMode.HALF_UP));
        inventoryItem.setSalePrice(request.salePrice().setScale(2, RoundingMode.HALF_UP));

        if (request.active() != null) {
            inventoryItem.setActive(request.active());
            inventoryItem.setDeletedAt(Boolean.FALSE.equals(request.active()) ? LocalDateTime.now() : null);
        }

        return toResponse(inventoryItem,
                businessAccessGuard.hasBusinessPermission(businessId, subject, "INVENTORY_COST_VIEW"));
    }

    @Transactional
    public void delete(Integer businessId, Integer inventoryItemId, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "INVENTORY_DELETE");
        InventoryItem inventoryItem = findInventoryItem(businessId, inventoryItemId);
        businessBranchAccessGuard.findAccessibleBranch(businessId, inventoryItem.getBranch().getId(), subject);
        inventoryItem.setActive(false);
        inventoryItem.setDeletedAt(LocalDateTime.now());
    }

    private Business findBusiness(Integer businessId) {
        return businessRepository.findByIdAndDeletedAtIsNull(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("business.not_found"));
    }

    private InventoryItem findInventoryItem(Integer businessId, Integer inventoryItemId) {
        return inventoryItemRepository.findByIdAndBusinessIdAndDeletedAtIsNull(inventoryItemId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("inventory_item.not_found"));
    }

    private BusinessCatalogItem findActiveItemCategory(Integer businessId, Integer itemCategoryId) {
        return businessCatalogLookupService.findActive(
                businessId,
                itemCategoryId,
                BusinessCatalogType.ITEM_CATEGORY);
    }

    private InventoryItemResponse toResponse(InventoryItem inventoryItem, boolean includeCost) {
        return new InventoryItemResponse(
                inventoryItem.getId(),
                inventoryItem.getBusiness().getId(),
                inventoryItem.getBranch().getId(),
                inventoryItem.getBranch().getName(),
                inventoryItem.getName(),
                inventoryItem.getItemCategory().getId(),
                inventoryItem.getItemCategory().getCode(),
                inventoryItem.getItemCategory().getName(),
                inventoryItem.getDescription(),
                inventoryItem.getSku(),
                inventoryItem.getQuantity(),
                inventoryItem.getMinimumStock(),
                includeCost ? inventoryItem.getUnitCost() : null,
                inventoryItem.getSalePrice(),
                inventoryItem.getActive(),
                inventoryItem.getCreatedAt());
    }

    private String normalizeSku(String value) {
        return value.trim().toUpperCase();
    }

    private String normalizeNullable(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }
}
