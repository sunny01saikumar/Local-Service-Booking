package com.sai.geoLocation.repository;

import com.sai.geoLocation.entity.BusinessCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BusinessCategoryRepository extends JpaRepository<BusinessCategory, UUID> {
}
