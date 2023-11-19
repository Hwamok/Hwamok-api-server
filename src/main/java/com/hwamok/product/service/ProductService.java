package com.hwamok.product.service;

import com.hwamok.category.domain.Category;
import com.hwamok.product.domain.Product;

import java.util.Optional;

public interface ProductService {
    Product createProduct(String name, String code, int price, Category category);

    Product getProductByName(String name);

    Product getProductByCode(String code);

    Product updateProduct(Long id, String name, String code, int price, Category category);

    void deleteProduct(Long id);

}
