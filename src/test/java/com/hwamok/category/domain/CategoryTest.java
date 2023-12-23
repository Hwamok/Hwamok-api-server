package com.hwamok.category.domain;

import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.core.exception.HwamokExceptionTest;
import com.hwamok.product.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;

import java.util.ArrayList;
import java.util.List;

import static com.hwamok.core.exception.HwamokExceptionTest.assertThatHwamokException;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {
  @Test
  void 카테고리_생성_성공() {
    Category category = new Category("화목한쇼핑몰", "CA001", "식품", 0L, null);

    assertThat(category.getBranch()).isEqualTo("화목한쇼핑몰");
    assertThat(category.getCode()).isEqualTo("CA001");
    assertThat(category.getName()).isEqualTo("식품");
    assertThat(category.getLevel()).isEqualTo(0);
    assertThat(category.getParentCategory()).isNull();
  }

  @Test
  void 하위_카테고리_생성_성공() {
    Category parentCategory = new Category("화목한쇼핑몰", "CA001", "식품", 0L, null);

    Category subCategory = new Category("화목한쇼핑몰", "CA002", "가공식품", 1L, parentCategory);

    assertThat(subCategory.getBranch()).isEqualTo("화목한쇼핑몰");
    assertThat(subCategory.getCode()).isEqualTo("CA002");
    assertThat(subCategory.getName()).isEqualTo("가공식품");
    assertThat(subCategory.getLevel()).isEqualTo(1);
    assertThat(subCategory.getParentCategory()).isSameAs(parentCategory);
  }

  @Test
  void 부모_카테고리로_서브_카테고리들_조회() {
    Category parentCategory = new Category("화목한쇼핑몰", "CA001", "식품", 0L, null);
    Category subCategory1 = new Category("화목한쇼핑몰", "CA002", "가공식품", 1L, parentCategory);
    Category subCategory2 = new Category("화목한쇼핑몰", "CA003", "신선식품", 1L, parentCategory);

    parentCategory.getSubCategory().add(subCategory1);
    parentCategory.getSubCategory().add(subCategory2);

    assertThat(parentCategory.getSubCategory().size()).isEqualTo(2);
    assertThat(parentCategory.getSubCategory()).containsExactly(subCategory1, subCategory2);
  }

  @ParameterizedTest
  @NullAndEmptySource
  void 카테고리_생성_실패__브랜치_null_혹은_빈값(String branch) {
    assertThatIllegalArgumentException().isThrownBy(() ->
            new Category(branch, "CA001", "식품", 0L, null));
  }

  @ParameterizedTest
  @NullAndEmptySource
  void 카테고리_생성_실패__코드_null_혹은_빈값(String code) {
    assertThatIllegalArgumentException().isThrownBy(() ->
            new Category("화목한쇼핑몰", code, "식품", 0L, null));
  }

  @ParameterizedTest
  @NullAndEmptySource
  void 카테고리_생성_실패__이름_null_혹은_빈값(String name) {
    assertThatIllegalArgumentException().isThrownBy(() ->
            new Category("화목한쇼핑몰", "CA001", name, 0L, null));
  }

  @ParameterizedTest
  @NullSource
  void 카테고리_생성_실패__레벨_null_혹은_빈값(Long level) {
    assertThatIllegalArgumentException().isThrownBy(() ->
            new Category("화목한쇼핑몰", "CA001", "식품", level, null));
  }

  @Test
  void 카테고리_생성_실패__브랜치_31글자_이상() {
    String wrongBranch = "화목한쇼핑몰화목한쇼핑몰화목한쇼핑몰화목한쇼핑몰화목한쇼핑몰화";

    assertThatHwamokException(ExceptionCode.NOT_BRANCH_FORM)
            .isThrownBy(()-> new Category(wrongBranch, "CA001", "식품", 0L, null));
  }

  @Test
  void 카테고리_생성_실패__코드_7글자_이상() {
    String wrongCode = "CA77777";

    assertThatHwamokException(ExceptionCode.NOT_CODE_FORM)
            .isThrownBy(()-> new Category("화목한쇼핑몰", wrongCode, "식품", 0L, null));
  }

  @Test
  void 카테고리_생성_실패__이름_31글자_이상() {
    String wrongName = "식품식품식품식품식품식품식품식품식품식품식품식품식품식품식품식";

    assertThatHwamokException(ExceptionCode.NOT_NAME_FORM)
            .isThrownBy(()-> new Category("화목한쇼핑몰", "CA001", wrongName, 0L, null));
  }

  @Test
  void 카테고리_생성_실패__레벨_음수() {
    Long level = -1L;

    assertThatHwamokException(ExceptionCode.NOT_LEVEL_FORM)
            .isThrownBy(()-> new Category("화목한쇼핑몰", "CA001", "식품", level, null));
  }

  @Test
  void 카테고리_상품_등록_성공() {
    Category category = new Category("화목한쇼핑몰", "CA001", "식품", 0L, null);
    Product product = new Product("상품", "S001", 1000, category);
    Product product2 = new Product("상품", "S002", 1000, category);

    category.registerProduct(product);
    category.registerProduct(product2);

    assertThat(category.getProducts()).contains(product);
    assertThat(category.getProducts()).contains(product2);
  }

  @Test
  void 카테고리_상품_업데이트_성공() {
    Category category = new Category("화목한쇼핑몰", "CA001", "식품", 0L, null);

    category.updateCategory("육류", "CA123", "소고기");

    assertThat(category.getBranch()).isEqualTo("육류");
    assertThat(category.getCode()).isEqualTo("CA123");
    assertThat(category.getName()).isEqualTo("소고기");
  }

  @ParameterizedTest
  @NullAndEmptySource
  void 카테고리_상품_업데이트_실패__브랜치_null_혹은_빈값(String branch) {
    Category category = new Category("화목한쇼핑몰", "CA001", "식품", 0L, null);

    assertThatIllegalArgumentException().isThrownBy(() ->
            category.updateCategory(branch, "S001", "소고기"));
  }

  @ParameterizedTest
  @NullAndEmptySource
  void 카테고리_상품_업데이트_실패__code_null_혹은_빈값(String code) {
    Category category = new Category("화목한쇼핑몰", "CA001", "식품", 0L, null);

    assertThatIllegalArgumentException().isThrownBy(() ->
            category.updateCategory("육류", code, "소고기"));
  }

  @ParameterizedTest
  @NullAndEmptySource
  void 카테고리_상품_업데이트_실패__이름_null_혹은_빈값(String name) {
    Category category = new Category("화목한쇼핑몰", "CA001", "식품", 0L, null);

    assertThatIllegalArgumentException().isThrownBy(() ->
            category.updateCategory("육류", "S001", name));
  }

  @Test
  void 카테고리_수정_실패__브랜치_31글자_이상() {
    Category category = new Category("화목한쇼핑몰", "CA001", "식품", 0L, null);

    String wrongBranch = "화목한쇼핑몰화목한쇼핑몰화목한쇼핑몰화목한쇼핑몰화목한쇼핑몰화";

    assertThatHwamokException(ExceptionCode.NOT_BRANCH_FORM)
            .isThrownBy(()-> category.updateCategory(wrongBranch, "S001", "소고기"));
  }

  @Test
  void 카테고리_수정_실패__코드_7글자_이상() {
    Category category = new Category("화목한쇼핑몰", "CA001", "식품", 0L, null);
    String wrongCode = "CA77777";

    assertThatHwamokException(ExceptionCode.NOT_CODE_FORM)
            .isThrownBy(()-> category.updateCategory("육류", wrongCode, "소고기"));
  }

  @Test
  void 카테고리_수정_실패__이름_31글자_이상() {
    Category category = new Category("화목한쇼핑몰", "CA001", "식품", 0L, null);
    String wrongName = "식품식품식품식품식품식품식품식품식품식품식품식품식품식품식품식";

    assertThatHwamokException(ExceptionCode.NOT_NAME_FORM)
            .isThrownBy(()-> category.updateCategory("육류", "S001", wrongName));
  }

  @Test
  void 카테고리_삭제_성공() {
    Category category = new Category("화목한쇼핑몰", "CA001", "식품", 0L, null);

    category.deleteCategory();

    assertThat(category.getStatus()).isEqualTo(CategoryStatus.INACTIVATE);
  }
}