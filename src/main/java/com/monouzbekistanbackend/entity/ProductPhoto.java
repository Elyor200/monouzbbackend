package com.monouzbekistanbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.EnableMBeanExport;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "product_photos")
public class ProductPhoto {
    @Id
    @Column(name = "product_photo_id")
    private UUID productPhotoId;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private boolean isMain;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "color")
    private String color;

    @Column(name = "created_At", nullable = false)
    private LocalDateTime createdAt;
}
