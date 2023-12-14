package com.hwamok.api.dto.category;

import com.hwamok.category.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CategoryReadDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String branch;
        private String code;
        private String name;
        private Long level;
        private Long parentId;

        public Response(Category category) {
            this.branch = category.getBranch();
            this.code = category.getCode();
            this.name = category.getName();
            this.level = category.getLevel();
            this.parentId = category.getParentId();
        }
    }
}
