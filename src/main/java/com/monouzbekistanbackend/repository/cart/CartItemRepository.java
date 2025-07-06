package com.monouzbekistanbackend.repository.cart;

import com.monouzbekistanbackend.entity.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    List<CartItem> findByCartCartId(UUID cartCartId);
}
