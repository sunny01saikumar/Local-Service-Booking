package com.sai.geoLocation.repository;

import com.sai.geoLocation.entity.BookingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface BookingSlotRepository extends JpaRepository<BookingSlot, UUID> {
    List<BookingSlot> findByServiceIdAndBusinessLocationId(UUID serviceId, UUID businessLocationId);
    List<BookingSlot> findByServiceIdAndBusinessLocationIdAndStartsAtBetween(
            UUID serviceId, UUID businessLocationId, OffsetDateTime start, OffsetDateTime end);
}
