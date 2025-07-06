package com.monouzbekistanbackend.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PaymentMethod {
    cash,
    card;

    @JsonCreator
    public static PaymentMethod fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Payment method cannot be null");
        }
        return PaymentMethod.valueOf(value.toLowerCase());
    }
}
