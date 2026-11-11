package com.tecmarket.backend.quotation.service;

import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.business.entity.BusinessBranch;
import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.business.security.BusinessBranchAccessGuard;
import com.tecmarket.backend.business.service.BusinessCatalogLookupService;
import com.tecmarket.backend.quotation.entity.Quotation;
import com.tecmarket.backend.quotation.repository.QuotationItemRepository;
import com.tecmarket.backend.quotation.repository.QuotationRepository;
import com.tecmarket.backend.repairorder.entity.RepairOrder;
import com.tecmarket.backend.repairorder.repository.RepairOrderRepository;
import com.tecmarket.backend.repairorder.service.RepairOrderTimelineService;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class QuotationBranchAuthorizationTest {

    @Test
    void refusesAQuotationFromAnotherBranchBeforeReadingItsItems() {
        QuotationRepository quotationRepository = mock(QuotationRepository.class);
        QuotationItemRepository itemRepository = mock(QuotationItemRepository.class);
        RepairOrderRepository orderRepository = mock(RepairOrderRepository.class);
        BusinessAccessGuard businessGuard = mock(BusinessAccessGuard.class);
        BusinessBranchAccessGuard branchGuard = mock(BusinessBranchAccessGuard.class);
        BusinessCatalogLookupService catalogLookup = mock(BusinessCatalogLookupService.class);
        RepairOrderTimelineService timelineService = mock(RepairOrderTimelineService.class);
        QuotationService service = new QuotationService(
                quotationRepository, itemRepository, orderRepository, catalogLookup, businessGuard, branchGuard,
                timelineService);

        Business business = new Business();
        business.setId(10);
        BusinessBranch branch = new BusinessBranch();
        branch.setId(20);
        branch.setBusiness(business);
        RepairOrder order = new RepairOrder();
        order.setId(30);
        order.setBusiness(business);
        order.setBranch(branch);
        Quotation quotation = new Quotation();
        quotation.setId(40);
        quotation.setBusiness(business);
        quotation.setRepairOrder(order);

        when(quotationRepository.findByIdAndBusinessIdAndDeletedAtIsNull(40, 10))
                .thenReturn(Optional.of(quotation));
        doThrow(new AccessDeniedException("auth.forbidden"))
                .when(branchGuard).findAccessibleBranch(10, 20, "99");

        assertThatThrownBy(() -> service.findById(10, 40, "99"))
                .isInstanceOf(AccessDeniedException.class);
        verify(itemRepository, never())
                .findByQuotationIdAndQuotationBusinessIdAndDeletedAtIsNullOrderByCreatedAtAscIdAsc(40, 10);
    }
}
