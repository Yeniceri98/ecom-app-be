package org.application.ecomappbe.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.application.ecomappbe.dto.OrderDto;
import org.application.ecomappbe.dto.OrderItemDto;
import org.application.ecomappbe.dto.OrderRequestDto;
import org.application.ecomappbe.exception.ResourceNotFoundException;
import org.application.ecomappbe.mapper.OrderItemMapper;
import org.application.ecomappbe.mapper.OrderMapper;
import org.application.ecomappbe.mapper.PaymentMapper;
import org.application.ecomappbe.mapper.ProductMapper;
import org.application.ecomappbe.model.*;
import org.application.ecomappbe.repository.*;
import org.application.ecomappbe.util.AuthUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    private final CartService cartService;
    private final AuthUtil authUtil;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final PaymentMapper paymentMapper;
    private final ProductMapper productMapper;

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository, CartRepository cartRepository, AddressRepository addressRepository, ProductRepository productRepository, PaymentRepository paymentRepository, CartService cartService, AuthUtil authUtil, OrderMapper orderMapper, OrderItemMapper orderItemMapper, PaymentMapper paymentMapper, ProductMapper productMapper) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.addressRepository = addressRepository;
        this.productRepository = productRepository;
        this.paymentRepository = paymentRepository;
        this.cartService = cartService;
        this.authUtil = authUtil;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.paymentMapper = paymentMapper;
        this.productMapper = productMapper;
    }

    @Override
    @Transactional
    public OrderDto orderProducts(OrderRequestDto orderRequestDto, String paymentMethod) {
        String email = authUtil.loggedInEmail();

        Cart cart = cartRepository.findCartByEmail(email);

        if (cart == null) {
            throw new ResourceNotFoundException("Cart not found for email: " + email);
        }

        Address address = addressRepository.findById(orderRequestDto.getAddressId()).orElseThrow(
                () -> new ResourceNotFoundException("Address not found with ID: " + orderRequestDto.getAddressId())
        );

        Order order = new Order();
        order.setOrderEmail(email);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus("Order Accepted");
        order.setAddress(address);

        Payment payment = new Payment();
        payment.setPaymentMethod(paymentMethod);
        payment.setPgPaymentId(orderRequestDto.getPgPaymentId());
        payment.setPgName(orderRequestDto.getPgName());
        payment.setPgStatus(orderRequestDto.getPgStatus());
        payment.setPgMessage(orderRequestDto.getPgMessage());
        payment.setOrder(order);

        paymentRepository.save(payment);

        order.setPayment(payment);
        Order savedOrder = orderRepository.save(order);

        List<CartItem> cartItems = cart.getCartItems();

        if (cartItems.isEmpty()) {
            throw new ResourceNotFoundException("Cart is empty");
        }

        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setDiscount(cartItem.getDiscount());
                    orderItem.setOrderedProductPrice(cartItem.getProductPrice());
                    orderItem.setProduct(cartItem.getProduct());
                    orderItem.setOrder(savedOrder);
                    return orderItem;
                }
        ).toList();

        orderItemRepository.saveAll(orderItems);

        // Create a copy of cart items to avoid ConcurrentModificationException
        List<CartItem> cartItemsCopy = new ArrayList<>(cart.getCartItems());

        cartItemsCopy.forEach(item -> {
            int quantity = item.getQuantity();
            Product product = item.getProduct();

            // Reduce Stock
            product.setQuantity(product.getQuantity() - quantity);
            productRepository.save(product);

            // Remove Items from the Cart
            cartService.removeProductFromCart(cart.getCartId(), item.getProduct().getProductId());
        });

        OrderDto orderDto = orderMapper.mapToDto(savedOrder);

        if (orderDto.getOrderItemsDto() == null) {
            orderDto.setOrderItemsDto(new ArrayList<>());
        }

        orderItems.forEach(item -> {
            if (item != null) {
                OrderItemDto itemDto = orderItemMapper.mapToDto(item);
                if (item.getProduct() != null) {
                    itemDto.setProductDto(productMapper.mapToDto(item.getProduct()));
                }
                orderDto.getOrderItemsDto().add(itemDto);
            }
        });

        orderDto.setAddressId(orderRequestDto.getAddressId());
        orderDto.setPaymentDto(paymentMapper.mapToDto(payment));

        return orderDto;
    }
}
