package com.sai.geoLocation.servicebooking;

import com.sai.geoLocation.entity.*;
import com.sai.geoLocation.exception.BusinessRuleException;
import com.sai.geoLocation.exception.ResourceNotFoundException;
import com.sai.geoLocation.repository.*;
import com.sai.geoLocation.service.CrudService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingService extends CrudService<Booking> {
    private final BookingRepository bookings;
    private final BusinessRepository businesses;
    private final BusinessLocationRepository locations;
    private final ServiceOfferingRepository services;
    private final WalletRepository wallets;
    private final WalletTransactionRepository walletTransactions;
    private final ReviewRepository reviews;
    private final UserRepository users;

    public BookingService(BookingRepository bookings, BusinessRepository businesses,
                          BusinessLocationRepository locations, ServiceOfferingRepository services,
                          WalletRepository wallets, WalletTransactionRepository walletTransactions,
                          ReviewRepository reviews, UserRepository users) {
        super(bookings);
        this.bookings = bookings;
        this.businesses = businesses;
        this.locations = locations;
        this.services = services;
        this.wallets = wallets;
        this.walletTransactions = walletTransactions;
        this.reviews = reviews;
        this.users = users;
    }

    @Transactional
    public Booking createBooking(UUID customerId, UUID serviceId, OffsetDateTime scheduledStart, String notes, BigDecimal customerLat, BigDecimal customerLng) {
        ServiceOffering service = services.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service offering not found"));

        Business business = businesses.findById(service.getBusinessId())
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

        // Match primary business location
        List<BusinessLocation> bizLocations = locations.findByBusinessId(business.getId());
        BusinessLocation selectLoc = bizLocations.stream()
                .filter(BusinessLocation::isPrimary)
                .findFirst()
                .orElse(bizLocations.isEmpty() ? null : bizLocations.get(0));

        if (selectLoc == null) {
            throw new BusinessRuleException("Provider has no business locations registered");
        }

        // Verify distance
        double distance = calculateDistance(customerLat, customerLng, selectLoc.getLatitude(), selectLoc.getLongitude());
        BigDecimal maxRadius = selectLoc.getServiceRadiusKm() != null ? selectLoc.getServiceRadiusKm() : BigDecimal.valueOf(10);
        if (distance > maxRadius.doubleValue()) {
            throw new BusinessRuleException("We do not offer services in your location yet. Max service radius is " + maxRadius + " km. You are " + String.format("%.2f", distance) + " km away.");
        }

        Booking booking = new Booking();
        booking.setBookingNumber("LH-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        booking.setCustomerId(customerId);
        booking.setBusinessId(business.getId());
        booking.setBusinessLocationId(selectLoc.getId());
        booking.setServiceId(service.getId());
        booking.setStatus("CREATED");
        booking.setScheduledStartAt(scheduledStart);
        booking.setScheduledEndAt(scheduledStart.plusMinutes(service.getDurationMinutes()));
        booking.setTotalAmount(service.getDiscountPrice() != null ? service.getDiscountPrice() : service.getPrice());
        booking.setNotes(notes);
        booking = bookings.save(booking);

        // Dispatch simulated WhatsApp notification to provider
        User customer = users.findById(customerId).orElseThrow();
        System.out.println("==================================================");
        System.out.println("WHATSAPP INTEGRATION: Notification sent to Provider " + business.getOwnerName() + " (" + business.getPhone() + ")");
        System.out.println("Message Content:");
        System.out.println("New Booking Received!\n" +
                           "Customer: " + customer.getFirstName() + " " + customer.getLastName() + "\n" +
                           "Service: " + service.getName() + "\n" +
                           "Address: Godavarikhani GPS (" + customerLat + ", " + customerLng + ")\n" +
                           "Booking Time: " + scheduledStart + "\n" +
                           "Accept: /api/v1/bookings/" + booking.getId() + "/accept");
        System.out.println("==================================================");

        return booking;
    }

    @Transactional
    public Booking acceptBooking(UUID bookingId) {
        Booking booking = findById(bookingId);
        if (!"CREATED".equals(booking.getStatus()) && !"ASSIGNED".equals(booking.getStatus())) {
            throw new BusinessRuleException("Booking cannot be accepted from state " + booking.getStatus());
        }

        booking.setStatus("ACCEPTED");
        booking = bookings.save(booking);

        Business business = businesses.findById(booking.getBusinessId()).orElseThrow();
        User customer = users.findById(booking.getCustomerId()).orElseThrow();

        // Dispatch simulated WhatsApp notification to customer
        System.out.println("==================================================");
        System.out.println("WHATSAPP INTEGRATION: Notification sent to Customer " + customer.getFirstName() + " (" + customer.getPhone() + ")");
        System.out.println("Message Content:");
        System.out.println("Your booking " + booking.getBookingNumber() + " has been accepted!\n" +
                           "Provider: " + business.getOwnerName() + "\n" +
                           "Phone: " + business.getPhone() + "\n" +
                           "Estimated Arrival: 25 minutes\n" +
                           "Track Provider Link: http://localhost:5173/track/" + booking.getId());
        System.out.println("==================================================");

        return booking;
    }

    @Transactional
    public Booking updateStatus(UUID bookingId, String nextStatus) {
        Booking booking = findById(bookingId);
        String currentStatus = booking.getStatus();

        // Validate state transitions
        boolean valid = false;
        if ("ACCEPTED".equals(currentStatus) && "ON_THE_WAY".equals(nextStatus)) valid = true;
        else if ("ON_THE_WAY".equals(currentStatus) && "ARRIVED".equals(nextStatus)) valid = true;
        else if ("ARRIVED".equals(currentStatus) && "WORK_STARTED".equals(nextStatus)) valid = true;
        else if ("WORK_STARTED".equals(currentStatus) && "WORK_COMPLETED".equals(nextStatus)) valid = true;

        if (!valid) {
            throw new BusinessRuleException("Invalid status transition from " + currentStatus + " to " + nextStatus);
        }

        booking.setStatus(nextStatus);
        return bookings.save(booking);
    }

    @Transactional
    public Booking confirmCompletionAndPay(UUID bookingId) {
        Booking booking = findById(bookingId);
        if (!"WORK_COMPLETED".equals(booking.getStatus())) {
            throw new BusinessRuleException("Cannot confirm and pay a booking that is not completed. Current status: " + booking.getStatus());
        }

        Wallet customerWallet = wallets.findByUserId(booking.getCustomerId())
                .orElseThrow(() -> new BusinessRuleException("Customer wallet not found"));

        if (customerWallet.getBalance().compareTo(booking.getTotalAmount()) < 0) {
            booking.setStatus("PAYMENT_PENDING");
            bookings.save(booking);
            throw new BusinessRuleException("Insufficient wallet balance. Please add funds to release payment.");
        }

        Business business = businesses.findById(booking.getBusinessId()).orElseThrow();
        Wallet providerWallet = wallets.findByUserId(business.getOwnerId())
                .orElseThrow(() -> new BusinessRuleException("Provider wallet not found"));

        // Deduct customer
        customerWallet.setBalance(customerWallet.getBalance().subtract(booking.getTotalAmount()));
        wallets.save(customerWallet);

        // Record customer debit transaction
        WalletTransaction custTxn = new WalletTransaction();
        custTxn.setWalletId(customerWallet.getId());
        custTxn.setUserId(booking.getCustomerId());
        custTxn.setTransactionType("DEBIT");
        custTxn.setAmount(booking.getTotalAmount());
        custTxn.setBalanceAfter(customerWallet.getBalance());
        custTxn.setReferenceType("BOOKING");
        custTxn.setReferenceId(booking.getId());
        custTxn.setDescription("Paid for booking " + booking.getBookingNumber());
        walletTransactions.save(custTxn);

        // Calculate commission
        BigDecimal rate = business.getCommissionRate().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        BigDecimal commissionAmount = booking.getTotalAmount().multiply(rate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal earningsAmount = booking.getTotalAmount().subtract(commissionAmount).setScale(2, RoundingMode.HALF_UP);

        // Credit provider
        providerWallet.setBalance(providerWallet.getBalance().add(earningsAmount));
        wallets.save(providerWallet);

        // Record provider credit transaction
        WalletTransaction provTxn = new WalletTransaction();
        provTxn.setWalletId(providerWallet.getId());
        provTxn.setUserId(business.getOwnerId());
        provTxn.setTransactionType("CREDIT");
        provTxn.setAmount(earningsAmount);
        provTxn.setBalanceAfter(providerWallet.getBalance());
        provTxn.setReferenceType("BOOKING");
        provTxn.setReferenceId(booking.getId());
        provTxn.setDescription("Earnings for booking " + booking.getBookingNumber() + " (Commission deducted: INR " + commissionAmount + ")");
        walletTransactions.save(provTxn);

        booking.setStatus("PAID");
        return bookings.save(booking);
    }

    @Transactional
    public Booking cancelBooking(UUID bookingId, String reason) {
        Booking booking = findById(bookingId);
        String currentStatus = booking.getStatus();

        if ("WORK_STARTED".equals(currentStatus) || "WORK_COMPLETED".equals(currentStatus) || "PAID".equals(currentStatus)) {
            throw new BusinessRuleException("Cannot cancel booking once work has started/completed or is paid");
        }

        booking.setStatus("CANCELLED");
        booking.setCancellationReason(reason);
        booking.setCancelledAt(OffsetDateTime.now());
        return bookings.save(booking);
    }

    @Transactional
    public Review rateBooking(UUID bookingId, UUID customerId, int rating, String comment) {
        Booking booking = findById(bookingId);
        if (!"PAID".equals(booking.getStatus()) && !"WORK_COMPLETED".equals(booking.getStatus())) {
            throw new BusinessRuleException("Can only rate completed bookings");
        }

        Review review = new Review();
        review.setBookingId(bookingId);
        review.setUserId(customerId);
        review.setBusinessId(booking.getBusinessId());
        review.setServiceId(booking.getServiceId());
        review.setRating(rating);
        review.setComment(comment);
        return reviews.save(review);
    }

    private double calculateDistance(BigDecimal lat1, BigDecimal lng1, BigDecimal lat2, BigDecimal lng2) {
        double r = 6371; // Earth's radius in km
        double dLat = Math.toRadians(lat2.doubleValue() - lat1.doubleValue());
        double dLng = Math.toRadians(lng2.doubleValue() - lng1.doubleValue());
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1.doubleValue())) * Math.cos(Math.toRadians(lat2.doubleValue())) *
                   Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return r * c;
    }
}
