package com.monouzbekistanbackend.entity;

import com.monouzbekistanbackend.enums.CategoryEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "categories")
public class Category {
    @Id
    @Column(name = "category_id", nullable = false)
    private UUID categoryId;

    //here name must be unique?
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryEnum category;

    @Column(name = "created_At", nullable = false)
    private LocalDateTime createdAt;
}
