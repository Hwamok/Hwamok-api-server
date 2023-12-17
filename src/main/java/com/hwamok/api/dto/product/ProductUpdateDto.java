package com.hwamok.api.dto.product;

import com.hwamok.category.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProductUpdateDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Request {
        private String name;

        private Integer price;

        private String code;

        private Category category;
    }
}
