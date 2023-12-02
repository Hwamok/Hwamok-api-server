package com.hwamok.category.domain;

import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.core.exception.HwamokException;
import com.hwamok.core.exception.HwamokExceptionTest;
import fixture.CategoryFixture;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static com.hwamok.core.exception.HwamokExceptionTest.assertThatHwamokException;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;

    private Category category2;

    @BeforeEach
    private void setup(){
        category = categoryRepository.save(CategoryFixture.createCategory());
        category2 = categoryRepository.save(CategoryFixture.createCategory(category));
    }

    @Test
    void 이름_및_브랜치로_카테고리_찾기_성공() {
        Category foundCategory = categoryRepository.findByBranchAndName("식품", "소고기")
                .orElseThrow(() -> new HwamokException(ExceptionCode.NOT_FOUND_CATEGORY));

        assertThat(foundCategory.getBranch()).isEqualTo("식품");
        assertThat(foundCategory.getCode()).isEqualTo("CA002");
        assertThat(foundCategory.getName()).isEqualTo("소고기");
        assertThat(foundCategory.getLevel()).isEqualTo(0);
        assertThat(foundCategory.getId()).isNotNull();
        assertThat(foundCategory.getParentCategory()).isSameAs(category);
    }

    @Test
    void 이름_및_브랜치로_존재_유무_확인_성공() {
        Boolean exist = categoryRepository.existsByBranchAndName("식품", "소고기");

        assertThat(exist).isEqualTo(true);
    }

    @Test
    void 이름_및_브랜치로_존재_유무_확인_실패__없는_이름() {
        Boolean exist = categoryRepository.existsByBranchAndName("식품", "식혜");

        assertThat(exist).isEqualTo(false);
    }

    @Test
    void 브랜치_및_상태로_카테고리_찾기_성공() {
        Category anotherBranchCategory = CategoryFixture.createCategory("newBranch");
        categoryRepository.save(anotherBranchCategory);

        List<Category> allCategory = categoryRepository.findAllByBranchAndStatus("식품", CategoryStatus.ACTIVATE);

        assertThat(allCategory.size()).isEqualTo(2);
        assertThat(allCategory.get(0).getCode()).isEqualTo("CA001");
    }

    @Test
    void 코드와_상태로_찾기_성공() {
        Category foundCategory = categoryRepository.findByCodeAndStatus("CA001", CategoryStatus.ACTIVATE)
                .orElseThrow(() -> new HwamokException(ExceptionCode.NOT_FOUND_CATEGORY));

        assertThat(foundCategory.getBranch()).isEqualTo("식품");
        assertThat(foundCategory.getCode()).isEqualTo("CA001");
        assertThat(foundCategory.getName()).isEqualTo("돼지고기");
        assertThat(foundCategory.getLevel()).isEqualTo(0);
        assertThat(foundCategory.getId()).isNotNull();
    }

    @Test
    void 이름과_상태로_찾기_성공() {
        Category category3 = CategoryFixture.createCategory("newBranch");
        categoryRepository.save(category3);

        Category category = categoryRepository.findByNameAndStatus("돼지고기", CategoryStatus.ACTIVATE)
                .orElseThrow(() -> new HwamokException(ExceptionCode.NOT_FOUND_CATEGORY));

        assertThat(category.getCode()).isEqualTo("CA001");
    }
}