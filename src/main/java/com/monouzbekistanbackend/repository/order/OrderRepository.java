package com.monouzbekistanbackend.repository.order;

import com.monouzbekistanbackend.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository  extends JpaRepository<Order, Long> {
    List<Order> findByUserUserId(String userId);

    Optional<Order> findOrderByOrderId(UUID orderId);

    Optional<Order> findByUserTelegramUserId(Long telegramUserId);

    List<Order> findByPhoneNumber(String phoneNumber);

    @Query("SELECT o FROM Order o WHERE o.orderId = :orderId")
    Order findOrderByOrderIdV2(@Param("orderId") UUID orderId);
}
