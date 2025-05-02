package org.application.ecomappbe.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long orderId;

    @Email
    private String orderEmail;

    private LocalDate orderDate;
    private Double totalAmount;
    private String orderStatus;
    private List<OrderItemDto> orderItemsDto;
    private PaymentDto paymentDto;
    private Long addressId;
}
