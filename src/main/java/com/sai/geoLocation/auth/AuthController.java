package com.sai.geoLocation.auth;

import com.sai.geoLocation.common.ApiResponse;
import com.sai.geoLocation.dto.AuthDtos;
import com.sai.geoLocation.entity.User;
import com.sai.geoLocation.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;

    public AuthController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ApiResponse<AuthDtos.AuthResponse> register(@Valid @RequestBody AuthDtos.RegisterRequest request) {
        return ApiResponse.ok("Registered successfully", authService.register(request));
    }

    @PostMapping("/login")
    public ApiResponse<AuthDtos.AuthResponse> login(@Valid @RequestBody AuthDtos.LoginRequest request) {
        return ApiResponse.ok("Logged in successfully", authService.login(request));
    }

    @PostMapping("/otp/send")
    public ApiResponse<String> sendOtp(@Valid @RequestBody AuthDtos.OtpRequest request) {
        return ApiResponse.ok("OTP sent", authService.generateOtp(request));
    }

    @PostMapping("/otp/verify")
    public ApiResponse<Boolean> verifyOtp(@Valid @RequestBody AuthDtos.OtpVerifyRequest request) {
        return ApiResponse.ok("OTP verified", authService.verifyOtp(request));
    }

    @PostMapping("/password/reset")
    public ApiResponse<String> resetPassword(@Valid @RequestBody AuthDtos.ResetPasswordRequest request) {
        return ApiResponse.ok("Password reset successful", authService.resetPassword(request));
    }

    @PutMapping("/profile")
    public ApiResponse<User> updateProfile(Principal principal, @Valid @RequestBody AuthDtos.ProfileUpdate request) {
        if (principal == null) {
            throw new com.sai.geoLocation.exception.BusinessRuleException("User is not authenticated");
        }
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        return ApiResponse.ok("Profile updated", authService.updateProfile(user.getId(), request));
    }

    @GetMapping("/me")
    public ApiResponse<User> getMe(Principal principal) {
        if (principal == null) {
            throw new com.sai.geoLocation.exception.BusinessRuleException("User is not authenticated");
        }
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        return ApiResponse.ok("Fetched successfully", user);
    }
}
