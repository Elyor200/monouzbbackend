package com.monouzbekistanbackend.dto.user;

import com.monouzbekistanbackend.enums.UserRole;
import lombok.Data;

@Data
public class UserResponse {
    private String firstName;
    private String lastName;
    private String username;
    private String phoneNumber;
    private UserRole role;
    private boolean isActive;
    private Long telegramUserId;
}
