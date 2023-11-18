package com.hwamok.product.domain;

import com.hwamok.category.domain.Category;
import com.hwamok.category.domain.CategoryRepository;
import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.core.exception.HwamokExceptionTest;
import fixture.CategoryFixture;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static fixture.CategoryFixture.createCategory;
import static org.assertj.core.api.Assertions.*;

class ProductTest {
    private Category category;

    @BeforeEach
    private void setup(){
        category = createCategory();
    }

    @Test
    void 상품만들기_성공() {
        Product product = new Product("사과", "s001", 10000, category);

        assertThat(product.getName()).isEqualTo("사과");
        assertThat(product.getCode()).isEqualTo("s001");
        assertThat(product.getPrice()).isEqualTo(10000);
        assertThat(product.getCategory()).isSameAs(category);
        assertThat(product.getProductStatus()).isEqualTo(ProductStatus.ACTIVATED);
        assertThat(product.getId()).isNull();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 상품만들기_실패__이름이_null_또는_빈값(String name) {
        assertThatIllegalArgumentException().isThrownBy(()->
                new Product(name, "s001", 10000, category));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 상품만들기_실패__코드가_null_또는_빈값(String code) {
        assertThatIllegalArgumentException().isThrownBy(()->
                new Product("사과", code, 10000, category));
    }

    @Test
    void 상품만들기_실패_가격이_null() {
        assertThatIllegalArgumentException().isThrownBy(()->
                new Product("사과", "s001", null, category));
    }

    @Test
    void 상품만들기_실패_카테고리가_null() {
        assertThatIllegalArgumentException().isThrownBy(()->
                new Product("사과", "s001", 10000, null));
    }

    @Test
    void 상품만들기_실패_이름_길이가_21자_이상() {
        String wrongName = "사과사과사과사과사과사과사과사과사과사과사";

        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_NAME_FORM)
                .isThrownBy(()-> new Product(wrongName, "s001", 10000, category));
    }

    @Test
    void 상품만들기_실패_가격이_음수() {
        Integer wrongPrice = -1;

        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_PRICE_FORM)
                .isThrownBy(()-> new Product("사과", "s001", wrongPrice, category));
    }

    @Test
    void 상품삭제_성공() {
        Product product = new Product("사과", "s001", 10000, category);

        product.deleteProduct();

        assertThat(product.getProductStatus()).isEqualTo(ProductStatus.INACTIVATED);
    }

    @Test
    void 상품업데이트_성공() {
        Category updateCategory = createCategory(category);
        Product product = new Product("사과", "s001", 10000, category);

        product.updateProduct("감", "s002", 5000, updateCategory);

        assertThat(product.getName()).isEqualTo("감");
        assertThat(product.getCode()).isEqualTo("s002");
        assertThat(product.getPrice()).isEqualTo(5000);
        assertThat(product.getCategory()).isSameAs(updateCategory);
    }
}