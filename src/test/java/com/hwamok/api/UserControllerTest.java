package com.hwamok.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hwamok.api.dto.user.*;
import com.hwamok.user.domain.User;
import com.hwamok.user.domain.UserRepository;
import fixture.UserFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 회원_가입_성공() throws Exception {
        UserCreateDto.Request request = new UserCreateDto.Request("hwamok@test.com", "1234", "hwamok",
                "2023-11-15", "01012345678", "GOOGLE",
                new UploadedFileCreateDto.Request("originalImage", "savedImage"),
                new AddressCreateDto.Request(12345,"15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success")
                );
    }

    @Test
    void 회원_수정_성공() throws Exception {
        User user = userRepository.save(UserFixture.create());

        UserUpdateDto.Request request = new UserUpdateDto.Request("12345", "hwamokhwa", "2023-11-16",
                "01012345679", "NAVER", new UploadedFileUpdateDto.Request("originalImage1", "savedImage1"),
                new AddressUpdateDto.Request(12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202"));

        mockMvc.perform(patch("/user/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success"),
                        jsonPath("data.password").value("12345"),
                        jsonPath("data.name").value("hwamokhwa"),
                        jsonPath("data.birthDay").value("2023-11-16"),
                        jsonPath("data.phone").value("01012345679"),
                        jsonPath("data.platform").value("NAVER"),
                        jsonPath("data.profile.originalFileName").value("originalImage1"),
                        jsonPath("data.profile.savedFileName").value("savedImage1"),
                        jsonPath("data.address.post").value(12346),
                        jsonPath("data.address.addr").value("17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea"),
                        jsonPath("data.address.detailAddr").value("202")
                );
    }

    @Test
    void 회원_정보_단건_조회() throws Exception {
        User user = userRepository.save(UserFixture.create());

        mockMvc.perform(get("/user/{id}", user.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success"),
                        jsonPath("data.email").value("hwamok@test.com"),
                        jsonPath("data.password").value("1234"),
                        jsonPath("data.name").value("hwamok"),
                        jsonPath("data.birthDay").value("2023-11-15"),
                        jsonPath("data.phone").value("01012345678"),
                        jsonPath("data.platform").value("GOOGLE"),
                        jsonPath("data.profile.originalFileName").value("originalImage"),
                        jsonPath("data.profile.savedFileName").value("savedImage"),
                        jsonPath("data.address.post").value(12345),
                        jsonPath("data.address.addr").value("15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea"),
                        jsonPath("data.address.detailAddr").value("201")
                );
    }

    @Test
    void 회원_탈퇴_성공() throws Exception {
        User user = userRepository.save(UserFixture.create());

        mockMvc.perform(delete("/user/{id}", user.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success")
                );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_email_null_혹은_빈값(String email) throws Exception {
        UserCreateDto.Request request = new UserCreateDto.Request(email, "1234", "hwamok",
                "2023-11-15", "01012345678", "GOOGLE",
                new UploadedFileCreateDto.Request("originalImage", "savedImage"),
                new AddressCreateDto.Request(12345,"15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다.")
                );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_password_null_혹은_빈값(String password) throws Exception {
        UserCreateDto.Request request = new UserCreateDto.Request("hwamok@test.com", password, "hwamok",
                "2023-11-15", "01012345678", "GOOGLE",
                new UploadedFileCreateDto.Request("originalImage", "savedImage"),
                new AddressCreateDto.Request(12345,"15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다.")
                );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_name_null_혹은_빈값(String name) throws Exception {
        UserCreateDto.Request request = new UserCreateDto.Request("hwamok@test.com", "1234", name,
                "2023-11-15", "01012345678", "GOOGLE",
                new UploadedFileCreateDto.Request("originalImage", "savedImage"),
                new AddressCreateDto.Request(12345,"15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다.")
                );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_birthDay_null_혹은_빈값(String birthDay) throws Exception {
        UserCreateDto.Request request = new UserCreateDto.Request("hwamok@test.com", "1234", "hwamok",
                birthDay, "01012345678", "GOOGLE",
                new UploadedFileCreateDto.Request("originalImage", "savedImage"),
                new AddressCreateDto.Request(12345,"15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다.")
                );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_phone_null_혹은_빈값(String phone) throws Exception {
        UserCreateDto.Request request = new UserCreateDto.Request("hwamok@test.com", "1234", "hwamok",
                "2023-11-15", phone, "GOOGLE",
                new UploadedFileCreateDto.Request("originalImage", "savedImage"),
                new AddressCreateDto.Request(12345,"15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다.")
                );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_platform_null_혹은_빈값(String platform) throws Exception {
        UserCreateDto.Request request = new UserCreateDto.Request("hwamok@test.com", "1234", "hwamok",
                "2023-11-15", "01012345678", platform,
                new UploadedFileCreateDto.Request("originalImage", "savedImage"),
                new AddressCreateDto.Request(12345,"15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다.")
                );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_addr_null_혹은_빈값(String addr) throws Exception {
        UserCreateDto.Request request = new UserCreateDto.Request("hwamok@test.com", "1234", "hwamok",
                "2023-11-15", "01012345678", "GOOGLE",
                new UploadedFileCreateDto.Request("originalImage", "savedImage"),
                new AddressCreateDto.Request(1234,addr,"201"));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다.")
                );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_detailAddr_null_혹은_빈값(String detailAddr) throws Exception {
        UserCreateDto.Request request = new UserCreateDto.Request("hwamok@test.com", "1234", "hwamok",
                "2023-11-15", "01012345678", "GOOGLE",
                new UploadedFileCreateDto.Request("originalImage", "savedImage"),
                new AddressCreateDto.Request(1234,"15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        detailAddr));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다.")
                );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_password_null_혹은_빈값(String password) throws Exception {
        User user = userRepository.save(UserFixture.create());

        UserUpdateDto.Request request = new UserUpdateDto.Request(password, "hwamokhwa", "2023-11-16",
                "01012345679", "NAVER", new UploadedFileUpdateDto.Request("originalImage1", "savedImage1"),
                new AddressUpdateDto.Request(12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202"));

        mockMvc.perform(patch("/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다.")
                );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_name_null_혹은_빈값(String name) throws Exception {
        User user = userRepository.save(UserFixture.create());

        UserUpdateDto.Request request = new UserUpdateDto.Request("1234", name, "2023-11-16",
                "01012345679", "NAVER", new UploadedFileUpdateDto.Request("originalImage1", "savedImage1"),
                new AddressUpdateDto.Request(12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202"));

        mockMvc.perform(patch("/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다.")
                );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_birthDay_null_혹은_빈값(String birthDay) throws Exception {
        User user = userRepository.save(UserFixture.create());

        UserUpdateDto.Request request = new UserUpdateDto.Request("1234", "hwamok", birthDay,
                "01012345679", "NAVER", new UploadedFileUpdateDto.Request("originalImage1", "savedImage1"),
                new AddressUpdateDto.Request(12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202"));

        mockMvc.perform(patch("/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다.")
                );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_phone_null_혹은_빈값(String phone) throws Exception {
        User user = userRepository.save(UserFixture.create());

        UserUpdateDto.Request request = new UserUpdateDto.Request("1234", "hwamok", "2023-11-16",
                phone, "NAVER", new UploadedFileUpdateDto.Request("originalImage1", "savedImage1"),
                new AddressUpdateDto.Request(12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202"));

        mockMvc.perform(patch("/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다.")
                );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_platform_null_혹은_빈값(String platform) throws Exception {
        User user = userRepository.save(UserFixture.create());

        UserUpdateDto.Request request = new UserUpdateDto.Request("1234", "hwamok", "2023-11-16",
                "01012345679", platform, new UploadedFileUpdateDto.Request("originalImage1", "savedImage1"),
                new AddressUpdateDto.Request(12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202"));

        mockMvc.perform(patch("/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다.")
                );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_addr_null_혹은_빈값(String addr) throws Exception {
        User user = userRepository.save(UserFixture.create());

        UserUpdateDto.Request request = new UserUpdateDto.Request("1234", "hwamok", "2023-11-16",
                "01012345679", "NAVER", new UploadedFileUpdateDto.Request("originalImage1", "savedImage1"),
                new AddressUpdateDto.Request(12346, addr, "202"));

        mockMvc.perform(patch("/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다.")
                );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_detailAddr_null_혹은_빈값(String detailAddr) throws Exception {
        User user = userRepository.save(UserFixture.create());

        UserUpdateDto.Request request = new UserUpdateDto.Request("1234", "hwamok", "2023-11-16",
                "01012345679", "NAVER", new UploadedFileUpdateDto.Request("originalImage1", "savedImage1"),
                new AddressUpdateDto.Request(12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", detailAddr));

        mockMvc.perform(patch("/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다.")
                );
    }
}