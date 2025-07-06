package com.monouzbekistanbackend.dto.order;

public record OrderItem(String productId,
                        Integer quantity,
                        String color,
                        String size) {
}
