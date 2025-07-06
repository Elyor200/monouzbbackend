package com.monouzbekistanbackend.dto;

import com.monouzbekistanbackend.entity.Category;
import com.monouzbekistanbackend.entity.Season;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteProductResponse {
    private String productId;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean available;
    private Category category;
    private Season season;
    private List<ProductPhotoDto> photos;
    private boolean isFavorite;
}
