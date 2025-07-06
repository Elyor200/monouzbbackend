package com.monouzbekistanbackend.repository;

import com.monouzbekistanbackend.entity.verification.PhoneVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PhoneVerificationRepository extends JpaRepository<PhoneVerification, UUID> {
    Optional<PhoneVerification> findTopByPhoneNumberOrderByExpiryDesc(String phoneNumber);
}
