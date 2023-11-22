package com.hwamok.product.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findProductByIdAndStatus(Long id, ProductStatus status);

    List<Product> findAllByNameAndStatus(String name, ProductStatus status);

    Optional<Product> findProductByCodeAndStatus(String code, ProductStatus status);
}
