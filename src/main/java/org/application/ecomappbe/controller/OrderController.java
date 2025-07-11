package org.application.ecomappbe.controller;

import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.application.ecomappbe.dto.OrderDto;
import org.application.ecomappbe.dto.OrderRequestDto;
import org.application.ecomappbe.dto.PaymentRequestDto;
import org.application.ecomappbe.service.OrderService;
import org.application.ecomappbe.service.StripeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final StripeService stripeService;

    @PostMapping("/order/users/payments/{paymentMethod}")
    public ResponseEntity<OrderDto> orderProducts(@RequestBody OrderRequestDto orderRequestDto, @PathVariable String paymentMethod) {
        return new ResponseEntity<>(orderService.orderProducts(orderRequestDto, paymentMethod), HttpStatus.CREATED);
    }

    @PostMapping("/order/payment-intent")
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentRequestDto paymentRequestDto) {
        PaymentIntent paymentIntent = stripeService.createPaymentIntent(paymentRequestDto);
        return new ResponseEntity<>(paymentIntent.getClientSecret(), HttpStatus.CREATED);
    }
}
