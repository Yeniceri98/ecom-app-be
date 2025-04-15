package org.application.ecomappbe.service;

import org.application.ecomappbe.dto.CartDto;

public interface CartService {
    CartDto addProductToCart(Long productId, Integer quantity);
}
