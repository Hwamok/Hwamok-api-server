package com.hwamok.category.service;

import com.hwamok.category.domain.Category;
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
    public Category create(String branch, String code, String name, Category parent) {
        Category category = new Category(branch, code, name, parent);

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
                                    .level(0L)
                                    .build()
                    );
            if (!categoryRepository.existsByBranchAndName(branch, "ROOT")) {
                categoryRepository.save(rootCategory);
            }
            category.registerParentCategory(rootCategory);
            category.registerLevel(1L);

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

    @Override
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new HwamokException(ExceptionCode.NOT_FOUND_CATEGORY));
        category.deleteCategory();
    }
}