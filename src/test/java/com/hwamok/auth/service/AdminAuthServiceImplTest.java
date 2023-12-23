package com.hwamok.auth.service;

import com.hwamok.admin.domain.Admin;
import com.hwamok.admin.domain.AdminRepository;
import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.security.jwt.JwtService;
import com.hwamok.security.jwt.JwtType;
import com.hwamok.utils.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.hwamok.core.exception.HwamokExceptionTest.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class AdminAuthServiceImplTest {
    @Autowired
    private AdminAuthService adminAuthService;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void 어드민로그인_성공() {
        Admin admin = adminRepository.save(new Admin("test1234", passwordEncoder.encode("1234"), "이름", "test@test.com", List.of(Role.ADMIN)));

        Pair<String, String> pair = adminAuthService.login(admin.getLoginId(), "1234");

        assertThat(pair.getFirst()).isEqualTo(jwtService.issue(admin.getId(), "어드민", JwtType.ACCESS));
        assertThat(pair.getSecond()).isEqualTo(jwtService.issue(admin.getId(), "어드민", JwtType.REFRESH));
    }

    @Test
    void 어드민로그인_실패__어드민정보_없음() {
        assertThatHwamokException(ExceptionCode.NOT_FOUND_ADMIN).isThrownBy(()-> adminAuthService.login("test@test.com", "1234"));
    }

    @Test
    void 어드민로그인_실패__패스워드_다름() {
        String fakePassword = "123";
        adminRepository.save(new Admin("test123", passwordEncoder.encode("1234"), "이름", "test@test.com", List.of(Role.ADMIN)));

        assertThatHwamokException(ExceptionCode.NOT_FOUND_ADMIN).isThrownBy(()-> adminAuthService.login("test@test.com", fakePassword));
    }
}