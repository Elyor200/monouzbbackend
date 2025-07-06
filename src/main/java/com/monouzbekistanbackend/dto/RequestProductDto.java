package com.monouzbekistanbackend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class RequestProductDto {
    private String name;
    private String description;
    private UUID categoryId;
    private UUID seasonId;
    private BigDecimal price;
    private boolean isAvailable;
    private String color;
    private List<String> availableColors;
}
