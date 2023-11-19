package com.hwamok.category.service;


import java.util.List;

public interface CategoryService {
    public Category saveCategory(String branch, String code, String name, Integer level, Category parent);

    public List<Category> getAllByBranch (String branch);

    // TODO: 2023-11-19 도메인 수정 후 추가 
//    public List<Category> getAllByCodeType (String codeType);

    public Category getCategoryByCode (String code);

    public List<Category> getAllByName (String name);
}
