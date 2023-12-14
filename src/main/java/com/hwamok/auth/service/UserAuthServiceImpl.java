package com.hwamok.auth.service;

import com.hwamok.admin.domain.AdminRepository;
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

@Service
@RequiredArgsConstructor
@Transactional
public class UserAuthServiceImpl implements UserAuthService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public Pair<String, String> login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new HwamokException(ExceptionCode.NOT_FOUND_USER));

        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new HwamokException(ExceptionCode.NOT_FOUND_USER);
        }

        String accessToken = jwtService.issue(user.getId(), user.getRoles(), JwtType.ACCESS);
        String refreshToken = jwtService.issue(user.getId(), user.getRoles(), JwtType.REFRESH);

        return Pair.of(accessToken,refreshToken);
    }
}
