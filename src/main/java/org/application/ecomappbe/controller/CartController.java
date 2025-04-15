package org.application.ecomappbe.controller;

import org.application.ecomappbe.dto.CartDto;
import org.application.ecomappbe.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDto> addProductToCart(@PathVariable Long productId, @PathVariable Integer quantity) {
        return ResponseEntity.ok(cartService.addProductToCart(productId, quantity));
    }
}
