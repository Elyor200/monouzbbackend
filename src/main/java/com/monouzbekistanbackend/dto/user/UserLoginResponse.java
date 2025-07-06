package com.monouzbekistanbackend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponse {
    private String username;
    private String phoneNumber;
    private Long telegramUserId;
}
