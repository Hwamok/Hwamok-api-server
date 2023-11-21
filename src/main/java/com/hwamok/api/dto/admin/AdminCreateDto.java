package com.hwamok.api.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AdminCreateDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String loginId;
        private String password;
        private String name;
        private String email;
    }
}
