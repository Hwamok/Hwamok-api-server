package com.hwamok.api;

import com.hwamok.api.dto.admin.AdminReadDto;
import com.hwamok.api.dto.category.CategoryCreateDTO;
import com.hwamok.api.dto.category.CategoryReadDTO;
import com.hwamok.api.dto.category.CategoryUpdateDTO;
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
    public ResponseEntity<ApiResult<?>> createCategory(@RequestBody CategoryCreateDTO.Request request) {
        categoryService.create(request.getBranch(), request.getCode(), request.getName(), request.getParentId());

        return Result.created();
    }

    @GetMapping("/branch")
    public ResponseEntity<ApiResult<List<Category>>> getAllBranchCategory(@RequestParam("branch")String branch) {
        List<Category> categoryList = categoryService.getAll(branch);

        return Result.ok(categoryList);
    }

    @GetMapping("/name")
    public ResponseEntity<ApiResult<CategoryReadDTO.Response>> getCategoryByName(@RequestParam("name")String name) {
        Category category = categoryService.getOneByName(name);

        CategoryReadDTO.Response response = CategoryReadDTO.Response.builder()
                .branch(category.getBranch())
                .level(category.getLevel())
                .name(category.getName())
                .code(category.getCode())
                .parentId(category.getParentId())
                .build();

        return Result.ok(response);
    }

    @GetMapping("/code")
    public ResponseEntity<ApiResult<CategoryReadDTO.Response>> getCategory(@RequestParam("code")String code) {
        Category category = categoryService.getOneByCode(code);

        CategoryReadDTO.Response response = new CategoryReadDTO.Response(category.getBranch(), category.getCode(),
                category.getName(), category.getLevel() , category.getParentId());

        return Result.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResult<?>> updateCategory(@PathVariable("id")Long id, @RequestBody CategoryUpdateDTO.Request request) {
        categoryService.update(id, request.getBranch(), request.getCode(), request.getName());

        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<?>> deleteCategory(@PathVariable("id")Long id) {
        categoryService.delete(id);

        return Result.ok();
    }
}