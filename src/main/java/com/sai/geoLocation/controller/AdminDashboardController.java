package com.sai.geoLocation.controller;

import com.sai.geoLocation.common.ApiResponse;
import com.sai.geoLocation.entity.Booking;
import com.sai.geoLocation.entity.Business;
import com.sai.geoLocation.repository.BookingRepository;
import com.sai.geoLocation.repository.BusinessRepository;
import com.sai.geoLocation.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin/dashboard")
public class AdminDashboardController {
    private final BookingRepository bookings;
    private final BusinessRepository businesses;
    private final UserRepository users;

    public AdminDashboardController(BookingRepository bookings, BusinessRepository businesses, UserRepository users) {
        this.bookings = bookings;
        this.businesses = businesses;
        this.users = users;
    }

    public record StatsResponse(
        BigDecimal totalRevenue,
        long totalBookings,
        double cancellationRate,
        List<String> popularServices,
        List<String> topProviders,
        long totalUsers
    ) {}

    @GetMapping("/stats")
    public ApiResponse<StatsResponse> getStats() {
        List<Booking> allBookings = bookings.findAll();
        
        BigDecimal totalRevenue = allBookings.stream()
                .filter(b -> "PAID".equals(b.getStatus()))
                .map(Booking::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalBookings = allBookings.size();
        long cancelled = allBookings.stream().filter(b -> "CANCELLED".equals(b.getStatus())).count();
        double cancellationRate = totalBookings == 0 ? 0.0 : (double) cancelled / totalBookings * 100;

        List<String> popularServices = List.of("Electrician Visit", "AC Repair Deep Cleaning", "Plumbing General Leakage Fix");
        List<String> topProviders = businesses.findAll().stream()
                .map(Business::getName)
                .limit(3)
                .collect(Collectors.toList());

        long totalUsers = users.count();

        StatsResponse stats = new StatsResponse(
                totalRevenue,
                totalBookings,
                cancellationRate,
                popularServices,
                topProviders,
                totalUsers
        );

        return ApiResponse.ok("Fetched dashboard analytics stats successfully", stats);
    }
}
