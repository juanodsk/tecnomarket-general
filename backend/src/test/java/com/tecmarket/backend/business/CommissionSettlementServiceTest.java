package com.tecmarket.backend.business;

import com.tecmarket.backend.repairorder.dto.EligibleCommissionOrderResponse;
import com.tecmarket.backend.repairorder.dto.TechnicianCommissionSummaryResponse;
import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.business.entity.BusinessUser;
import com.tecmarket.backend.business.repository.BusinessUserRepository;
import com.tecmarket.backend.repairorder.entity.RepairOrder;
import com.tecmarket.backend.repairorder.repository.*;
import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.repairorder.service.CommissionSettlementService;
import com.tecmarket.backend.repairorder.service.RepairOrderCommissionService;
import com.tecmarket.backend.permission.entity.Role;
import com.tecmarket.backend.user.entity.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommissionSettlementServiceTest {

    @Mock private CommissionSettlementRepository settlementRepository;
    @Mock private RepairOrderCommissionRepository commissionRepository;
    @Mock private RepairOrderRepository repairOrderRepository;
    @Mock private RepairOrderPartRepository repairOrderPartRepository;
    @Mock private BusinessUserRepository businessUserRepository;
    @Mock private BusinessAccessGuard businessAccessGuard;
    @Mock private RepairOrderCommissionService commissionService;

    private CommissionSettlementService service;

    @BeforeEach
    void setUp() {
        service = new CommissionSettlementService(
                settlementRepository,
                commissionRepository,
                repairOrderRepository,
                repairOrderPartRepository,
                businessUserRepository,
                businessAccessGuard,
                commissionService);
    }

    @Test
    void eligibleOrderCalculatesCommissionFromFinalPriceMinusParts() {
        Business business = new Business();
        business.setId(1);

        Role technicianRole = new Role();
        technicianRole.setCode("TECHNICIAN");

        AppUser user = new AppUser();
        user.setId(10);
        user.setName("Ana");
        user.setSurname("Tecnica");
        user.setEmail("ana@tecmarket.test");

        BusinessUser technician = new BusinessUser();
        technician.setId(3);
        technician.setBusiness(business);
        technician.setRole(technicianRole);
        technician.setUser(user);
        technician.setCommissionPercentage(new BigDecimal("20.00"));

        RepairOrder order = new RepairOrder();
        order.setId(15);
        order.setBusiness(business);
        order.setAssignedBusinessUser(technician);
        order.setOrderNumber("OS-001-000015");
        order.setFinalPrice(new BigDecimal("300000.00"));
        order.setReceivedAt(LocalDateTime.of(2026, 6, 10, 9, 0));

        when(businessUserRepository.findByIdAndBusinessIdAndDeletedAtIsNull(3, 1))
                .thenReturn(Optional.of(technician));
        when(businessUserRepository.findActiveByBusinessAndRoleCode(1, "TECHNICIAN"))
                .thenReturn(List.of(technician));
        when(repairOrderRepository.findEligibleForCommissionSettlement(
                1,
                3,
                LocalDate.of(2026, 6, 1).atStartOfDay(),
                LocalDate.of(2026, 7, 1).atStartOfDay()))
                .thenReturn(List.of(order));
        when(repairOrderPartRepository.sumActivePartsTotal(15, 1))
                .thenReturn(new BigDecimal("100000.00"));

        List<EligibleCommissionOrderResponse> result = service.findEligibleOrders(
                1,
                3,
                LocalDate.of(2026, 6, 1),
                LocalDate.of(2026, 6, 30),
                "2");

        assertThat(result).singleElement().satisfies(item -> {
            assertThat(item.orderTotal()).isEqualByComparingTo("300000.00");
            assertThat(item.partsTotal()).isEqualByComparingTo("100000.00");
            assertThat(item.commissionBase()).isEqualByComparingTo("200000.00");
            assertThat(item.commissionPercentage()).isEqualByComparingTo("20.00");
            assertThat(item.commissionAmount()).isEqualByComparingTo("40000.00");
        });

        List<TechnicianCommissionSummaryResponse> summaries = service.findTechnicianSummaries(
                1,
                LocalDate.of(2026, 6, 1),
                LocalDate.of(2026, 6, 30),
                "2");

        assertThat(summaries).singleElement().satisfies(summary -> {
            assertThat(summary.eligibleOrdersCount()).isEqualTo(1);
            assertThat(summary.totalCommissionBase()).isEqualByComparingTo("200000.00");
            assertThat(summary.estimatedCommissionAmount()).isEqualByComparingTo("40000.00");
        });
    }
}
