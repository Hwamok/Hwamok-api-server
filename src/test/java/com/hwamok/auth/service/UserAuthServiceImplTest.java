package com.hwamok.auth.service;

import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.core.exception.HwamokExceptionTest;
import com.hwamok.security.jwt.JwtService;
import com.hwamok.security.jwt.JwtType;
import com.hwamok.user.domain.Address;
import com.hwamok.user.domain.UploadedFile;
import com.hwamok.user.domain.User;
import com.hwamok.user.domain.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static com.hwamok.core.exception.HwamokExceptionTest.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class UserAuthServiceImplTest {
    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void 유저로그인_성공() {
        User user = userRepository.save(new User("hwamok@test.com", passwordEncoder.encode("1234"),
                "hwamok","2023-11-15", "01012345678", "GOOGLE",
                new UploadedFile("originalImage", "savedImage"),
                new Address(12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201")));

        Pair<String, String> pair = userAuthService.login(user.getEmail(), "1234");

        assertThat(pair.getFirst()).isEqualTo(jwtService.issue(user.getId(), user.getRole().getName(), JwtType.ACCESS));
        assertThat(pair.getSecond()).isEqualTo(jwtService.issue(user.getId(), user.getRole().getName(), JwtType.REFRESH));
    }

    @Test
    void 유저로그인_실패__유저정보_없음() {
        assertThatHwamokException(ExceptionCode.NOT_FOUND_USER).isThrownBy(()-> userAuthService.login("test@test.com", "1234"));
    }

    @Test
    void 유저로그인_실패__패스워드_다름() {
        String fakePassword = "123";
        userRepository.save(new User("hwamok@test.com", passwordEncoder.encode("1234"),
                "hwamok","2023-11-15", "01012345678", "GOOGLE",
                new UploadedFile("originalImage", "savedImage"),
                new Address(12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201")));

        assertThatHwamokException(ExceptionCode.NOT_FOUND_USER).isThrownBy(()-> userAuthService.login("test@test.com", fakePassword));
    }
}
