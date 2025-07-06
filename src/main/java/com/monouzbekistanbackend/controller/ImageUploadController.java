package com.monouzbekistanbackend.controller;

import com.monouzbekistanbackend.dto.image.ProductPhotoResponse;
import com.monouzbekistanbackend.entity.Product;
import com.monouzbekistanbackend.entity.ProductPhoto;
import com.monouzbekistanbackend.repository.ProductImageRepository;
import com.monouzbekistanbackend.repository.ProductRepository;
import com.monouzbekistanbackend.service.product.ProductService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
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

    @PostMapping(value = "upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductPhotoResponse> uploadImage(@RequestParam("file") MultipartFile file,
                                                            @RequestParam("productId") String productId,
                                                            @RequestParam("color") String color ) throws IOException {
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        String filaName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String UPLOADED_FILE = "C:/Users/user/Pictures/MonoUzbBotPhotos/";
        Path path = Paths.get(UPLOADED_FILE + filaName);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        ProductPhoto productPhoto = new ProductPhoto();
        productPhoto.setProductPhotoId(UUID.randomUUID());
        productPhoto.setMain(false);
        productPhoto.setProduct(product);
        productPhoto.setColor(color);
        productPhoto.setUrl("/images/" + filaName);
        productPhoto.setCreatedAt(LocalDateTime.now());
        productImageRepository.save(productPhoto);

        return ResponseEntity.ok(new ProductPhotoResponse(
                productPhoto.getProductPhotoId(),
                productPhoto.getUrl(),
                productPhoto.getColor(),
                productPhoto.isMain())
        );
    }
}
