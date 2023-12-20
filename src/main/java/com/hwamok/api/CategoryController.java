package com.hwamok.api;

import com.hwamok.api.dto.category.CategoryCreateDto;
import com.hwamok.api.dto.category.CategoryReadDto;
import com.hwamok.api.dto.category.CategoryUpdateDto;
import com.hwamok.category.domain.Category;
import com.hwamok.category.service.CategoryService;
import com.hwamok.core.response.ApiResult;
import com.hwamok.core.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResult<?>> createCategory(@RequestBody CategoryCreateDto.Request request) {
        categoryService.create(request.getBranch(), request.getCode(), request.getName(), request.getParentId());

        return Result.created();
    }

    @GetMapping("/branch")
    public ResponseEntity<ApiResult<List<Category>>> getAllBranchCategory(@RequestParam("branch")String branch) {
        List<Category> categoryList = categoryService.getAll(branch);

        return Result.ok(categoryList);
    }

    @GetMapping("/name")
    public ResponseEntity<ApiResult<CategoryReadDto.Response>> getCategoryByName(@RequestParam("name")String name) {
        Category category = categoryService.getOneByName(name);

        CategoryReadDto.Response response = new CategoryReadDto.Response(category);

        return Result.ok(response);
    }

    @GetMapping("/code")
    public ResponseEntity<ApiResult<CategoryReadDto.Response>> getCategory(@RequestParam("code")String code) {
        Category category = categoryService.getOneByCode(code);

        CategoryReadDto.Response response = new CategoryReadDto.Response(category);

        return Result.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResult<?>> updateCategory(@PathVariable("id")Long id, @RequestBody CategoryUpdateDto.Request request) {
        categoryService.update(id, request.getBranch(), request.getCode(), request.getName());

        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<?>> deleteCategory(@PathVariable("id")Long id) {
        categoryService.delete(id);

        return Result.ok();
    }
}
