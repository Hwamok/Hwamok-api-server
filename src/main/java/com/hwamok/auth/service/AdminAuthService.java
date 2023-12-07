package com.hwamok.auth.service;

import org.springframework.data.util.Pair;

public interface AdminAuthService {
    Pair<String, String> login(String loginId, String password);
}
