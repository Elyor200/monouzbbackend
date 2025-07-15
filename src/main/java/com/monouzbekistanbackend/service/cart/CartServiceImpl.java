package com.monouzbekistanbackend.service.cart;

import com.monouzbekistanbackend.dto.cart.CartItemRequest;
import com.monouzbekistanbackend.dto.cart.CartItemResponse;
import com.monouzbekistanbackend.dto.cart.CartResponse;
import com.monouzbekistanbackend.entity.Product;
import com.monouzbekistanbackend.entity.ProductPhoto;
import com.monouzbekistanbackend.entity.User;
import com.monouzbekistanbackend.entity.cart.Cart;
import com.monouzbekistanbackend.entity.cart.CartItem;
import com.monouzbekistanbackend.repository.ProductRepository;
import com.monouzbekistanbackend.repository.UserRepository;
import com.monouzbekistanbackend.repository.cart.CartItemRepository;
import com.monouzbekistanbackend.repository.cart.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public CartResponse getActiveCart(Long telegramUserId) {
        User user = userRepository.findByTelegramUserId(telegramUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart =cartRepository.findByUserUserIdAndIsActiveTrue(user.getUserId())
                .orElseGet(() -> createNewCart(user));

        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
        return mapToResponse(cart);
    }

    @Override
    public CartResponse addItem(Long telegramUserId, CartItemRequest request) {
        User user = userRepository.findByTelegramUserId(telegramUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUserUserIdAndIsActiveTrue(user.getUserId())
                .orElseGet(() -> createNewCart(user));

        Product product = productRepository.findByProductId(request.productId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItem> existingItemOpt = cart.getCartItems().stream()
                .filter(cartItem ->
                        cartItem.getProduct().getProductId().equals(product.getProductId()) &&
                        cartItem.getColor().equals(request.color()) &&
                        cartItem.getSize().equals(request.size()))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + request.quantity());
            existingItem.setTotalPrice(existingItem.getUnitPrice().multiply(BigDecimal.valueOf(existingItem.getQuantity())));
        } else {
            CartItem newItem = new CartItem();
            newItem.setCartItemId(UUID.randomUUID());
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(request.quantity());
            newItem.setColor(request.color());
            newItem.setSize(request.size());
            newItem.setUnitPrice(product.getPrice());
            newItem.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(request.quantity())));
            cart.getCartItems().add(newItem);
        }

        cart.setIsActive(true);
        cartRepository.save(cart);
        return mapToResponse(cart);
    }

    @Override
    public CartResponse updateItem(Long telegramUserId, UUID itemId, int quantity) {
        Cart cart = cartRepository.findByUserTelegramUserIdAndIsActiveTrue(telegramUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CartItem item = cart.getCartItems().stream()
                .filter(i -> i.getCartItemId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (quantity <= 0) {
            cart.getCartItems().remove(item);
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
            item.setTotalPrice(item.getUnitPrice().multiply(BigDecimal.valueOf(quantity)));
        }

        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
        return mapToResponse(cart);
    }

    @Override
    public void removeItem(Long telegramUserId, UUID cartItemId) {
        Cart cart = cartRepository.findByUserTelegramUserIdAndIsActiveTrue(telegramUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean removed = cart.getCartItems().removeIf(i -> i.getCartItemId().equals(cartItemId));
        if (!removed) {
            throw new RuntimeException("Item not found");
        }
        cartRepository.save(cart);
    }

    @Override
    public void clearCart(Long telegramUserId) {
        Cart cart = cartRepository.findByUserTelegramUserId(telegramUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    private Cart createNewCart(User user) {
        Cart cart = new Cart();
        cart.setCartId(UUID.randomUUID());
        cart.setUser(user);
        cart.setIsActive(true);
        cart.setCartItems(new ArrayList<>());
        cart.setCreatedAt(LocalDateTime.now());
        cart.setUpdatedAt(LocalDateTime.now());
        return cartRepository.save(cart);
    }

    private CartResponse mapToResponse(Cart cart) {
        List<CartItemResponse> itemResponses = cart.getCartItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            BigDecimal total = cartItem.getUnitPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            String imageUrl = product.getPhotos().stream()
                    .filter(photo -> photo.getColor() != null && photo.getColor().equalsIgnoreCase(cartItem.getColor()))
                    .findFirst()
                    .map(productPhoto -> {
                        String url = productPhoto.getUrl();
                        return url.startsWith("http") ? url : "/images/" + new File(url).getName();
                    })
                    .orElse(null);

            return new CartItemResponse(
                    cartItem.getCartItemId(),
                    cartItem.getProduct().getProductId(),
                    cartItem.getProduct().getName(),
                    cartItem.getUnitPrice(),
                    imageUrl,
                    cartItem.getColor(),
                    cartItem.getSize(),
                    cartItem.getQuantity(),
                    total
            );
        }).toList();

        BigDecimal totalAmount = itemResponses.stream()
                .map(CartItemResponse::total)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(cart.getCartId(), itemResponses, totalAmount, cart.getIsActive());
    }

}
