package com.tecmarket.backend.subscription.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "subscription_usage")
public class SubscriptionUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "business_subscription_id", nullable = false, foreignKey = @ForeignKey(name = "fk_business_subscription_id_subscription_usage"))
    private BusinessSubscription businessSubscription;

    @Column(name = "users_count", nullable = false)
    private Integer usersCount = 0;

    @Column(name = "orders_count", nullable = false)
    private Integer ordersCount = 0;

    @Column(name = "branches_count", nullable = false)
    private Integer branchesCount = 0;

    @Column(name = "storage_used", nullable = false)
    private Long storageUsed = 0L;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    protected void onWrite() {
        this.updatedAt = LocalDateTime.now();
        if (this.usersCount == null) {
            this.usersCount = 0;
        }
        if (this.ordersCount == null) {
            this.ordersCount = 0;
        }
        if (this.branchesCount == null) {
            this.branchesCount = 0;
        }
        if (this.storageUsed == null) {
            this.storageUsed = 0L;
        }
    }
}
