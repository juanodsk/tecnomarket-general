package com.tecmarket.backend.business.entity;

import com.tecmarket.backend.parameter.entity.City;
import com.tecmarket.backend.user.entity.DocumentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "business")
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "document_type_id", nullable = false, foreignKey = @ForeignKey(name = "fk_document_type_id_business"))
    private DocumentType documentType;

    @Column(name = "document_number", nullable = false, length = 50)
    private String documentNumber;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "legal_name", nullable = false, length = 200)
    private String legalName;

    @Column(name = "email", nullable = false, length = 150)
    private String email;

    @Column(name = "phone", nullable = false, length = 30)
    private String phone;

    @Column(name = "address", nullable = false, length = 200)
    private String address;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false, foreignKey = @ForeignKey(name = "fk_city_id_business"))
    private City city;

    @Column(name = "logo", length = 500)
    private String logo;

    @Column(name = "logo_bucket_name", length = 100)
    private String logoBucketName;

    @Column(name = "logo_object_key", length = 700)
    private String logoObjectKey;

    @Column(name = "cover_image", length = 500)
    private String coverImage;

    @Column(name = "cover_image_bucket_name", length = 100)
    private String coverImageBucketName;

    @Column(name = "cover_image_object_key", length = 700)
    private String coverImageObjectKey;

    @Column(name = "rating_average", precision = 3, scale = 2)
    private BigDecimal ratingAverage = BigDecimal.ZERO;

    @Column(name = "rating_count")
    private Integer ratingCount = 0;

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

        if (this.active == null) {
            this.active = true;
        }
        if (this.ratingAverage == null) {
            this.ratingAverage = BigDecimal.ZERO;
        }
        if (this.ratingCount == null) {
            this.ratingCount = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
