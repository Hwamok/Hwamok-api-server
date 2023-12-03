package com.hwamok.category.service;

import com.hwamok.category.domain.Category;
import com.hwamok.category.domain.CategoryRepository;
import com.hwamok.category.domain.CategoryStatus;
import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.core.exception.HwamokExceptionTest;
import fixture.CategoryFixture;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.hwamok.core.exception.HwamokExceptionTest.assertThatHwamokException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Transactional
class CategoryServiceImplTest {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category1;

    private Category category2;

    @BeforeEach
    private void setup(){
        category1 = categoryRepository.save(CategoryFixture.createCategory());

        category2 = categoryRepository.save(CategoryFixture.createCategory(category1));
    }

    @Test
    void 카테고리_추가_성공() {
        Category category = categoryService.create("식품", "CA003", "과자", null);
        Category rootCategory = categoryRepository.findByBranchAndName("식품", "root")
                .orElseThrow(() -> new RuntimeException());

        assertThat(category.getBranch()).isEqualTo("식품");
        assertThat(category.getCode()).isEqualTo("CA003");
        assertThat(category.getLevel()).isEqualTo(1);
        assertThat(category.getName()).isEqualTo("과자");
        assertThat(category.getParentCategory()).isSameAs(rootCategory);
    }

    @Test
    void 카테고리_추가_성공__부모_추가() {
        Category parentCategory = new Category("식품", "CA005", "식혜", 3L, null);
        categoryRepository.save(parentCategory);
        Category category = categoryService.create("식품", "CA001", "수정과", parentCategory.getId());

        assertThat(category.getBranch()).isEqualTo("식품");
        assertThat(category.getCode()).isEqualTo("CA001");
        assertThat(category.getLevel()).isEqualTo(4);
        assertThat(category.getName()).isEqualTo("수정과");
        assertThat(category.getParentCategory()).isSameAs(parentCategory);
    }

    @Test
    void 브랜치로_모든_카테고리_조회하기_성공() {
        List<Category> categoryList = categoryService.getAll("식품");


        assertThat(categoryList.size()).isEqualTo(2);
        assertThat(categoryList.get(0).getName()).isEqualTo("돼지고기");
        assertThat(categoryList.get(1).getName()).isEqualTo("소고기");
    }

    @Test
    void 브랜치로_모든_카테고리_조회하기_실패_삭제된_카테고리_조회() {
        category1.deleteCategory();
        category2.deleteCategory();

        List<Category> categoryList = categoryService.getAll("식품");

        assertThat(categoryList.size()).isEqualTo(0);
    }

    @Test
    void 코드로_카테고리_조회하기_성공() {
        Category category = categoryService.getOneByCode("CA001");

        assertThat(category.getBranch()).isEqualTo("식품");
        assertThat(category.getCode()).isEqualTo("CA001");
        assertThat(category.getLevel()).isEqualTo(0);
        assertThat(category.getName()).isEqualTo("돼지고기");
        assertThat(category.getParentCategory()).isNull();
    }

    @Test
    void 코드로_카테고리_조회하기_실패__존재하지_않는_코드() {
        assertThatHwamokException(ExceptionCode.NOT_FOUND_CATEGORY)
                .isThrownBy(() -> categoryService.getOneByName("fa001"));
    }

    @Test
    void 코드로_카테고리_조회하기_실패__삭제된_카테고리_조회() {
        category1.deleteCategory();

        assertThatHwamokException(ExceptionCode.NOT_FOUND_CATEGORY)
                .isThrownBy(() -> categoryService.getOneByCode("CA001"));
    }

    @Test
    void 이름으로_카테고리_조회하기_성공() {
        Category category = categoryService.getOneByName("돼지고기");

        assertThat(category.getCode()).isEqualTo("CA001");
    }

    @Test
    void 이름으로_카테고리_조회하기_실패__삭제된_카테고리() {
        category1.deleteCategory();

        assertThatHwamokException(ExceptionCode.NOT_FOUND_CATEGORY)
                .isThrownBy(() -> categoryService.getOneByName("돼지고기"));
    }

    @Test
    void 이름으로_카테고리_조회하기_실패__없는_이름() {
        assertThatHwamokException(ExceptionCode.NOT_FOUND_CATEGORY)
                .isThrownBy(() -> categoryService.getOneByName("없는이름"));
    }

    @Test
    void 카테고리_업데이트_성공() {
        Long id = category1.getId();

        categoryService.update(id, "가구", "C002", "의자");

        assertThat(category1.getBranch()).isEqualTo("가구");
        assertThat(category1.getCode()).isEqualTo("C002");
        assertThat(category1.getName()).isEqualTo("의자");
    }

    @Test
    void 카테고리_업데이트_실패_없는_id() {
        assertThatHwamokException(ExceptionCode.NOT_FOUND_CATEGORY)
                .isThrownBy(() -> categoryService.update(-1L, "가구", "C002", "의자"));
    }

    @Test
    void 카테고리_삭제_성공() {
        Long categoryId = category1.getId();

        categoryService.delete(categoryId);

        assertThat(category1.getStatus()).isEqualTo(CategoryStatus.INACTIVATE);
    }

    @Test
    void 카테고리_삭제_실패__없는_Id() {
        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_FOUND_CATEGORY)
                .isThrownBy(() -> categoryService.delete(-1L));
    }
}