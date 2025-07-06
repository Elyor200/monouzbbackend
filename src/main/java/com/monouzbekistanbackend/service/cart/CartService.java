package com.monouzbekistanbackend.service.cart;

import com.monouzbekistanbackend.dto.cart.CartItemRequest;
import com.monouzbekistanbackend.dto.cart.CartResponse;

import java.util.UUID;

public interface CartService {
    CartResponse getActiveCart(Long telegramUserId);
    CartResponse addItem(Long telegramUserId, CartItemRequest request);
    CartResponse updateItem(Long telegramUserId, UUID itemId, int quantity);
    void removeItem(Long telegramUserId, UUID cartItemId);
    void clearCart(Long telegramUserId);
}
