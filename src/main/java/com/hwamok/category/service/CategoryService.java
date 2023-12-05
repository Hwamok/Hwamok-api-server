package com.hwamok.category.service;


import com.hwamok.category.domain.Category;

import java.util.List;

public interface CategoryService {
    Category create(String branch, String code, String name, long parentId);

    List<Category> getAll (String branch);

    // TODO: 2023-11-19 도메인 수정 후 추가 
//    public List<Category> getAllByCodeType (String codeType);

    Category getOneByCode (String code);

    Category getOneByName (String name);

    void update(Long id, String branch, String code, String name);

    void delete(Long id);
}
