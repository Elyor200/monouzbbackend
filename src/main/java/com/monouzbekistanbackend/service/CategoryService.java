package com.monouzbekistanbackend.service;

import com.monouzbekistanbackend.dto.category.CategoryGroupResponse;
import com.monouzbekistanbackend.entity.Category;
import com.monouzbekistanbackend.enums.CategoryEnum;
import com.monouzbekistanbackend.enums.category.CategoryGroups;
import com.monouzbekistanbackend.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryGroupService categoryGroupService;

    public CategoryService(CategoryRepository categoryRepository, CategoryGroupService categoryGroupService) {
        this.categoryRepository = categoryRepository;
        this.categoryGroupService = categoryGroupService;
    }

    public Category createCategory (String name) {
        CategoryEnum categoryEnum;
        try {
            categoryEnum = CategoryEnum.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid category name");
        }

        if (categoryRepository.existsByCategory(categoryEnum)) {
            throw new RuntimeException("Category already exists");
        }

        Category category = new Category();
        category.setCategoryId(UUID.randomUUID());
        category.setCategory(categoryEnum);
        category.setCreatedAt(LocalDateTime.now());
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<CategoryGroupResponse> getGroupedCategories() {
        return categoryGroupService.GetCategoryGroups();
    }
}
