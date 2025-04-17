package org.application.ecomappbe.controller;

import org.application.ecomappbe.dto.CartDto;
import org.application.ecomappbe.service.CartService;
import org.application.ecomappbe.util.AuthUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService, AuthUtil authUtil) {
        this.cartService = cartService;
    }

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDto> addProductToCart(@PathVariable Long productId, @PathVariable Integer quantity) {
        return ResponseEntity.ok(cartService.addProductToCart(productId, quantity));
    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartDto>> getAllCarts() {
        return ResponseEntity.ok(cartService.getAllCarts());
    }

    @GetMapping("/carts/user/cart")
    public ResponseEntity<CartDto> getCartOfUser() {
        return ResponseEntity.ok(cartService.getCartOfUser());
    }

    @PutMapping("/carts/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDto> updateProductQuantityInCart(@PathVariable Long productId, @PathVariable String operation) {
        return ResponseEntity.ok(cartService.updateProductQuantityInCart(productId, operation.equalsIgnoreCase("add") ? 1 : -1));
    }

    @DeleteMapping("/carts/{cartId}/products/{productId}")
    public ResponseEntity<String> removeProductFromCart(@PathVariable Long cartId, @PathVariable Long productId) {
        cartService.removeProductFromCart(cartId, productId);
        return ResponseEntity.ok("Product removed from cart successfully");
    }
}
