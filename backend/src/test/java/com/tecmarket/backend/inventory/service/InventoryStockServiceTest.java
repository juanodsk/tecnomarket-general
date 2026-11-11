package com.tecmarket.backend.inventory.service;

import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.business.entity.BusinessBranch;
import com.tecmarket.backend.business.entity.BusinessCatalogItem;
import com.tecmarket.backend.business.entity.BusinessUser;
import com.tecmarket.backend.business.repository.BusinessUserRepository;
import com.tecmarket.backend.business.service.BusinessCatalogLookupService;
import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.inventory.entity.InventoryItem;
import com.tecmarket.backend.inventory.entity.StockMovement;
import com.tecmarket.backend.inventory.repository.InventoryItemRepository;
import com.tecmarket.backend.inventory.repository.StockMovementRepository;
import com.tecmarket.backend.repairorder.entity.RepairOrder;
import com.tecmarket.backend.repairorder.repository.RepairOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InOrder;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryStockServiceTest {

    @Mock private InventoryItemRepository inventoryItemRepository;
    @Mock private BusinessCatalogLookupService businessCatalogLookupService;
    @Mock private BusinessUserRepository businessUserRepository;
    @Mock private RepairOrderRepository repairOrderRepository;
    @Mock private StockMovementRepository stockMovementRepository;

    private InventoryStockService service;
    private InventoryItem item;
    private BusinessCatalogItem movementType;

    @BeforeEach
    void setUp() {
        service = new InventoryStockService(
                inventoryItemRepository,
                businessCatalogLookupService,
                businessUserRepository,
                repairOrderRepository,
                stockMovementRepository);

        Business business = new Business();
        business.setId(1);

        BusinessBranch branch = new BusinessBranch();
        branch.setId(2);
        branch.setBusiness(business);

        item = new InventoryItem();
        item.setId(3);
        item.setBusiness(business);
        item.setBranch(branch);
        item.setQuantity(10);
        item.setActive(true);

        BusinessUser actor = new BusinessUser();
        actor.setId(4);
        actor.setBusiness(business);
        actor.setActive(true);

        RepairOrder order = new RepairOrder();
        order.setId(5);
        order.setBusiness(business);
        order.setBranch(branch);

        movementType = new BusinessCatalogItem();
        movementType.setCode("OUT");

        lenient().when(inventoryItemRepository.findForUpdateByIdAndBusinessId(3, 1)).thenReturn(Optional.of(item));
        lenient().when(businessUserRepository.findByIdAndBusinessIdAndDeletedAtIsNull(4, 1)).thenReturn(Optional.of(actor));
        lenient().when(repairOrderRepository.findByIdAndBusinessIdAndDeletedAtIsNull(5, 1)).thenReturn(Optional.of(order));
        lenient().when(stockMovementRepository.save(any(StockMovement.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void outMovementDiscountsStockAndLinksOrder() {
        when(businessCatalogLookupService.findActiveByCode(1, com.tecmarket.backend.business.entity.BusinessCatalogType.MOVEMENT_TYPE, "OUT"))
                .thenReturn(movementType);

        StockMovement movement = service.applyMovementByCode(1, 2, 3, "OUT", 4, 4, 5, null, "Uso en reparación");

        assertThat(item.getQuantity()).isEqualTo(6);
        assertThat(movement.getRepairOrder().getId()).isEqualTo(5);
        assertThat(movement.getQuantity()).isEqualTo(4);
    }

    @Test
    void returnMovementRestoresStock() {
        movementType.setCode("RETURN");
        when(businessCatalogLookupService.findActiveByCode(1, com.tecmarket.backend.business.entity.BusinessCatalogType.MOVEMENT_TYPE, "RETURN"))
                .thenReturn(movementType);

        service.applyMovementByCode(1, 2, 3, "RETURN", 3, 4, 5, null, "Devolución");

        assertThat(item.getQuantity()).isEqualTo(13);
    }

    @Test
    void insufficientStockDoesNotMutateItemOrSaveMovement() {
        when(businessCatalogLookupService.findActiveByCode(1, com.tecmarket.backend.business.entity.BusinessCatalogType.MOVEMENT_TYPE, "OUT"))
                .thenReturn(movementType);

        assertThatThrownBy(() -> service.applyMovementByCode(1, 2, 3, "OUT", 11, 4, 5, null, null))
                .isInstanceOf(ConflictException.class);

        assertThat(item.getQuantity()).isEqualTo(10);
        verify(stockMovementRepository, never()).save(any());
    }

    @Test
    void inactiveInventoryItemCannotBeMoved() {
        item.setActive(false);
        when(businessCatalogLookupService.findActiveByCode(1, com.tecmarket.backend.business.entity.BusinessCatalogType.MOVEMENT_TYPE, "OUT"))
                .thenReturn(movementType);

        assertThatThrownBy(() -> service.applyMovementByCode(1, 2, 3, "OUT", 1, 4, 5, null, null))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(stockMovementRepository, never()).save(any());
    }

    @Test
    void locksMultipleItemsInStableOrderToAvoidDeadlocks() {
        InventoryItem itemSeven = new InventoryItem();
        itemSeven.setId(7);
        itemSeven.setActive(true);
        InventoryItem itemEight = new InventoryItem();
        itemEight.setId(8);
        itemEight.setActive(true);
        when(inventoryItemRepository.findForUpdateByIdAndBusinessId(7, 1)).thenReturn(Optional.of(itemSeven));
        when(inventoryItemRepository.findForUpdateByIdAndBusinessId(8, 1)).thenReturn(Optional.of(itemEight));

        service.lockItemsForUpdate(1, List.of(8, 7, 8));

        InOrder locks = inOrder(inventoryItemRepository);
        locks.verify(inventoryItemRepository).findForUpdateByIdAndBusinessId(7, 1);
        locks.verify(inventoryItemRepository).findForUpdateByIdAndBusinessId(8, 1);
    }
}
