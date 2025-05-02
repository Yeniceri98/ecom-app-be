package org.application.ecomappbe.controller;

import lombok.RequiredArgsConstructor;
import org.application.ecomappbe.dto.OrderDto;
import org.application.ecomappbe.dto.OrderRequestDto;
import org.application.ecomappbe.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/order/users/payments/{paymentMethod}")
    public ResponseEntity<OrderDto> orderProducts(@RequestBody OrderRequestDto orderRequestDto, @PathVariable String paymentMethod) {
        return new ResponseEntity<>(orderService.orderProducts(orderRequestDto, paymentMethod), HttpStatus.CREATED);
    }
}
