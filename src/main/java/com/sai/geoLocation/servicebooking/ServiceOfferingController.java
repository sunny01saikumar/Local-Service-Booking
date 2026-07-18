package com.sai.geoLocation.servicebooking;

import com.sai.geoLocation.common.ApiResponse;
import com.sai.geoLocation.controller.CrudController;
import com.sai.geoLocation.entity.ServiceOffering;
import com.sai.geoLocation.entity.BusinessLocation;
import com.sai.geoLocation.repository.ServiceOfferingRepository;
import com.sai.geoLocation.repository.BusinessRepository;
import com.sai.geoLocation.repository.BusinessLocationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/services")
public class ServiceOfferingController extends CrudController<ServiceOffering> {
    private final ServiceOfferingRepository offeringRepository;
    private final BusinessRepository businessRepository;
    private final BusinessLocationRepository locationRepository;

    public ServiceOfferingController(ServiceOfferingService service,
                                     ServiceOfferingRepository offeringRepository,
                                     BusinessRepository businessRepository,
                                     BusinessLocationRepository locationRepository) {
        super(service);
        this.offeringRepository = offeringRepository;
        this.businessRepository = businessRepository;
        this.locationRepository = locationRepository;
    }

    public record ServiceSearchResponse(
        UUID serviceId,
        String serviceName,
        String description,
        java.math.BigDecimal price,
        java.math.BigDecimal discountPrice,
        Integer durationMinutes,
        UUID categoryId,
        UUID businessId,
        String shopName,
        String shopAddress,
        double distanceKm,
        double serviceRadiusKm,
        boolean withinRadius
    ) {}

    @GetMapping("/search")
    public ApiResponse<List<ServiceSearchResponse>> searchServices(
            @RequestParam("lat") double lat,
            @RequestParam("lng") double lng,
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "categoryId", required = false) UUID categoryId
    ) {
        List<ServiceOffering> offerings;
        if (categoryId != null) {
            offerings = offeringRepository.findByCategoryId(categoryId);
        } else {
            offerings = offeringRepository.findAll();
        }

        List<ServiceSearchResponse> results = offerings.stream()
            .filter(o -> {
                if (query == null || query.trim().isEmpty()) return true;
                return o.getName().toLowerCase().contains(query.toLowerCase()) || 
                       (o.getDescription() != null && o.getDescription().toLowerCase().contains(query.toLowerCase()));
            })
            .map(o -> {
                var bizOpt = businessRepository.findById(o.getBusinessId());
                if (bizOpt.isEmpty()) return null;
                var biz = bizOpt.get();

                var locations = locationRepository.findByBusinessId(o.getBusinessId());
                if (locations.isEmpty()) return null;
                var loc = locations.stream().filter(BusinessLocation::isPrimary).findFirst()
                        .orElse(locations.get(0));

                if (loc.getLatitude() == null || loc.getLongitude() == null) return null;

                double distance = calculateDistance(lat, lng, loc.getLatitude().doubleValue(), loc.getLongitude().doubleValue());
                double radius = loc.getServiceRadiusKm() != null ? loc.getServiceRadiusKm().doubleValue() : 10.0;

                return new ServiceSearchResponse(
                    o.getId(),
                    o.getName(),
                    o.getDescription(),
                    o.getPrice(),
                    o.getDiscountPrice(),
                    o.getDurationMinutes(),
                    o.getCategoryId(),
                    o.getBusinessId(),
                    biz.getName(),
                    loc.getLine1() + ", " + loc.getCity(),
                    distance,
                    radius,
                    distance <= radius
                );
            })
            .filter(java.util.Objects::nonNull)
            .filter(ServiceSearchResponse::withinRadius)
            .sorted(java.util.Comparator.comparingDouble(ServiceSearchResponse::distanceKm))
            .collect(Collectors.toList());

        return ApiResponse.ok("Filtered services by geolocation radius successfully", results);
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371; // kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }
}
