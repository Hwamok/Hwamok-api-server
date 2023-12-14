package com.hwamok.api.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProductReadDto {
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Response {
        private String name;

        private String code;

        private Integer price;

        private Long categoryId;


    }
}
