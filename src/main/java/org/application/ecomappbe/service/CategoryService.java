package org.application.ecomappbe.service;

import org.application.ecomappbe.dto.CategoryDto;
import org.application.ecomappbe.dto.CategoryResponseList;

public interface CategoryService {
    CategoryResponseList getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    CategoryDto getCategoryById(Long categoryId);
    CategoryDto createCategory(CategoryDto categoryDto);
    CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId);
    void deleteCategory(Long categoryId);
}
