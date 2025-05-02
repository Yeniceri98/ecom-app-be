package org.application.ecomappbe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.application.ecomappbe.model.Order;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    private Long orderItemId;
    private Integer quantity;
    private Double discount;
    private Double orderedProductPrice;
    private ProductDto productDto;
    private Order order;
}
