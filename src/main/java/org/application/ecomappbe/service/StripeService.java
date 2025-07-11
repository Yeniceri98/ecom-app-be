package org.application.ecomappbe.service;

import com.stripe.model.PaymentIntent;
import org.application.ecomappbe.dto.PaymentRequestDto;

public interface StripeService {
    PaymentIntent createPaymentIntent(PaymentRequestDto paymentRequestDto);
}
