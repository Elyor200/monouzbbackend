package com.monouzbekistanbackend.dto.order;

import com.monouzbekistanbackend.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID orderId,
        String userId,
        BigDecimal totalAmount,
        LocalDateTime createdAt,
        OrderStatus status,
        List<OrderItemResponse> orderItemResponses
) {}
