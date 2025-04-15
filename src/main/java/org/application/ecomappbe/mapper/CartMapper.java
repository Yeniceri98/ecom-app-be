package org.application.ecomappbe.mapper;

import org.application.ecomappbe.dto.CartDto;
import org.application.ecomappbe.model.Cart;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto mapToDto(Cart cart);
    Cart mapToEntity(CartDto cartDto);

    List<CartDto> mapToDtoList(List<Cart> carts);
    List<Cart> mapToEntityList(List<CartDto> cartDtos);
}
