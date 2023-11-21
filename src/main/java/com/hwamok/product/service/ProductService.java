package com.hwamok.product.service;

import com.hwamok.category.domain.Category;
import com.hwamok.product.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Product createProduct(String name, String code, Integer price, Category category);

    List<Product> getProductByName(String name);

    Product getProductByCode(String code);

    Product updateProduct(Long id, String name, String code, Integer price, Category category);

    void deleteProduct(Long id);
}
