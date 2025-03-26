package org.application.ecomappbe.service;

import org.application.ecomappbe.dto.CategoryDto;
import org.application.ecomappbe.dto.CategoryResponseList;
import org.application.ecomappbe.exception.ResourceAlreadyExistsException;
import org.application.ecomappbe.exception.ResourceNotFoundException;
import org.application.ecomappbe.mapper.CategoryMapper;
import org.application.ecomappbe.model.Category;
import org.application.ecomappbe.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository repository, CategoryMapper categoryMapper) {
        this.repository = repository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryResponseList getAllCategories(Integer pageNumber, Integer pageSize) {
        // Pagination
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
        Page<Category> categoryPage = repository.findAll(pageDetails);

        // List<Category> categories = repository.findAll();
        List<Category> categories = categoryPage.getContent();  // Pagination Update
        List<CategoryDto> categoryDtos = categoryMapper.mapToDtoList(categories);

        return new CategoryResponseList(categoryDtos);
    }

    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        Category category = repository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category with ID " + categoryId + " is not found")
        );
        return categoryMapper.mapToDto(category);
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        if (repository.existsByCategoryName(categoryDto.getCategoryName())) {
            throw new ResourceAlreadyExistsException(categoryDto.getCategoryName() + " already exists");
        }

        Category category = categoryMapper.mapToEntity(categoryDto);    // DTO to Entity
        Category savedCategory = repository.save(category);             // Save Entity

        return categoryMapper.mapToDto(savedCategory);                  // Entity to DTO
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId) {
        Category existingCategory = repository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category with ID " + categoryId + " is not found")
        );

        existingCategory.setCategoryName(categoryDto.getCategoryName());
        Category updatedCategory = repository.save(existingCategory);

        return categoryMapper.mapToDto(updatedCategory);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category existingCategory = repository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category with ID " + categoryId + " is not found")
        );

        repository.delete(existingCategory);
    }
}
