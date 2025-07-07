package com.monouzbekistanbackend.config;

import com.monouzbekistanbackend.service.TelegramBotService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MonoUzbBot extends TelegramWebhookBot {
    private final String botToken;
    private final String botUsername;
    private final TelegramBotService telegramBotService;

    public MonoUzbBot(@Value("${telegram.bot.token}") String botToken,
                      @Value("${telegram.bot.username}") String botUsername,
                      TelegramBotService telegramBotService) {
        this.botToken = botToken;
        this.botUsername = botUsername;
        this.telegramBotService = telegramBotService;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return "8114214391:AAH87KGeNzym0fBU41MSE1h80GfW1jo9cuc";
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        if (update.hasMessage()) {
            telegramBotService.handleIncomingMessage(update.getMessage());
        }
        return null;
    }

    @Override
    public String getBotPath() {
        return "/webhook";
    }
}
