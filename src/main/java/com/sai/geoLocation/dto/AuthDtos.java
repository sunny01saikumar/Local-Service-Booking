package com.sai.geoLocation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public final class AuthDtos {
    private AuthDtos() {}

    public record RegisterRequest(
        @NotBlank String firstName,
        String lastName,
        @Email @NotBlank String email,
        @NotBlank String password,
        String phone,
        String role, // CUSTOMER or SHOP_OWNER
        @NotBlank String otp
    ) {}

    public record LoginRequest(
        @Email @NotBlank String email,
        @NotBlank String password
    ) {}

    public record AuthResponse(
        String token,
        String tokenType,
        String email,
        String role
    ) {}

    public record OtpRequest(
        @NotBlank String target, // phone or email
        @NotBlank String purpose // LOGIN, REGISTER, PASSWORD_RESET
    ) {}

    public record OtpVerifyRequest(
        @NotBlank String target,
        @NotBlank String purpose,
        @NotBlank String otp
    ) {}

    public record ResetPasswordRequest(
        @Email @NotBlank String email,
        @NotBlank String otp,
        @NotBlank String newPassword
    ) {}

    public record ProfileUpdate(
        String firstName,
        String lastName,
        String phone,
        String avatarUrl
    ) {}
}
