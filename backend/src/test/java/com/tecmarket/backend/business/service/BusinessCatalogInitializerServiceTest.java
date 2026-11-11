package com.tecmarket.backend.business.service;

import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.business.entity.BusinessCatalogItem;
import com.tecmarket.backend.business.entity.BusinessCatalogType;
import com.tecmarket.backend.business.repository.BusinessCatalogItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unchecked")
class BusinessCatalogInitializerServiceTest {

    @Mock private BusinessCatalogItemRepository businessCatalogItemRepository;

    @Test
    void initializesCompleteCatalogOnceAndPreservesItOnRepeatedCalls() {
        Business business = new Business();
        business.setId(7);
        List<BusinessCatalogItem> stored = new ArrayList<>();

        when(businessCatalogItemRepository.findByBusinessIdAndDeletedAtIsNull(7))
                .thenAnswer(invocation -> List.copyOf(stored));
        when(businessCatalogItemRepository.saveAll(anyList())).thenAnswer(invocation -> {
            List<BusinessCatalogItem> saved = invocation.getArgument(0);
            stored.addAll(saved);
            return saved;
        });

        BusinessCatalogInitializerService service = new BusinessCatalogInitializerService(businessCatalogItemRepository);
        service.initialize(business);
        service.initialize(business);

        ArgumentCaptor<List<BusinessCatalogItem>> captor = ArgumentCaptor.forClass(List.class);
        verify(businessCatalogItemRepository, times(1)).saveAll(captor.capture());

        List<BusinessCatalogItem> initialized = captor.getValue();
        assertThat(initialized).hasSize(66);
        assertThat(initialized)
                .extracting(BusinessCatalogItem::getCatalogType)
                .containsAll(EnumSet.allOf(BusinessCatalogType.class));
        assertThat(initialized)
                .filteredOn(item -> item.getCatalogType() == BusinessCatalogType.REPAIR_ORDER_STATUS)
                .extracting(BusinessCatalogItem::getCode)
                .contains("RECEIVED", "DELIVERED", "CANCELLED");
        assertThat(initialized)
                .filteredOn(item -> item.getCatalogType() == BusinessCatalogType.MOVEMENT_TYPE)
                .extracting(BusinessCatalogItem::getCode)
                .containsExactlyInAnyOrder("IN", "OUT", "ADJUSTMENT", "RETURN");
        assertThat(initialized)
                .filteredOn(item -> item.getCatalogType() == BusinessCatalogType.MOVEMENT_TYPE)
                .allMatch(item -> Boolean.TRUE.equals(item.getProtectedItem()));
        assertThat(initialized)
                .allMatch(item -> item.getBusiness() == business && Boolean.TRUE.equals(item.getActive()));
    }

    @Test
    void insertsOnlyMissingCodesWithoutOverwritingExistingCatalogs() {
        Business business = new Business();
        business.setId(8);

        BusinessCatalogItem existing = new BusinessCatalogItem();
        existing.setBusiness(business);
        existing.setCatalogType(BusinessCatalogType.REPAIR_ORDER_STATUS);
        existing.setCode("RECEIVED");
        existing.setName("Nombre personalizado");
        existing.setProtectedItem(true);
        existing.setActive(true);

        when(businessCatalogItemRepository.findByBusinessIdAndDeletedAtIsNull(8))
                .thenReturn(List.of(existing));
        when(businessCatalogItemRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        new BusinessCatalogInitializerService(businessCatalogItemRepository).initialize(business);

        ArgumentCaptor<List<BusinessCatalogItem>> captor = ArgumentCaptor.forClass(List.class);
        verify(businessCatalogItemRepository).saveAll(captor.capture());
        assertThat(captor.getValue()).hasSize(65);
        assertThat(captor.getValue())
                .noneMatch(item -> item.getCatalogType() == BusinessCatalogType.REPAIR_ORDER_STATUS
                        && "RECEIVED".equals(item.getCode()));
        assertThat(existing.getName()).isEqualTo("Nombre personalizado");
    }
}
