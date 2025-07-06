package com.monouzbekistanbackend.dto.order;

import java.math.BigDecimal;

public record OrderItemResponse(
        String productId,
        String name,
        BigDecimal price,
        int quantity,
        BigDecimal total
)
{}
