package org.application.ecomappbe.mapper;

import org.application.ecomappbe.dto.OrderItemDto;
import org.application.ecomappbe.model.OrderItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItemDto mapToDto(OrderItem orderItem);
    OrderItem mapToEntity(OrderItemDto orderItemDto);

    List<OrderItemDto> mapToDtoList(List<OrderItem> orderItems);
    List<OrderItem> mapToEntityList(List<OrderItemDto> orderItemDtos);
}
