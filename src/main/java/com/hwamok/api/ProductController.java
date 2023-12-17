package com.hwamok.api;

import com.hwamok.api.dto.product.ProductCreateDto;
import com.hwamok.api.dto.product.ProductReadDto;
import com.hwamok.api.dto.product.ProductUpdateDto;
import com.hwamok.core.exception.ApiExceptionHandler;
import com.hwamok.core.response.ApiResult;
import com.hwamok.core.response.Result;
import com.hwamok.product.domain.Product;
import com.hwamok.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResult<?>> create(@RequestBody ProductCreateDto.Request request) {
        productService.create(request.getName(), request.getCode(), request.getPrice(), request.getCategory());

        return Result.created();
    }

    @GetMapping("/name")
    public ResponseEntity<ApiResult<List<ProductReadDto.Response>>> getOneByName(@RequestParam("name")String name) {
        List<Product> productList = productService.getProductByName(name);

        List<ProductReadDto.Response> dtoList = ProductReadDto.Response.createDtoList(productList);

        return Result.ok(dtoList);
    }

    @GetMapping("/code")
    public ResponseEntity<ApiResult<ProductReadDto.Response>> getOneByCode(@RequestParam("code")String code) {
        Product product = productService.getProductByCode(code);

        ProductReadDto.Response response = new ProductReadDto.Response(product);

        return Result.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResult<?>> update(@PathVariable("id")Long id, @RequestBody ProductUpdateDto.Request request) {
        productService.update(id, request.getName(), request.getCode(), request.getPrice(), request.getCategory());

        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<?>> delete(@PathVariable("id")Long id) {
        productService.delete(id);

        return Result.ok();
    }
}
