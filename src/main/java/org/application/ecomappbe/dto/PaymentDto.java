package org.application.ecomappbe.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private Long paymentId;

    @NotBlank
    @Size(min = 4, message = "Payment method must be at least 4 characters long")
    private String paymentMethod;

    private String pgPaymentId;
    private String pgName;
    private String pgStatus;
    private String pgMessage;
}
