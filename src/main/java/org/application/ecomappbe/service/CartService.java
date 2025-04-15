package org.application.ecomappbe.service;

import org.application.ecomappbe.dto.CartDto;

import java.util.List;

public interface CartService {
    CartDto addProductToCart(Long productId, Integer quantity);
    List<CartDto> getAllCarts();
    CartDto getCartOfUser();
}
