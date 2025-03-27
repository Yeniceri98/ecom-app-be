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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryResponseList getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        // Sorting
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        // Pagination
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);

        // List<Category> categories = categoryRepository.findAll();
        List<Category> categories = categoryPage.getContent();
        List<CategoryDto> categoryDtos = categoryMapper.mapToDtoList(categories);

        // Set pagination fields
        CategoryResponseList responseList = new CategoryResponseList();
        responseList.setContent(categoryDtos);
        responseList.setPageNumber(categoryPage.getNumber());
        responseList.setPageSize(categoryPage.getSize());
        responseList.setTotalElements(categoryPage.getTotalElements());
        responseList.setTotalPages(categoryPage.getTotalPages());
        responseList.setLastPage(categoryPage.isLast());

        return responseList;
    }

    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category with ID " + categoryId + " is not found")
        );
        return categoryMapper.mapToDto(category);
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        if (categoryRepository.existsByCategoryName(categoryDto.getCategoryName())) {
            throw new ResourceAlreadyExistsException(categoryDto.getCategoryName() + " already exists");
        }

        Category category = categoryMapper.mapToEntity(categoryDto);    // DTO to Entity
        Category savedCategory = categoryRepository.save(category);             // Save Entity

        return categoryMapper.mapToDto(savedCategory);                  // Entity to DTO
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId) {
        Category existingCategory = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category with ID " + categoryId + " is not found")
        );

        existingCategory.setCategoryName(categoryDto.getCategoryName());
        Category updatedCategory = categoryRepository.save(existingCategory);

        return categoryMapper.mapToDto(updatedCategory);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category existingCategory = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category with ID " + categoryId + " is not found")
        );

        categoryRepository.delete(existingCategory);
    }
}
