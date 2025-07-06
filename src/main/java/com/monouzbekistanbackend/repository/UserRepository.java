package com.monouzbekistanbackend.repository;

import com.monouzbekistanbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserId(String userId);

    Optional<User> findByUsername(String username);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByTelegramUserId(Long telegramUserId);

    Optional<User> findByUsernameAndPhoneNumber(String username, String phoneNumber);
}
