package com.monouzbekistanbackend.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Data
@AllArgsConstructor
public class OrderMessageDto {
    @Getter
    private String text;
    private InlineKeyboardMarkup markup;
}
