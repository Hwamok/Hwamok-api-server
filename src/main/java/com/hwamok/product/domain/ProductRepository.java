package com.hwamok.product.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findProductById(Long id);

    Optional<Product> findProductByName(String name);

    Optional<Product> findProductByCode(String code);
}
