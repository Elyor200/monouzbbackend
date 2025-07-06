package com.monouzbekistanbackend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ResponseProductDto {
    private String productId;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean isAvailable;
    private String category;
    private String season;
    private List<String> imageUrl;
    private boolean isFavorite;
    private String color;
    private List<String> availableColors;
}
