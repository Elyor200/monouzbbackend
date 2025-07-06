package com.monouzbekistanbackend.service;

import com.monouzbekistanbackend.enums.UserRole;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class IdGeneratorService {
    public String generateUserId(UserRole role) {
        String roleCode = String.valueOf(role.ordinal());
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String sequence = generateRandomSequence(6);
        return roleCode + date + sequence;
    }

    private String generateRandomSequence(int length) {
        int max = (int) Math.pow(10, length) - 1;
        int randomNumber = ThreadLocalRandom.current().nextInt(0, max);
        return String.format("%0" + length + "d", randomNumber);
    }

    public String generateProductId() {
        String prefix = "3";
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int randomNumber = ThreadLocalRandom.current().nextInt(0, 9999);
        String sequence = String.format("%04d", randomNumber);
        return prefix + date + sequence;
    }
}
