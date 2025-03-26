package org.application.ecomappbe.controller;

import jakarta.validation.Valid;
import org.application.ecomappbe.config.AppConstants;
import org.application.ecomappbe.dto.CategoryDto;
import org.application.ecomappbe.dto.CategoryResponseList;
import org.application.ecomappbe.service.CategoryServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private final CategoryServiceImpl service;

    public CategoryController(CategoryServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponseList> getAllCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize
    ) {
        return new ResponseEntity<>(service.getAllCategories(pageNumber, pageSize), HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long categoryId) {
        return new ResponseEntity<>(service.getCategoryById(categoryId), HttpStatus.OK);
    }

    @PostMapping("/admin/category")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return new ResponseEntity<>(service.createCategory(categoryDto), HttpStatus.CREATED);
    }

    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable Long categoryId) {
        return new ResponseEntity<>(service.updateCategory(categoryDto, categoryId), HttpStatus.OK);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        service.deleteCategory(categoryId);
        return new ResponseEntity<>("Category with ID " + categoryId + " is deleted successfully", HttpStatus.OK);
    }
}
