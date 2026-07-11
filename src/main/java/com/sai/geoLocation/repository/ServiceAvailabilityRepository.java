package com.sai.geoLocation.repository;

import com.sai.geoLocation.entity.ServiceAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ServiceAvailabilityRepository extends JpaRepository<ServiceAvailability, UUID> {
    List<ServiceAvailability> findByServiceId(UUID serviceId);
    List<ServiceAvailability> findByBusinessLocationId(UUID businessLocationId);
}
