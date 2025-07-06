package com.monouzbekistanbackend.dto.order;

import com.monouzbekistanbackend.dto.cart.CartItemResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class CheckSummaryResponse {
    private List<CartItemResponse> cartItems;
    private BigDecimal totalAmount;
}
