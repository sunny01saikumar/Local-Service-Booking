package com.sai.geoLocation.business;

import com.sai.geoLocation.common.ApiResponse;
import com.sai.geoLocation.entity.Business;
import com.sai.geoLocation.entity.BusinessLocation;
import com.sai.geoLocation.entity.BusinessDocument;
import com.sai.geoLocation.entity.User;
import com.sai.geoLocation.repository.UserRepository;
import com.sai.geoLocation.repository.BusinessRepository;
import com.sai.geoLocation.controller.CrudController;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/providers")
public class BusinessController extends CrudController<Business> {
    private final BusinessService businessService;
    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;

    public BusinessController(BusinessService service, UserRepository userRepository, BusinessRepository businessRepository) {
        super(service);
        this.businessService = service;
        this.userRepository = userRepository;
        this.businessRepository = businessRepository;
    }

    public record ProviderRegisterRequest(
        String name,
        String legalName,
        String ownerName,
        String phone,
        String email,
        String gstNumber,
        String businessType,
        String locationName,
        String line1,
        String line2,
        String city,
        String state,
        String postalCode,
        java.math.BigDecimal latitude,
        java.math.BigDecimal longitude
    ) {}

    public record KycUploadRequest(
        String documentType,
        String documentNumber,
        String fileUrl
    ) {}

    public record ApproveRequest(
        boolean approve,
        String notes
    ) {}

    @PostMapping("/register")
    public ApiResponse<Business> registerProvider(Principal principal, @RequestBody ProviderRegisterRequest request) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        
        Business business = new Business();
        business.setOwnerId(user.getId());
        business.setName(request.name());
        business.setLegalName(request.legalName());
        business.setOwnerName(request.ownerName());
        business.setPhone(request.phone());
        business.setEmail(request.email());
        business.setGstNumber(request.gstNumber());
        business.setBusinessType(request.businessType());
        business.setProvidesServices(true);
        business.setProvidesProducts("BOTH".equals(request.businessType()));

        BusinessLocation location = new BusinessLocation();
        location.setName(request.locationName());
        location.setPhone(request.phone());
        location.setLine1(request.line1());
        location.setLine2(request.line2());
        location.setCity(request.city());
        location.setState(request.state());
        location.setPostalCode(request.postalCode());
        location.setLatitude(request.latitude());
        location.setLongitude(request.longitude());

        return ApiResponse.ok("Provider registered successfully", businessService.registerBusiness(business, location));
    }

    @PostMapping("/kyc")
    public ApiResponse<BusinessDocument> uploadKyc(Principal principal, @RequestBody KycUploadRequest request) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        Business business = businessService.getByOwnerId(user.getId());
        BusinessDocument doc = businessService.submitKycDocument(
                business.getId(),
                request.documentType(),
                request.documentNumber(),
                request.fileUrl()
        );
        return ApiResponse.ok("KYC document submitted", doc);
    }

    @GetMapping("/my-business")
    public ApiResponse<Business> getMyBusiness(Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        return ApiResponse.ok("Fetched provider business profile", businessService.getByOwnerId(user.getId()));
    }

    @GetMapping("/my-business/locations")
    public ApiResponse<List<BusinessLocation>> getMyLocations(Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        Business business = businessService.getByOwnerId(user.getId());
        return ApiResponse.ok("Fetched locations", businessService.getLocations(business.getId()));
    }

    @PutMapping("/locations/{locationId}")
    public ApiResponse<BusinessLocation> updateLocation(@PathVariable UUID locationId, @RequestBody BusinessLocation update) {
        return ApiResponse.ok("Location updated successfully", businessService.updateLocation(locationId, update));
    }

    @GetMapping("/kyc/documents")
    public ApiResponse<List<BusinessDocument>> getMyKycDocuments(Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        Business business = businessService.getByOwnerId(user.getId());
        return ApiResponse.ok("Fetched KYC documents", businessService.getKycDocuments(business.getId()));
    }

    // Admin KYC Endpoint
    @GetMapping("/admin/pending")
    public ApiResponse<List<Business>> getPendingApprovals() {
        return ApiResponse.ok("Fetched pending approvals", businessRepository.findAll().stream()
                .filter(b -> "PENDING".equals(b.getApprovalStatus()))
                .toList());
    }

    @PostMapping("/admin/{businessId}/approve")
    public ApiResponse<Business> approveProvider(Principal principal, @PathVariable UUID businessId, @RequestBody ApproveRequest request) {
        User admin = userRepository.findByEmail(principal.getName()).orElseThrow();
        return ApiResponse.ok("Provider registration status updated",
                businessService.approveBusiness(businessId, admin.getId(), request.approve(), request.notes()));
    }
}
