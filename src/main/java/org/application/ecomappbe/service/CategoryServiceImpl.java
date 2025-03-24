package org.application.ecomappbe.service;

import org.application.ecomappbe.exception.CategoryNotFoundException;
import org.application.ecomappbe.model.Category;
import org.application.ecomappbe.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;

    public CategoryServiceImpl(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Category> getAllCategories() {
        return repository.findAll();
    }

    @Override
    public Category createCategory(Category category) {
        return repository.save(category);
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        Category existingCategory = repository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException("Category with ID " + categoryId + " is not found"));

        existingCategory.setCategoryName(category.getCategoryName());

        return repository.save(existingCategory);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category existingCategory = repository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException("Category with ID " + categoryId + " is not found"));
        repository.delete(existingCategory);
    }
}
