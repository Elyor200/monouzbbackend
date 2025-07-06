package com.monouzbekistanbackend.enums.category;

import com.monouzbekistanbackend.enums.CategoryEnum;

import java.util.EnumSet;

public class CategoryGroups {
    public static final EnumSet<CategoryEnum> Tops = EnumSet.of(
            CategoryEnum.Shirt, CategoryEnum.T_shirt, CategoryEnum.Jacket,
            CategoryEnum.Hoodie, CategoryEnum.Sweater, CategoryEnum.Coat
    );

    public static final EnumSet<CategoryEnum> Bottoms = EnumSet.of(
            CategoryEnum.Pants, CategoryEnum.Jeans, CategoryEnum.Shorts
    );

    public static final EnumSet<CategoryEnum> Looks = EnumSet.of(
            CategoryEnum.Look
    );

    public static final EnumSet<CategoryEnum> Footwear = EnumSet.of(
            CategoryEnum.Shoes, CategoryEnum.Boots, CategoryEnum.Sneakers, CategoryEnum.Sandals
    );

    public static final EnumSet<CategoryEnum> Add_ons  = EnumSet.of(
            CategoryEnum.Accessories, CategoryEnum.Hat, CategoryEnum.Bag,
            CategoryEnum.Belt, CategoryEnum.Scarf, CategoryEnum.Gloves
    );

    public static final EnumSet<CategoryEnum> Sets = EnumSet.of(
            CategoryEnum.Suit
    );
}
