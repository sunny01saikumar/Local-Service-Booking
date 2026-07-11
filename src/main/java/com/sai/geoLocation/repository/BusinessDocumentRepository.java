package com.sai.geoLocation.repository;

import com.sai.geoLocation.entity.BusinessDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface BusinessDocumentRepository extends JpaRepository<BusinessDocument, UUID> {
    List<BusinessDocument> findByBusinessId(UUID businessId);
}
