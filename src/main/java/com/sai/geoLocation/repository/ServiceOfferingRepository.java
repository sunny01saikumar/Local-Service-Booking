package com.sai.geoLocation.repository;

import com.sai.geoLocation.entity.ServiceOffering;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ServiceOfferingRepository extends JpaRepository<ServiceOffering, UUID> {
    List<ServiceOffering> findByCategoryId(UUID categoryId);
    List<ServiceOffering> findByCategoryIdAndStatus(UUID categoryId, String status);
}
