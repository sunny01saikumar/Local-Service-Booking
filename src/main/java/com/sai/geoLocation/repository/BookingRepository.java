package com.sai.geoLocation.repository;

import com.sai.geoLocation.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
    List<Booking> findByCustomerIdOrderByCreatedAtDesc(UUID customerId);
    List<Booking> findByBusinessIdOrderByCreatedAtDesc(UUID businessId);
}
