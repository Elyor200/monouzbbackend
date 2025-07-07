package com.monouzbekistanbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class TelegramBotConfig {
    @Bean
    public TelegramBotsApi telegramBotsApi(MonoUzbBot monoUzbBot) throws TelegramApiException {
        SetWebhook setWebhook = SetWebhook.builder()
                .url("https://monouzbbackend.onrender.com/webhook")
                .build();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(monoUzbBot, setWebhook);
        return telegramBotsApi;
    }
}
