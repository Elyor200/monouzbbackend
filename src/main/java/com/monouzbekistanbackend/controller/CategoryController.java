package com.monouzbekistanbackend.controller;

import com.monouzbekistanbackend.dto.ResponseProductDto;
import com.monouzbekistanbackend.dto.category.CategoryGroupResponse;
import com.monouzbekistanbackend.entity.Category;
import com.monouzbekistanbackend.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/create-category")
    public ResponseEntity<?> createCategory(@RequestParam String name) {
        try {
            Category category = categoryService.createCategory(name);
            return ResponseEntity.ok(category);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/category-groups")
    public ResponseEntity<?> getGroupedCategories() {
        try {
            List<CategoryGroupResponse> groupedCategories = categoryService.getGroupedCategories();
            return ResponseEntity.ok(groupedCategories);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getAllCategories")
    public ResponseEntity<?> getAllCategories() {
        try {
            List<Category> allCategories = categoryService.getAllCategories();
            return ResponseEntity.ok(allCategories);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
