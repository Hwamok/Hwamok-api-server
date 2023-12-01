package com.hwamok.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hwamok.admin.domain.Admin;
import com.hwamok.admin.domain.AdminRepository;
import com.hwamok.admin.domain.Role;
import com.hwamok.api.dto.auth.AdminLoginDto;
import com.hwamok.api.dto.auth.UserLoginDto;
import com.hwamok.user.domain.Address;
import com.hwamok.user.domain.UploadedFile;
import com.hwamok.user.domain.User;
import com.hwamok.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.ArrayList;
import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    private List<Role> roles = new ArrayList<>();

    @BeforeEach
    void setUp() {
        roles.add(Role.SUPER);
        roles.add(Role.ADMIN);
    }

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

    @Test
    void 관리자로그인_성공() throws Exception {
        Admin admin = adminRepository
                .save(new Admin("test123",
                      passwordEncoder.encode("1234"), "이름", "test@test.com", roles));
        AdminLoginDto.Request request = new AdminLoginDto.Request(admin.getLoginId(), "1234");

        mockMvc.perform(post("/auth/adminLogin")
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
    void 어드민로그인_실패__필수값_NULL_또는_공백(String parameter) throws Exception {
        Admin admin = adminRepository
                .save(new Admin("test123",
                        passwordEncoder.encode("1234"), "이름", "test@test.com", roles));

        AdminLoginDto.Request request = new AdminLoginDto.Request(parameter, "1234");

        mockMvc.perform(post("/auth/adminLogin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E005"),
                        jsonPath("message").value("관리자정보를 찾을 수 없습니다.")
                );
    }

    @Test
    void 관리자로그인_실패__어드민정보_없음() throws Exception {
        AdminLoginDto.Request request = new AdminLoginDto.Request("test@test.com", "1234");

        mockMvc.perform(post("/auth/adminLogin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E005"),
                        jsonPath("message").value("관리자정보를 찾을 수 없습니다.")
                );
    }

    @Test
    void 관리자로그인_실패__비밀번호_불일치() throws Exception {
        String fakePassword = "123";
        Admin admin = adminRepository
                .save(new Admin("test123",
                        passwordEncoder.encode("1234"), "이름", "test@test.com", roles));
        AdminLoginDto.Request request = new AdminLoginDto.Request(admin.getLoginId(), fakePassword);

        mockMvc.perform(post("/auth/adminLogin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E005"),
                        jsonPath("message").value("관리자정보를 찾을 수 없습니다.")
                );
    }
}