package com.hwamok.api.dto.admin;

import com.hwamok.admin.domain.Admin;
import com.hwamok.admin.domain.AdminStatus;
import com.hwamok.admin.domain.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

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
        private List<Role> roles;

        public Response(Admin admin) {
            this.id = admin.getId();
            this.createdAt = admin.getCreatedAt();
            this.loginId = admin.getLoginId();
            this.name = admin.getName();
            this.email = admin.getEmail();
            this.status = admin.getStatus();
            this.roles = admin.getRoles();
        }
    }
}
