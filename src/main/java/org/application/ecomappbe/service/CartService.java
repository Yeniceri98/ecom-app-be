package org.application.ecomappbe.service;

import org.application.ecomappbe.dto.CartDto;
import org.application.ecomappbe.dto.CartItemDto;

import java.util.List;

public interface CartService {
    CartDto addProductToCart(Long productId, Integer quantity);
    String createOrUpdateCartWithItems(List<CartItemDto> cartItemDtos);
    List<CartDto> getAllCarts();
    CartDto getCartOfUser();
    CartDto updateProductQuantityInCart(Long productId, int quantity);
    void removeProductFromCart(Long cartId, Long productId);
}
