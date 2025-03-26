package org.application.ecomappbe.mapper;

import org.application.ecomappbe.dto.CategoryDto;
import org.application.ecomappbe.model.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto mapToDto(Category category);
    Category mapToEntity(CategoryDto categoryDto);

    // List Conversions (Optional. You don't have to implement extra conversions in Service class if you do this here)
    List<CategoryDto> mapToDtoList(List<Category> categories);
    List<Category> mapToEntityList(List<CategoryDto> categoryDtos);
}
