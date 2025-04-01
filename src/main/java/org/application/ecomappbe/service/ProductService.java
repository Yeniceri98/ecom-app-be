package org.application.ecomappbe.service;

import org.application.ecomappbe.dto.ProductDto;
import org.application.ecomappbe.dto.ProductResponse;

public interface ProductService {
    ProductResponse getAllProducts();
    ProductResponse getProductsByCategory(Long categoryId);
    ProductResponse getProductsByKeyword(String keyword);
    ProductDto addProduct(ProductDto productDto, Long categoryId);
    ProductDto updateProduct(ProductDto productDto, Long productId);
    void deleteProduct(Long productId);
}
