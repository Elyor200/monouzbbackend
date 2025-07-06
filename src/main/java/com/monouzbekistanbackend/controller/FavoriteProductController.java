package com.monouzbekistanbackend.controller;

import com.monouzbekistanbackend.dto.FavoriteDto;
import com.monouzbekistanbackend.dto.ResponseProductDto;
import com.monouzbekistanbackend.repository.FavoriteProductRepository;
import com.monouzbekistanbackend.service.FavoriteProductsService;
import lombok.AllArgsConstructor;
import org.glassfish.grizzly.memory.BufferArray;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/favorite-products")
public class FavoriteProductController {
    private final FavoriteProductsService favoriteProductsService;
    private final FavoriteProductRepository favoriteProductRepository;

    @GetMapping("/getFavoriteProductsByTelegramUserId")
    public ResponseEntity<List<?>> getFavoriteProductsByTelegramUserId(Long telegramUserId) {
        try {
            List<ResponseProductDto> favoriteProducts = favoriteProductsService.getFavoriteProducts(telegramUserId);
            return ResponseEntity.ok(favoriteProducts);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonList(e.getMessage()));
        }
    }

    @PostMapping("/addFavoriteProduct")
    public ResponseEntity<?> addFavoriteProduct(@RequestBody FavoriteDto favoriteDto) {
        try {
            boolean addFavoriteProduct = favoriteProductsService.addFavoriteProduct(favoriteDto);
            return ResponseEntity.ok(addFavoriteProduct ? "Added" : "Removed");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/isFavoriteProduct")
    public ResponseEntity<Boolean> isFavoriteProduct(@RequestParam("telegramUserId") Long telegramUserId, @RequestParam String productId) {
        boolean exists = favoriteProductRepository.existsByUserTelegramUserIdAndProductProductId(telegramUserId, productId);
        return ResponseEntity.ok(exists);
    }
 }
