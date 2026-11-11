package com.tecmarket.backend.repairorder.entity;

import com.tecmarket.backend.user.entity.AppUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import com.tecmarket.backend.business.entity.Business;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "repair_order_quality_check")
public class RepairOrderQualityCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false, foreignKey = @ForeignKey(name = "fk_business_id_repair_order_quality_check"))
    private Business business;

    @Column(name = "repair_order_id")
    private Integer repairOrderId;

    @Column(name = "intake_ticket_code", nullable = false, length = 60)
    private String intakeTicketCode;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "business_quality_test_id", nullable = false, foreignKey = @ForeignKey(name = "fk_business_quality_test_id_repair_order_quality_check"))
    private BusinessQualityTest businessQualityTest;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "checked_by_user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_checked_by_user_id_repair_order_quality_check"))
    private AppUser checkedByUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "result", nullable = false, length = 20)
    private RepairOrderQualityCheckResult result;

    @Column(name = "notes", length = 300)
    private String notes;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "checked_at", nullable = false)
    private LocalDateTime checkedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.checkedAt == null) {
            this.checkedAt = now;
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
