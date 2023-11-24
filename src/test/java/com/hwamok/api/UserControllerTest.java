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
                "01012345679", "NAVER",
                new UploadedFileUpdateDto.Request("originalImage1", "savedImage1"),
                new AddressUpdateDto.Request(12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea",
                        "202"));

        mockMvc.perform(patch("/user/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success")
                );
    }

    @Test
    void 회원_정보_단건_조회_성공() throws Exception {
        User user = userRepository.save(UserFixture.create());

        mockMvc.perform(get("/user/{id}", user.getId()))
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
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success")
                );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_email_필수값_입력_null_혹은_빈값(String email) throws Exception {
        UserCreateDto.Request request = new UserCreateDto.Request(email, "1234", "hwamok",
                "2023-11-15", "01012345678", "GOOGLE",
                new UploadedFileCreateDto.Request("originalImage", "savedImage"),
                new AddressCreateDto.Request(12345,"15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다.")
                );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_password_필수값_입력_null_혹은_빈값(String password) throws Exception {
        UserCreateDto.Request request = new UserCreateDto.Request("hwamok@test.com", password, "hwamok",
                "2023-11-15", "01012345678", "GOOGLE",
                new UploadedFileCreateDto.Request("originalImage", "savedImage"),
                new AddressCreateDto.Request(12345,"15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다.")
                );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_name_필수값_입력_null_혹은_빈값(String name) throws Exception {
        UserCreateDto.Request request = new UserCreateDto.Request("hwamok@test.com", "1234", name,
                "2023-11-15", "01012345678", "GOOGLE",
                new UploadedFileCreateDto.Request("originalImage", "savedImage"),
                new AddressCreateDto.Request(12345,"15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다.")
                );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_password_필수값_입력_null_혹은_빈값(String password) throws Exception {
        User user = userRepository.save(UserFixture.create());

        UserUpdateDto.Request request = new UserUpdateDto.Request(password, "hwamokhwa", "2023-11-16",
                "01012345679", "NAVER",
                new UploadedFileUpdateDto.Request("originalImage1", "savedImage1"),
                new AddressUpdateDto.Request(12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea",
                        "202"));

        mockMvc.perform(patch("/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다.")
                );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_name_필수값_입력_null_혹은_빈값(String name) throws Exception {
        User user = userRepository.save(UserFixture.create());

        UserUpdateDto.Request request = new UserUpdateDto.Request("1234", name, "2023-11-16",
                "01012345679", "NAVER",
                new UploadedFileUpdateDto.Request("originalImage1", "savedImage1"),
                new AddressUpdateDto.Request(12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea",
                        "202"));

        mockMvc.perform(patch("/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다.")
                );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_birthDay_필수값_입력_null_혹은_빈값(String birthDay) throws Exception {
        User user = userRepository.save(UserFixture.create());

        UserUpdateDto.Request request = new UserUpdateDto.Request("1234", "hwamok", birthDay,
                "01012345679", "NAVER",
                new UploadedFileUpdateDto.Request("originalImage1", "savedImage1"),
                new AddressUpdateDto.Request(12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea",
                        "202"));

        mockMvc.perform(patch("/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다.")
                );
    }

    @Test
    void 회원_수정_실패_존재하지_않는_회원() throws Exception {
        mockMvc.perform(get("/user/{id}", -1l))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E007"),
                        jsonPath("message").value("사용자 정보를 찾을 수 없습니다.")
                );
    }

    @Test
    void 회원_가입_실패_email_골뱅이_없음() throws Exception {
        String fakeEmail = "hwamoktest.com";

        UserCreateDto.Request request = new UserCreateDto.Request(fakeEmail, "1234", "hwamok",
                "2023-11-15", "01012345678", "GOOGLE",
                new UploadedFileCreateDto.Request("originalImage", "savedImage"),
                new AddressCreateDto.Request(12345,"15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E003"),
                        jsonPath("message").value("이메일형식이 다릅니다.")
                );
    }

    @Test
    void 회원_가입_실패_email_점_없음() throws Exception {
        String fakeEmail = "hwamok@testcom";

        UserCreateDto.Request request = new UserCreateDto.Request(fakeEmail, "1234", "hwamok",
                "2023-11-15", "01012345678", "GOOGLE",
                new UploadedFileCreateDto.Request("originalImage", "savedImage"),
                new AddressCreateDto.Request(12345,"15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E003"),
                        jsonPath("message").value("이메일형식이 다릅니다.")
                );
    }

    @Test
    void 회원_가입_실패_name_특수문자() throws Exception {
        String fakeName = "hwamok!";

        UserCreateDto.Request request = new UserCreateDto.Request("hwamok@test.com", "1234", fakeName,
                "2023-11-15", "01012345678", "GOOGLE",
                new UploadedFileCreateDto.Request("originalImage", "savedImage"),
                new AddressCreateDto.Request(12345,"15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E004"),
                        jsonPath("message").value("이름형식이 다릅니다.")
                );
    }

    @Test
    void 회원_가입_실패_name_두_글자_미만() throws Exception {
        String fakeName = "화";

        UserCreateDto.Request request = new UserCreateDto.Request("hwamok@test.com", "1234", fakeName,
                "2023-11-15", "01012345678", "GOOGLE",
                new UploadedFileCreateDto.Request("originalImage", "savedImage"),
                new AddressCreateDto.Request(12345,"15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E004"),
                        jsonPath("message").value("이름형식이 다릅니다.")
                );
    }

    @Test
    void 회원_가입_실패_validate_name_영문한글_혼용() throws Exception {
        String fakeName = "화목hwamok";

        UserCreateDto.Request request = new UserCreateDto.Request("hwamok@test.com", "1234", fakeName,
                "2023-11-15", "01012345678", "GOOGLE",
                new UploadedFileCreateDto.Request("originalImage", "savedImage"),
                new AddressCreateDto.Request(12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E004"),
                        jsonPath("message").value("이름형식이 다릅니다.")
                );
    }

    @Test
    void 회원_가입_실패_birthDay_다시_없음() throws Exception {
        String fakeBirthDay = "20231115";

        UserCreateDto.Request request = new UserCreateDto.Request("hwamok@test.com", "1234", "hwamok",
                fakeBirthDay, "01012345678", "GOOGLE",
                new UploadedFileCreateDto.Request("originalImage", "savedImage"),
                new AddressCreateDto.Request(12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E008"),
                        jsonPath("message").value("날짜 형식이 다릅니다.")
                );
    }

    @Test
    void 회원_가입_실패_birthDay_슬래시_변경() throws Exception {
        String fakeBirthDay = "2023/11/15";

        UserCreateDto.Request request = new UserCreateDto.Request("hwamok@test.com", "1234", "hwamok",
                fakeBirthDay, "01012345678", "GOOGLE",
                new UploadedFileCreateDto.Request("originalImage", "savedImage"),
                new AddressCreateDto.Request(12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E008"),
                        jsonPath("message").value("날짜 형식이 다릅니다.")
                );
    }

    @Test
    void 회원_가입_실패_validate_post_5자리_아래일때() throws Exception {
        int fakePost = 1234;

        UserCreateDto.Request request = new UserCreateDto.Request("hwamok@test.com", "1234", "hwamok",
                "2023-11-15", "01012345678", "GOOGLE",
                new UploadedFileCreateDto.Request("originalImage", "savedImage"),
                new AddressCreateDto.Request(fakePost, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E009"),
                        jsonPath("message").value("우편 번호 형식이 다릅니다.")
                );
    }

    @Test
    void 회원_가입_실패_validate_phone_숫자_제외_다른_문자() throws Exception {
        String fakePhone = "010-1234#56";

        UserCreateDto.Request request = new UserCreateDto.Request("hwamok@test.com", "1234", "hwamok",
                "2023-11-15", fakePhone, "GOOGLE",
                new UploadedFileCreateDto.Request("originalImage", "savedImage"),
                new AddressCreateDto.Request(12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E010"),
                        jsonPath("message").value("핸드폰 번호 형식이 다릅니다.")
                );
    }

    @Test
    void 회원_가입_실패_validate_phone_11자리_미만() throws Exception {
        String fakePhone = "0101234567";

        UserCreateDto.Request request = new UserCreateDto.Request("hwamok@test.com", "1234", "hwamok",
                "2023-11-15", fakePhone, "GOOGLE",
                new UploadedFileCreateDto.Request("originalImage", "savedImage"),
                new AddressCreateDto.Request(12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E010"),
                        jsonPath("message").value("핸드폰 번호 형식이 다릅니다.")
                );
    }

    @Test
    void 회원_가입_실패_validate_phone_첫째자리_0_제외() throws Exception {
        String fakePhone = "21012345678";

        UserCreateDto.Request request = new UserCreateDto.Request("hwamok@test.com", "1234", "hwamok",
                "2023-11-15", fakePhone, "GOOGLE",
                new UploadedFileCreateDto.Request("originalImage", "savedImage"),
                new AddressCreateDto.Request(12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E010"),
                        jsonPath("message").value("핸드폰 번호 형식이 다릅니다.")
                );
    }

    @Test
    void 회원_가입_실패_validate_phone_두째자리_1_제외() throws Exception {
        String fakePhone = "00012345678";

        UserCreateDto.Request request = new UserCreateDto.Request("hwamok@test.com", "1234", "hwamok",
                "2023-11-15", fakePhone, "GOOGLE",
                new UploadedFileCreateDto.Request("originalImage", "savedImage"),
                new AddressCreateDto.Request(12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E010"),
                        jsonPath("message").value("핸드폰 번호 형식이 다릅니다.")
                );
    }

    @Test
    void 회원_가입_실패_알_수_없는_platform () throws Exception {
        String fakePlatform = "Platform";

        UserCreateDto.Request request = new UserCreateDto.Request("hwamok@test.com", "1234", "hwamok",
                "2023-11-15", "01012345678", fakePlatform,
                new UploadedFileCreateDto.Request("originalImage", "savedImage"),
                new AddressCreateDto.Request(12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E020"),
                        jsonPath("message").value("알 수 없는 플랫폼입니다.")
                );
    }

    @Test
    void 회원_수정_실패_name_특수문자() throws Exception {
        User user = userRepository.save(UserFixture.create());

        String fakeName = "hwamok!";

        UserUpdateDto.Request request = new UserUpdateDto.Request("12345", fakeName, "2023-11-16",
                "01012345679", "NAVER",
                new UploadedFileUpdateDto.Request("originalImage1", "savedImage1"),
                new AddressUpdateDto.Request(12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea",
                        "202"));

        mockMvc.perform(patch("/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E004"),
                        jsonPath("message").value("이름형식이 다릅니다.")
                );
    }

    @Test
    void 회원_수정_실패_name_두_글자_미만() throws Exception {
        User user = userRepository.save(UserFixture.create());

        String fakeName = "화";

        UserUpdateDto.Request request = new UserUpdateDto.Request("12345", fakeName, "2023-11-16",
                "01012345679", "NAVER",
                new UploadedFileUpdateDto.Request("originalImage1", "savedImage1"),
                new AddressUpdateDto.Request(12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea",
                        "202"));

        mockMvc.perform(patch("/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E004"),
                        jsonPath("message").value("이름형식이 다릅니다.")
                );
    }

    @Test
    void 회원_수정_실패_name_영문한글_혼용() throws Exception {
        User user = userRepository.save(UserFixture.create());

        String fakeName = "화목hwamok";

        UserUpdateDto.Request request = new UserUpdateDto.Request("12345", fakeName, "2023-11-16",
                "01012345679", "NAVER",
                new UploadedFileUpdateDto.Request("originalImage1", "savedImage1"),
                new AddressUpdateDto.Request(12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea",
                        "202"));

        mockMvc.perform(patch("/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E004"),
                        jsonPath("message").value("이름형식이 다릅니다.")
                );
    }

    @Test
    void 회원_수정_실패_birthDay_다시_없음() throws Exception {
        User user = userRepository.save(UserFixture.create());

        String fakeBirthDay = "20231115";

        UserUpdateDto.Request request = new UserUpdateDto.Request("12345", "hwamokhwa", fakeBirthDay,
                "01012345679", "NAVER",
                new UploadedFileUpdateDto.Request("originalImage1", "savedImage1"),
                new AddressUpdateDto.Request(12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea",
                        "202"));

        mockMvc.perform(patch("/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E008"),
                        jsonPath("message").value("날짜 형식이 다릅니다.")
                );
    }

    @Test
    void 회원_수정_실패_birthDay_슬래시_변경() throws Exception {
        User user = userRepository.save(UserFixture.create());

        String fakeBirthDay = "2023/11/15";

        UserUpdateDto.Request request = new UserUpdateDto.Request("12345", "hwamokhwa", fakeBirthDay,
                "01012345679", "NAVER",
                new UploadedFileUpdateDto.Request("originalImage1", "savedImage1"),
                new AddressUpdateDto.Request(12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea",
                        "202"));

        mockMvc.perform(patch("/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E008"),
                        jsonPath("message").value("날짜 형식이 다릅니다.")
                );
    }

    @Test
    void 회원_수정_실패_post_5자리_아래일때() throws Exception {
        User user = userRepository.save(UserFixture.create());

        int fakePost = 1234;

        UserUpdateDto.Request request = new UserUpdateDto.Request("12345", "hwamokhwa", "2023-11-16",
                "01012345679", "NAVER",
                new UploadedFileUpdateDto.Request("originalImage1", "savedImage1"),
                new AddressUpdateDto.Request(fakePost, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea",
                        "202"));

        mockMvc.perform(patch("/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E009"),
                        jsonPath("message").value("우편 번호 형식이 다릅니다.")
                );
    }

    @Test
    void 회원_수정_실패_phone_숫자_제외_다른_문자() throws Exception {
        User user = userRepository.save(UserFixture.create());

        String fakePhone = "010-1234#56";

        UserUpdateDto.Request request = new UserUpdateDto.Request("12345", "hwamokhwa", "2023-11-16",
                fakePhone, "NAVER",
                new UploadedFileUpdateDto.Request("originalImage1", "savedImage1"),
                new AddressUpdateDto.Request(12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea",
                        "202"));

        mockMvc.perform(patch("/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E010"),
                        jsonPath("message").value("핸드폰 번호 형식이 다릅니다.")
                );
    }

    @Test
    void 회원_수정_실패_phone_11자리_미만() throws Exception {
        User user = userRepository.save(UserFixture.create());

        String fakePhone = "0101234567";

        UserUpdateDto.Request request = new UserUpdateDto.Request("12345", "hwamokhwa", "2023-11-16",
                fakePhone, "NAVER",
                new UploadedFileUpdateDto.Request("originalImage1", "savedImage1"),
                new AddressUpdateDto.Request(12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea",
                        "202"));

        mockMvc.perform(patch("/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E010"),
                        jsonPath("message").value("핸드폰 번호 형식이 다릅니다.")
                );
    }

    @Test
    void 회원_수정_실패_phone_첫째자리_0_제외() throws Exception {
        User user = userRepository.save(UserFixture.create());

        String fakePhone = "21012345678";

        UserUpdateDto.Request request = new UserUpdateDto.Request("12345", "hwamokhwa", "2023-11-16",
                fakePhone, "NAVER",
                new UploadedFileUpdateDto.Request("originalImage1", "savedImage1"),
                new AddressUpdateDto.Request(12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea",
                        "202"));

        mockMvc.perform(patch("/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E010"),
                        jsonPath("message").value("핸드폰 번호 형식이 다릅니다.")
                );
    }

    @Test
    void 회원_수정_실패_phone_두째자리_1_제외() throws Exception {
        User user = userRepository.save(UserFixture.create());

        String fakePhone = "00012345678";

        UserUpdateDto.Request request = new UserUpdateDto.Request("12345", "hwamokhwa", "2023-11-16",
                fakePhone, "NAVER",
                new UploadedFileUpdateDto.Request("originalImage1", "savedImage1"),
                new AddressUpdateDto.Request(12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea",
                        "202"));

        mockMvc.perform(patch("/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E010"),
                        jsonPath("message").value("핸드폰 번호 형식이 다릅니다.")
                );
    }

    @Test
    void 회원_수정_실패_알_수_없는_platform () throws Exception {
        User user = userRepository.save(UserFixture.create());

        String fakePlatform = "Platform";

        UserUpdateDto.Request request = new UserUpdateDto.Request("12345", "hwamokhwa", "2023-11-16",
                "01012345679", fakePlatform,
                new UploadedFileUpdateDto.Request("originalImage1", "savedImage1"),
                new AddressUpdateDto.Request(12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea",
                        "202"));

        mockMvc.perform(patch("/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E020"),
                        jsonPath("message").value("알 수 없는 플랫폼입니다.")
                );
    }

}