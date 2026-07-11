package com.sai.geoLocation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "booking_slots")
public class BookingSlot extends BaseEntity {
    @Column(name = "service_id", nullable = false)
    private UUID serviceId;

    @Column(name = "business_location_id", nullable = false)
    private UUID businessLocationId;

    @Column(name = "starts_at", nullable = false)
    private OffsetDateTime startsAt;

    @Column(name = "ends_at", nullable = false)
    private OffsetDateTime endsAt;

    @Column(nullable = false)
    private Integer capacity = 1;

    @Column(name = "booked_count", nullable = false)
    private Integer bookedCount = 0;

    @Column(nullable = false)
    private String status = "OPEN";
}
