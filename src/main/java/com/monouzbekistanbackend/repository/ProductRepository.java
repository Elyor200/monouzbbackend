package com.monouzbekistanbackend.repository;

import com.monouzbekistanbackend.entity.Category;
import com.monouzbekistanbackend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByAvailableTrue();

    List<Product> findBySeasonName(String seasonName);

    List<Product> findByCategory(Category category);

    Optional<Product> findByProductId(String productId);

    @Query("SELECT DISTINCT p.color FROM Product p WHERE p.name = (SELECT p2.name FROM Product p2 WHERE p2.productId = :productId)")
    List<String> findColorsByProductId(String productId);
}
