package com.hwamok.api.dto.admin;

import com.hwamok.utils.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class AdminCreateDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String loginId;
        private String password;
        private String name;
        private String email;
        private List<Role> roles;
    }
}
