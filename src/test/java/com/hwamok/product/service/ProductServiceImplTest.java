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
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.hwamok.core.exception.HwamokExceptionTest.assertThatHwamokException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;


@SpringBootTest
@Transactional
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
        Product product = productService.create("사과", "S001", 10000, category);

        //카테고리에 등록해줘야하는 절차 필요

        assertThat(product.getId()).isNotNull();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 상품_생성_실패__이름이_null_또는_빈값(String name) {
        assertThatIllegalArgumentException()
                .isThrownBy(()-> productService.create(name, "S001", 10000, category));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 상품_생성_실패__코드가_null_또는_빈값(String code) {
        assertThatIllegalArgumentException()
                .isThrownBy(()-> productService.create("사과", code, 10000, category));
    }

    @Test
    void 상품_생성_실패__가격이_빈값() {
        assertThatIllegalArgumentException()
                .isThrownBy(()-> productService.create("사과", "S001", null, category));
    }

    @Test
    void 상품_생성_실패__카테고리가_빈값() {
        assertThatIllegalArgumentException()
                .isThrownBy(()-> productService.create("사과", "S001", 10000, null));
    }

    @Test
    void 상품_생성_실패__이름_길이가_21자_이상() {
        String wrongName = "사과사과사과사과사과사과사과사과사과사과사";

        assertThatHwamokException(ExceptionCode.NOT_NAME_FORM)
                .isThrownBy(() -> productService.create(wrongName, "S001", 10000, category));
    }

    @Test
    void 상품_생성_실패__가격이_음수() {
        Integer wrongPrice = -1;

        assertThatHwamokException(ExceptionCode.NOT_PRICE_FORM)
                .isThrownBy(() -> productService.create("사과", "S001", wrongPrice, category));
    }

    @Test
    void 이름으로_모든_상품_가져오기_성공() {
        Product product1 = productRepository.save(ProductFixture.createProduct("사과", "S012", category));

        List<Product> productList = productService.getProductByName("사과");

        assertList(0, productList, product);
        assertList(1, productList, product1);
    }

    @Test
    void 코드로_상품_가져오기_성공() {
        Product foundProduct = productService.getProductByCode("S001");

        assertThat(foundProduct.getId()).isEqualTo(product.getId());
    }

    @Test
    void 코드로_상품_가져오기_실패__없는_코드() {
        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_FOUND_PRODUCT)
                .isThrownBy(() -> productService.getProductByCode("T234"));
    }

    @Test
    void 코드로_상품_가져오기_실패__삭제된_상품() {
        product.deleteProduct();
        productRepository.save(product);

        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_FOUND_PRODUCT)
                .isThrownBy(() -> productService.getProductByCode("S001"));
    }

    @Test
    void 상품_수정하기_성공() {
        Long id = product.getId();

        Product updatedProduct = productService.update(id, "변경", "L1111", 10, category);

        assertThat(updatedProduct.getId()).isEqualTo(product.getId());
        assertThat(updatedProduct.getName()).isEqualTo("변경");
        assertThat(updatedProduct.getCode()).isEqualTo("L1111");
        assertThat(updatedProduct.getPrice()).isEqualTo(10);
    }

    @Test
    void 상품_수정하기_실패__없는_id() {
        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_FOUND_PRODUCT)
                .isThrownBy(() -> productService.update(-1L,"변경", "L1111", 10, category));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 상품_수정하기_실패__이름이_null_또는_빈값(String name) {
        Long productId = product.getId();

        assertThatIllegalArgumentException()
                .isThrownBy(()-> productService.update(productId, null, "L1111", 10, category));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 상품_수정하기_실패__코드가_null_또는_빈값(String code) {
        Long productId = product.getId();

        assertThatIllegalArgumentException()
                .isThrownBy(()-> productService.update(productId, "변경", code, 10, category));
    }

    @Test
    void 상품_수정하기_실패__가격이_null() {
        Long productId = product.getId();

        assertThatIllegalArgumentException()
                .isThrownBy(()-> productService.update(productId, "변경", "T123", null, category));
    }

    @Test
    void 상품_수정하기_실패__카테고리가_null() {
        Long productId = product.getId();

        assertThatIllegalArgumentException()
                .isThrownBy(()-> productService.update(productId, "변경", "T123", 100, null));
    }

    @Test
    void 상품_수정하기_실패__이름이_21자_이상() {
        String wrongName = "사과사과사과사과사과사과사과사과사과사과사";
        Long productId = product.getId();

        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_NAME_FORM)
                .isThrownBy(()-> productService.update(productId, wrongName, "T123", 100, category));
    }

    @Test
    void 상품_수정하기_실패__가격이_음수() {
        Long productId = product.getId();

        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_PRICE_FORM)
                .isThrownBy(()-> productService.update(productId, "감귤", "T123", -1, category));
    }

    @Test
    void 상품_삭제_성공() {
        Long productId = product.getId();

        productService.delete(productId);

        assertThat(product.getStatus()).isEqualTo(ProductStatus.INACTIVATED);
    }

    @Test
    void 상품_삭제_실패__없는_id() {
        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_FOUND_PRODUCT)
                .isThrownBy(()->productService.delete(-1L));
    }

    private void assertList(int index, List<Product> list, Product product) {
        assertThat(list.get(index).getId()).isEqualTo(product.getId());
        assertThat(list.get(index).getName()).isEqualTo(product.getName());
        assertThat(list.get(index).getCode()).isEqualTo(product.getCode());
        assertThat(list.get(index).getPrice()).isEqualTo(product.getPrice());
        assertThat(list.get(index).getCategory()).isEqualTo(product.getCategory());
        assertThat(list.get(index).getStatus()).isEqualTo(product.getStatus());
    }
}
