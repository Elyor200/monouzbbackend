package com.monouzbekistanbackend.entity.order;

import com.monouzbekistanbackend.entity.User;
import com.monouzbekistanbackend.enums.OrderStatus;
import com.monouzbekistanbackend.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "full_name")
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(name = "delivery_method", nullable = false)
    private String deliveryMethod;

    @Column(name = "delivery_address", nullable = false)
    private String deliveryAddress;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lng")
    private Double lng;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "note")
    private String note;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;


    @Column(name = "created_At", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_At", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist()
    public void onCreate() {
        orderId = UUID.randomUUID();
        createdAt = LocalDateTime.now();
        status = OrderStatus.PENDING;
    }

    @PreUpdate
    public void upDate() {
        updatedAt = LocalDateTime.now();
    }
}
