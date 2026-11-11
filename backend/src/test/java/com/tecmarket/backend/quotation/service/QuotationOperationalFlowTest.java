package com.tecmarket.backend.quotation.service;

import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.business.entity.BusinessBranch;
import com.tecmarket.backend.business.entity.BusinessCatalogItem;
import com.tecmarket.backend.business.entity.BusinessCatalogType;
import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.business.security.BusinessBranchAccessGuard;
import com.tecmarket.backend.business.service.BusinessCatalogLookupService;
import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.quotation.dto.UpdateQuotationStatusRequest;
import com.tecmarket.backend.quotation.entity.Quotation;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuotationOperationalFlowTest {

    @Mock private QuotationRepository quotationRepository;
    @Mock private QuotationItemRepository quotationItemRepository;
    @Mock private RepairOrderRepository repairOrderRepository;
    @Mock private BusinessCatalogLookupService catalogLookupService;
    @Mock private BusinessAccessGuard businessAccessGuard;
    @Mock private BusinessBranchAccessGuard branchAccessGuard;
    @Mock private RepairOrderTimelineService timelineService;

    private QuotationService service;
    private RepairOrder repairOrder;
    private Quotation quotation;

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
        BusinessCatalogItem orderStatus = status("DIAGNOSIS", 1);

        repairOrder = new RepairOrder();
        repairOrder.setId(30);
        repairOrder.setBusiness(business);
        repairOrder.setBranch(branch);
        repairOrder.setStatus(orderStatus);

        AppUser customer = new AppUser();
        customer.setId(40);
        customer.setName("Cliente");
        customer.setSurname("Prueba");

        quotation = new Quotation();
        quotation.setId(50);
        quotation.setBusiness(business);
        quotation.setRepairOrder(repairOrder);
        quotation.setCustomer(customer);
        quotation.setQuotationStatus(status("DRAFT", 2));
        quotation.setTotalAmount(new BigDecimal("120000.00"));
        quotation.setExpiresAt(LocalDateTime.now().plusDays(2));

        when(quotationRepository.findForUpdateByIdAndBusinessId(50, 10)).thenReturn(Optional.of(quotation));
        when(repairOrderRepository.findForUpdateByIdAndBusinessId(30, 10)).thenReturn(Optional.of(repairOrder));
    }

    @Test
    void rejectsAJumpFromDraftToApprovedWithoutWritingHistory() {
        BusinessCatalogItem approved = status("APPROVED", 4);
        when(catalogLookupService.findActive(10, 4, BusinessCatalogType.QUOTATION_STATUS))
                .thenReturn(approved);

        assertThatThrownBy(() -> service.updateStatus(
                10, 50, new UpdateQuotationStatusRequest(4, null), "99"))
                .isInstanceOf(ConflictException.class)
                .hasMessage("quotation.status.invalid_transition");

        verify(timelineService, never()).recordInternalEvent(repairOrder, "99", "Cotización 50 cambió de DRAFT a APPROVED.");
    }

    @Test
    void serializesAndAuditsTheDraftToPendingTransition() {
        BusinessCatalogItem pending = status("PENDING", 3);
        when(catalogLookupService.findActive(10, 3, BusinessCatalogType.QUOTATION_STATUS))
                .thenReturn(pending);
        when(quotationItemRepository
                .findByQuotationIdAndQuotationBusinessIdAndDeletedAtIsNullOrderByCreatedAtAscIdAsc(50, 10))
                .thenReturn(List.of());

        var response = service.updateStatus(
                10, 50, new UpdateQuotationStatusRequest(3, "Enviada al cliente"), "99");

        assertThat(response.statusCode()).isEqualTo("PENDING");
        verify(quotationRepository).findForUpdateByIdAndBusinessId(50, 10);
        verify(repairOrderRepository).findForUpdateByIdAndBusinessId(30, 10);
        verify(timelineService).recordInternalEvent(
                repairOrder, "99", "Cotización 50 cambió de DRAFT a PENDING.");
    }

    private BusinessCatalogItem status(String code, int id) {
        BusinessCatalogItem status = new BusinessCatalogItem();
        status.setId(id);
        status.setCode(code);
        status.setName(code);
        return status;
    }
}
