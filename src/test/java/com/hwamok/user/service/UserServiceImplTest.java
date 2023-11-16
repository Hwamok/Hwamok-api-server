package com.hwamok.user.service;

import com.hwamok.user.domain.Address;
import com.hwamok.user.domain.UploadedFile;
import com.hwamok.user.domain.User;
import com.hwamok.user.domain.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Transactional
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 회원_가입_성공() {
        User user = userRepository.save(new User("hwamok@test.com", "1234", "hwamok", "20231115", "01012345678", "GOOGLE",
                "ACTIVATED", "originalImage", "savedImage",
                12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201"));

        assertThat(user.getId()).isNotNull();


    }
}