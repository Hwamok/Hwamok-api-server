package com.hwamok.category.service;

import com.hwamok.category.domain.CategoryRepository;
import fixture.CategoryFixture;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        Category category = categoryService.saveCategory("화목한스프링", "CA003", "식품", 0,null);
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
        Category category = categoryService.saveCategory("화목한스프링", "CA001", "식품", 0,category1);

        assertThat(category.getBranch()).isEqualTo("화목한스프링");
        assertThat(category.getCode()).isEqualTo("CA001");
        assertThat(category.getLevel()).isEqualTo(1);
        assertThat(category.getName()).isEqualTo("식품");
        assertThat(category.getName()).isEqualTo("식품");
        assertThat(category.getParentCategory()).isSameAs(null);
    }

    @Test
    void 카테고리_추가_실패__branch_없음() {
    }
}