package com.hwamok.product.service;

import com.hwamok.category.domain.Category;
import com.hwamok.category.domain.CategoryRepository;
import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.core.exception.HwamokExceptionTest;
import com.hwamok.product.domain.Product;
import com.hwamok.product.domain.ProductRepository;
import com.hwamok.product.domain.ProductStatus;
import fixture.CategoryFixture;
import fixture.ProductFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.hwamok.core.exception.HwamokExceptionTest.assertThatHwamokException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;


@SpringBootTest
class ProductServiceImplTest {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductService productService;

    private Category category;

    private Product product;

    @BeforeEach
    private void setup(){
        category = categoryRepository.save(CategoryFixture.createCategory());

        product = productRepository.save(ProductFixture.createProduct(category));
    }

    @Test
    void 상품_생성_성공() {
        Product product = productService.createProduct("사과", "S001", 10000, null);

        assertThat(product.getId()).isNotNull();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 상품_생성_실패__이름이_null_또는_빈값(String name) {
        assertThatIllegalArgumentException()
                .isThrownBy(()-> productService.createProduct(name, "S001", 10000, null));
    }
}