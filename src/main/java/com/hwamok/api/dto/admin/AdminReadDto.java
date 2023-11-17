package com.hwamok.api.dto.admin;

import com.hwamok.admin.domain.Admin;
import com.hwamok.admin.domain.AdminStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

public class AdminReadDto {
    @Getter
    @NoArgsConstructor
    public static class Response {
        private Long id;
        private Instant createdAt;
        private String loginId;
        private String name;
        private String email;
        private AdminStatus status;

        public Response(Long id, Instant createdAt, String loginId, String name, String email, AdminStatus status) {
            this.id = id;
            this.createdAt = createdAt;
            this.loginId = loginId;
            this.name = name;
            this.email = email;
            this.status = status;
        }

        public Response(Admin admin) {
            this.id = admin.getId();
            this.createdAt = admin.getCreatedAt();
            this.loginId = admin.getLoginId();
            this.name = admin.getName();
            this.email = admin.getEmail();
            this.status = admin.getStatus();
        }


    }


}
