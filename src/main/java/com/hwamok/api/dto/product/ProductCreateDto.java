package com.hwamok.api.dto.product;

import com.hwamok.category.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProductCreateDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String name;
        private String code;
        private Integer price;
        private Category category;
    }
}
