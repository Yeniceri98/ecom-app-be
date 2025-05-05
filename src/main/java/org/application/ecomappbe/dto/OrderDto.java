package org.application.ecomappbe.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long orderId;

    @Email
    private String orderEmail;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate orderDate;

    private Double totalAmount;
    private String orderStatus;
    private List<OrderItemDto> orderItemsDto = new ArrayList<>();
    private PaymentDto paymentDto;
    private Long addressId;
}
