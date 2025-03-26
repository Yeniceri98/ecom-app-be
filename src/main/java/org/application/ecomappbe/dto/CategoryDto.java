package org.application.ecomappbe.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Long categoryId;

    @Size(min = 3, message = "Category Name field should have at least 3 characters")
    private String categoryName;
}
