package org.application.ecomappbe.repository;

import org.application.ecomappbe.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c WHERE c.user.email = :email")
    Cart findCartByEmail(String email);
}
