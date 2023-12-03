package com.hwamok.category.service;


import com.hwamok.category.domain.Category;

import java.util.List;

public interface CategoryService {
    public Category create(String branch, String code, String name, Long parentId);

    public List<Category> getAll (String branch);

    // TODO: 2023-11-19 도메인 수정 후 추가 
//    public List<Category> getAllByCodeType (String codeType);

    public Category getOneByCode (String code);

    public Category getOneByName (String name);

    public void update(Long id, String branch, String code, String name);

    public void delete(Long id);
}
