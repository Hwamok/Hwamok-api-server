package com.hwamok.api;

import com.hwamok.api.dto.product.ProductCreateDto;
import com.hwamok.api.dto.product.ProductReadDto;
import com.hwamok.core.exception.ApiExceptionHandler;
import com.hwamok.core.response.ApiResult;
import com.hwamok.core.response.Result;
import com.hwamok.product.domain.Product;
import com.hwamok.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

//    @GetMapping("/{id}")
//    public ResponseEntity<ApiResult<ProductReadDto.Response>> getOneBy
}
