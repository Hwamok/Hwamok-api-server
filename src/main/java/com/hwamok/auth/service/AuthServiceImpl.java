package com.hwamok.auth.service;

import com.hwamok.admin.domain.Admin;
import com.hwamok.admin.domain.AdminRepository;
import com.hwamok.admin.domain.Role;
import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.core.exception.HwamokException;
import com.hwamok.security.jwt.JwtService;
import com.hwamok.security.jwt.JwtType;
import com.hwamok.user.domain.User;
import com.hwamok.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public Pair<String, String> userLogin(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new HwamokException(ExceptionCode.NOT_FOUND_USER));

        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new HwamokException(ExceptionCode.NOT_FOUND_USER);
        }

        String accessToken = jwtService.issue(user.getId(), user.getRole().getName(), JwtType.ACCESS);
        String refreshToken = jwtService.issue(user.getId(), user.getRole().getName(), JwtType.REFRESH);

        return Pair.of(accessToken,refreshToken);
    }

    @Override
    public Pair<String, String> adminLogin(String loginId, String password) {
        Admin admin = adminRepository.findByLoginId(loginId)
                .orElseThrow(()-> new HwamokException(ExceptionCode.NOT_FOUND_ADMIN));

        if(!passwordEncoder.matches(password, admin.getPassword())){
            throw new HwamokException(ExceptionCode.NOT_FOUND_ADMIN);
        }
        List<Role> roles = admin.getRoles();
        String role = "";
        if(roles.contains(Role.SUPER)){
            role = Role.SUPER.getName();
        } else {
            role = Role.ADMIN.getName();
        }

        String accessToken = jwtService.issue(admin.getId(), role, JwtType.ACCESS);
        String refreshToken = jwtService.issue(admin.getId(), role, JwtType.REFRESH);

        return Pair.of(accessToken,refreshToken);
    }
}
