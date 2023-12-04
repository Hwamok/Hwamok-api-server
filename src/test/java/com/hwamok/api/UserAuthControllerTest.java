package com.hwamok.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hwamok.api.dto.auth.UserLoginDto;
import com.hwamok.user.domain.Address;
import com.hwamok.user.domain.UploadedFile;
import com.hwamok.user.domain.User;
import com.hwamok.user.domain.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class UserAuthControllerTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void 유저로그인_성공() throws Exception {
        User user = userRepository
                .save(new User("hwamok@test.com", passwordEncoder.encode("1234"), "hwamok",
                        "2023-11-15", "01012345678", "GOOGLE",
                        new UploadedFile("originalImage", "savedImage"),
                        new Address(12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201")));
        UserLoginDto.Request request = new UserLoginDto.Request(user.getEmail(), "1234");

        mockMvc.perform(post("/auth/userLogin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success"),
                        jsonPath("data.accessToken").isNotEmpty(),
                        jsonPath("data.refreshToken").isNotEmpty()
                );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 유저로그인_실패__필수값_NULL_또는_공백(String parameter) throws Exception {
        User user = userRepository
                .save(new User("hwamok@test.com", passwordEncoder.encode("1234"), "hwamok",
                        "2023-11-15", "01012345678", "GOOGLE",
                        new UploadedFile("originalImage", "savedImage"),
                        new Address(12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201")));
        UserLoginDto.Request request = new UserLoginDto.Request(parameter, "1234");

        mockMvc.perform(post("/auth/userLogin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E007"),
                        jsonPath("message").value("사용자 정보를 찾을 수 없습니다.")
                );
    }

    @Test
    void 유저로그인_실패__유저정보_없음() throws Exception {
        UserLoginDto.Request request = new UserLoginDto.Request("test@test.com", "1234");

        mockMvc.perform(post("/auth/userLogin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E007"),
                        jsonPath("message").value("사용자 정보를 찾을 수 없습니다.")
                );
    }

    @Test
    void 유저로그인_실패__패스워드_불일치() throws Exception {
        String fakePassword = "123";
        User user = userRepository
                .save(new User("hwamok@test.com", passwordEncoder.encode("1234"), "hwamok",
                        "2023-11-15", "01012345678", "GOOGLE",
                        new UploadedFile("originalImage", "savedImage"),
                        new Address(12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201")));
        UserLoginDto.Request request = new UserLoginDto.Request(user.getEmail(), fakePassword);

        mockMvc.perform(post("/auth/userLogin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E007"),
                        jsonPath("message").value("사용자 정보를 찾을 수 없습니다.")
                );
    }
}
