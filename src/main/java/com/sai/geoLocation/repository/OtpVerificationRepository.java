package com.sai.geoLocation.repository;

import com.sai.geoLocation.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface OtpVerificationRepository extends JpaRepository<OtpVerification, UUID> {
    Optional<OtpVerification> findFirstByPhoneAndPurposeOrderByCreatedAtDesc(String phone, String purpose);
    Optional<OtpVerification> findFirstByEmailAndPurposeOrderByCreatedAtDesc(String email, String purpose);
}
