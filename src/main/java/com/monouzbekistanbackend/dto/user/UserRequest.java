package com.monouzbekistanbackend.dto.user;

import com.monouzbekistanbackend.enums.UserRole;
import lombok.Data;

@Data
public class UserRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String phoneNumber;
    private UserRole role;
//    private boolean isActive;
}
