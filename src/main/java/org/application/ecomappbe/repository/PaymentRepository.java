package org.application.ecomappbe.repository;

import org.application.ecomappbe.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
