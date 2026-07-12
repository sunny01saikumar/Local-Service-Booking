package com.sai.geoLocation.auth;

import com.sai.geoLocation.dto.AuthDtos;
import com.sai.geoLocation.entity.User;
import com.sai.geoLocation.entity.Wallet;
import com.sai.geoLocation.entity.OtpVerification;
import com.sai.geoLocation.exception.BusinessRuleException;
import com.sai.geoLocation.exception.ResourceNotFoundException;
import com.sai.geoLocation.jwt.JwtService;
import com.sai.geoLocation.repository.UserRepository;
import com.sai.geoLocation.repository.RoleRepository;
import com.sai.geoLocation.repository.WalletRepository;
import com.sai.geoLocation.repository.OtpVerificationRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.List;

@Service
public class AuthService {
    private final UserRepository users;
    private final RoleRepository roles;
    private final WalletRepository wallets;
    private final OtpVerificationRepository otps;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository users, RoleRepository roles, WalletRepository wallets,
                       OtpVerificationRepository otps, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtService jwtService) {
        this.users = users;
        this.roles = roles;
        this.wallets = wallets;
        this.otps = otps;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthDtos.AuthResponse register(AuthDtos.RegisterRequest request) {
        if (users.existsByEmail(request.email())) {
            throw new BusinessRuleException("Email already exists");
        }

        // Verify registration OTP (WhatsApp verification)
        String phone = request.phone();
        if (phone == null || phone.trim().isEmpty()) {
            throw new BusinessRuleException("Phone number is required for registration");
        }

        OtpVerification verify = otps.findFirstByPhoneAndPurposeOrderByCreatedAtDesc(phone, "REGISTER")
                .orElseThrow(() -> new BusinessRuleException("No registration OTP requested for phone: " + phone));

        if (verify.getVerifiedAt() == null && !passwordEncoder.matches(request.otp(), verify.getOtpHash())) {
            throw new BusinessRuleException("Invalid registration OTP");
        }

        if (verify.getExpiresAt().isBefore(OffsetDateTime.now())) {
            throw new BusinessRuleException("Registration OTP has expired");
        }

        // Mark OTP as verified so it cannot be reused
        verify.setVerifiedAt(OffsetDateTime.now());
        otps.save(verify);

        String roleCode = request.role();
        if (roleCode == null || roleCode.trim().isEmpty()) {
            roleCode = "CUSTOMER";
        }
        roleCode = roleCode.toUpperCase();
        var role = roles.findByCode(roleCode)
                .orElseThrow(() -> new BusinessRuleException("Role not found: " + request.role()));

        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setEmailVerified(true);
        user.setPhoneVerified(true);
        user = users.save(user);

        roles.assignRoleToUser(UUID.randomUUID(), user.getId(), role.getId());

        // Initialize Wallet
        Wallet wallet = new Wallet();
        wallet.setUserId(user.getId());
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setCurrency("INR");
        wallets.save(wallet);

        String token = jwtService.generateToken(org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPasswordHash())
                .authorities("ROLE_" + roleCode)
                .build());

        return new AuthDtos.AuthResponse(token, "Bearer", user.getEmail(), roleCode);
    }

    public AuthDtos.AuthResponse login(AuthDtos.LoginRequest request) {
        var auth = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        authenticationManager.authenticate(auth);

        var user = users.findByEmail(request.email()).orElseThrow();
        List<String> roleCodes = roles.findRoleCodesByUserId(user.getId());
        String roleCode = roleCodes.isEmpty() ? "CUSTOMER" : roleCodes.get(0);

        String token = jwtService.generateToken(org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPasswordHash())
                .authorities("ROLE_" + roleCode)
                .build());

        return new AuthDtos.AuthResponse(token, "Bearer", user.getEmail(), roleCode);
    }

    @Transactional
    public String generateOtp(AuthDtos.OtpRequest request) {
        String otp = String.format("%06d", new java.util.Random().nextInt(1000000));
        String target = request.target();
        String purpose = request.purpose().toUpperCase();

        OtpVerification verify = new OtpVerification();
        if (target.contains("@")) {
            verify.setEmail(target);
        } else {
            verify.setPhone(target);
        }
        verify.setOtpHash(passwordEncoder.encode(otp));
        verify.setPurpose(purpose);
        verify.setExpiresAt(OffsetDateTime.now().plusMinutes(5));
        otps.save(verify);

        // If target is a phone number, attempt to send a real SMS via Twilio
        if (!target.contains("@")) {
            sendTwilioSms(target, otp);
        }

        // Log to console simulating SMS/WhatsApp Cloud API notification integration
        System.out.println("==================================================");
        System.out.println("SIMULATED OTP DISPATCH via WhatsApp/SMS to: " + target);
        System.out.println("Purpose: " + purpose);
        System.out.println("OTP Code: " + otp);
        System.out.println("==================================================");

        return otp;
    }

    @Transactional
    public boolean verifyOtp(AuthDtos.OtpVerifyRequest request) {
        String target = request.target();
        String purpose = request.purpose().toUpperCase();

        java.util.Optional<OtpVerification> optionalVerify;
        if (target.contains("@")) {
            optionalVerify = otps.findFirstByEmailAndPurposeOrderByCreatedAtDesc(target, purpose);
        } else {
            optionalVerify = otps.findFirstByPhoneAndPurposeOrderByCreatedAtDesc(target, purpose);
        }

        if (optionalVerify.isEmpty()) {
            throw new BusinessRuleException("No OTP requested for " + target);
        }

        OtpVerification verify = optionalVerify.get();
        if (verify.getVerifiedAt() != null) {
            throw new BusinessRuleException("OTP already verified");
        }
        if (verify.getExpiresAt().isBefore(OffsetDateTime.now())) {
            throw new BusinessRuleException("OTP has expired");
        }

        if (!passwordEncoder.matches(request.otp(), verify.getOtpHash())) {
            verify.setAttemptCount(verify.getAttemptCount() + 1);
            otps.save(verify);
            throw new BusinessRuleException("Invalid OTP code");
        }

        verify.setVerifiedAt(OffsetDateTime.now());
        otps.save(verify);
        return true;
    }

    @Transactional
    public String resetPassword(AuthDtos.ResetPasswordRequest request) {
        // Validate OTP verification first
        OtpVerification verify = otps.findFirstByEmailAndPurposeOrderByCreatedAtDesc(request.email(), "PASSWORD_RESET")
                .orElseThrow(() -> new BusinessRuleException("No password reset requested"));

        if (verify.getVerifiedAt() == null || !passwordEncoder.matches(request.otp(), verify.getOtpHash())) {
            throw new BusinessRuleException("OTP is not verified or invalid");
        }

        User user = users.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.email()));

        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        users.save(user);
        return "Password reset successfully";
    }

    @Transactional
    public User updateProfile(UUID userId, AuthDtos.ProfileUpdate update) {
        User user = users.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (update.firstName() != null) user.setFirstName(update.firstName());
        if (update.lastName() != null) user.setLastName(update.lastName());
        if (update.phone() != null) user.setPhone(update.phone());
        if (update.avatarUrl() != null) user.setAvatarUrl(update.avatarUrl());

        return users.save(user);
    }

    private void sendTwilioSms(String toPhone, String code) {
        String accountSid = System.getenv("TWILIO_ACCOUNT_SID");
        String authToken = System.getenv("TWILIO_AUTH_TOKEN");
        String fromPhone = System.getenv("TWILIO_PHONE_NUMBER");

        if (accountSid == null || authToken == null || fromPhone == null) {
            System.out.println("Twilio credentials not configured in environment variables. Skipping real SMS dispatch.");
            return;
        }

        try {
            String url = "https://api.twilio.com/2010-04-01/Accounts/" + accountSid + "/Messages.json";
            String formattedTo = toPhone.trim();
            if (!formattedTo.startsWith("+")) {
                if (formattedTo.length() == 10) {
                    formattedTo = "+91" + formattedTo;
                } else {
                    formattedTo = "+" + formattedTo;
                }
            }

            java.net.URL urlObj = new java.net.URL(url);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) urlObj.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            String auth = accountSid + ":" + authToken;
            String authHeader = "Basic " + java.util.Base64.getEncoder().encodeToString(auth.getBytes());
            conn.setRequestProperty("Authorization", authHeader);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String postData = "To=" + java.net.URLEncoder.encode(formattedTo, "UTF-8")
                    + "&From=" + java.net.URLEncoder.encode(fromPhone, "UTF-8")
                    + "&Body=" + java.net.URLEncoder.encode("Your LocalHub verification code is: " + code, "UTF-8");

            try (java.io.OutputStream os = conn.getOutputStream()) {
                os.write(postData.getBytes("UTF-8"));
            }

            int responseCode = conn.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                System.out.println("Twilio SMS sent successfully to: " + formattedTo);
            } else {
                try (java.io.InputStream es = conn.getErrorStream()) {
                    if (es != null) {
                        String errorResponse = new String(es.readAllBytes(), "UTF-8");
                        System.err.println("Twilio API Error Response: " + errorResponse);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to send SMS via Twilio: " + e.getMessage());
        }
    }
}
