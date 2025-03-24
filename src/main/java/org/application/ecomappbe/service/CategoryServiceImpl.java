package org.application.ecomappbe.service;

import org.application.ecomappbe.exception.CategoryNotFoundException;
import org.application.ecomappbe.model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private List<Category> categories = new ArrayList<>();
    private Long categoryId = 1L;

    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public Category createCategory(Category category) {
        category.setCategoryId(categoryId++);
        categories.add(category);
        return category;
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        Category existingCategory = categories.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst()
                // .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with ID " + categoryId + " is not found "));  // Faster implementation for exception handling
                .orElseThrow(() -> new CategoryNotFoundException("Category with ID " + categoryId + " is not found"));      // Global Exception Handling (Recommended)

        existingCategory.setCategoryName(category.getCategoryName());

        return existingCategory;
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category category = categories.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst()
                .orElseThrow(() -> new CategoryNotFoundException("Category with ID " + categoryId + " is not found"));
        categories.remove(category);
    }
}
