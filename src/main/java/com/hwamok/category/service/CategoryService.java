package com.hwamok.category.service;


import com.hwamok.category.domain.Category;

import java.util.List;

public interface CategoryService {
    public Category create(String branch, String code, String name, Category parent);

    public List<Category> getAllByBranch (String branch);

    // TODO: 2023-11-19 도메인 수정 후 추가 
//    public List<Category> getAllByCodeType (String codeType);

    public Category getCategoryByCode (String code);

    public List<Category> getAllByName (String name);

    public void delete(Long id);
}
