package com.monouzbekistanbackend.dto.order;

import com.monouzbekistanbackend.enums.PaymentMethod;


public record OrderPlacementRequest(
        Long telegramUserId,
        String fullName,
        String phoneNumber,
        String deliveryMethod,
        PaymentMethod paymentMethod,
        String deliveryAddress,
        Double lat,
        Double lng) {
}
