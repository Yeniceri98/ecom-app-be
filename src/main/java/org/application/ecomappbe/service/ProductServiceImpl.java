package org.application.ecomappbe.service;

import org.application.ecomappbe.dto.ProductDto;
import org.application.ecomappbe.dto.ProductResponse;
import org.application.ecomappbe.exception.ResourceNotFoundException;
import org.application.ecomappbe.mapper.ProductMapper;
import org.application.ecomappbe.model.Category;
import org.application.ecomappbe.model.Product;
import org.application.ecomappbe.repository.CategoryRepository;
import org.application.ecomappbe.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ProductResponse getAllProducts() {
        List<Product> productList = productRepository.findAll();
        List<ProductDto> productDtoList = productMapper.mapToDtoList(productList);
        return new ProductResponse(productDtoList);
    }

    @Override
    public ProductResponse getProductsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category with ID " + categoryId + " is not found")
        );

        List<Product> productList = productRepository.findByCategoryOrderByPriceAsc(category);  // Custom Query Method
        List<ProductDto> productDtoList = productMapper.mapToDtoList(productList);
        return new ProductResponse(productDtoList);
    }

    @Override
    public ProductResponse getProductsByKeyword(String keyword) {
        List<Product> productList = productRepository.findByProductNameContainingIgnoreCase(keyword);
        List<ProductDto> productDtoList = productMapper.mapToDtoList(productList);
        return new ProductResponse(productDtoList);
    }

    @Override
    public ProductDto addProduct(ProductDto productDto, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category with ID " + categoryId + " is not found")
        );

        // Converting DTO to Entity
        Product product = productMapper.mapToEntity(productDto);

        // Setting the Fields (These won't be in the request since we declare and set them in here)
        product.setImage("default.png");
        double specialPrice = product.getPrice() - (product.getDiscount() * 0.01) * product.getPrice();
        product.setSpecialPrice(specialPrice);
        product.setCategory(category);

        // Saving the Product
        Product savedProduct = productRepository.save(product);

        // Converting back to DTO
        return productMapper.mapToDto(savedProduct);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, Long productId) {
        Product existingProduct = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product with ID " + productId + " is not found")
        );

        // Updating Fields
        existingProduct.setProductName(productDto.getProductName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setQuantity(productDto.getQuantity());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setDiscount(productDto.getDiscount());

        double specialPrice = existingProduct.getPrice() - (existingProduct.getDiscount() * 0.01) * existingProduct.getPrice();
        existingProduct.setSpecialPrice(specialPrice);

        // Saving Updated Product
        Product updatedProduct = productRepository.save(existingProduct);

        // Converting to DTO
        return productMapper.mapToDto(updatedProduct);
    }

    @Override
    public ProductDto updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product existingProduct = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product with ID " + productId + " is not found")
        );

        try {
            // Uploading image & getting the file name of uploaded image
            String path = "images/";
            String fileName = uploadImage(path, image);

            // Updating new file name to product
            existingProduct.setImage(fileName);

            // Saving updated product
            Product updatedProduct = productRepository.save(existingProduct);

            // Converting to DTO
            return productMapper.mapToDto(updatedProduct);
        } catch (IOException e) {
            throw new IOException("Failed to upload image for product with ID " + productId, e);
        }
    }

    private String uploadImage(String path, MultipartFile file) throws IOException {
        // Original File
        String originalFileName = file.getOriginalFilename();

        // Generating Unique File Name
        String randomId = UUID.randomUUID().toString();
        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf(".")));   // EX: originalFileName: example.jpg / randomId: 123 ---> 123.jpg (fileName at the end after the process)
        String filePath = path + File.separator + fileName;

        // Check if directory exists and create if it doesn't
        File folder = new File(path);

        if (!folder.exists()) {
            folder.mkdir();
        }

        // Uploading to the server
        Files.copy(file.getInputStream(), Paths.get(filePath));

        return fileName;
    }

    @Override
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product with ID " + productId + " is not found")
        );

        productRepository.delete(product);
    }
}
