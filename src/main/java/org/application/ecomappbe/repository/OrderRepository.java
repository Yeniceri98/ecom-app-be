package org.application.ecomappbe.repository;

import org.application.ecomappbe.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
}
