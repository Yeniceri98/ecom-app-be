package org.application.ecomappbe.service;

import jakarta.transaction.Transactional;
import org.application.ecomappbe.dto.CartDto;
import org.application.ecomappbe.dto.CartItemDto;
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

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId).orElse(null);

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

    @Transactional
    @Override
    public String createOrUpdateCartWithItems(List<CartItemDto> cartItemDtos) {
        Cart cart = createCart();

        if (!cart.getCartItems().isEmpty()) {
            cartItemRepository.deleteAllByCartId(cart.getCartId());
        }

        double totalPrice = 0.00;

        // Process each item in the request to add to the Cart
        for (CartItemDto cartItemDto : cartItemDtos) {
            Long productId = cartItemDto.getProductId();
            Integer quantity = cartItemDto.getQuantity();

            Product product = productRepository.findById(productId).orElseThrow(
                    () -> new ResourceNotFoundException("Product with ID " + productId + " is not found")
            );

            // product.setQuantity(product.getQuantity() - quantity);
            totalPrice += product.getSpecialPrice() * quantity;

            // Create & Save Cart Item
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setDiscount(product.getDiscount());
            cartItemRepository.save(cartItem);
        }

        cart.setTotalPrice(totalPrice);
        cartRepository.save(cart);

        return "Cart created / updated with the items successfully";
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

    @Transactional
    @Override
    public CartDto updateProductQuantityInCart(Long productId, int quantity) {
        String email = authUtil.loggedInEmail();
        Cart userCart = cartRepository.findCartByEmail(email);
        Long cartId = userCart.getCartId();

        Cart cart = cartRepository.findById(cartId).orElseThrow(
                () -> new ResourceNotFoundException("Cart not found with ID: " + cartId)
        );

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product not found with ID: " + productId)
        );

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(productId, cartId).orElseThrow(
                () -> new ResourceNotFoundException("Cart item not found with product ID: " + productId + " and cart ID: " + cartId)
        );

        if (quantity == 1) {    // ADD Product to Cart
            if (product.getQuantity() == 0) {
                throw new APIException(product.getProductName() + " is out of stock");
            }

            if (product.getQuantity() < quantity) {
                throw new APIException("Please make an order of the " + product.getProductName() + " less than or equal to " + product.getQuantity());
            }

            product.setQuantity(product.getQuantity() - 1);
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {    // REMOVE Product from Cart
            if (cartItem.getQuantity() < 1) {
                throw new APIException("Cannot remove more items than exist in cart");
            }

            product.setQuantity(product.getQuantity() + 1);
            cartItem.setQuantity(cartItem.getQuantity() - 1);
        }

        productRepository.save(product);

        cartItem.setProductPrice(product.getSpecialPrice());
        cartItem.setDiscount(product.getDiscount());
        CartItem updatedCartItem = cartItemRepository.save(cartItem);

        if (updatedCartItem.getQuantity() == 0) {
            cartItemRepository.delete(cartItem);
            cart.getCartItems().remove(cartItem);
        }

        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
        cartRepository.save(cart);

        return buildCartDtoWithProducts(cart);
    }

    @Transactional
    @Override
    public void removeProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(
                () -> new ResourceNotFoundException("Cart not found with ID: " + cartId)
        );

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId).orElseThrow(
                () -> new ResourceNotFoundException("Cart item with cart ID: " + cartId + " and product ID: " + productId + " not found")
        );

        Product product = cartItem.getProduct();
        product.setQuantity(product.getQuantity() + cartItem.getQuantity());
        productRepository.save(product);

        cart.getCartItems().remove(cartItem);
        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));
        cartRepository.save(cart);

        cartItemRepository.deleteCartItemByProductIdAndCartId(cartId, productId);
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
