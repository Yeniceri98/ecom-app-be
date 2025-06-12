package org.application.ecomappbe.repository;

import org.application.ecomappbe.model.Category;
import org.application.ecomappbe.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Page<Product> findByCategoryOrderByPriceAsc(Category category, Pageable pageable);
    Page<Product> findByProductNameContainingIgnoreCase(String keyword, Pageable pageable);
}
