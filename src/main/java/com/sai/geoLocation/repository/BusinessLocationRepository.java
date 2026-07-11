package com.sai.geoLocation.repository;

import com.sai.geoLocation.entity.BusinessLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BusinessLocationRepository extends JpaRepository<BusinessLocation, UUID> {
    List<BusinessLocation> findByBusinessId(UUID businessId);
}
