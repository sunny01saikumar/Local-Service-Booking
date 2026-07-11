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
@Table(name = "bookings")
public class Booking extends BaseEntity {
    @Column(name = "booking_number", nullable = false, unique = true)
    private String bookingNumber;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "business_id", nullable = false)
    private UUID businessId;

    @Column(name = "business_location_id", nullable = false)
    private UUID businessLocationId;

    @Column(name = "service_id", nullable = false)
    private UUID serviceId;

    @Column(name = "booking_slot_id")
    private UUID bookingSlotId;

    @Column(nullable = false)
    private String status = "PENDING";

    @Column(name = "scheduled_start_at", nullable = false)
    private OffsetDateTime scheduledStartAt;

    @Column(name = "scheduled_end_at", nullable = false)
    private OffsetDateTime scheduledEndAt;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    private String notes;

    @Column(name = "cancellation_reason")
    private String cancellationReason;

    @Column(name = "cancelled_at")
    private OffsetDateTime cancelledAt;
}
