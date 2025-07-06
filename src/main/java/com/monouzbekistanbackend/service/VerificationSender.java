package com.monouzbekistanbackend.service;

import com.monouzbekistanbackend.entity.verification.PhoneVerification;
import com.monouzbekistanbackend.repository.PhoneVerificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
public class VerificationSender {
    private final PhoneVerificationRepository phoneVerificationRepository;
    private final TelegramBotService telegramBotService;

    public VerificationSender(PhoneVerificationRepository phoneVerificationRepository,
                              TelegramBotService telegramBotService) {
        this.phoneVerificationRepository = phoneVerificationRepository;
        this.telegramBotService = telegramBotService;
    }

    public void sendVerificationCode(String phoneNumber, Long telegramUserId) {
        String code = generateCode();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(5);

        PhoneVerification phoneVerification = new PhoneVerification();
        phoneVerification.setPhoneVerificationId(UUID.randomUUID());
        phoneVerification.setPhoneNumber(phoneNumber);
        phoneVerification.setCode(code);
        phoneVerification.setExpiry(expiry);
        phoneVerificationRepository.save(phoneVerification);

        telegramBotService.sendMessage(telegramUserId, "Verification code <b>" + code + "</b>");
    }

    private String generateCode() {
        return String.valueOf(new Random().nextInt(900_000) + 100_000);
    }

}
