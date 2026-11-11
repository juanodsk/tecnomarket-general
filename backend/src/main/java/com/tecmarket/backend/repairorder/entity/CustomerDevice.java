package com.tecmarket.backend.repairorder.entity;

import com.tecmarket.backend.user.entity.AppUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.business.entity.BusinessCatalogItem;
import com.tecmarket.backend.business.entity.BusinessUser;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "customer_device")
public class CustomerDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false, foreignKey = @ForeignKey(name = "fk_business_id_customer_device"))
    private Business business;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_id_customer_device"))
    private AppUser customer;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "device_type_id", nullable = false, foreignKey = @ForeignKey(name = "fk_device_type_id_customer_device"))
    private BusinessCatalogItem deviceType;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false, foreignKey = @ForeignKey(name = "fk_brand_id_customer_device"))
    private BusinessCatalogItem brand;

    @Column(name = "model", nullable = false, length = 50)
    private String model;

    @Column(name = "serial_number", length = 60)
    private String serialNumber;

    @Column(name = "imei", length = 60)
    private String imei;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "registered_by_business_user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_business_user_id_customer_device"))
    private BusinessUser registeredByBusinessUser;

    @Column(name = "color", nullable = false, length = 20)
    private String color;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

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

        if (this.registeredAt == null) {
            this.registeredAt = now;
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

