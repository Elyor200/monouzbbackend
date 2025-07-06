package com.monouzbekistanbackend.entity.order;

import com.monouzbekistanbackend.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "order_items")
public class OrderItem {
    @Id
    @Column(name = "order_item_id", nullable = false)
    private UUID orderItemId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "color")
    private String color;

    @Column(name = "size")
    private String size;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal priceAtOrderTime;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @PrePersist
    public void prePersist() {
        orderItemId = UUID.randomUUID();
        totalPrice = priceAtOrderTime.multiply(BigDecimal.valueOf(quantity));
    }
}
