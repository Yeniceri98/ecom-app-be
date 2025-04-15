package org.application.ecomappbe.service;

import org.application.ecomappbe.dto.CartDto;
import org.application.ecomappbe.dto.ProductDto;
import org.application.ecomappbe.exception.APIException;
import org.application.ecomappbe.exception.ResourceNotFoundException;
import org.application.ecomappbe.mapper.CartMapper;
import org.application.ecomappbe.mapper.ProductMapper;
import org.application.ecomappbe.model.Cart;
import org.application.ecomappbe.model.CartItem;
import org.application.ecomappbe.model.Product;
import org.application.ecomappbe.repository.CartItemRepository;
import org.application.ecomappbe.repository.CartRepository;
import org.application.ecomappbe.repository.ProductRepository;
import org.application.ecomappbe.util.AuthUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final AuthUtil authUtil;
    private final CartMapper cartMapper;
    private final ProductMapper productMapper;

    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductRepository productRepository, AuthUtil authUtil, CartMapper cartMapper, ProductMapper productMapper) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.authUtil = authUtil;
        this.cartMapper = cartMapper;
        this.productMapper = productMapper;
    }

    @Override
    public CartDto addProductToCart(Long productId, Integer quantity) {
        Cart cart = createCart();

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product with ID " + productId + " is not found")
        );

        if (product.getQuantity() == 0) {
            throw new APIException(product.getProductName() + " is out of stock");
        }

        if (product.getQuantity() < quantity) {
            throw new APIException("Please make an order of the " + product.getProductName() + " less than or equal to " + product.getQuantity());
        }

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), product.getProductId()).orElse(null);

        if (cartItem == null) {
            CartItem newCartItem = new CartItem();
            newCartItem.setProduct(product);
            newCartItem.setCart(cart);
            newCartItem.setQuantity(quantity);
            newCartItem.setDiscount(product.getDiscount());
            newCartItem.setProductPrice(product.getSpecialPrice());

            cartItemRepository.save(newCartItem);
            cart.getCartItems().add(newCartItem);
        } else {
            // If the product already exists in the cart, update the quantity
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
        }

        product.setQuantity(product.getQuantity() - quantity); // Decrease the quantity by the amount added to the cart
        productRepository.save(product);

        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
        cartRepository.save(cart);

        return buildCartDtoWithProducts(cart);
    }

    @Override
    public List<CartDto> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();

        if (carts.isEmpty()) {
            throw new APIException("No cart exists");
        }

        return carts.stream()
                .map(this::buildCartDtoWithProducts)
                .collect(Collectors.toList());
    }

    @Override
    public CartDto getCartOfUser() {
        String email = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(email);

        if (cart == null) {
            throw new APIException("Cart not found for related user");
        }

        Long cartId = cart.getCartId();
        Cart findCartByEmailAndId = cartRepository.findCartByEmailAndId(email, cartId);

        if (findCartByEmailAndId == null) {
            throw new APIException("Cart not found with the provided email and cart ID");
        }

        return buildCartDtoWithProducts(findCartByEmailAndId);
    }

    // Helper Methods
    private CartDto buildCartDtoWithProducts(Cart cart) {
        CartDto cartDto = cartMapper.mapToDto(cart);

        List<CartItem> cartItems = cart.getCartItems();

        List<ProductDto> productDtos = cartItems.stream().map(cartItem -> {
            ProductDto productDto = productMapper.mapToDto(cartItem.getProduct());
            productDto.setQuantity(cartItem.getQuantity());
            return productDto;
        }).collect(Collectors.toList());

        cartDto.setProducts(productDtos);
        return cartDto;
    }

    private Cart createCart() {
        Cart cart = cartRepository.findCartByEmail(authUtil.loggedInEmail());

        if (cart != null) {
            return cart;
        }

        Cart newCart = new Cart();
        newCart.setTotalPrice(0.0);
        newCart.setUser(authUtil.loggedInUser());

        return cartRepository.save(newCart);
    }
}
