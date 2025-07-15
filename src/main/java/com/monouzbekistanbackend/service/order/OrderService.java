package com.monouzbekistanbackend.service.order;

import com.monouzbekistanbackend.dto.cart.CartItemResponse;
import com.monouzbekistanbackend.dto.order.*;
import com.monouzbekistanbackend.entity.Product;
import com.monouzbekistanbackend.entity.ProductPhoto;
import com.monouzbekistanbackend.entity.User;
import com.monouzbekistanbackend.entity.cart.CartItem;
import com.monouzbekistanbackend.entity.order.Order;
import com.monouzbekistanbackend.entity.order.OrderItem;
import com.monouzbekistanbackend.entity.cart.Cart;
import com.monouzbekistanbackend.enums.OrderStatus;
import com.monouzbekistanbackend.enums.PaymentMethod;
import com.monouzbekistanbackend.repository.UserRepository;
import com.monouzbekistanbackend.repository.cart.CartItemRepository;
import com.monouzbekistanbackend.repository.cart.CartRepository;
import com.monouzbekistanbackend.repository.order.OrderRepository;
import com.monouzbekistanbackend.service.cart.CartService;
import jakarta.ws.rs.NotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartService cartService;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, CartRepository cartRepository, CartItemRepository cartItemRepository, CartService cartService, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    public CheckSummaryResponse getCheckSummary(String userId) {
        Cart cart = cartRepository.findByUserUserId(userId)
                .orElseThrow(() -> new NotFoundException("No active cart for found"));

        List<CartItemResponse> itemResponses = cart.getCartItems().stream()
                .map(cartItem -> {
                    Product product = cartItem.getProduct();

                    String imageUrl = product.getPhotos().stream()
                            .findFirst()
                            .map(ProductPhoto::getUrl)
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
                            cartItem.getUnitPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()))
                    );
                })
                .toList();

        BigDecimal total = itemResponses.stream()
                .map(CartItemResponse::total)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CheckSummaryResponse(itemResponses, total);
    }

    public OrderResponse placeOrder(OrderPlacementRequest request) {
        User user = userRepository.findByTelegramUserId(request.telegramUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUserUserIdAndIsActiveTrue(user.getUserId())
                .orElseThrow(() -> new NotFoundException("Cart not found"));

        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems.isEmpty()) {
            throw new NotFoundException("Cart is empty");
        }

        ZoneId uzbZone = ZoneId.of("Asia/Tashkent");
        LocalDateTime localDateTime = ZonedDateTime.now(uzbZone).toLocalDateTime();

        Order order = new Order();
//        order.setOrderId(UUID.randomUUID());
        order.setUser(cart.getUser());
        order.setTotalAmount(cart.calculateTotal());
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentMethod(request.paymentMethod());
        order.setPhoneNumber(request.phoneNumber());
        order.setDeliveryMethod(request.deliveryMethod());
        order.setDeliveryAddress(request.deliveryAddress());
        order.setLat(request.lat());
        order.setLng(request.lng());
        order.setFullName(request.fullName());
        order.setUpdatedAt(localDateTime);
        order.setCreatedAt(localDateTime);

        List<OrderItem> itemList = cartItems.stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setProductName(cartItem.getProduct().getName());
            orderItem.setColor(cartItem.getColor());
            orderItem.setSize(cartItem.getSize());
            orderItem.setPriceAtOrderTime(cartItem.getUnitPrice());
            orderItem.setTotalPrice(cartItem.getUnitPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            return orderItem;
        }).toList();

        order.setOrderItems(itemList);
        orderRepository.save(order);

        cart.setIsActive(false);
        cart.setUpdatedAt(localDateTime);
        cartRepository.save(cart);

        return mapToOrderResponse(order);
    }

    public List<OrderSummaryResponse> getUserOrderHistory(String phoneNumber) {
        List<Order> orderList = orderRepository.findByPhoneNumberOrderByCreatedAtDesc(phoneNumber);
        return orderList.stream().map(order ->
            new OrderSummaryResponse(
                    order.getOrderId(),
                    order.getTotalAmount(),
                    order.getStatus().name(),
                    order.getCreatedAt()
            )
        ).toList();
    }

    public OrderDetailsResponse getOrderDetails(UUID orderId) {
        Order order = orderRepository.findOrderByOrderId(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        List<OrderDetailsResponse.OrderItemsDetails> itemsDetailsList = order.getOrderItems().stream().map(orderItem ->
                new OrderDetailsResponse.OrderItemsDetails(
                        orderItem.getProduct().getName(),
                        orderItem.getProduct().getProductId(),
                        formatColor(orderItem.getColor()),
                        orderItem.getSize(),
                        orderItem.getQuantity(),
                        orderItem.getPriceAtOrderTime(),
                        orderItem.getTotalPrice()
                )
        ).toList();

        return new OrderDetailsResponse(
                order.getOrderId(),
                order.getTotalAmount(),
                order.getStatus().name(),
                order.getCreatedAt(),
                order.getPaymentMethod().name(),
                order.getPhoneNumber(),
                order.getDeliveryAddress(),
                itemsDetailsList
        );
    }

    private OrderResponse mapToOrderResponse(Order order) {
        List<OrderItemResponse> items = order.getOrderItems().stream().map(item -> new OrderItemResponse(
                item.getProduct().getProductId(),
                item.getProduct().getName(),
                item.getPriceAtOrderTime(),
                item.getQuantity(),
                item.getTotalPrice()
        )).toList();

        return new OrderResponse(
                order.getOrderId(),
                order.getUser().getUserId(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                order.getStatus(),
                items
        );
    }

    private String formatColor(String color) {
        if (color == null || color.isBlank()) {
            return "";
        }

        String withSpaces = color.replaceAll("(?<!^)([A-Z])", " $1");

        return Arrays.stream(withSpaces.trim().split("\\s+"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }
}
