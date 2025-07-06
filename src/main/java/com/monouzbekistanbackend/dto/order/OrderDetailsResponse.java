package com.monouzbekistanbackend.dto.order;

import com.monouzbekistanbackend.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderDetailsResponse(
        UUID orderId,
        BigDecimal totalAmount,
        String status,
        LocalDateTime createdAt,
        String paymentMethod,
        String phoneNumber,
        String address,
        List<OrderItemsDetails> orderItemDetails
) {
    public record OrderItemsDetails(
            String productName,
            int quantity,
            BigDecimal unitPrice,
            BigDecimal totalPrice
    ){}
}
