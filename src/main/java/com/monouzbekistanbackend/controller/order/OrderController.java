package com.monouzbekistanbackend.controller.order;

import com.monouzbekistanbackend.dto.order.*;
import com.monouzbekistanbackend.entity.User;
import com.monouzbekistanbackend.entity.order.Order;
import com.monouzbekistanbackend.repository.UserRepository;
import com.monouzbekistanbackend.repository.order.OrderRepository;
import com.monouzbekistanbackend.service.TelegramBotService;
import com.monouzbekistanbackend.service.order.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("v1/orders")
public class OrderController {
    private final OrderService orderService;
    private final TelegramBotService telegramBotService;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderController(OrderService orderService,
                           TelegramBotService telegramBotService, OrderRepository orderRepository, UserRepository userRepository) {
        this.orderService = orderService;
        this.telegramBotService = telegramBotService;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/place-order")
    public ResponseEntity<?> placeOrder(@RequestBody OrderPlacementRequest orderPlacementRequest) {
        try {
            OrderResponse orderResponse = orderService.placeOrder(orderPlacementRequest);

//            should be fixed to take chatId from user table
            Long adminChatId = 706499698L;
            User user = userRepository.findByTelegramUserId(orderPlacementRequest.telegramUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Order order = orderRepository.findOrderByOrderIdV2(orderResponse.orderId());

            String message = telegramBotService.buildOrderSummary(order);
            telegramBotService.sendMessage(user.getTelegramUserId(), message);
            telegramBotService.sendProductPhoto(order, user.getTelegramUserId());

            telegramBotService.sendMessage(adminChatId, message);
            telegramBotService.sendProductPhoto(order, adminChatId);
            return ResponseEntity.ok(orderResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/checkout")
    public ResponseEntity<CheckSummaryResponse> checkout(@RequestParam String userId) {
        return ResponseEntity.ok(orderService.getCheckSummary(userId));
    }

    @GetMapping("/getUserHistory")
    public ResponseEntity<List<OrderSummaryResponse>> getHistory(@RequestParam String phoneNumber) {
        return ResponseEntity.ok(orderService.getUserOrderHistory(phoneNumber));
    }

    @GetMapping("/getOrderDetails/{orderId}")
    public ResponseEntity<OrderDetailsResponse> getOrderDetails(@PathVariable UUID orderId) {
        return ResponseEntity.ok(orderService.getOrderDetails(orderId));
    }
}
