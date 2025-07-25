package com.monouzbekistanbackend.controller;

import com.monouzbekistanbackend.dto.image.ProductPhotoResponse;
import com.monouzbekistanbackend.entity.Product;
import com.monouzbekistanbackend.entity.ProductPhoto;
import com.monouzbekistanbackend.repository.ProductImageRepository;
import com.monouzbekistanbackend.repository.ProductRepository;
import com.monouzbekistanbackend.service.product.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/images")
public class ImageUploadController {
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final ProductImageRepository productImageRepository;

    public ImageUploadController(ProductRepository productRepository,
                                 ProductService productService,
                                 ProductImageRepository productImageRepository) {
        this.productRepository = productRepository;
        this.productService = productService;
        this.productImageRepository = productImageRepository;
    }

    @PostMapping("upload-image")
    public ResponseEntity<ProductPhotoResponse> uploadImage(@RequestParam("imageUrl") String imageUrl,
                                                            @RequestParam("productId") String productId,
                                                            @RequestParam("color") String color ) throws IOException {
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductPhoto productPhoto = new ProductPhoto();
        productPhoto.setProductPhotoId(UUID.randomUUID());
        productPhoto.setMain(false);
        productPhoto.setProduct(product);
        productPhoto.setColor(color);
        productPhoto.setUrl(imageUrl);
        productPhoto.setCreatedAt(LocalDateTime.now());
        productImageRepository.save(productPhoto);

        return ResponseEntity.ok(new ProductPhotoResponse(
                productPhoto.getProductPhotoId(),
                productPhoto.getUrl(),
                productPhoto.getColor(),
                productPhoto.isMain())
        );
    }

    @DeleteMapping("delete-image")
    public ResponseEntity<String> deleteImageByProductIdAndColor(@RequestParam("productId") String productId,
                                                                 @RequestParam String color,
                                                                 @RequestParam String imageUrl) throws IOException {
        List<ProductPhoto> matches = productImageRepository.findProductPhotoByProductProductIdAndColor(productId, color)
                .stream()
                .filter(photo -> photo.getUrl().equals(imageUrl))
                .toList();

        if (matches.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No matching image found for product " + productId + " with the given URL.");
        }

        productImageRepository.deleteAll(matches);
        return ResponseEntity.ok("Image deleted: " + imageUrl);
    }
}
