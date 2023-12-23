package com.hwamok.auth.service;

import com.hwamok.admin.domain.Admin;
import com.hwamok.admin.domain.AdminRepository;
import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.core.exception.HwamokException;
import com.hwamok.security.jwt.JwtService;
import com.hwamok.security.jwt.JwtType;
import com.hwamok.utils.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminAuthServiceImpl implements AdminAuthService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public Pair<String, String> login(String loginId, String password) {
        Admin admin = adminRepository.findByLoginId(loginId)
                .orElseThrow(()-> new HwamokException(ExceptionCode.NOT_FOUND_ADMIN));

        if(!passwordEncoder.matches(password, admin.getPassword())){
            throw new HwamokException(ExceptionCode.NOT_FOUND_ADMIN);
        }

        String accessToken = jwtService.issue(admin.getId(), admin.getRoles(), JwtType.ACCESS);
        String refreshToken = jwtService.issue(admin.getId(), admin.getRoles(), JwtType.REFRESH);

        return Pair.of(accessToken,refreshToken);
    }
}
