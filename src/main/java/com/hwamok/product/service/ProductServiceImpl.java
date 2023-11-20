package com.hwamok.product.service;

import com.hwamok.category.domain.Category;
import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.core.exception.HwamokException;
import com.hwamok.product.domain.Product;
import com.hwamok.product.domain.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;

    @Override
    public Product createProduct(String name, String code, Integer price, Category category) {
        return productRepository.save( new Product(name, code, price, category));
    }

    @Override
    public Product getProductByName(String name) {
        return productRepository.findProductByName(name).orElseThrow(()->
                new HwamokException(ExceptionCode.NOT_FOUND_PRODUCT));
    }

    @Override
    public Product getProductByCode(String code) {
        return productRepository.findProductByCode(code).orElseThrow(()->
                new HwamokException(ExceptionCode.NOT_FOUND_PRODUCT));
    }

    @Override
    public Product updateProduct(Long id, String name, String code, Integer price, Category category) {
        Product product = productRepository.findProductById(id).orElseThrow(() ->
                new HwamokException(ExceptionCode.NOT_FOUND_PRODUCT));
        product.updateProduct(name, code, price, category);
        return product;
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findProductById(id).orElseThrow(() ->
                new HwamokException(ExceptionCode.NOT_FOUND_PRODUCT));
        product.deleteProduct();
    }
}
