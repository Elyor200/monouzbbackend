package com.monouzbekistanbackend.controller;

import com.monouzbekistanbackend.service.VerificationHelper;
import com.monouzbekistanbackend.service.VerificationSender;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/phoneVerification")
public class PhoneVerificationController {
    private final VerificationHelper verificationHelper;
    private final VerificationSender verificationSender;

    public PhoneVerificationController(VerificationHelper verificationHelper,
                                       VerificationSender verificationSender) {
        this.verificationSender = verificationSender;
        this.verificationHelper = verificationHelper;
    }

    @PostMapping("/send-verification-code")
    public ResponseEntity<String> sendVerificationCode(@RequestParam String phoneNumber, @RequestParam Long telegramUserId) {
        try {
            verificationSender.sendVerificationCode(phoneNumber, telegramUserId);
            return ResponseEntity.ok("Verification code sent");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, String>> verify(@RequestParam String phoneNumber, @RequestParam String code) {
        Map<String, String> response = new HashMap<>();
        try {
            verificationHelper.verifyCode(phoneNumber, code);
            response.put("message", "Verified");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
