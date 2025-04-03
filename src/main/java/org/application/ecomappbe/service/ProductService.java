package org.application.ecomappbe.service;

import org.application.ecomappbe.dto.ProductDto;
import org.application.ecomappbe.dto.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ProductResponse getProductsByCategory(Long categoryId);
    ProductResponse getProductsByKeyword(String keyword);
    ProductDto addProduct(ProductDto productDto, Long categoryId);
    ProductDto updateProduct(ProductDto productDto, Long productId);
    ProductDto updateProductImage(Long productId, MultipartFile image) throws IOException;
    void deleteProduct(Long productId);
}
