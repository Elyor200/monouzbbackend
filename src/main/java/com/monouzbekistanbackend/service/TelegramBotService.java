package com.monouzbekistanbackend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monouzbekistanbackend.config.MonoUzbBot;
import com.monouzbekistanbackend.dto.cart.CartItemResponse;
import com.monouzbekistanbackend.dto.order.OrderDetailsResponse;
import com.monouzbekistanbackend.dto.order.OrderItemResponse;
import com.monouzbekistanbackend.dto.order.OrderResponse;
import com.monouzbekistanbackend.entity.ProductPhoto;
import com.monouzbekistanbackend.entity.User;
import com.monouzbekistanbackend.entity.cart.CartItem;
import com.monouzbekistanbackend.entity.order.Order;
import com.monouzbekistanbackend.entity.order.OrderItem;
import com.monouzbekistanbackend.repository.ProductImageRepository;
import com.monouzbekistanbackend.repository.UserRepository;
import com.monouzbekistanbackend.service.user.TempUserSave;
import com.monouzbekistanbackend.service.user.UserData;
import jakarta.ws.rs.client.Client;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.support.ObjectNameManager;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import javax.print.DocFlavor;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.NumberFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class TelegramBotService {

    private final MonoUzbBot monoUzbBot;
    private final UserRepository userRepository;
    private final IdGeneratorService idGeneratorService;
    private final TempUserSave tempUserSave;
    private final ProductImageRepository productImageRepository;

    @Getter
    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.webAppUrl}")
    private String webAppUrl;

    private final String baseUrl = "https://api.telegram.org/bot";
    private final RestTemplate restTemplate = new RestTemplate();

    public TelegramBotService(@Lazy MonoUzbBot monoUzbBot,
                              UserRepository userRepository, IdGeneratorService idGeneratorService, TempUserSave tempUserSave, ProductImageRepository productImageRepository) {
        this.monoUzbBot = monoUzbBot;
        this.userRepository = userRepository;
        this.idGeneratorService = idGeneratorService;
        this.tempUserSave = tempUserSave;
        this.productImageRepository = productImageRepository;
    }

    public void handleIncomingMessage(Message message) {
        Long chatId = message.getChatId();
        String text = message.getText();
        String firstName = message.getFrom().getFirstName();

        if (message.hasText()) {
            if (text.equals("/start")){
                sendMessage(chatId, "Assalomu aleykum " + firstName);
                sleep(1500);

                Optional<User> optionalUser = userRepository.findByTelegramUserId(chatId);
                if (!optionalUser.isPresent()) {
                    sendContactRequest(chatId);
                    return;
                }

                sendMiniAppButton(chatId);
            }
        }

        if (message.hasContact()) {
            String phoneNumber = message.getContact().getPhoneNumber();
            if (!phoneNumber.startsWith("+")) {
                phoneNumber = "+" + phoneNumber;
            }

            UserData userData = new UserData();
            userData.setChatId(chatId);
            tempUserSave.saveTempUser(phoneNumber, userData);

            sendMiniAppButton(chatId);
            sendMessage(chatId, "Please visit the web page to place your order");
        }
    }

    public void sendMiniAppButton(Long telegramUserId) {
        String url = "https://api.telegram.org/bot" + getBotToken() + "/sendMessage";

        Map<String, Object> body = getStringObjectMap(telegramUserId);

        try {
            HttpClient client = HttpClient.newHttpClient();
            ObjectMapper mapper = new ObjectMapper();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        System.out.println("Response: " + response.body());
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String, Object> getStringObjectMap(Long telegramUserId) {
        Map<String, Object> webApp = new HashMap<>();
        webApp.put("url", webAppUrl);

        Map<String, Object> button = new HashMap<>();
        button.put("text", "Open shop");
        button.put("web_app", webApp);

        List<Object> keyboardRow = new ArrayList<>();
        keyboardRow.add(button);

        List<Object> keyboard = new ArrayList<>();
        keyboard.add(keyboardRow);

        Map<String, Object> replyMarkup = new HashMap<>();
        replyMarkup.put("keyboard", keyboard);
        replyMarkup.put("resize_keyboard", true);
        replyMarkup.put("one_time_keyboard", true);

        Map<String, Object> body = new HashMap<>();
        body.put("chat_id", telegramUserId);
        body.put("text", "Welcome Click the button to open shop");
        body.put("reply_markup", replyMarkup);
        return body;
    }

    public void sendMessage(Long chatId, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .parseMode("HTML")
                .build();
        try {
            monoUzbBot.execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendContactRequest(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Please share your phone number");

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(true);

        KeyboardButton contactButton = new KeyboardButton("Share phone number");
        contactButton.setRequestContact(true);

        KeyboardRow row = new KeyboardRow();
        row.add(contactButton);
        keyboard.setKeyboard(Collections.singletonList(row));
        message.setReplyMarkup(keyboard);

        try {
            monoUzbBot.execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String buildOrderSummary(Order order) {
        StringBuilder sb = new StringBuilder();
        ZonedDateTime zonedDateTime = order.getCreatedAt().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("Asia/Tashkent"));
        NumberFormat format = NumberFormat.getInstance(new Locale("uz", "UZ"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.of("Asia/Tashkent"));

        sb.append("<b>Order Confirmation </b>\n\n");
        sb.append("\uD83C\uDD94 Order ID: <code>").append(order.getOrderId()).append("</code>\n");
        sb.append("\uD83D\uDCE6 Status: <b>").append(order.getStatus()).append("</b>\n");
        sb.append("\uD83D\uDCB3 Payment: <b>").append(order.getPaymentMethod().toString().toUpperCase()).append("</b>\n");
        sb.append("\uD83D\uDD52 Created At: <b>").append(formatter.format(zonedDateTime)).append("</b>\n");
        sb.append("\n");

        sb.append("👤 <b>Customer:</b>\n\n");
        sb.append("Full Name: ").append(order.getFullName()).append("\n");
        sb.append("Phone: ").append(order.getPhoneNumber()).append("\n");

        if ("deliver".equalsIgnoreCase(order.getDeliveryMethod())) {
            sb.append("Type of delivery: <b>").append(order.getDeliveryMethod().toUpperCase()).append("</b>\n");
            sb.append("Address: <b>").append(order.getDeliveryAddress()).append("</b>\n");
        } else {
            sb.append("Type of delivery: <b>").append(order.getDeliveryMethod().toUpperCase()).append("</b>\n");
            sb.append("Delivery:").append("<b>").append(" Self pick up from store").append("</b>\n");
        }

        sb.append("\n\uD83D\uDED2 <b>Items:</b>\n\n");
        for (OrderItem item : order.getOrderItems()) {
            sb.append("Product Name: ").append(item.getProductName());
            sb.append("\nProduct ID: <code>").append(item.getProduct().getProductId()).append("</code>");
            sb.append("\nSize: ").append(item.getSize());
            sb.append("\nColor: ").append(item.getColor().toUpperCase());
            sb.append("\nQuantity: ").append(item.getQuantity());
            sb.append("\nPrice: ").append(format.format(item.getTotalPrice())).append(" UZS\n\n");
        }

        sb.append("Total: <b>")
                .append(format.format(order.getTotalAmount()))
                .append(" UZS</b>\n\n");

        sb.append("Thank you for using Mono Uzbekistan");
        return sb.toString();
    }

    public void sendTelegramPhoto(Long telegramUserId, String photoUrl, String caption) {
        String url = "https://api.telegram.org/bot" + botToken + "/sendPhoto";

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> body = new HashMap<>();
        body.put("chat_id", telegramUserId);
        body.put("photo", photoUrl);
        body.put("caption", caption);
        body.put("parse_mode", "HTML");

        restTemplate.postForObject(url, body, String.class);
    }

    public void sendProductPhoto(Order order, Long telegramUserId) {
        for (OrderItem item : order.getOrderItems()) {
            Optional<ProductPhoto> photoOptional = productImageRepository.findProductPhotoByProductProductIdAndColor(item.getProduct().getProductId(), item.getColor());
            if (photoOptional.isEmpty()) continue;

            String imageUrl = photoOptional.get().getUrl();
            String caption = String.format(item.getProduct().getProductId());

            sendTelegramPhoto(telegramUserId, imageUrl, caption);
        }
    }

    public String getUserAvatarUrl(Long telegramUserId) {
        String getPhotoUrl = String.format(
                "https://api.telegram.org/bot%s/getUserProfilePhotos?user_id=%d&limit=1",
                getBotToken(), telegramUserId
        );

        ResponseEntity<Map> photoResponse = restTemplate.getForEntity(getPhotoUrl, Map.class);
        if (photoResponse.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> result = (Map<String, Object>) photoResponse.getBody().get("result");
            List<List<Map<String, Object>>> photos = (List<List<Map<String, Object>>>) result.get("photos");

            if (!photos.isEmpty()) {
                String fileId = (String) photos.get(0).get(0).get("file_id");

                String getFileUrl = String.format(
                        "https://api.telegram.org/bot%s/getFile?file_id=%s",
                        getBotToken(), fileId
                );

                ResponseEntity<Map> fileResponse = restTemplate.getForEntity(getFileUrl, Map.class);
                if (fileResponse.getStatusCode().is2xxSuccessful()) {
                    Map<String, Object> fileResult = (Map<String, Object>) fileResponse.getBody().get("result");
                    String filePath = (String) fileResult.get("file_path");

                    return String.format("https://api.telegram.org/file/bot%s/%s", getBotToken(), filePath);
                }
            }
        }
        return null;
    }
}
