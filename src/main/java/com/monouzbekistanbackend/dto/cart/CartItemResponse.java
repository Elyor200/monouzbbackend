package com.monouzbekistanbackend.dto.cart;

import java.math.BigDecimal;
import java.util.UUID;

public record CartItemResponse(UUID cartItemId,
                               String productId,
                               String productName,
                               BigDecimal productPrice,
                               String imageUrl,
                               String color,
                               String size,
                               int quantity,
                               BigDecimal total
) {

    public BigDecimal calculateTotal() {
        return productPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
