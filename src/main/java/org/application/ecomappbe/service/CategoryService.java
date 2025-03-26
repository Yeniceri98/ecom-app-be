package org.application.ecomappbe.service;

import org.application.ecomappbe.dto.CategoryDto;
import org.application.ecomappbe.dto.CategoryResponseList;

public interface CategoryService {
    CategoryResponseList getAllCategories(Integer pageNumber, Integer pageSize);
    CategoryDto getCategoryById(Long categoryId);
    CategoryDto createCategory(CategoryDto categoryDto);
    CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId);
    void deleteCategory(Long categoryId);
}
