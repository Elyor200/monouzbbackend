package com.monouzbekistanbackend.config;

import com.monouzbekistanbackend.dto.order.OrderMessageDto;
import com.monouzbekistanbackend.entity.order.Order;
import com.monouzbekistanbackend.service.TelegramBotService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.UUID;

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
        return botToken;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        if (update.hasMessage()) {
            telegramBotService.handleIncomingMessage(update.getMessage());
        }

        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();

            if (data.startsWith("update_status:")) {
                String[] parts = data.split(":");
                UUID orderId = UUID.fromString(parts[1]);
                String newStatus = parts[2];

                Order updatedOrder = telegramBotService.updateOrderStatus(orderId, newStatus);
                OrderMessageDto dto = telegramBotService.buildOrderSummary(updatedOrder, true);
                EditMessageText editMessageText = new EditMessageText();
                editMessageText.setChatId(update.getCallbackQuery().getMessage().getChatId());
                editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                editMessageText.setText(dto.getText());
                editMessageText.setReplyMarkup(dto.getMarkup());
                editMessageText.setParseMode("HTML");

                String confirmStatus = "Order status updated <b>" + newStatus + "</b>";
                SendMessage message = new SendMessage();
                message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                message.setText(confirmStatus);
                message.setParseMode("HTML");

                String userNotifyText = "Your order status is now <b>" + newStatus + "</b>";

                SendMessage userMessage = new SendMessage();
                userMessage.setChatId(updatedOrder.getUser().getTelegramUserId());
                userMessage.setText(userNotifyText);
                userMessage.setParseMode("HTML");

                telegramBotService.sendMessage(update.getCallbackQuery().getMessage().getChatId(), confirmStatus);
                return editMessageText;
            }
        }

        return null;
    }

    @Override
    public String getBotPath() {
        return "/webhook";
    }
}
