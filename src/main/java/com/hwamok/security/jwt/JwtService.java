package com.hwamok.security.jwt;

import jakarta.servlet.http.HttpServletRequest;

public interface JwtService {
    String issue(Long id, String role, JwtType type);

    String resolveToken(HttpServletRequest request);

    boolean validate(String token);

    Long getId(String token);

    String getRole(String token);
}
