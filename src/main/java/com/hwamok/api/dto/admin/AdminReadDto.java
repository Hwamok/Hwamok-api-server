package com.hwamok.api.dto.admin;

import com.hwamok.admin.domain.AdminStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

public class AdminReadDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private Instant createdAt;
        private String loginId;
        private String name;
        private String email;
        private AdminStatus status;
    }
}
