package com.hwamok.security.jwt;

import com.hwamok.utils.Role;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface JwtService {
    String issue(Long id, List<Role> role, JwtType type);

    String resolveToken(HttpServletRequest request);

    boolean validate(String token);

    Long getId(String token);

    List getRoles(String token);
}
