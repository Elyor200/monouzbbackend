package com.monouzbekistanbackend.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    USER(1),
    ADMIN(2);

    private final int code;

    UserRole(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
