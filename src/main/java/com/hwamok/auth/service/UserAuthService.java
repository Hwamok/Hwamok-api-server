package com.hwamok.auth.service;

import org.springframework.data.util.Pair;

public interface UserAuthService {
    Pair<String, String> login(String email, String password);
}
