package com.tecmarket.backend.repairorder.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

import com.tecmarket.backend.business.entity.BusinessUser;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "repair_order_attachment")
public class RepairOrderAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "repair_order_id", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_repair_order_id_repair_order_attachment"))
    private RepairOrder repairOrder;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by_business_user_id", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_business_user_id_repair_order_attachment"))
    private BusinessUser uploadedByBusinessUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by_business_user_id", foreignKey = @ForeignKey(name = "fk_deleted_by_business_user_id_repair_order_attachment"))
    private BusinessUser deletedByBusinessUser;

    @Column(name = "file_url", length = 1000, updatable = false)
    private String fileUrl;

    @Column(name = "bucket_name", length = 100, updatable = false)
    private String bucketName;

    @Column(name = "object_key", length = 700, updatable = false)
    private String objectKey;

    @Column(name = "original_file_name", length = 255, updatable = false)
    private String originalFileName;

    @Column(name = "content_type", length = 100, updatable = false)
    private String contentType;

    @Column(name = "file_size", updatable = false)
    private Long fileSize;

    @Column(name = "checksum_sha256", length = 64, columnDefinition = "CHAR(64)", updatable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    private String checksumSha256;

    @Column(name = "description", length = 500, updatable = false)
    private String description;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "retention_until")
    private LocalDateTime retentionUntil;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.active == null) {
            this.active = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
