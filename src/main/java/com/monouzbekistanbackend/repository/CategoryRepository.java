package com.monouzbekistanbackend.repository;

import com.monouzbekistanbackend.entity.Category;
import com.monouzbekistanbackend.enums.CategoryEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    Optional<Category> findByCategoryId(UUID categoryId);

    boolean existsByCategory(CategoryEnum category);

    Optional<Category> findByCategory(CategoryEnum categoryEnum);
}
