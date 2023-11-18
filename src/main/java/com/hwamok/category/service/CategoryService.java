package com.hwamok.category.service;


import com.hwamok.category.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    public Category saveCategory(String branch, String code, int level, String name, Category parent);

    public List<Category> getAllByBranch (String branch);

    public List<Category> getAllByCodeType (String codeType);

    public Category getCategoryByCode (String code);

    public List<Category> getAllByName (String name);
}
