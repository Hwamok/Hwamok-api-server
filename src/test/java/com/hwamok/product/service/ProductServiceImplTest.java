package com.hwamok.product.service;

import com.hwamok.product.domain.Product;
import com.hwamok.product.domain.ProductRepository;
import com.hwamok.product.domain.ProductStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class ProductServiceImplTest {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Test
    void 상품_생성_성공() {
        Product product = productService.createProduct("사과", "S001", 10000, null);

        Assertions.assertThat(product.getId()).isNotNull();
    }
}