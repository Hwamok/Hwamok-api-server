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
    public Category create(String branch, String code, String name, long parentId) {
        return categoryRepository.findById(parentId)
                .map(parentCategory -> createSubCategory(branch, code, name, parentCategory))
                .orElseGet(() -> createRootAndSubCategory(branch, code, name));
    }

    @Override
    public List<Category> getAll(String branch) {
        return categoryRepository.findAllByBranchAndStatus(branch,CategoryStatus.ACTIVATE);
    }

    // TODO: 2023-11-19 도메인 수정 후 추가 
//    @Override
//    public List<Category> getAllByCodeType(String codeType) {
//        return categoryRepository.findAllByCodeTypeAndStatus(codeType, CategoryStatus.ACTIVATE);
//    }

    @Override
    public Category getOneByCode(String code) {
        return categoryRepository.findByCodeAndStatus(code, CategoryStatus.ACTIVATE)
                .orElseThrow(() -> new HwamokException(ExceptionCode.NOT_FOUND_CATEGORY));
    }

    @Override
    public Category getOneByName(String name) {
        return categoryRepository.findByNameAndStatus(name, CategoryStatus.ACTIVATE)
                .orElseThrow(() -> new HwamokException(ExceptionCode.NOT_FOUND_CATEGORY));
    }

    @Override
    public void update(Long id, String branch, String code, String name) {
        Category category = categoryRepository.findByIdAndStatus(id, CategoryStatus.ACTIVATE)
                .orElseThrow(() -> new HwamokException(ExceptionCode.NOT_FOUND_CATEGORY));

        category.updateCategory(branch, code, name);
    }

    @Override
    public void delete(Long id) {
        Category category = categoryRepository.findByIdAndStatus(id, CategoryStatus.ACTIVATE)
                .orElseThrow(() -> new HwamokException(ExceptionCode.NOT_FOUND_CATEGORY));

        category.deleteCategory();
    }

    private Category createSubCategory(String branch, String code, String name, Category parentCategory) {
        Category subCategory = new Category(branch, code, name, parentCategory.getLevel() + 1, parentCategory);
        return categoryRepository.save(subCategory);
    }

    private Category createRootAndSubCategory(String branch, String code, String name) {
        Category rootCategory = categoryRepository.save(new Category(branch, "RT000", "ROOT", 0L, null));
        return createSubCategory(branch, code, name, rootCategory);
    }

}
