package com.monouzbekistanbackend.service;

import com.monouzbekistanbackend.dto.FavoriteDto;
import com.monouzbekistanbackend.dto.ResponseProductDto;
import com.monouzbekistanbackend.entity.Favorite;
import com.monouzbekistanbackend.entity.Product;
import com.monouzbekistanbackend.entity.User;
import com.monouzbekistanbackend.repository.FavoriteProductRepository;
import com.monouzbekistanbackend.repository.ProductRepository;
import com.monouzbekistanbackend.repository.UserRepository;
import com.monouzbekistanbackend.service.product.ProductService;
import com.monouzbekistanbackend.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FavoriteProductsService {
    private final FavoriteProductRepository favoriteProductRepository;
    private final ProductService productService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public List<ResponseProductDto> getFavoriteProducts(Long telegramUserId) {
        List<Favorite> favoriteList = favoriteProductRepository.findByUserTelegramUserId(telegramUserId);

        return favoriteList.stream()
                .map(fav -> productService.mapToResponseDto(fav.getProduct()))
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean addFavoriteProduct(FavoriteDto favoriteDto) {
        var existing = favoriteProductRepository.findByUserTelegramUserIdAndProductProductId(favoriteDto.getTelegramUserId(), favoriteDto.getProductId());
        if (existing.isPresent()) {
            favoriteProductRepository.delete(existing.get());
            return false;
        } else {
            User user = userRepository.findByTelegramUserId(favoriteDto.getTelegramUserId()).orElseThrow(() -> new RuntimeException("User not found"));
            Product product = productRepository.findByProductId(favoriteDto.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));

            Favorite favorite = new Favorite();
            favorite.setFavoriteId(UUID.randomUUID());
            favorite.setUser(user);
            favorite.setProduct(product);
            favoriteProductRepository.save(favorite);
            return true;
        }
    }
}
