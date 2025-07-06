package com.monouzbekistanbackend.controller.cart;

import com.monouzbekistanbackend.dto.cart.CartItemRequest;
import com.monouzbekistanbackend.dto.cart.CartResponse;
import com.monouzbekistanbackend.entity.cart.Cart;
import com.monouzbekistanbackend.service.cart.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/getCart")
    public ResponseEntity<CartResponse> getCart(@RequestParam  Long telegramUserId) {
        return ResponseEntity.ok(cartService.getActiveCart(telegramUserId));
    }

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(@RequestParam Long telegramUserId, @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartService.addItem(telegramUserId, request));
    }

    @PutMapping("/update/{itemId}")
    public ResponseEntity<CartResponse> updateItem(@RequestParam Long telegramUserId, @PathVariable UUID itemId, @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.updateItem(telegramUserId, itemId, quantity));
    }

    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<Void> removeItem(@RequestParam Long telegramUserId, @PathVariable UUID cartItemId) {
        cartService.removeItem(telegramUserId, cartItemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@RequestParam Long telegramUserId) {
        cartService.clearCart(telegramUserId);
        return ResponseEntity.noContent().build();
    }
}
