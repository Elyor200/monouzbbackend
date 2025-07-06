package com.monouzbekistanbackend.service;

import com.monouzbekistanbackend.entity.User;
import com.monouzbekistanbackend.entity.verification.PhoneVerification;
import com.monouzbekistanbackend.repository.PhoneVerificationRepository;
import com.monouzbekistanbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class VerificationHelper {
    private final PhoneVerificationRepository phoneVerificationRepository;
    private final UserRepository userRepository;

    public VerificationHelper(PhoneVerificationRepository phoneVerificationRepository, UserRepository userRepository) {
        this.phoneVerificationRepository = phoneVerificationRepository;
        this.userRepository = userRepository;
    }

    public void verifyCode(String phoneNumber, String code) {
        PhoneVerification phoneVerification = phoneVerificationRepository.findTopByPhoneNumberOrderByExpiryDesc(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Verification code for this number not found"));

        int attempts = phoneVerification.getAttempts() != null ? phoneVerification.getAttempts() : 0;

        if (attempts >= 3) {
            throw new RuntimeException("Please resend new verification code");
        }

         if (phoneVerification.isVerified()) {
            throw new RuntimeException("Verification code has already been used");
        } else if (phoneVerification.getExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification code is expired");
        }

        if (!code.equals(phoneVerification.getCode())) {
            phoneVerification.setAttempts(phoneVerification.getAttempts() + 1);
            phoneVerificationRepository.save(phoneVerification);
            throw new RuntimeException("Verification code is incorrect");
        }

        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setActive(true);
        userRepository.save(user);

        phoneVerification.setVerified(true);
        phoneVerificationRepository.save(phoneVerification);
    }
}
