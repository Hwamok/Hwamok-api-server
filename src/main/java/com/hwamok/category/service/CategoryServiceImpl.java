package com.hwamok.category.service;

import com.hwamok.category.domain.CategoryRepository;
import com.hwamok.category.domain.CategoryStatus;
import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.core.exception.HwamokException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;

    @Override
    public Category saveCategory(String branch, String code, String name, Integer level, Category parent) {
        Category category = Category.builder()
                .branch(branch)
                .code(code)
                .name(name)
                .build();

        if (parent == null) {
            if (categoryRepository.existsByBranchAndName(branch, name)) {
                throw new RuntimeException();
            }
            Category rootCategory = categoryRepository.findByBranchAndName(branch,"ROOT")
                    .orElseGet( () ->
                            Category.builder()
                                    .name("ROOT")
                                    .code("RT000")
                                    .branch(branch)
                                    .level(0)
                                    .build()
                    );
            if (!categoryRepository.existsByBranchAndName(branch, "ROOT")) {
                categoryRepository.save(rootCategory);
            }
            category.registerParentCategory(rootCategory);
            category.registerLevel(1);

        } else {
            category.registerLevel(parent.getLevel() + 1);
            category.registerParentCategory(parent);
            parent.getSubCategory().add(category);
        }

        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllByBranch(String branch) {
        return categoryRepository.findAllByBranchAndStatus(branch,CategoryStatus.ACTIVATE);
    }

    // TODO: 2023-11-19 도메인 수정 후 추가 
//    @Override
//    public List<Category> getAllByCodeType(String codeType) {
//        return categoryRepository.findAllByCodeTypeAndStatus(codeType, CategoryStatus.ACTIVATE);
//    }

    @Override
    public Category getCategoryByCode(String code) {
        return categoryRepository.findByCodeAndStatus(code, CategoryStatus.ACTIVATE)
                .orElseThrow(() -> new HwamokException(ExceptionCode.NOT_FOUND_CATEGORY));
    }

    @Override
    public List<Category> getAllByName(String name) {
        return categoryRepository.findAllByNameAndStatus(name, CategoryStatus.ACTIVATE);
    }

}
