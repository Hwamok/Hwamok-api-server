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
        Category category = categoryService.create("화목한스프링", "CA003", "식품", null);
        Category rootCategory = categoryRepository.findByBranchAndName("화목한스프링", "root")
                .orElseThrow(() -> new RuntimeException());

        assertThat(category.getBranch()).isEqualTo("화목한스프링");
        assertThat(category.getCode()).isEqualTo("CA003");
        assertThat(category.getLevel()).isEqualTo(1);
        assertThat(category.getName()).isEqualTo("식품");
        assertThat(category.getParentCategory()).isSameAs(rootCategory);
    }

    @Test
    void 카테고리_추가_성공__부모_추가() {
        Category parentCategory = new Category("화목한스프링", "CA005", "과자", 3L, null);
        Category category = categoryService.create("화목한스프링", "CA001", "식품", parentCategory);

        assertThat(category.getBranch()).isEqualTo("화목한스프링");
        assertThat(category.getCode()).isEqualTo("CA001");
        assertThat(category.getLevel()).isEqualTo(4);
        assertThat(category.getName()).isEqualTo("식품");
        assertThat(category.getParentCategory()).isSameAs(parentCategory);
    }

    @Test
    void 브랜치로_모든_카테고리_조회하기_성공() {
        List<Category> categoryList = categoryService.getAllByBranch("화목한쇼핑몰");


        assertThat(categoryList.size()).isEqualTo(2);
        assertThat(categoryList.get(0).getName()).isEqualTo("식품");
        assertThat(categoryList.get(1).getName()).isEqualTo("의류");
    }

    @Test
    void 브랜치로_모든_카테고리_조회하기_실패_삭제된_카테고리_조회() {
        category1.deleteCategory();
        category2.deleteCategory();

        List<Category> categoryList = categoryService.getAllByBranch("화목한쇼핑몰");

        assertThat(categoryList.size()).isEqualTo(0);
    }

    @Test
    void 코드로_카테고리_조회하기_성공() {
        Category category = categoryService.getCategoryByCode("CA001");

        assertThat(category.getBranch()).isEqualTo("화목한쇼핑몰");
        assertThat(category.getCode()).isEqualTo("CA001");
        assertThat(category.getLevel()).isEqualTo(0);
        assertThat(category.getName()).isEqualTo("식품");
        assertThat(category.getParentCategory()).isNull();
    }

    @Test
    void 코드로_카테고리_조회하기_실패__존재하지_않는_코드() {
        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_FOUND_CATEGORY)
                .isThrownBy(() -> categoryService.getCategoryByCode("fa001"));
    }

    @Test
    void 코드로_카테고리_조회하기_실패__삭제된_카테고리_조회() {
        category1.deleteCategory();

        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_FOUND_CATEGORY)
                .isThrownBy(() -> categoryService.getCategoryByCode("CA001"));
    }

    @Test
    void 이름으로_모든_카테고리_조회하기_성공() {
        Category category3 = CategoryFixture.createCategory("화목한스프링");
        categoryRepository.save(category3);

        List<Category> categoryList = categoryService.getAllByName("식품");

        assertThat(categoryList.size()).isEqualTo(2);
        assertThat(categoryList.get(0).getCode()).isEqualTo("CA001");
        assertThat(categoryList.get(1).getCode()).isEqualTo("CA003");
    }

    @Test
    void 이름으로_모든_카테고리_조회하기_성공__삭제된_카테고리() {
        category1.deleteCategory();

        List<Category> categoryList = categoryService.getAllByName("식품");

        assertThat(categoryList.size()).isEqualTo(0);
    }

    @Test
    void 이름으로_모든_카테고리_조회하기_성공__없는_이름() {
        List<Category> categoryList = categoryService.getAllByName("없는이름");

        assertThat(categoryList.size()).isEqualTo(0);
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