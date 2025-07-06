package com.monouzbekistanbackend.entity.verification;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "phone_verification")
public class PhoneVerification {
    @Id
    private UUID phoneVerificationId;

    private String phoneNumber;
    private String code;
    private LocalDateTime expiry;
    private Integer attempts = 0;
    private boolean verified = false;

}
