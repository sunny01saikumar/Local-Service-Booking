package com.sai.geoLocation.servicebooking;

import com.sai.geoLocation.common.ApiResponse;
import com.sai.geoLocation.entity.Booking;
import com.sai.geoLocation.entity.Review;
import com.sai.geoLocation.entity.User;
import com.sai.geoLocation.entity.Business;
import com.sai.geoLocation.repository.UserRepository;
import com.sai.geoLocation.repository.BusinessRepository;
import com.sai.geoLocation.repository.BookingRepository;
import com.sai.geoLocation.controller.CrudController;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController extends CrudController<Booking> {
    private final BookingService bookingService;
    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;
    private final BookingRepository bookingRepository;

    public BookingController(BookingService service, UserRepository userRepository,
                             BusinessRepository businessRepository, BookingRepository bookingRepository) {
        super(service);
        this.bookingService = service;
        this.userRepository = userRepository;
        this.businessRepository = businessRepository;
        this.bookingRepository = bookingRepository;
    }

    public record BookingRequest(
        UUID serviceId,
        OffsetDateTime scheduledStart,
        String notes,
        BigDecimal latitude,
        BigDecimal longitude
    ) {}

    public record StatusUpdateRequest(String status) {}
    public record CancelRequest(String reason) {}
    public record RateRequest(int rating, String comment) {}

    @PostMapping("/create")
    public ApiResponse<Booking> createBooking(Principal principal, @Valid @RequestBody BookingRequest request) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        Booking booking = bookingService.createBooking(
                user.getId(),
                request.serviceId(),
                request.scheduledStart(),
                request.notes(),
                request.latitude(),
                request.longitude()
        );
        return ApiResponse.ok("Booking created successfully", booking);
    }

    @PostMapping("/{bookingId}/accept")
    public ApiResponse<Booking> acceptBooking(@PathVariable UUID bookingId) {
        Booking booking = bookingService.acceptBooking(bookingId);
        return ApiResponse.ok("Booking accepted successfully", booking);
    }

    @PostMapping("/{bookingId}/status")
    public ApiResponse<Booking> updateStatus(@PathVariable UUID bookingId, @RequestBody StatusUpdateRequest request) {
        Booking booking = bookingService.updateStatus(bookingId, request.status());
        return ApiResponse.ok("Booking status updated to " + request.status(), booking);
    }

    @PostMapping("/{bookingId}/confirm-pay")
    public ApiResponse<Booking> confirmAndPay(@PathVariable UUID bookingId) {
        Booking booking = bookingService.confirmCompletionAndPay(bookingId);
        return ApiResponse.ok("Payment released and booking completed", booking);
    }

    @PostMapping("/{bookingId}/cancel")
    public ApiResponse<Booking> cancelBooking(@PathVariable UUID bookingId, @RequestBody CancelRequest request) {
        Booking booking = bookingService.cancelBooking(bookingId, request.reason());
        return ApiResponse.ok("Booking cancelled successfully", booking);
    }

    @PostMapping("/{bookingId}/rate")
    public ApiResponse<Review> rateBooking(Principal principal, @PathVariable UUID bookingId, @RequestBody RateRequest request) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        Review review = bookingService.rateBooking(bookingId, user.getId(), request.rating(), request.comment());
        return ApiResponse.ok("Rating submitted successfully", review);
    }

    @GetMapping("/customer")
    public ApiResponse<List<Booking>> getCustomerBookings(Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        List<Booking> list = bookingRepository.findByCustomerIdOrderByCreatedAtDesc(user.getId());
        return ApiResponse.ok("Fetched customer bookings", list);
    }

    @GetMapping("/provider")
    public ApiResponse<List<Booking>> getProviderBookings(Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        Business business = businessRepository.findByOwnerId(user.getId()).orElseThrow();
        List<Booking> list = bookingRepository.findByBusinessIdOrderByCreatedAtDesc(business.getId());
        return ApiResponse.ok("Fetched provider bookings", list);
    }
}
