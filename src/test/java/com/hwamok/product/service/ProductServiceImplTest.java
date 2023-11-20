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
        Product product = productService.createProduct("사과", "S001", 10000, category);

        assertThat(product.getId()).isNotNull();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 상품_생성_실패__이름이_null_또는_빈값(String name) {
        assertThatIllegalArgumentException()
                .isThrownBy(()-> productService.createProduct(name, "S001", 10000, category));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 상품_생성_실패__코드가_null_또는_빈값(String code) {
        assertThatIllegalArgumentException()
                .isThrownBy(()-> productService.createProduct("사과", code, 10000, category));
    }

    @Test
    void 상품_생성_실패__가격이_빈값() {
        assertThatIllegalArgumentException()
                .isThrownBy(()-> productService.createProduct("사과", "S001", null, category));
    }

    @Test
    void 상품_생성_실패__카테고리가_빈값() {
        assertThatIllegalArgumentException()
                .isThrownBy(()-> productService.createProduct("사과", "S001", 10000, null));
    }

    @Test
    void 상품_생성_실패__이름_길이가_21자_이상() {
        String wrongName = "사과사과사과사과사과사과사과사과사과사과사";

        assertThatHwamokException(ExceptionCode.NOT_NAME_FORM)
                .isThrownBy(() -> productService.createProduct(wrongName, "S001", 10000, category));
    }

    @Test
    void 상품_생성_실패__가격이_음수() {
        Integer wrongPrice = -1;

        assertThatHwamokException(ExceptionCode.NOT_PRICE_FORM)
                .isThrownBy(() -> productService.createProduct("사과", "S001", wrongPrice, category));
    }

    @Test
    void 이름으로_상품_가져오기_성공() {
        Product foundProduct = productService.getProductByName("사과");

        assertThat(foundProduct.getId()).isNotNull();
    }

}