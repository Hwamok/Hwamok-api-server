package com.hwamok.product.domain;

import com.hwamok.category.domain.Category;
import com.hwamok.category.domain.CategoryRepository;
import fixture.CategoryFixture;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ProductRepositoryTest {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    private Category category;

    private Product product;

    @BeforeEach
    private void setup() {
        category = categoryRepository.save(CategoryFixture.createCategory());

        product = productRepository.save(new Product("사과", "F001", 10000, category));
    }

    @Test
    void 상품_ID와_상태로_조회하기_성공() {
        Long id = product.getId();

        Product foundProduct = productRepository.findProductByIdAndStatus(id, ProductStatus.ACTIVATED)
                .orElseThrow(() -> new RuntimeException());

        assertThat(foundProduct.getId()).isEqualTo(id);
    }

    @Test
    void 이름과_상태로_모든_상품_조회하기_성공() {
        Category category2 = categoryRepository.save(CategoryFixture.createCategory());
        Product product2 = productRepository.save(new Product("사과", "S001", 10000, category2));

        List<Product> productList = productRepository.findAllByNameAndStatus("사과", ProductStatus.ACTIVATED);

        assertThat(productList.size()).isEqualTo(2);
    }

    @Test
    void 코드와_상태로_상품_조회하기_성공() {
        String code = product.getCode();

        Product foundProduct = productRepository.findProductByCodeAndStatus(code, ProductStatus.ACTIVATED)
                .orElseThrow(() -> new RuntimeException());

        assertThat(foundProduct.getId()).isEqualTo(product.getId());
    }
}