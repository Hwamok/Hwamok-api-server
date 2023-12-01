package com.hwamok.auth.service;

import org.springframework.data.util.Pair;

public interface AuthService {
    Pair<String, String> userLogin(String email, String password);

    Pair<String, String> adminLogin(String loginId, String password);
}
