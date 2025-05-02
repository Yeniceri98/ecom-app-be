package org.application.ecomappbe.mapper;

import org.application.ecomappbe.dto.OrderDto;
import org.application.ecomappbe.model.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto mapToDto(Order order);
    Order mapToEntity(OrderDto orderDto);

    List<OrderDto> mapToDtoList(List<Order> orders);
    List<Order> mapToEntityList(List<OrderDto> orderDtos);
}
