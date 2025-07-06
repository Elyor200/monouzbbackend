package com.monouzbekistanbackend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetUsernameRequest {
    private String phoneNumber;
    private String newUsername;
}
