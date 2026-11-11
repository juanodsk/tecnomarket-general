package com.tecmarket.backend.repairorder.entity;

import com.tecmarket.backend.user.entity.AppUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.tecmarket.backend.appointment.entity.Appointment;
import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.business.entity.BusinessBranch;
import com.tecmarket.backend.business.entity.BusinessCatalogItem;
import com.tecmarket.backend.business.entity.BusinessOffering;
import com.tecmarket.backend.business.entity.BusinessUser;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "repair_order")
public class RepairOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "order_number", nullable = false, length = 20)
    private String orderNumber;

    @Column(name = "order_sequence", nullable = false)
    private Integer orderSequence;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false, foreignKey = @ForeignKey(name = "fk_business_id_repair_order"))
    private Business business;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false, foreignKey = @ForeignKey(name = "fk_branch_id_repair_order"))
    private BusinessBranch branch;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_app_user_id_repair_order"))
    private AppUser customer;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_device_id", nullable = false, foreignKey = @ForeignKey(name = "fk_customer_device_id_repair_order"))
    private CustomerDevice customerDevice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", foreignKey = @ForeignKey(name = "fk_appointment_id_repair_order"))
    private Appointment appointment;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "business_service_id", nullable = false, foreignKey = @ForeignKey(name = "fk_business_service_id_repair_order"))
    private BusinessOffering businessService;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false, foreignKey = @ForeignKey(name = "fk_repair_order_status_id_repair_order"))
    private BusinessCatalogItem status;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "received_by_business_user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_repair_order_received_by_business_user"))
    private BusinessUser receivedByBusinessUser;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_business_user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_repair_order_assigned_business_user"))
    private BusinessUser assignedBusinessUser;

    @Column(name = "problem_description", nullable = false)
    private String problemDescription;

    @Column(name = "diagnosis")
    private String diagnosis;

    @Column(name = "estimated_price", precision = 12, scale = 2)
    private BigDecimal estimatedPrice;

    @Column(name = "final_price", precision = 12, scale = 2)
    private BigDecimal finalPrice;

    @Column(name = "idempotency_key", length = 100)
    private String idempotencyKey;

    @Column(name = "idempotency_fingerprint", length = 64)
    private String idempotencyFingerprint;

    @Column(name = "received_at")
    private LocalDateTime receivedAt;

    @Column(name = "estimated_delivery_at")
    private LocalDateTime estimatedDeliveryAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.receivedAt == null) {
            this.receivedAt = now;
        }
        if (this.active == null) {
            this.active = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

