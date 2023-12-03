package com.hwamok.api.dto.category;

import com.hwamok.category.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CategoryReadDTO {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String branch;
        private String code;
        private String name;
        private Long level;
        private Long parentId;
    }
}
