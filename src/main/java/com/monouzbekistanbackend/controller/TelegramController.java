package com.monouzbekistanbackend.controller;

import com.monouzbekistanbackend.service.TelegramBotService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/telegram")
public class TelegramController {
    private final TelegramBotService telegramBotService;

    @GetMapping("/getUserAvatar")
    public ResponseEntity<?> getUserAvatar(@RequestParam Long telegramUserId) {
        String userAvatarUrl = telegramBotService.getUserAvatarUrl(telegramUserId);
        if (userAvatarUrl != null) {
            return ResponseEntity.ok(userAvatarUrl);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
