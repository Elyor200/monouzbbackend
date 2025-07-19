package com.monouzbekistanbackend.controller;

import com.monouzbekistanbackend.config.MonoUzbBot;
import com.monouzbekistanbackend.service.TelegramBotService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class WebhookController {

    private final TelegramBotService telegramBotService;
    private final MonoUzbBot monoUzbBot;

    public WebhookController(TelegramBotService telegramBotService, MonoUzbBot monoUzbBot) {
        this.telegramBotService = telegramBotService;
        this.monoUzbBot = monoUzbBot;
    }

    @PostMapping("/webhook")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return monoUzbBot.onWebhookUpdateReceived(update);
    }
}
