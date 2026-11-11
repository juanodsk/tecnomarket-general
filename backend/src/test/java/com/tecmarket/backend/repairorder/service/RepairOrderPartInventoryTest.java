package com.tecmarket.backend.repairorder.service;

import com.tecmarket.backend.appointment.repository.AppointmentRepository;
import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.business.entity.BusinessBranch;
import com.tecmarket.backend.business.entity.BusinessCatalogItem;
import com.tecmarket.backend.business.entity.BusinessUser;
import com.tecmarket.backend.business.repository.BusinessOfferingRepository;
import com.tecmarket.backend.business.repository.BusinessRepository;
import com.tecmarket.backend.business.repository.BusinessUserRepository;
import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.business.security.BusinessBranchAccessGuard;
import com.tecmarket.backend.business.service.BusinessCatalogLookupService;
import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.inventory.entity.InventoryItem;
import com.tecmarket.backend.inventory.entity.StockMovement;
import com.tecmarket.backend.inventory.service.InventoryStockService;
import com.tecmarket.backend.repairorder.dto.CreateRepairOrderPartRequest;
import com.tecmarket.backend.repairorder.dto.UpdateRepairOrderDiagnosisRequest;
import com.tecmarket.backend.repairorder.dto.UpdateRepairOrderFinancialsRequest;
import com.tecmarket.backend.repairorder.dto.UpdateRepairOrderStatusRequest;
import com.tecmarket.backend.repairorder.entity.RepairOrderHistoryVisibility;
import com.tecmarket.backend.repairorder.dto.UpdateRepairOrderPartRequest;
import com.tecmarket.backend.repairorder.entity.RepairOrderAttachment;
import com.tecmarket.backend.repairorder.entity.RepairOrder;
import com.tecmarket.backend.repairorder.entity.RepairOrderPart;
import com.tecmarket.backend.repairorder.mapper.RepairOrderMapper;
import com.tecmarket.backend.repairorder.repository.CustomerDeviceRepository;
import com.tecmarket.backend.repairorder.repository.RepairOrderAttachmentRepository;
import com.tecmarket.backend.repairorder.repository.RepairOrderCommissionRepository;
import com.tecmarket.backend.repairorder.repository.RepairOrderHistoryRepository;
import com.tecmarket.backend.repairorder.repository.RepairOrderPartRepository;
import com.tecmarket.backend.repairorder.repository.RepairOrderRepository;
import com.tecmarket.backend.storage.service.MinioStorageService;
import com.tecmarket.backend.storage.service.StorageDeletionQueueService;
import com.tecmarket.backend.storage.config.MinioStorageProperties;
import com.tecmarket.backend.storage.dto.StoredObjectResponse;
import com.tecmarket.backend.user.entity.AppUser;
import com.tecmarket.backend.user.repository.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RepairOrderPartInventoryTest {

    private static final Integer BUSINESS_ID = 1;
    private static final Integer BRANCH_ID = 2;
    private static final Integer ORDER_ID = 3;
    private static final Integer USED_BY_ID = 4;
    private static final Integer ACTOR_BUSINESS_USER_ID = 5;
    private static final Integer ACTOR_USER_ID = 50;
    private static final String SUBJECT = "50";

    @Mock private RepairOrderRepository repairOrderRepository;
    @Mock private BusinessRepository businessRepository;
    @Mock private AppUserRepository appUserRepository;
    @Mock private CustomerDeviceRepository customerDeviceRepository;
    @Mock private AppointmentRepository appointmentRepository;
    @Mock private BusinessOfferingRepository businessOfferingRepository;
    @Mock private BusinessCatalogLookupService businessCatalogLookupService;
    @Mock private RepairOrderHistoryRepository repairOrderHistoryRepository;
    @Mock private RepairOrderAttachmentRepository repairOrderAttachmentRepository;
    @Mock private RepairOrderPartRepository repairOrderPartRepository;
    @Mock private RepairOrderCommissionRepository repairOrderCommissionRepository;
    @Mock private InventoryStockService inventoryStockService;
    @Mock private BusinessUserRepository businessUserRepository;
    @Mock private BusinessAccessGuard businessAccessGuard;
    @Mock private BusinessBranchAccessGuard businessBranchAccessGuard;
    @Mock private RepairOrderMapper repairOrderMapper;
    @Mock private JdbcTemplate jdbcTemplate;
    @Mock private MinioStorageService minioStorageService;
    @Mock private StorageDeletionQueueService storageDeletionQueueService;
    @Mock private MinioStorageProperties minioStorageProperties;

    private RepairOrderService service;
    private RepairOrder order;
    private BusinessUser usedBy;
    private BusinessUser actor;
    private InventoryItem firstItem;

    @BeforeEach
    void setUp() {
        service = new RepairOrderService(
                repairOrderRepository,
                businessRepository,
                appUserRepository,
                customerDeviceRepository,
                appointmentRepository,
                businessOfferingRepository,
                businessCatalogLookupService,
                repairOrderHistoryRepository,
                repairOrderAttachmentRepository,
                repairOrderPartRepository,
                repairOrderCommissionRepository,
                inventoryStockService,
                businessUserRepository,
                businessAccessGuard,
                businessBranchAccessGuard,
                repairOrderMapper,
                jdbcTemplate,
                minioStorageService,
                storageDeletionQueueService,
                minioStorageProperties);

        Business business = new Business();
        business.setId(BUSINESS_ID);

        BusinessBranch branch = new BusinessBranch();
        branch.setId(BRANCH_ID);
        branch.setBusiness(business);

        BusinessCatalogItem status = new BusinessCatalogItem();
        status.setCode("RECEIVED");

        order = new RepairOrder();
        order.setId(ORDER_ID);
        order.setBusiness(business);
        order.setBranch(branch);
        order.setStatus(status);
        order.setOrderNumber("OS-001-01-000001");

        AppUser usedByUser = user(40, "tecnico@tecmarket.test");
        usedBy = businessUser(USED_BY_ID, business, usedByUser);
        actor = businessUser(ACTOR_BUSINESS_USER_ID, business, user(ACTOR_USER_ID, "actor@tecmarket.test"));

        firstItem = inventoryItem(10, branch, "BAT-01");

        when(repairOrderRepository.findForUpdateByIdAndBusinessId(ORDER_ID, BUSINESS_ID))
                .thenReturn(Optional.of(order));
        lenient().when(businessUserRepository.findByIdAndBusinessIdAndDeletedAtIsNull(USED_BY_ID, BUSINESS_ID))
                .thenReturn(Optional.of(usedBy));
        lenient().when(businessUserRepository.findByBusinessIdAndUserIdAndActiveTrueAndDeletedAtIsNull(BUSINESS_ID, ACTOR_USER_ID))
                .thenReturn(Optional.of(actor));
        lenient().when(appUserRepository.findById(ACTOR_USER_ID)).thenReturn(Optional.of(actor.getUser()));
        lenient().when(minioStorageProperties.getEvidenceRetentionDays()).thenReturn(90);
    }

    @Test
    void uploadEvidencePersistsChecksumRegistersRollbackCleanupAndAudits() {
        MockMultipartFile file = new MockMultipartFile("file", "proof.png", "image/png", new byte[] {1});
        StoredObjectResponse stored = new StoredObjectResponse(
                "private-evidence",
                "businesses/1/repair-orders/3/random.png",
                "https://evidence.example.com/signed",
                "proof.png",
                "image/png",
                81,
                "a".repeat(64));
        when(minioStorageService.uploadRepairEvidence(anyString(), eq(file))).thenReturn(stored);
        when(minioStorageService.resolveRepairEvidenceUrl(stored.bucket(), stored.objectKey()))
                .thenReturn(stored.url());
        when(repairOrderAttachmentRepository.save(any(RepairOrderAttachment.class))).thenAnswer(invocation -> {
            RepairOrderAttachment attachment = invocation.getArgument(0);
            attachment.setId(30);
            return attachment;
        });

        var response = service.uploadAttachment(BUSINESS_ID, ORDER_ID, file, "Ingreso", SUBJECT);

        assertThat(response.checksumSha256()).isEqualTo("a".repeat(64));
        ArgumentCaptor<RepairOrderAttachment> attachmentCaptor = ArgumentCaptor.forClass(RepairOrderAttachment.class);
        verify(repairOrderAttachmentRepository).save(attachmentCaptor.capture());
        assertThat(attachmentCaptor.getValue().getFileUrl()).isNull();
        assertThat(attachmentCaptor.getValue().getChecksumSha256()).isEqualTo("a".repeat(64));
        verify(minioStorageService).registerRollbackCleanup(stored);
        verify(repairOrderHistoryRepository).save(any());
    }

    @Test
    void deleteEvidenceSoftDeletesAuditsActorAndQueuesPhysicalDeletionAfterRetention() {
        RepairOrderAttachment attachment = new RepairOrderAttachment();
        attachment.setId(30);
        attachment.setRepairOrder(order);
        attachment.setUploadedByBusinessUser(actor);
        attachment.setBucketName("private-evidence");
        attachment.setObjectKey("businesses/1/repair-orders/3/random.png");
        when(repairOrderAttachmentRepository
                .findByIdAndRepairOrderIdAndRepairOrderBusinessIdAndDeletedAtIsNull(30, ORDER_ID, BUSINESS_ID))
                .thenReturn(Optional.of(attachment));

        service.deleteAttachment(BUSINESS_ID, ORDER_ID, 30, SUBJECT);

        assertThat(attachment.getActive()).isFalse();
        assertThat(attachment.getDeletedByBusinessUser()).isSameAs(actor);
        assertThat(attachment.getRetentionUntil()).isAfter(attachment.getDeletedAt().plusDays(89));
        verify(storageDeletionQueueService).enqueue(
                eq("private-evidence"),
                eq("businesses/1/repair-orders/3/random.png"),
                eq("EVIDENCE_RETENTION_EXPIRED"),
                eq("REPAIR_ORDER_ATTACHMENT"),
                eq(30),
                eq(attachment.getRetentionUntil()));
        verify(minioStorageService, never()).delete(anyString(), anyString());
        verify(repairOrderHistoryRepository).save(any());
    }

    @Test
    void createPartCreatesStockOutForAuthenticatedActor() {
        when(repairOrderPartRepository.save(any(RepairOrderPart.class))).thenAnswer(invocation -> {
            RepairOrderPart part = invocation.getArgument(0);
            part.setId(20);
            return part;
        });
        when(inventoryStockService.applyMovementByCode(
                eq(BUSINESS_ID), eq(BRANCH_ID), eq(firstItem.getId()), eq("OUT"), eq(2),
                eq(ACTOR_BUSINESS_USER_ID), eq(ORDER_ID), eq(null), anyString()))
                .thenReturn(movement(firstItem));

        var response = service.createPart(
                BUSINESS_ID,
                ORDER_ID,
                new CreateRepairOrderPartRequest(firstItem.getId(), 2, new BigDecimal("85000.00"), USED_BY_ID),
                SUBJECT);

        assertThat(response.inventoryItemId()).isEqualTo(firstItem.getId());
        assertThat(response.quantity()).isEqualTo(2);
        verify(inventoryStockService).applyMovementByCode(
                BUSINESS_ID, BRANCH_ID, firstItem.getId(), "OUT", 2,
                ACTOR_BUSINESS_USER_ID, ORDER_ID, null, "Salida por repuesto agregado a la orden OS-001-01-000001");
        verify(repairOrderHistoryRepository).save(any());
    }

    @Test
    void createPartReplaysWithoutDiscountingStockAgain() {
        CreateRepairOrderPartRequest request = new CreateRepairOrderPartRequest(
                firstItem.getId(), 2, new BigDecimal("85000.00"), USED_BY_ID);
        RepairOrderPart existing = part(firstItem, 2);
        existing.setIdempotencyKey("part-retry-0001");
        existing.setIdempotencyFingerprint(
                com.tecmarket.backend.common.util.IdempotencySupport.fingerprint(SUBJECT, request));
        when(repairOrderPartRepository.findByRepairOrderIdAndIdempotencyKey(
                ORDER_ID, "part-retry-0001"))
                .thenReturn(Optional.of(existing));

        var response = service.createPart(
                BUSINESS_ID, ORDER_ID, request, SUBJECT, "part-retry-0001");

        assertThat(response.id()).isEqualTo(existing.getId());
        verify(inventoryStockService, never()).applyMovementByCode(
                any(), any(), any(), any(), any(), any(), any(), any(), any());
        verify(repairOrderPartRepository, never()).save(any());
    }

    @Test
    void updatePartAppliesOnlyQuantityDelta() {
        RepairOrderPart part = part(firstItem, 2);
        when(repairOrderPartRepository.findByIdAndRepairOrderIdAndRepairOrderBusinessIdAndDeletedAtIsNull(20, ORDER_ID, BUSINESS_ID))
                .thenReturn(Optional.of(part));
        when(inventoryStockService.applyMovementByCode(
                eq(BUSINESS_ID), eq(BRANCH_ID), eq(firstItem.getId()), eq("OUT"), eq(3),
                eq(ACTOR_BUSINESS_USER_ID), eq(ORDER_ID), eq(null), anyString()))
                .thenReturn(movement(firstItem));

        service.updatePart(
                BUSINESS_ID,
                ORDER_ID,
                20,
                new UpdateRepairOrderPartRequest(firstItem.getId(), 5, new BigDecimal("85000.00"), USED_BY_ID),
                SUBJECT);

        assertThat(part.getQuantity()).isEqualTo(5);
        verify(inventoryStockService).applyMovementByCode(
                BUSINESS_ID, BRANCH_ID, firstItem.getId(), "OUT", 3,
                ACTOR_BUSINESS_USER_ID, ORDER_ID, null, "Ajuste de repuesto en la orden OS-001-01-000001");
        verify(repairOrderHistoryRepository).save(any());
    }

    @Test
    void updatePartReturnsOnlyRemovedQuantity() {
        RepairOrderPart part = part(firstItem, 5);
        when(repairOrderPartRepository.findByIdAndRepairOrderIdAndRepairOrderBusinessIdAndDeletedAtIsNull(20, ORDER_ID, BUSINESS_ID))
                .thenReturn(Optional.of(part));
        when(inventoryStockService.applyMovementByCode(
                eq(BUSINESS_ID), eq(BRANCH_ID), eq(firstItem.getId()), eq("RETURN"), eq(3),
                eq(ACTOR_BUSINESS_USER_ID), eq(ORDER_ID), eq(null), anyString()))
                .thenReturn(movement(firstItem));

        service.updatePart(
                BUSINESS_ID,
                ORDER_ID,
                20,
                new UpdateRepairOrderPartRequest(firstItem.getId(), 2, new BigDecimal("85000.00"), USED_BY_ID),
                SUBJECT);

        assertThat(part.getQuantity()).isEqualTo(2);
        verify(inventoryStockService).applyMovementByCode(
                BUSINESS_ID, BRANCH_ID, firstItem.getId(), "RETURN", 3,
                ACTOR_BUSINESS_USER_ID, ORDER_ID, null, "Ajuste de repuesto en la orden OS-001-01-000001");
    }

    @Test
    void updatePartReturnsOldItemAndTakesNewItem() {
        InventoryItem replacement = inventoryItem(11, order.getBranch(), "BAT-02");
        RepairOrderPart part = part(firstItem, 2);
        when(repairOrderPartRepository.findByIdAndRepairOrderIdAndRepairOrderBusinessIdAndDeletedAtIsNull(20, ORDER_ID, BUSINESS_ID))
                .thenReturn(Optional.of(part));
        when(inventoryStockService.applyMovementByCode(
                eq(BUSINESS_ID), eq(BRANCH_ID), eq(firstItem.getId()), eq("RETURN"), eq(2),
                eq(ACTOR_BUSINESS_USER_ID), eq(ORDER_ID), eq(null), anyString()))
                .thenReturn(movement(firstItem));
        when(inventoryStockService.applyMovementByCode(
                eq(BUSINESS_ID), eq(BRANCH_ID), eq(replacement.getId()), eq("OUT"), eq(4),
                eq(ACTOR_BUSINESS_USER_ID), eq(ORDER_ID), eq(null), anyString()))
                .thenReturn(movement(replacement));

        service.updatePart(
                BUSINESS_ID,
                ORDER_ID,
                20,
                new UpdateRepairOrderPartRequest(replacement.getId(), 4, new BigDecimal("95000.00"), USED_BY_ID),
                SUBJECT);

        InOrder movements = inOrder(inventoryStockService);
        movements.verify(inventoryStockService).applyMovementByCode(
                BUSINESS_ID, BRANCH_ID, firstItem.getId(), "RETURN", 2,
                ACTOR_BUSINESS_USER_ID, ORDER_ID, null,
                "Devolución por cambio de repuesto en la orden OS-001-01-000001");
        movements.verify(inventoryStockService).applyMovementByCode(
                BUSINESS_ID, BRANCH_ID, replacement.getId(), "OUT", 4,
                ACTOR_BUSINESS_USER_ID, ORDER_ID, null,
                "Salida por cambio de repuesto en la orden OS-001-01-000001");
        assertThat(part.getInventoryItem()).isSameAs(replacement);
        assertThat(part.getQuantity()).isEqualTo(4);
    }

    @Test
    void deletePartReturnsStockBeforeSoftDelete() {
        RepairOrderPart part = part(firstItem, 2);
        when(repairOrderPartRepository.findByIdAndRepairOrderIdAndRepairOrderBusinessIdAndDeletedAtIsNull(20, ORDER_ID, BUSINESS_ID))
                .thenReturn(Optional.of(part));

        service.deletePart(BUSINESS_ID, ORDER_ID, 20, SUBJECT);

        verify(inventoryStockService).applyMovementByCode(
                BUSINESS_ID, BRANCH_ID, firstItem.getId(), "RETURN", 2,
                ACTOR_BUSINESS_USER_ID, ORDER_ID, null,
                "Devolución por eliminación de repuesto en la orden OS-001-01-000001");
        assertThat(part.getActive()).isFalse();
        assertThat(part.getDeletedAt()).isNotNull();
        verify(repairOrderHistoryRepository).save(any());
    }

    @Test
    void createPartDoesNotPersistWhenStockMovementFails() {
        when(inventoryStockService.applyMovementByCode(
                eq(BUSINESS_ID), eq(BRANCH_ID), eq(firstItem.getId()), eq("OUT"), eq(20),
                eq(ACTOR_BUSINESS_USER_ID), eq(ORDER_ID), eq(null), anyString()))
                .thenThrow(new ConflictException("inventory.stock.insufficient"));

        assertThatThrownBy(() -> service.createPart(
                BUSINESS_ID,
                ORDER_ID,
                new CreateRepairOrderPartRequest(firstItem.getId(), 20, new BigDecimal("85000.00"), USED_BY_ID),
                SUBJECT))
                .isInstanceOf(ConflictException.class);

        verify(repairOrderPartRepository, never()).save(any());
    }

    @Test
    void financialAndDiagnosisChangesAreAudited() {
        when(repairOrderPartRepository.sumActivePartsTotal(ORDER_ID, BUSINESS_ID))
                .thenReturn(new BigDecimal("85000.00"));

        service.updateFinancials(
                BUSINESS_ID,
                ORDER_ID,
                new UpdateRepairOrderFinancialsRequest(new BigDecimal("150000.00")),
                SUBJECT);
        service.updateDiagnosis(
                BUSINESS_ID,
                ORDER_ID,
                new UpdateRepairOrderDiagnosisRequest("  Falla confirmada en circuito de carga.  "),
                SUBJECT);

        assertThat(order.getFinalPrice()).isEqualByComparingTo("150000.00");
        assertThat(order.getDiagnosis()).isEqualTo("Falla confirmada en circuito de carga.");
        verify(repairOrderHistoryRepository, org.mockito.Mockito.times(2)).save(any());
    }

    @Test
    void deliveryRequiresClosePermissionAndCreatesDeliveryTimestampAndHistory() {
        BusinessCatalogItem ready = new BusinessCatalogItem();
        ready.setCode("READY_FOR_PICKUP");
        order.setStatus(ready);
        order.setFinalPrice(new BigDecimal("150000.00"));
        BusinessCatalogItem delivered = new BusinessCatalogItem();
        delivered.setId(90);
        delivered.setCode("DELIVERED");
        delivered.setName("Entregada");
        when(businessCatalogLookupService.findActive(
                BUSINESS_ID, 90, com.tecmarket.backend.business.entity.BusinessCatalogType.REPAIR_ORDER_STATUS))
                .thenReturn(delivered);

        service.updateStatus(
                BUSINESS_ID,
                ORDER_ID,
                new UpdateRepairOrderStatusRequest(90, null, RepairOrderHistoryVisibility.CUSTOMER),
                SUBJECT);

        verify(businessAccessGuard).assertHasBusinessPermission(BUSINESS_ID, SUBJECT, "REPAIR_ORDER_CLOSE");
        assertThat(order.getDeliveredAt()).isNotNull();
        verify(repairOrderHistoryRepository).save(any());
    }

    @Test
    void deliveryIsRejectedUntilFinalPriceIsRecorded() {
        BusinessCatalogItem ready = new BusinessCatalogItem();
        ready.setCode("READY_FOR_PICKUP");
        order.setStatus(ready);
        BusinessCatalogItem delivered = new BusinessCatalogItem();
        delivered.setId(90);
        delivered.setCode("DELIVERED");
        when(businessCatalogLookupService.findActive(
                BUSINESS_ID, 90, com.tecmarket.backend.business.entity.BusinessCatalogType.REPAIR_ORDER_STATUS))
                .thenReturn(delivered);

        assertThatThrownBy(() -> service.updateStatus(
                BUSINESS_ID,
                ORDER_ID,
                new UpdateRepairOrderStatusRequest(90, null, RepairOrderHistoryVisibility.CUSTOMER),
                SUBJECT))
                .isInstanceOf(ConflictException.class)
                .hasMessage("repair_order.final_price.required_for_delivery");

        assertThat(order.getDeliveredAt()).isNull();
        verify(repairOrderHistoryRepository, never()).save(any());
    }

    private RepairOrderPart part(InventoryItem item, int quantity) {
        RepairOrderPart part = new RepairOrderPart();
        part.setId(20);
        part.setRepairOrder(order);
        part.setInventoryItem(item);
        part.setQuantity(quantity);
        part.setUnitPrice(new BigDecimal("85000.00"));
        part.setTotalPrice(new BigDecimal("85000.00").multiply(BigDecimal.valueOf(quantity)));
        part.setUsedByBusinessUser(usedBy);
        part.setActive(true);
        return part;
    }

    private InventoryItem inventoryItem(int id, BusinessBranch branch, String sku) {
        BusinessCatalogItem category = new BusinessCatalogItem();
        category.setCode("SPARE_PART");
        category.setName("Repuesto");

        InventoryItem item = new InventoryItem();
        item.setId(id);
        item.setBranch(branch);
        item.setBusiness(branch.getBusiness());
        item.setItemCategory(category);
        item.setName("Batería " + sku);
        item.setSku(sku);
        item.setActive(true);
        return item;
    }

    private StockMovement movement(InventoryItem item) {
        StockMovement movement = new StockMovement();
        movement.setInventoryItem(item);
        return movement;
    }

    private BusinessUser businessUser(int id, Business business, AppUser user) {
        BusinessUser businessUser = new BusinessUser();
        businessUser.setId(id);
        businessUser.setBusiness(business);
        businessUser.setUser(user);
        businessUser.setActive(true);
        return businessUser;
    }

    private AppUser user(int id, String email) {
        AppUser user = new AppUser();
        user.setId(id);
        user.setName("Usuario");
        user.setSurname("Prueba");
        user.setEmail(email);
        user.setActive(true);
        return user;
    }
}
