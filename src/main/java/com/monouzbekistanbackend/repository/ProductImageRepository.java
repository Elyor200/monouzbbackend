package com.monouzbekistanbackend.repository;

import com.monouzbekistanbackend.entity.ProductPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductPhoto, UUID> {
    Optional<ProductPhoto> findProductPhotoByProductProductIdAndColor(String productId, String color);

    void findByProductProductIdAndColor(String productId, String color);
}
