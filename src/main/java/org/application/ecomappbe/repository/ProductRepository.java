package org.application.ecomappbe.repository;

import org.application.ecomappbe.model.Category;
import org.application.ecomappbe.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryOrderByPriceAsc(Category category);
    List<Product> findByProductNameContainingIgnoreCase(String keyword);
}
