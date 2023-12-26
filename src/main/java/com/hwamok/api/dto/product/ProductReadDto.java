package com.hwamok.api.dto.product;

import com.hwamok.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductReadDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String name;
        private String code;
        private Integer price;
        private Long categoryId;

        public Response (Product product) {
            this.name = product.getName();
            this.code = product.getCode();
            this.price = product.getPrice();
            this.categoryId = product.getCategory().getId();
        }
    }
}
