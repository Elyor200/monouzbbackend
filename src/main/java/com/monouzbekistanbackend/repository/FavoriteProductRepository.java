package com.monouzbekistanbackend.repository;

import com.monouzbekistanbackend.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FavoriteProductRepository extends JpaRepository<Favorite, UUID> {
    List<Favorite> findByUserTelegramUserId(Long telegramUserId);

    Optional<Favorite> findByUserTelegramUserIdAndProductProductId(Long userTelegramId, String productProductId);

    List<Favorite> findAllByUserTelegramUserId(Long userTelegramUserId);

    boolean existsByUserTelegramUserIdAndProductProductId(Long telegramUserId, String productProductId);

    @Query("select f.product.productId from Favorite f " +
            "left join User u on u.userId = f.user.userId " +
            "where u.telegramUserId = :telegramUserId")
    List<String> findFavoriteProductsByTelegramUserId(Long telegramUserId);

    @Query("SELECT f.product.productId FROM Favorite f WHERE f.user.telegramUserId = :telegramUserId")
    List<String> findFavoriteProductIdsByTelegramUserId(Long telegramUserId);
}
