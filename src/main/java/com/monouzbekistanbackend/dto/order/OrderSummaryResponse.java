package com.monouzbekistanbackend.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderSummaryResponse(
        UUID orderId,
        BigDecimal totalAmount,
        String status,
        LocalDateTime createdAt
) {}
