package com.monouzbekistanbackend.dto.cart;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CartResponse(UUID cartId, List<CartItemResponse> items, BigDecimal totalAmount, boolean isActive) {
}
