package com.monouzbekistanbackend.controller.product;

import com.monouzbekistanbackend.dto.FavoriteProductResponse;
import com.monouzbekistanbackend.dto.RequestProductDto;
import com.monouzbekistanbackend.dto.ResponseProductDto;
import com.monouzbekistanbackend.enums.CategoryEnum;
import com.monouzbekistanbackend.enums.SeasonEnum;
import com.monouzbekistanbackend.service.product.ProductService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/v1/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create-product")
    public ResponseEntity<?> createProduct(@RequestBody RequestProductDto requestProductDto) {
        try {
            ResponseProductDto responseProductDto = productService.createProduct(requestProductDto);
            return ResponseEntity.ok(responseProductDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/getAllProducts")
    public ResponseEntity<?> getAllProducts(@RequestParam Long telegramUserId) {
        try {
            List<ResponseProductDto> products = productService.getAllProducts(telegramUserId);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/getAllAvailableProducts")
    public ResponseEntity<?> getAllAvailableProducts() {
        try {
            List<ResponseProductDto> products = productService.getAllAvailableProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/getProductsBySeason")
    public ResponseEntity<?> getProductsBySeason(@RequestParam SeasonEnum season) {
        try {
            List<ResponseProductDto> response = productService.getProductBySeason(season);
            return ResponseEntity.ok(response);
         } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/getProductByCategory")
    public ResponseEntity<?> getProductByCategory(@RequestParam CategoryEnum category) {
        try {
            List<ResponseProductDto> productByCategory = productService.getProductByCategory(category);
            return ResponseEntity.ok(productByCategory);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/getByProductId")
    public ResponseEntity<?> getByProductId(@RequestParam String productId) {
        try {
            ResponseProductDto productById = productService.getProductById(productId);
            return ResponseEntity.ok(productById);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/getProductsWithFavoriteStatus")
    public ResponseEntity<?> getProductsWithFavoriteStatus(@RequestParam Long telegramUserId) {
        try {
            List<FavoriteProductResponse> allProductsWithFavoriteStatus = productService.getAllProductsWithFavoriteStatus(telegramUserId);
            return ResponseEntity.ok(allProductsWithFavoriteStatus);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("deleteProductById")
    public ResponseEntity<?> deleteProductById(@RequestParam String productId) {
        productService.deleteProductById(productId);
        return ResponseEntity.ok().build();
    }
}
