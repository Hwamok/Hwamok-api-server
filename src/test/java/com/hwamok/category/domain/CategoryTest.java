package com.hwamok.category.domain;

import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.core.exception.HwamokExceptionTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;

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
    Assertions.assertThatIllegalArgumentException().isThrownBy(() ->
            new Category(branch, "CA001", "식품", 0L, null));
  }

  @ParameterizedTest
  @NullAndEmptySource
  void 카테고리_생성_실패__코드_null_혹은_빈값(String code) {
    Assertions.assertThatIllegalArgumentException().isThrownBy(() ->
            new Category("화목한쇼핑몰", code, "식품", 0L, null));
  }

  @ParameterizedTest
  @NullAndEmptySource
  void 카테고리_생성_실패__이름_null_혹은_빈값(String name) {
    Assertions.assertThatIllegalArgumentException().isThrownBy(() ->
            new Category("화목한쇼핑몰", "CA001", name, 0L, null));
  }

  @ParameterizedTest
  @NullSource
  void 카테고리_생성_실패__레벨_null_혹은_빈값(Long level) {
    Assertions.assertThatIllegalArgumentException().isThrownBy(() ->
            new Category("화목한쇼핑몰", "CA001", "식품", level, null));
  }

  @Test
  void 카테고리_생성_실패__브랜치_31글자_이상() {
    String wrongBranch = "화목한쇼핑몰화목한쇼핑몰화목한쇼핑몰화목한쇼핑몰화목한쇼핑몰화";

    HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_BRANCH_FORM)
            .isThrownBy(()-> new Category(wrongBranch, "CA001", "식품", 0L, null));
  }

  @Test
  void 카테고리_생성_실패__코드_7글자_이상() {
    String wrongCode = "CA77777";

    HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_CODE_FORM)
            .isThrownBy(()-> new Category("화목한쇼핑몰", wrongCode, "식품", 0L, null));
  }

  @Test
  void 카테고리_생성_실패__이름_31글자_이상() {
    String wrongName = "식품식품식품식품식품식품식품식품식품식품식품식품식품식품식품식";

    HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_NAME_FORM)
            .isThrownBy(()-> new Category("화목한쇼핑몰", "CA001", wrongName, 0L, null));
  }

  @Test
  void 카테고리_생성_실패__레벨_음수() {
    Long level = -1L;

    HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_LEVEL_FORM)
            .isThrownBy(()-> new Category("화목한쇼핑몰", "CA001", "식품", level, null));
  }
}