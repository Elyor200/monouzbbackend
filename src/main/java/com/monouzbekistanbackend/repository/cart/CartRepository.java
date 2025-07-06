package com.monouzbekistanbackend.repository.cart;

import com.monouzbekistanbackend.entity.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByUserUserId(String userId);

    Optional<Cart> findByUserTelegramUserId(Long telegramUserId);

    Optional<Cart> findByUserTelegramUserIdAndIsActiveTrue(Long telegramUserId);

    Optional<Cart> findByUserUserIdAndIsActiveTrue(String userId);

}
