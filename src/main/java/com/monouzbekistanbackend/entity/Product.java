package com.monouzbekistanbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "products")
public class Product {
    @Id
    @Column(name = "product_id", nullable = false)
    private String productId;

    private String name;
    private String description;
    private BigDecimal price;
    private boolean available;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductPhoto> photos = new ArrayList<>();

    private String color;

    @ElementCollection
    @CollectionTable(name = "product_colors", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "available_colors")
    private List<String> availableColors;

    @Column(name = "created_At", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_At", nullable = false)
    private LocalDateTime updatedAt;
}
