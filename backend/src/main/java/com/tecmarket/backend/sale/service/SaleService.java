package com.tecmarket.backend.sale.service;

import com.tecmarket.backend.sale.dto.CreateSaleItemRequest;
import com.tecmarket.backend.sale.dto.CreateSaleRequest;
import com.tecmarket.backend.sale.dto.SaleItemResponse;
import com.tecmarket.backend.sale.dto.SaleResponse;
import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.business.entity.BusinessBranch;
import com.tecmarket.backend.business.entity.BusinessCatalogItem;
import com.tecmarket.backend.business.entity.BusinessCatalogType;
import com.tecmarket.backend.business.entity.BusinessUser;
import com.tecmarket.backend.inventory.entity.InventoryItem;
import com.tecmarket.backend.sale.entity.Sale;
import com.tecmarket.backend.sale.entity.SaleItem;
import com.tecmarket.backend.business.repository.*;
import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.business.security.BusinessBranchAccessGuard;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.common.pagination.PageRequestFactory;
import com.tecmarket.backend.common.pagination.PageResponse;
import com.tecmarket.backend.user.entity.AppUser;
import com.tecmarket.backend.user.repository.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.util.IdempotencySupport;

import com.tecmarket.backend.business.service.BusinessCatalogLookupService;
import com.tecmarket.backend.inventory.repository.InventoryItemRepository;
import com.tecmarket.backend.sale.repository.SaleItemRepository;
import com.tecmarket.backend.sale.repository.SaleRepository;
import com.tecmarket.backend.inventory.service.InventoryStockService;
@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final SaleItemRepository saleItemRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final BusinessRepository businessRepository;
    private final BusinessUserRepository businessUserRepository;
    private final BusinessCatalogLookupService businessCatalogLookupService;
    private final AppUserRepository appUserRepository;
    private final InventoryStockService inventoryStockService;
    private final BusinessAccessGuard businessAccessGuard;
    private final BusinessBranchAccessGuard businessBranchAccessGuard;

    public SaleService(
            SaleRepository saleRepository,
            SaleItemRepository saleItemRepository,
            InventoryItemRepository inventoryItemRepository,
            BusinessRepository businessRepository,
            BusinessUserRepository businessUserRepository,
            BusinessCatalogLookupService businessCatalogLookupService,
            AppUserRepository appUserRepository,
            InventoryStockService inventoryStockService,
            BusinessAccessGuard businessAccessGuard,
            BusinessBranchAccessGuard businessBranchAccessGuard) {
        this.saleRepository = saleRepository;
        this.saleItemRepository = saleItemRepository;
        this.inventoryItemRepository = inventoryItemRepository;
        this.businessRepository = businessRepository;
        this.businessUserRepository = businessUserRepository;
        this.businessCatalogLookupService = businessCatalogLookupService;
        this.appUserRepository = appUserRepository;
        this.inventoryStockService = inventoryStockService;
        this.businessAccessGuard = businessAccessGuard;
        this.businessBranchAccessGuard = businessBranchAccessGuard;
    }

    @Transactional(readOnly = true)
    public PageResponse<SaleResponse> findAllByBusiness(
            Integer businessId, Integer page, Integer size, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "SALE_VIEW");
        return PageResponse.from(saleRepository
                .findByBusinessIdAndBranchIdInAndDeletedAtIsNullOrderByCreatedAtDescIdDesc(
                        businessId,
                        businessBranchAccessGuard.accessibleBranchIds(businessId, subject),
                        PageRequestFactory.of(page, size))
                .map(this::toResponseWithoutItems));
    }

    @Transactional(readOnly = true)
    public SaleResponse findById(Integer businessId, Integer saleId, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "SALE_VIEW");
        Sale sale = findSale(businessId, saleId);
        businessBranchAccessGuard.findAccessibleBranch(businessId, sale.getBranch().getId(), subject);
        List<SaleItem> items = saleItemRepository.findBySaleIdAndSaleBusinessIdAndDeletedAtIsNullOrderByCreatedAtAscIdAsc(
                saleId,
                businessId);
        return toResponse(sale, items,
                businessAccessGuard.hasBusinessPermission(businessId, subject, "SALE_COST_VIEW"));
    }

    @Transactional(readOnly = true)
    public List<SaleItemResponse> findItems(Integer businessId, Integer saleId, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "SALE_VIEW");
        Sale sale = findSale(businessId, saleId);
        businessBranchAccessGuard.findAccessibleBranch(businessId, sale.getBranch().getId(), subject);
        boolean includeCost = businessAccessGuard.hasBusinessPermission(businessId, subject, "SALE_COST_VIEW");
        return saleItemRepository.findBySaleIdAndSaleBusinessIdAndDeletedAtIsNullOrderByCreatedAtAscIdAsc(saleId, businessId)
                .stream()
                .map(item -> toSaleItemResponse(item, includeCost))
                .toList();
    }

    @Transactional
    public SaleResponse create(Integer businessId, CreateSaleRequest request, String subject) {
        return create(businessId, request, subject, null);
    }

    @Transactional
    public SaleResponse create(
            Integer businessId,
            CreateSaleRequest request,
            String subject,
            String rawIdempotencyKey) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "SALE_CREATE");
        String idempotencyKey = IdempotencySupport.normalize(rawIdempotencyKey);
        String fingerprint = IdempotencySupport.fingerprint(subject, request);
        BusinessBranch branch = businessBranchAccessGuard.findAccessibleBranchForUpdate(
                businessId, request.branchId(), subject);
        if (idempotencyKey != null) {
            Sale existing = saleRepository
                    .findByBusinessIdAndIdempotencyKeyAndDeletedAtIsNull(businessId, idempotencyKey)
                    .orElse(null);
            if (existing != null) {
                assertSameFingerprint(existing.getIdempotencyFingerprint(), fingerprint);
                List<SaleItem> items = saleItemRepository
                        .findBySaleIdAndSaleBusinessIdAndDeletedAtIsNullOrderByCreatedAtAscIdAsc(existing.getId(), businessId);
                return toResponse(existing, items,
                        businessAccessGuard.hasBusinessPermission(businessId, subject, "SALE_COST_VIEW"));
            }
        }
        Business business = findBusiness(businessId);
        BusinessUser soldBy = findBusinessUserBySubject(businessId, subject);
        businessBranchAccessGuard.assertBusinessUserAssigned(businessId, branch.getId(), soldBy.getId());
        BusinessCatalogItem paymentMethod = findPaymentMethod(businessId, request.paymentMethodId());
        AppUser customer = findCustomer(request.customerId());

        Sale sale = new Sale();
        sale.setBusiness(business);
        sale.setBranch(branch);
        sale.setSoldByBusinessUser(soldBy);
        sale.setPaymentMethod(paymentMethod);
        sale.setCustomer(customer);
        sale.setTotalAmount(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        sale.setIdempotencyKey(idempotencyKey);
        sale.setIdempotencyFingerprint(idempotencyKey == null ? null : fingerprint);
        sale.setActive(true);
        sale.setDeletedAt(null);

        Sale savedSale = saleRepository.save(sale);
        List<SaleItem> savedItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

        inventoryStockService.lockItemsForUpdate(
                businessId,
                request.items().stream().map(CreateSaleItemRequest::inventoryItemId).toList());

        for (CreateSaleItemRequest itemRequest : request.items()) {
            InventoryItem inventoryItem = findInventoryItem(businessId, itemRequest.inventoryItemId());
            if (!inventoryItem.getBranch().getId().equals(branch.getId())) {
                throw new ResourceNotFoundException("inventory_item.not_found");
            }
            SaleItem saleItem = buildSaleItem(savedSale, inventoryItem, itemRequest.quantity());
            SaleItem savedSaleItem = saleItemRepository.save(saleItem);
            savedItems.add(savedSaleItem);

            inventoryStockService.applyMovementByCode(
                    businessId,
                    branch.getId(),
                    inventoryItem.getId(),
                    "OUT",
                    itemRequest.quantity(),
                    soldBy.getId(),
                    null,
                    savedSaleItem,
                    "Salida por venta #" + savedSale.getId());

            totalAmount = totalAmount.add(savedSaleItem.getTotalPrice()).setScale(2, RoundingMode.HALF_UP);
        }

        savedSale.setTotalAmount(totalAmount);
        return toResponse(savedSale, savedItems,
                businessAccessGuard.hasBusinessPermission(businessId, subject, "SALE_COST_VIEW"));
    }

    private SaleItem buildSaleItem(Sale sale, InventoryItem inventoryItem, Integer quantity) {
        BigDecimal unitCost = inventoryItem.getUnitCost().setScale(2, RoundingMode.HALF_UP);
        BigDecimal unitPrice = inventoryItem.getSalePrice().setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalCost = unitCost.multiply(BigDecimal.valueOf(quantity.longValue())).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity.longValue()))
                .setScale(2, RoundingMode.HALF_UP);

        SaleItem saleItem = new SaleItem();
        saleItem.setSale(sale);
        saleItem.setInventoryItem(inventoryItem);
        saleItem.setQuantity(quantity);
        saleItem.setUnitCost(unitCost);
        saleItem.setUnitPrice(unitPrice);
        saleItem.setTotalCost(totalCost);
        saleItem.setTotalPrice(totalPrice);
        saleItem.setActive(true);
        saleItem.setDeletedAt(null);
        return saleItem;
    }

    private SaleResponse toResponseWithoutItems(Sale sale) {
        return new SaleResponse(
                sale.getId(),
                sale.getBusiness().getId(),
                sale.getBranch().getId(),
                sale.getBranch().getName(),
                sale.getCustomer() != null ? sale.getCustomer().getId() : null,
                sale.getCustomer() != null ? sale.getCustomer().getName() + " " + sale.getCustomer().getSurname() : null,
                sale.getSoldByBusinessUser().getId(),
                sale.getSoldByBusinessUser().getUser().getName() + " "
                        + sale.getSoldByBusinessUser().getUser().getSurname(),
                sale.getPaymentMethod().getId(),
                sale.getPaymentMethod().getCode(),
                sale.getPaymentMethod().getName(),
                sale.getTotalAmount(),
                sale.getCreatedAt(),
                List.of());
    }

    private SaleResponse toResponse(Sale sale, List<SaleItem> items, boolean includeCost) {
        return new SaleResponse(
                sale.getId(),
                sale.getBusiness().getId(),
                sale.getBranch().getId(),
                sale.getBranch().getName(),
                sale.getCustomer() != null ? sale.getCustomer().getId() : null,
                sale.getCustomer() != null ? sale.getCustomer().getName() + " " + sale.getCustomer().getSurname() : null,
                sale.getSoldByBusinessUser().getId(),
                sale.getSoldByBusinessUser().getUser().getName() + " "
                        + sale.getSoldByBusinessUser().getUser().getSurname(),
                sale.getPaymentMethod().getId(),
                sale.getPaymentMethod().getCode(),
                sale.getPaymentMethod().getName(),
                sale.getTotalAmount(),
                sale.getCreatedAt(),
                items.stream().map(item -> toSaleItemResponse(item, includeCost)).toList());
    }

    private SaleItemResponse toSaleItemResponse(SaleItem saleItem, boolean includeCost) {
        return new SaleItemResponse(
                saleItem.getId(),
                saleItem.getInventoryItem().getId(),
                saleItem.getInventoryItem().getSku(),
                saleItem.getInventoryItem().getName(),
                saleItem.getQuantity(),
                includeCost ? saleItem.getUnitCost() : null,
                saleItem.getUnitPrice(),
                includeCost ? saleItem.getTotalCost() : null,
                saleItem.getTotalPrice());
    }

    private Sale findSale(Integer businessId, Integer saleId) {
        return saleRepository.findByIdAndBusinessIdAndDeletedAtIsNull(saleId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("sale.not_found"));
    }

    private Business findBusiness(Integer businessId) {
        return businessRepository.findByIdAndDeletedAtIsNull(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("business.not_found"));
    }

    private BusinessUser findBusinessUserBySubject(Integer businessId, String subject) {
        Integer userId;
        try {
            userId = Integer.valueOf(subject);
        } catch (NumberFormatException exception) {
            throw new org.springframework.security.authentication.BadCredentialsException("auth.token.invalid");
        }
        return businessUserRepository
                .findByBusinessIdAndUserIdAndActiveTrueAndDeletedAtIsNull(businessId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("business_user.not_found"));
    }

    private BusinessCatalogItem findPaymentMethod(Integer businessId, Integer paymentMethodId) {
        return businessCatalogLookupService.findActive(
                businessId,
                paymentMethodId,
                BusinessCatalogType.PAYMENT_METHOD);
    }

    private AppUser findCustomer(Integer customerId) {
        if (customerId == null) {
            return null;
        }
        return appUserRepository.findById(customerId)
                .filter(value -> Boolean.TRUE.equals(value.getActive()) && value.getDeletedAt() == null)
                .orElseThrow(() -> new ResourceNotFoundException("user.not_found"));
    }

    private InventoryItem findInventoryItem(Integer businessId, Integer inventoryItemId) {
        return inventoryItemRepository.findByIdAndBusinessIdAndDeletedAtIsNull(inventoryItemId, businessId)
                .filter(value -> Boolean.TRUE.equals(value.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("inventory_item.not_found"));
    }

    private void assertSameFingerprint(String stored, String current) {
        if (stored == null || !stored.equals(current)) {
            throw new ConflictException("idempotency.key.reused");
        }
    }
}
