package com.sai.geoLocation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "businesses")
public class Business extends BaseEntity {
    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Column(nullable = false)
    private String name;

    @Column(name = "legal_name")
    private String legalName;

    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @Column(nullable = false)
    private String phone;

    private String email;

    @Column(name = "gst_number", unique = true)
    private String gstNumber;

    @Column(name = "business_type", nullable = false)
    private String businessType;

    @Column(name = "approval_status", nullable = false)
    private String approvalStatus = "PENDING";

    @Column(name = "verification_status", nullable = false)
    private String verificationStatus = "UNVERIFIED";

    @Column(name = "approval_notes")
    private String approvalNotes;

    @Column(name = "approved_by")
    private UUID approvedBy;

    @Column(name = "approved_at")
    private OffsetDateTime approvedAt;

    @Column(name = "rejected_reason")
    private String rejectedReason;

    @Column(name = "banner_url")
    private String bannerUrl;

    @Column(name = "logo_url")
    private String logoUrl;

    private String description;

    @Column(name = "provides_products", nullable = false)
    private boolean providesProducts;

    @Column(name = "provides_services", nullable = false)
    private boolean providesServices;

    @Column(name = "commission_rate", nullable = false)
    private BigDecimal commissionRate = BigDecimal.ZERO;
}
