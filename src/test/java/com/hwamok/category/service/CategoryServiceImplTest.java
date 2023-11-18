package com.hwamok.category.service;

import com.hwamok.category.domain.Category;
import com.hwamok.category.domain.CategoryRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Transactional
class CategoryServiceImplTest {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    // TODO: 2023-11-19 테스트가 아얘 안돌아가는데 뭐가 문제일까요... 내일 함 찾아볼게요...
    @Test
    void 카테고리_추가_성공() {
        Category rootCategory = new Category("화목한스프링", "RT000", "ROOT", 0, null);

        Category category = categoryService.saveCategory("화목한스프링", "CA001", 1, "식품", null);

        assertThat(category.getBranch()).isEqualTo("화목한스프링");
        assertThat(category.getCode()).isEqualTo("CA001");
        assertThat(category.getLevel()).isEqualTo(1);
        assertThat(category.getName()).isEqualTo("식품");
        assertThat(category.getName()).isEqualTo("식품");
        assertThat(category.getParentCategory()).isSameAs(rootCategory);
    }
}