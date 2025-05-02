package org.application.ecomappbe.service;

import org.application.ecomappbe.dto.OrderDto;
import org.application.ecomappbe.dto.OrderRequestDto;

public interface OrderService {
    OrderDto orderProducts(OrderRequestDto orderRequestDto, String paymentMethod);
}
