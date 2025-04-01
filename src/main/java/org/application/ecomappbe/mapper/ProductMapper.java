package org.application.ecomappbe.mapper;

import org.application.ecomappbe.dto.ProductDto;
import org.application.ecomappbe.model.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto mapToDto(Product product);
    Product mapToEntity(ProductDto productDto);
    List<ProductDto> mapToDtoList(List<Product> products);
    List<Product> mapToEntityList(List<ProductDto> productDtos);
}
