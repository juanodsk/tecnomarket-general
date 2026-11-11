package com.tecmarket.backend.quotation.service;

import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.business.entity.BusinessBranch;
import com.tecmarket.backend.business.entity.BusinessCatalogItem;
import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.business.security.BusinessBranchAccessGuard;
import com.tecmarket.backend.business.service.BusinessCatalogLookupService;
import com.tecmarket.backend.quotation.entity.Quotation;
import com.tecmarket.backend.quotation.entity.QuotationItem;
import com.tecmarket.backend.quotation.repository.QuotationItemRepository;
import com.tecmarket.backend.quotation.repository.QuotationRepository;
import com.tecmarket.backend.repairorder.entity.RepairOrder;
import com.tecmarket.backend.repairorder.repository.RepairOrderRepository;
import com.tecmarket.backend.repairorder.service.RepairOrderTimelineService;
import com.tecmarket.backend.user.entity.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuotationListServiceTest {

    @Mock private QuotationRepository quotationRepository;
    @Mock private QuotationItemRepository quotationItemRepository;
    @Mock private RepairOrderRepository repairOrderRepository;
    @Mock private BusinessCatalogLookupService catalogLookupService;
    @Mock private BusinessAccessGuard businessAccessGuard;
    @Mock private BusinessBranchAccessGuard branchAccessGuard;
    @Mock private RepairOrderTimelineService timelineService;

    private QuotationService service;
    private RepairOrder repairOrder;

    @BeforeEach
    void setUp() {
        service = new QuotationService(
                quotationRepository,
                quotationItemRepository,
                repairOrderRepository,
                catalogLookupService,
                businessAccessGuard,
                branchAccessGuard,
                timelineService);

        Business business = new Business();
        business.setId(10);
        BusinessBranch branch = new BusinessBranch();
        branch.setId(20);
        branch.setBusiness(business);
        repairOrder = new RepairOrder();
        repairOrder.setId(30);
        repairOrder.setBusiness(business);
        repairOrder.setBranch(branch);
    }

    @Test
    void listsOnlyTheRequestedOrderAndGroupsItemsWithoutNPlusOneQueries() {
        Quotation first = quotation(50, "DRAFT");
        Quotation second = quotation(51, "PENDING");
        QuotationItem firstItem = item(70, first, "Pantalla", "150000.00");
        QuotationItem secondItem = item(71, first, "Mano de obra", "50000.00");

        when(repairOrderRepository.findByIdAndBusinessIdAndDeletedAtIsNull(30, 10))
                .thenReturn(Optional.of(repairOrder));
        when(quotationRepository.findByBusinessIdAndRepairOrderIdAndActiveTrueAndDeletedAtIsNullOrderByCreatedAtDescIdDesc(
                org.mockito.ArgumentMatchers.eq(10),
                org.mockito.ArgumentMatchers.eq(30),
                any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(first, second), PageRequest.of(0, 20), 2));
        when(quotationItemRepository.findActiveByQuotationIdsAndBusinessId(List.of(50, 51), 10))
                .thenReturn(List.of(firstItem, secondItem));

        var response = service.findAllByRepairOrder(10, 30, 0, 20, "99");

        assertThat(response.items()).extracting(value -> value.id()).containsExactly(50, 51);
        assertThat(response.items().get(0).items()).hasSize(2);
        assertThat(response.items().get(1).items()).isEmpty();
        assertThat(response.totalElements()).isEqualTo(2);
        verify(businessAccessGuard).assertHasBusinessPermission(10, "99", "QUOTATION_VIEW");
        verify(branchAccessGuard).findAccessibleBranch(10, 20, "99");
        verify(quotationItemRepository).findActiveByQuotationIdsAndBusinessId(List.of(50, 51), 10);
    }

    @Test
    void rejectsAnInaccessibleBranchBeforeListingQuotations() {
        when(repairOrderRepository.findByIdAndBusinessIdAndDeletedAtIsNull(30, 10))
                .thenReturn(Optional.of(repairOrder));
        doThrow(new AccessDeniedException("auth.forbidden"))
                .when(branchAccessGuard).findAccessibleBranch(10, 20, "99");

        assertThatThrownBy(() -> service.findAllByRepairOrder(10, 30, 0, 20, "99"))
                .isInstanceOf(AccessDeniedException.class);

        verify(quotationRepository, never())
                .findByBusinessIdAndRepairOrderIdAndActiveTrueAndDeletedAtIsNullOrderByCreatedAtDescIdDesc(
                        org.mockito.ArgumentMatchers.anyInt(),
                        org.mockito.ArgumentMatchers.anyInt(),
                        any(Pageable.class));
        verify(quotationItemRepository, never())
                .findActiveByQuotationIdsAndBusinessId(
                        org.mockito.ArgumentMatchers.anyList(),
                        org.mockito.ArgumentMatchers.anyInt());
    }

    private Quotation quotation(Integer id, String statusCode) {
        AppUser customer = new AppUser();
        customer.setId(40);
        customer.setName("Cliente");
        customer.setSurname("Prueba");
        BusinessCatalogItem status = new BusinessCatalogItem();
        status.setCode(statusCode);
        status.setName(statusCode);

        Quotation quotation = new Quotation();
        quotation.setId(id);
        quotation.setBusiness(repairOrder.getBusiness());
        quotation.setRepairOrder(repairOrder);
        quotation.setCustomer(customer);
        quotation.setQuotationStatus(status);
        quotation.setTotalAmount(new BigDecimal("200000.00"));
        return quotation;
    }

    private QuotationItem item(
            Integer id,
            Quotation quotation,
            String description,
            String totalPrice) {
        QuotationItem item = new QuotationItem();
        item.setId(id);
        item.setQuotation(quotation);
        item.setDescription(description);
        item.setQuantity(1);
        item.setUnitPrice(new BigDecimal(totalPrice));
        item.setTotalPrice(new BigDecimal(totalPrice));
        return item;
    }
}
