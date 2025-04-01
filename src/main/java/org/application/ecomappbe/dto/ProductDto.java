package org.application.ecomappbe.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long productId;

    @Size(min = 3, message = "Product Name field should have at least 3 characters")
    private String productName;

    private String image;

    @NotBlank
    private String description;

    @NotBlank
    private Integer quantity;

    @NotBlank
    private Double price;

    @NotBlank
    private Double discount;

    private Double specialPrice;
}
