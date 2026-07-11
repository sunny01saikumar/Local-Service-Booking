package com.sai.geoLocation;

import com.sai.geoLocation.auth.AuthService;
import com.sai.geoLocation.dto.AuthDtos;
import com.sai.geoLocation.entity.*;
import com.sai.geoLocation.repository.*;
import com.sai.geoLocation.servicebooking.BookingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@SpringBootTest(properties = {
    "spring.flyway.enabled=false",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@ActiveProfiles("test")
public class LocalHubTests {

    @Autowired
    private AuthService authService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ServiceOfferingRepository serviceRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private BusinessLocationRepository locationRepository;

    @Test
    @Transactional
    public void testAuthAndBookingFlow() {
        // Pre-populate roles for the in-memory test database
        if (roleRepository.findByCode("CUSTOMER").isEmpty()) {
            Role r = new Role();
            r.setCode("CUSTOMER");
            r.setName("Customer");
            roleRepository.save(r);
        }
        if (roleRepository.findByCode("SHOP_OWNER").isEmpty()) {
            Role r = new Role();
            r.setCode("SHOP_OWNER");
            r.setName("Shop Owner");
            roleRepository.save(r);
        }

        // 1. Register customer
        AuthDtos.RegisterRequest regCust = new AuthDtos.RegisterRequest(
                "Test", "Customer", "test_cust@localhub.test", "password123", "+919876543210", "CUSTOMER"
        );
        AuthDtos.AuthResponse respCust = authService.register(regCust);
        Assertions.assertNotNull(respCust.token());
        Assertions.assertEquals("CUSTOMER", respCust.role());

        User customer = userRepository.findByEmail("test_cust@localhub.test").orElseThrow();

        // 2. Register provider
        AuthDtos.RegisterRequest regProv = new AuthDtos.RegisterRequest(
                "Ramesh", "Provider", "ramesh@localhub.test", "password123", "+919876543211", "SHOP_OWNER"
        );
        AuthDtos.AuthResponse respProv = authService.register(regProv);
        Assertions.assertEquals("SHOP_OWNER", respProv.role());

        User provider = userRepository.findByEmail("ramesh@localhub.test").orElseThrow();

        // 3. Setup provider business
        Business business = new Business();
        business.setOwnerId(provider.getId());
        business.setName("Ramesh AC Services");
        business.setOwnerName("Ramesh");
        business.setPhone("+919876543211");
        business.setBusinessType("SERVICES");
        business.setProvidesServices(true);
        business.setApprovalStatus("APPROVED");
        business.setVerificationStatus("VERIFIED");
        business = businessRepository.save(business);

        BusinessLocation loc = new BusinessLocation();
        loc.setBusinessId(business.getId());
        loc.setName("Godavarikhani Branch");
        loc.setLine1("Main Road");
        loc.setCity("Godavarikhani");
        loc.setState("Telangana");
        loc.setPostalCode("505209");
        loc.setLatitude(BigDecimal.valueOf(18.8000));
        loc.setLongitude(BigDecimal.valueOf(79.4500));
        loc.setServiceRadiusKm(BigDecimal.valueOf(10));
        loc.setPrimary(true);
        locationRepository.save(loc);

        // 4. Create Service
        ServiceOffering service = new ServiceOffering();
        service.setBusinessId(business.getId());
        service.setCategoryId(UUID.randomUUID()); // mock category id
        service.setName("AC Repair");
        service.setSlug("ac-repair");
        service.setDurationMinutes(60);
        service.setPrice(BigDecimal.valueOf(500.00));
        service.setStatus("PUBLISHED");
        service = serviceRepository.save(service);

        // 5. Test Geolocation Matching & Booking Creation
        // Customer location (close to provider location: distance ~0km)
        OffsetDateTime scheduledTime = OffsetDateTime.now().plusDays(1);
        Booking booking = bookingService.createBooking(
                customer.getId(),
                service.getId(),
                scheduledTime,
                "AC not cooling",
                BigDecimal.valueOf(18.8000),
                BigDecimal.valueOf(79.4500)
        );

        Assertions.assertNotNull(booking.getBookingNumber());
        Assertions.assertEquals("CREATED", booking.getStatus());

        // 6. Test OTP verification logic
        AuthDtos.OtpRequest otpReq = new AuthDtos.OtpRequest("test_cust@localhub.test", "LOGIN");
        String otpSent = authService.generateOtp(otpReq);
        Assertions.assertNotNull(otpSent);

        AuthDtos.OtpVerifyRequest verifyReq = new AuthDtos.OtpVerifyRequest("test_cust@localhub.test", "LOGIN", "123456");
        boolean verified = authService.verifyOtp(verifyReq);
        Assertions.assertTrue(verified);
    }
}
