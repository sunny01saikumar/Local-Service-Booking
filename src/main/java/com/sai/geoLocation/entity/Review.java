package com.sai.geoLocation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "reviews")
public class Review extends BaseEntity {
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "business_id")
    private UUID businessId;

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "service_id")
    private UUID serviceId;

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "booking_id")
    private UUID bookingId;

    @Column(nullable = false)
    private Integer rating;

    private String title;
    private String comment;

    @Column(name = "moderation_status", nullable = false)
    private String moderationStatus = "PUBLISHED";
}
