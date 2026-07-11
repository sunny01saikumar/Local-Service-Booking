package com.sai.geoLocation.business;

import com.sai.geoLocation.entity.Business;
import com.sai.geoLocation.entity.BusinessLocation;
import com.sai.geoLocation.entity.BusinessDocument;
import com.sai.geoLocation.exception.BusinessRuleException;
import com.sai.geoLocation.exception.ResourceNotFoundException;
import com.sai.geoLocation.repository.BusinessRepository;
import com.sai.geoLocation.repository.BusinessLocationRepository;
import com.sai.geoLocation.repository.BusinessDocumentRepository;
import com.sai.geoLocation.service.CrudService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class BusinessService extends CrudService<Business> {
    private final BusinessRepository businesses;
    private final BusinessLocationRepository locations;
    private final BusinessDocumentRepository documents;

    public BusinessService(BusinessRepository repository, BusinessLocationRepository locations, BusinessDocumentRepository documents) {
        super(repository);
        this.businesses = repository;
        this.locations = locations;
        this.documents = documents;
    }

    @Transactional
    public Business registerBusiness(Business business, BusinessLocation primaryLocation) {
        if (businesses.findByOwnerId(business.getOwnerId()).isPresent()) {
            throw new BusinessRuleException("You have already registered a business/provider profile");
        }

        business.setApprovalStatus("PENDING");
        business.setVerificationStatus("UNVERIFIED");
        business = businesses.save(business);

        primaryLocation.setBusinessId(business.getId());
        primaryLocation.setPrimary(true);
        locations.save(primaryLocation);

        return business;
    }

    @Transactional
    public BusinessDocument submitKycDocument(UUID businessId, String docType, String docNumber, String fileUrl) {
        Business business = businesses.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found: " + businessId));

        BusinessDocument doc = new BusinessDocument();
        doc.setBusinessId(businessId);
        doc.setDocumentType(docType.toUpperCase());
        doc.setDocumentNumber(docNumber);
        doc.setFileUrl(fileUrl);
        doc.setVerificationStatus("PENDING");
        doc = documents.save(doc);

        business.setVerificationStatus("IN_REVIEW");
        businesses.save(business);

        return doc;
    }

    @Transactional
    public Business approveBusiness(UUID businessId, UUID adminUserId, boolean approve, String notes) {
        Business business = businesses.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found: " + businessId));

        if (approve) {
            business.setApprovalStatus("APPROVED");
            business.setVerificationStatus("VERIFIED");
            business.setApprovedBy(adminUserId);
            business.setApprovedAt(OffsetDateTime.now());
            business.setApprovalNotes(notes);
        } else {
            business.setApprovalStatus("REJECTED");
            business.setVerificationStatus("FAILED");
            business.setRejectedReason(notes);
        }

        return businesses.save(business);
    }

    public Business getByOwnerId(UUID ownerId) {
        return businesses.findByOwnerId(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("No business registered for owner: " + ownerId));
    }

    public List<BusinessDocument> getKycDocuments(UUID businessId) {
        return documents.findByBusinessId(businessId);
    }

    @Transactional
    public BusinessLocation updateLocation(UUID locationId, BusinessLocation update) {
        BusinessLocation loc = locations.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found"));

        loc.setName(update.getName());
        loc.setPhone(update.getPhone());
        loc.setLine1(update.getLine1());
        loc.setLine2(update.getLine2());
        loc.setCity(update.getCity());
        loc.setState(update.getState());
        loc.setPostalCode(update.getPostalCode());
        loc.setLatitude(update.getLatitude());
        loc.setLongitude(update.getLongitude());
        if (update.getOpeningTime() != null) loc.setOpeningTime(update.getOpeningTime());
        if (update.getClosingTime() != null) loc.setClosingTime(update.getClosingTime());
        if (update.getServiceRadiusKm() != null) loc.setServiceRadiusKm(update.getServiceRadiusKm());

        return locations.save(loc);
    }

    public List<BusinessLocation> getLocations(UUID businessId) {
        return locations.findByBusinessId(businessId);
    }
}
