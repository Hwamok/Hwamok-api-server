package com.hwamok.api;

import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hwamok.api.dto.user.*;
import com.hwamok.user.domain.User;
import com.hwamok.user.domain.UserRepository;
import fixture.UserFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.hwamok.utils.CreateValueUtil.stringLength;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
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
                )
                .andDo(document("회원 생성 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("hwamok@test.com"),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamok"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-15"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345678"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("GOOGLE"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12345),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("201")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("S000"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("success"),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserCreateDto.Request"))
                                        .responseSchema(Schema.schema("UserCreateDto.Response"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
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
                )
                .andDo(document("회원 수정 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("12345"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamokhwa"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-16"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345679"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("NAVER"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage1"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage1"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12346),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("202")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("S000"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("success"),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserUpdateDto.Request"))
                                        .responseSchema(Schema.schema("UserUpdateDto.Response"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
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
                )
                .andDo(document("회원 정보 단건 조회 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("S000"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("success"),
                                                        PayloadDocumentation.fieldWithPath("data.id").type(JsonFieldType.NUMBER).description(1),
                                                        PayloadDocumentation.fieldWithPath("data.email").type(JsonFieldType.STRING).description("hwamok@test.com"),
                                                        PayloadDocumentation.fieldWithPath("data.password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("data.name").type(JsonFieldType.STRING).description("hwamok"),
                                                        PayloadDocumentation.fieldWithPath("data.birthDay").type(JsonFieldType.STRING).description("2023-11-15"),
                                                        PayloadDocumentation.fieldWithPath("data.phone").type(JsonFieldType.STRING).description("01012345678"),
                                                        PayloadDocumentation.fieldWithPath("data.platform").type(JsonFieldType.STRING).description("GOOGLE"),
                                                        PayloadDocumentation.fieldWithPath("data.profile.originalFileName").type(JsonFieldType.STRING).description("originalImage"),
                                                        PayloadDocumentation.fieldWithPath("data.profile.savedFileName").type(JsonFieldType.STRING).description("savedImage"),
                                                        PayloadDocumentation.fieldWithPath("data.address.post").type(JsonFieldType.NUMBER).description(123465),
                                                        PayloadDocumentation.fieldWithPath("data.address.addr").type(JsonFieldType.STRING).description("15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("data.address.detailAddr").type(JsonFieldType.STRING).description("201"),
                                                        PayloadDocumentation.fieldWithPath("data.status").type(JsonFieldType.STRING).description("ACTIVATED"),
                                                        PayloadDocumentation.fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("The timestamp when the data was created"),
                                                        PayloadDocumentation.fieldWithPath("data.role").type(JsonFieldType.STRING).description("USER")
                                                )
                                        )
                                        .responseSchema(Schema.schema("UserUpdateDto.Response"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
    void 회원_정보_단건_조회_실패_존재하지_않는_회원() throws Exception {
        User user = userRepository.save(UserFixture.create());

        mockMvc.perform(get("/user/{id}", -1l))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E007"),
                        jsonPath("message").value("사용자 정보를 찾을 수 없습니다.")
                )
                .andDo(document("회원 정보 단건 조회 실패 존재하지 않는 회원 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E007"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("사용자 정보를 찾을 수 없습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .responseSchema(Schema.schema("NOT_FOUND_USER"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
    void 회원_탈퇴_성공() throws Exception {
        User user = userRepository.save(UserFixture.create());

        mockMvc.perform(delete("/user/{id}", user.getId()))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success")
                )
                .andDo(document("회원 탈퇴 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("S000"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("success"),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .responseSchema(Schema.schema("UserDeleteDto.Response"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
    void 회원_탈퇴_실패_존재하지_않는_회원() throws Exception {
        User user = userRepository.save(UserFixture.create());

        mockMvc.perform(delete("/user/{id}", -1l))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E007"),
                        jsonPath("message").value("사용자 정보를 찾을 수 없습니다.")
                )
                .andDo(document("회원 탈퇴 실패 존재하지 않는 회원 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E007"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("사용자 정보를 찾을 수 없습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .responseSchema(Schema.schema("NOT_FOUND_USER"))
                                        .build()
                        )
                ));
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
                )
                .andDo(document("회원 가입 실패 email 필수값 입력 null 혹은 빈값 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("email").optional().type(JsonFieldType.STRING).description(email),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamok"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-15"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345678"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("GOOGLE"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12345),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("201")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E001"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("필수 값이 누락되었습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserCreateNoEmailDto.Request"))
                                        .responseSchema(Schema.schema("REQUIRED_PARAMETER"))
                                        .build()
                        )
                ));
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
                )
                .andDo(document("회원 가입 실패 password 필수값 입력 null 혹은 빈값 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("hwamok@test.com"),
                                                        PayloadDocumentation.fieldWithPath("password").optional().type(JsonFieldType.STRING).description(password),                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamok"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-15"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345678"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("GOOGLE"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12345),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("201")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E001"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("필수 값이 누락되었습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserCreateNoPasswordDto.Request"))
                                        .responseSchema(Schema.schema("REQUIRED_PARAMETER"))
                                        .build()
                        )
                ));

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
                )
                .andDo(document("회원 가입 실패 name 필수값 입력 null 혹은 빈값 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("hwamok@test.com"),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").optional().type(JsonFieldType.STRING).description(name),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-15"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345678"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("GOOGLE"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12345),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("201")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E001"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("필수 값이 누락되었습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserCreateNoNameDto.Request"))
                                        .responseSchema(Schema.schema("REQUIRED_PARAMETER"))
                                        .build()
                        )
                ));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @WithUserDetails
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
                )
                .andDo(document("회원 수정 실패 password 필수값 입력 null 혹은 빈값 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("password").optional().type(JsonFieldType.STRING).description(password),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamokhwa"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-16"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345679"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("NAVER"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage1"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage1"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12346),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("202")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E001"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("필수 값이 누락되었습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserUpdateNoPasswordDto.Request"))
                                        .responseSchema(Schema.schema("REQUIRED_PARAMETER"))
                                        .build()
                        )
                ));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @WithUserDetails
    void 회원_수정_실패_name_필수값_입력_null_혹은_빈값(String name) throws Exception {
        User user = userRepository.save(UserFixture.create());

        UserUpdateDto.Request request = new UserUpdateDto.Request("12345", name, "2023-11-16",
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
                )
                .andDo(document("회원 수정 실패 name 필수값 입력 null 혹은 빈값 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("12345"),
                                                        PayloadDocumentation.fieldWithPath("name").optional().type(JsonFieldType.STRING).description(name),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-16"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345679"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("NAVER"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage1"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage1"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12346),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("202")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E001"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("필수 값이 누락되었습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserUpdateNoNameDto.Request"))
                                        .responseSchema(Schema.schema("REQUIRED_PARAMETER"))
                                        .build()
                        )
                ));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @WithUserDetails
    void 회원_수정_실패_birthDay_필수값_입력_null_혹은_빈값(String birthDay) throws Exception {
        User user = userRepository.save(UserFixture.create());

        UserUpdateDto.Request request = new UserUpdateDto.Request("12345", "hwamokhwa", birthDay,
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
                )
                .andDo(document("회원 수정 실패 birthDay 필수값 입력 null 혹은 빈값 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("12345"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamokhwa"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").optional().type(JsonFieldType.STRING).description(birthDay),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345679"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("NAVER"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage1"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage1"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12346),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("202")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E001"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("필수 값이 누락되었습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserUpdateNoBirthDayDto.Request"))
                                        .responseSchema(Schema.schema("REQUIRED_PARAMETER"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
    void 회원_수정_실패_존재하지_않는_회원() throws Exception {
        User user = userRepository.save(UserFixture.create());

        UserUpdateDto.Request request = new UserUpdateDto.Request("12345", "hwamokhwa", "2023-11-16",
                "01012345679", "NAVER",
                new UploadedFileUpdateDto.Request("originalImage1", "savedImage1"),
                new AddressUpdateDto.Request(12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea",
                        "202"));

        mockMvc.perform(patch("/user/{id}", -1l)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E007"),
                        jsonPath("message").value("사용자 정보를 찾을 수 없습니다.")
                )
                .andDo(document("회원 수정 실패 존재하지 않는 회원 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("12345"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamokhwa"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-16"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345679"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("NAVER"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage1"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage1"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12346),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("202")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E007"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("사용자 정보를 찾을 수 없습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserUpdateDto.Request"))
                                        .responseSchema(Schema.schema("NOT_FOUND_USER"))
                                        .build()
                        )
                ));
    }

    @Test
    void 회원_가입_실패_email_골뱅이_없음() throws Exception {
        String fakeEmail = "hwamoktest.com";

        UserCreateDto.Request request = new UserCreateDto.Request(fakeEmail, "12345", "hwamok",
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
                )
                .andDo(document("회원 가입 실패 email 골뱅이 없음 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description(fakeEmail),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamok"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-15"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345678"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("GOOGLE"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12345),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("201")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E003"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("이메일형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserCreateFakeEmailDto.Request"))
                                        .responseSchema(Schema.schema("NOT_EMAIL_FORM"))
                                        .build()
                        )
                ));
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
                )
                .andDo(document("회원 가입 실패 email 점 없음 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description(fakeEmail),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamok"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-15"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345678"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("GOOGLE"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12345),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("201")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E003"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("이메일형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserCreateFakeEmailDto.Request"))
                                        .responseSchema(Schema.schema("NOT_EMAIL_FORM"))
                                        .build()
                        )
                ));
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
                )
                .andDo(document("회원 가입 실패 name 특수문자 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("hwamok@test.com"),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description(fakeName),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-15"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345678"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("GOOGLE"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12345),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("201")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E004"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("이름형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserCreateFakeNameDto.Request"))
                                        .responseSchema(Schema.schema("NOT_NAME_FORM"))
                                        .build()
                        )
                ));
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
                )
                .andDo(document("회원 가입 실패 name 두 글자 미만 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("hwamok@test.com"),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description(fakeName),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-15"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345678"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("GOOGLE"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12345),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("201")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E004"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("이름형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserCreateFakeNameDto.Request"))
                                        .responseSchema(Schema.schema("NOT_NAME_FORM"))
                                        .build()
                        )
                ));
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
                )
                .andDo(document("회원 가입 실패 name 영문 한글 혼용 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("hwamok@test.com"),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description(fakeName),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-15"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345678"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("GOOGLE"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12345),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("201")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E004"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("이름형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserCreateFakeNameDto.Request"))
                                        .responseSchema(Schema.schema("NOT_NAME_FORM"))
                                        .build()
                        )
                ));
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
                )
                .andDo(document("회원 가입 실패 birthDay - 다시 없음 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("hwamok@test.com"),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamok"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description(fakeBirthDay),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345678"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("GOOGLE"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12345),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("201")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E008"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("날짜 형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserCreateFakeBirthDayDto.Request"))
                                        .responseSchema(Schema.schema("NOT_DATE_FORM"))
                                        .build()
                        )
                ));
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
                )
                .andDo(document("회원 가입 실패 birthDay 슬래시로 변경 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("hwamok@test.com"),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamok"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description(fakeBirthDay),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345678"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("GOOGLE"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12345),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("201")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E008"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("날짜 형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserCreateFakeBirthDayDto.Request"))
                                        .responseSchema(Schema.schema("NOT_DATE_FORM"))
                                        .build()
                        )
                ));
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
                )
                .andDo(document("회원 가입 실패 post 5자리 미만 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("hwamok@test.com"),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamok"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-15"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345678"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("GOOGLE"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(fakePost),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("201")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E009"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("우편 번호 형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserCreateFakePostDto.Request"))
                                        .responseSchema(Schema.schema("NOT_POST_FORM"))
                                        .build()
                        )
                ));
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
                )
                .andDo(document("회원 가입 실패 phone 숫자 제외 다른 문자 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("hwamok@test.com"),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamok"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-15"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description(fakePhone),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("GOOGLE"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12345),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("201")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E010"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("핸드폰 번호 형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserCreateFakePhoneDto.Request"))
                                        .responseSchema(Schema.schema("NOT_PHONE_FORM"))
                                        .build()
                        )
                ));
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
                )
                .andDo(document("회원 가입 실패 phone 11자리 미만 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("hwamok@test.com"),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamok"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-15"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description(fakePhone),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("GOOGLE"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12345),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("201")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E010"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("핸드폰 번호 형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserCreateFakePhoneDto.Request"))
                                        .responseSchema(Schema.schema("NOT_PHONE_FORM"))
                                        .build()
                        )
                ));
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
                )
                .andDo(document("회원 가입 실패 phone 첫째자리 0 제외 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("hwamok@test.com"),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamok"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-15"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description(fakePhone),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("GOOGLE"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12345),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("201")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E010"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("핸드폰 번호 형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserCreateFakePhoneDto.Request"))
                                        .responseSchema(Schema.schema("NOT_PHONE_FORM"))
                                        .build()
                        )
                ));
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
                )
                .andDo(document("회원 가입 실패 phone 두째자리 1 제외 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("hwamok@test.com"),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamok"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-15"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description(fakePhone),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("GOOGLE"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12345),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("201")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E010"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("핸드폰 번호 형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserCreateFakePhoneDto.Request"))
                                        .responseSchema(Schema.schema("NOT_PHONE_FORM"))
                                        .build()
                        )
                ));
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
                )
                .andDo(document("회원 가입 실패 알 수 없는 platform API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("hwamok@test.com"),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamok"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-15"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345678"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description(fakePlatform),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12345),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("201")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E020"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("알 수 없는 플랫폼입니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserCreateFakePlatformDto.Request"))
                                        .responseSchema(Schema.schema("NOT_KNOWN_PLATFORM"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
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
                )
                .andDo(document("회원 수정 실패 name 특수문자 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("12345"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description(fakeName),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-16"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345679"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("NAVER"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage1"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage1"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12346),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("202")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E004"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("이름형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserUpdateFakeNameDto.Request"))
                                        .responseSchema(Schema.schema("NOT_NAME_FORM"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
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
                )
                .andDo(document("회원 수정 실패 name 두 글자 미만 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("12345"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description(fakeName),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-16"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345679"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("NAVER"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage1"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage1"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12346),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("202")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E004"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("이름형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserUpdateFakeNameDto.Request"))
                                        .responseSchema(Schema.schema("NOT_NAME_FORM"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
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
                )
                .andDo(document("회원 수정 실패 name 영문 한글 혼용 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("12345"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description(fakeName),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-16"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345679"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("NAVER"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage1"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage1"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12346),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("202")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E004"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("이름형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserUpdateFakeNameDto.Request"))
                                        .responseSchema(Schema.schema("NOT_NAME_FORM"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
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
                )
                .andDo(document("회원 수정 실패 birthDay - 다시 없음 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("12345"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamokhwa"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description(fakeBirthDay),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345679"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("NAVER"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage1"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage1"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12346),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("202")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E008"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("날짜 형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserUpdateFakeBirthDayDto.Request"))
                                        .responseSchema(Schema.schema("NOT_DATE_FORM"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
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
                )
                .andDo(document("회원 수정 실패 birthDay 슬래시 변경 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("12345"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamokhwa"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description(fakeBirthDay),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345679"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("NAVER"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage1"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage1"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12346),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("202")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E008"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("날짜 형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserUpdateFakeBirthDayDto.Request"))
                                        .responseSchema(Schema.schema("NOT_DATE_FORM"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
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
                )
                .andDo(document("회원 수정 실패 post 5자리 미만 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("12345"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamokhwa"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-16"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345679"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("NAVER"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage1"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage1"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(fakePost),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("202")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E009"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("우편 번호 형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserUpdateFakePostDto.Request"))
                                        .responseSchema(Schema.schema("NOT_POST_FORM"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
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
                )
                .andDo(document("회원 수정 실패 phone 숫자 제외 다른 문자 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("12345"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamokhwa"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-16"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description(fakePhone),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("NAVER"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage1"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage1"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12346),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("202")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E010"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("핸드폰 번호 형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserUpdateFakePhoneDto.Request"))
                                        .responseSchema(Schema.schema("NOT_PHONE_FORM"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
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
                )
                .andDo(document("회원 수정 실패 phone 11자리 미만 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("12345"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamokhwa"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-16"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description(fakePhone),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("NAVER"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage1"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage1"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12346),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("202")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E010"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("핸드폰 번호 형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserUpdateFakePhoneDto.Request"))
                                        .responseSchema(Schema.schema("NOT_PHONE_FORM"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
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
                )
                .andDo(document("회원 수정 실패 phone 첫짜자리 0 제외 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("12345"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamokhwa"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-16"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description(fakePhone),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("NAVER"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage1"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage1"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12346),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("202")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E010"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("핸드폰 번호 형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserUpdateFakePhoneDto.Request"))
                                        .responseSchema(Schema.schema("NOT_PHONE_FORM"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
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
                )
                .andDo(document("회원 수정 실패 phone 두째자리 1 제외 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("12345"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamokhwa"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-16"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description(fakePhone),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("NAVER"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage1"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage1"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12346),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("202")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E010"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("핸드폰 번호 형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserUpdateFakePhoneDto.Request"))
                                        .responseSchema(Schema.schema("NOT_PHONE_FORM"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
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
                )
                .andDo(document("회원 수정 실패 알 수 없는 platform API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("12345"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamokhwa"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-16"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345679"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("NAVER"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage1"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage1"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12346),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("202")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E020"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("알 수 없는 플랫폼입니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserUpdateFakePlatformDto.Request"))
                                        .responseSchema(Schema.schema("NOT_KNOWN_PLATFORM"))
                                        .build()
                        )
                ));
    }

    @Test
    void 회원_가입_실패_email_50글자_초과() throws Exception {
        String fakeEmail = stringLength(51);

        UserCreateDto.Request request = new UserCreateDto.Request(fakeEmail, "1234", "hwamok",
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
                        jsonPath("code").value("E021"),
                        jsonPath("message").value("이메일의 길이가 초과되었습니다.")
                )
                .andDo(document("회원 가입 실패 email 50글자 초과 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description(fakeEmail),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamok"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-15"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345678"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("GOOGLE"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12345),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("201")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E021"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("이메일의 길이가 초과되었습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserCreateFakeEmailDto.Request"))
                                        .responseSchema(Schema.schema("OVER_LENGTH_EMAIL"))
                                        .build()
                        )
                ));
    }

    @Test
    void 회원_가입_실패_name_20글자_초과() throws Exception {
        String fakeName = stringLength(21);

        UserCreateDto.Request request = new UserCreateDto.Request("hwamok@test.com", "1234", fakeName,
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
                        jsonPath("code").value("E022"),
                        jsonPath("message").value("이름의 길이가 초과되었습니다.")
                )
                .andDo(document("회원 가입 실패 name 20글자 초과 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("hwamok@test.com"),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description(fakeName),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-15"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345678"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("GOOGLE"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12345),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("201")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E022"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("이름의 길이가 초과되었습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserCreateFakeNameDto.Request"))
                                        .responseSchema(Schema.schema("OVER_LENGTH_NAME"))
                                        .build()
                        )
                ));
    }

    @Test
    void 회원_가입_실패_birthDay_10글자_초과() throws Exception {
        String fakeBirthDay = stringLength(11);

        UserCreateDto.Request request = new UserCreateDto.Request("hwamok@test.com", "1234", "hwamok",
                fakeBirthDay, "01012345678", "GOOGLE",
                new UploadedFileCreateDto.Request("originalImage", "savedImage"),
                new AddressCreateDto.Request(12345,"15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E023"),
                        jsonPath("message").value("날짜의 길이가 초과되었습니다.")
                )
                .andDo(document("회원 가입 실패 birthDay 10글자 초과 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("hwamok@test.com"),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamok"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description(fakeBirthDay),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345678"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("GOOGLE"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12345),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("201")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E023"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("날짜의 길이가 초과되었습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserCreateFakeBirthDayDto.Request"))
                                        .responseSchema(Schema.schema("OVER_LENGTH_DATE"))
                                        .build()
                        )
                ));
    }

    @Test
    void 회원_가입_실패_phone_11글자_초과() throws Exception {
        String fakePhone = stringLength(12);

        UserCreateDto.Request request = new UserCreateDto.Request("hwamok@test.com", "1234", "hwamok",
                "2023-11-15", fakePhone, "GOOGLE",
                new UploadedFileCreateDto.Request("originalImage", "savedImage"),
                new AddressCreateDto.Request(12345,"15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E024"),
                        jsonPath("message").value("핸드폰 번호의 길이가 초과되었습니다.")
                )
                .andDo(document("회원 가입 실패 phone 11글자 초과 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("hwamok@test.com"),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamok"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-15"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description(fakePhone),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("GOOGLE"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12345),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("201")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E024"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("핸드폰 번호의 길이가 초과되었습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserCreateFakePhoneDto.Request"))
                                        .responseSchema(Schema.schema("OVER_LENGTH_PHONE"))
                                        .build()
                        )
                ));
    }

    @Test
    void 회원_가입_실패_platform_11글자_초과() throws Exception {
        String fakePlatform = stringLength(12);

        UserCreateDto.Request request = new UserCreateDto.Request("hwamok@test.com", "1234", "hwamok",
                "2023-11-15", "01012345678", fakePlatform,
                new UploadedFileCreateDto.Request("originalImage", "savedImage"),
                new AddressCreateDto.Request(12345,"15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E025"),
                        jsonPath("message").value("플랫폼의 길이가 초과되었습니다.")
                )
                .andDo(document("회원 가입 실패 platform 11글자 초과 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("hwamok@test.com"),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamok"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-15"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345678"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description(fakePlatform),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12345),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("201")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E025"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("플랫폼의 길이가 초과되었습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserCreateFakePlatformDto.Request"))
                                        .responseSchema(Schema.schema("OVER_LENGTH_PLATFORM"))
                                        .build()
                        )
                ));
    }

    @Test
    void 회원_가입_실패_addr_80글자_초과() throws Exception {
        String fakeAddr = stringLength(81);

        UserCreateDto.Request request = new UserCreateDto.Request("hwamok@test.com", "1234", "hwamok",
                "2023-11-15", "01012345678", "GOOGLE",
                new UploadedFileCreateDto.Request("originalImage", "savedImage"),
                new AddressCreateDto.Request(12345, fakeAddr,
                        "201"));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E026"),
                        jsonPath("message").value("주소의 길이가 초과되었습니다.")
                )
                .andDo(document("회원 가입 실패 addr 80글자 초과 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("hwamok@test.com"),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamok"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-15"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345678"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("GOOGLE"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12345),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description(fakeAddr),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("201")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E026"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("주소의 길이가 초과되었습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserCreateFakeAddrDto.Request"))
                                        .responseSchema(Schema.schema("OVER_LENGTH_ADDR"))
                                        .build()
                        )
                ));
    }

    @Test
    void 회원_가입_실패_detailAddr_10글자_초과() throws Exception {
        String fakeDetailAddr = stringLength(11);

        UserCreateDto.Request request = new UserCreateDto.Request("hwamok@test.com", "1234", "hwamok",
                "2023-11-15", "01012345678", "GOOGLE",
                new UploadedFileCreateDto.Request("originalImage", "savedImage"),
                new AddressCreateDto.Request(12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        fakeDetailAddr));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E027"),
                        jsonPath("message").value("주소 상세의 길이가 초과되었습니다.")
                )
                .andDo(document("회원 가입 실패 detailAddr 10글자 초과 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("hwamok@test.com"),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamok"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-15"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345678"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("GOOGLE"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12345),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description(fakeDetailAddr)
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E027"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("주소 상세의 길이가 초과되었습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserCreateFakeDetailAddrDto.Request"))
                                        .responseSchema(Schema.schema("OVER_LENGTH_DETAILADDR"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
    void 회원_수정_실패_name_20글자_초과() throws Exception {
        User user = userRepository.save(UserFixture.create());

        String fakeName = stringLength(21);

        UserUpdateDto.Request request = new UserUpdateDto.Request("12345", fakeName, "2023-11-16",
                "01012345679", "NAVER",
                new UploadedFileUpdateDto.Request("originalImage1", "savedImage1"),
                new AddressUpdateDto.Request(12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea",
                        "202"));

        mockMvc.perform(patch("/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E022"),
                        jsonPath("message").value("이름의 길이가 초과되었습니다.")
                )
                .andDo(document("회원 수정 실패 name 20글자 초과 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("12345"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description(fakeName),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-16"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345679"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("NAVER"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage1"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage1"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12346),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("202")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E022"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("이름의 길이가 초과되었습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserUpdateFakeNameDto.Request"))
                                        .responseSchema(Schema.schema("OVER_LENGTH_NAME"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
    void 회원_수정_실패_birthDay_10글자_초과() throws Exception {
        User user = userRepository.save(UserFixture.create());

        String fakeBirthDay = stringLength(11);

        UserUpdateDto.Request request = new UserUpdateDto.Request("12345", "hwamokhwa", fakeBirthDay,
                "01012345679", "NAVER",
                new UploadedFileUpdateDto.Request("originalImage1", "savedImage1"),
                new AddressUpdateDto.Request(12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea",
                        "202"));

        mockMvc.perform(patch("/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E023"),
                        jsonPath("message").value("날짜의 길이가 초과되었습니다.")
                )
                .andDo(document("회원 수정 실패 birthDay 10글자 초과 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("12345"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamokhwa"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description(fakeBirthDay),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345679"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("NAVER"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage1"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage1"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12346),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("202")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E023"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("날짜의 길이가 초과되었습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserUpdateFakeBirthDayDto.Request"))
                                        .responseSchema(Schema.schema("OVER_LENGTH_DATE"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
    void 회원_수정_실패_phone_11글자_초과() throws Exception {
        User user = userRepository.save(UserFixture.create());

        String fakePhone = stringLength(12);

        UserUpdateDto.Request request = new UserUpdateDto.Request("12345", "hwamokhwa", "2023-11-16",
                fakePhone, "NAVER",
                new UploadedFileUpdateDto.Request("originalImage1", "savedImage1"),
                new AddressUpdateDto.Request(12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea",
                        "202"));

        mockMvc.perform(patch("/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E024"),
                        jsonPath("message").value("핸드폰 번호의 길이가 초과되었습니다.")
                )
                .andDo(document("회원 수정 실패 birthDay 11글자 초과 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("12345"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamokhwa"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-16"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description(fakePhone),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("NAVER"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage1"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage1"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12346),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("202")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E024"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("핸드폰 번호의 길이가 초과되었습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserUpdateFakePhoneDto.Request"))
                                        .responseSchema(Schema.schema("OVER_LENGTH_PHONE"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
    void 회원_수정_실패_platform_11글자_초과() throws Exception {
        User user = userRepository.save(UserFixture.create());

        String fakePlatform = stringLength(12);

        UserUpdateDto.Request request = new UserUpdateDto.Request("12345", "hwamokhwa", "2023-11-16",
                "01012345679", fakePlatform,
                new UploadedFileUpdateDto.Request("originalImage1", "savedImage1"),
                new AddressUpdateDto.Request(12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea",
                        "202"));

        mockMvc.perform(patch("/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E025"),
                        jsonPath("message").value("플랫폼의 길이가 초과되었습니다.")
                )
                .andDo(document("회원 수정 실패 platform 11글자 초과 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("12345"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamokhwa"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-16"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345679"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description(fakePlatform),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage1"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage1"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12346),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("202")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E025"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("플랫폼의 길이가 초과되었습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserUpdateFakePlatformDto.Request"))
                                        .responseSchema(Schema.schema("OVER_LENGTH_PLATFORM"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
    void 회원_수정_실패_addr_80글자_초과() throws Exception {
        User user = userRepository.save(UserFixture.create());

        String fakeAddr = stringLength(81);

        UserUpdateDto.Request request = new UserUpdateDto.Request("12345", "hwamokhwa", "2023-11-16",
                "01012345679", "NAVER",
                new UploadedFileUpdateDto.Request("originalImage1", "savedImage1"),
                new AddressUpdateDto.Request(12346, fakeAddr,"202"));

        mockMvc.perform(patch("/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E026"),
                        jsonPath("message").value("주소의 길이가 초과되었습니다.")
                )
                .andDo(document("회원 수정 실패 addr 80글자 초과 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("12345"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamokhwa"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-16"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345679"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("NAVER"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage1"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage1"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12346),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description(fakeAddr),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description("202")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E026"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("주소의 길이가 초과되었습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserUpdateFakeAddrDto.Request"))
                                        .responseSchema(Schema.schema("OVER_LENGTH_ADDR"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
    void 회원_수정_실패_detailAddr_10글자_초과() throws Exception {
        User user = userRepository.save(UserFixture.create());

        String fakeDetailAddr = stringLength(11);

        UserUpdateDto.Request request = new UserUpdateDto.Request("12345", "hwamokhwa", "2023-11-16",
                "01012345679", "NAVER",
                new UploadedFileUpdateDto.Request("originalImage1", "savedImage1"),
                new AddressUpdateDto.Request(12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea",
                        fakeDetailAddr));

        mockMvc.perform(patch("/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E027"),
                        jsonPath("message").value("주소 상세의 길이가 초과되었습니다.")
                )
                .andDo(document("회원 수정 실패 detailAddr 10글자 초과 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("User")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("12345"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("hwamokhwa"),
                                                        PayloadDocumentation.fieldWithPath("birthDay").type(JsonFieldType.STRING).description("2023-11-16"),
                                                        PayloadDocumentation.fieldWithPath("phone").type(JsonFieldType.STRING).description("01012345679"),
                                                        PayloadDocumentation.fieldWithPath("platform").type(JsonFieldType.STRING).description("NAVER"),
                                                        PayloadDocumentation.fieldWithPath("profile.originalFileName").type(JsonFieldType.STRING).description("originalImage1"),
                                                        PayloadDocumentation.fieldWithPath("profile.savedFileName").type(JsonFieldType.STRING).description("savedImage1"),
                                                        PayloadDocumentation.fieldWithPath("address.post").type(JsonFieldType.NUMBER).description(12346),
                                                        PayloadDocumentation.fieldWithPath("address.addr").type(JsonFieldType.STRING).description("17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea"),
                                                        PayloadDocumentation.fieldWithPath("address.detailAddr").type(JsonFieldType.STRING).description(fakeDetailAddr)
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E027"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("주소 상세의 길이가 초과되었습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("UserUpdateFakeDetailAddrDto.Request"))
                                        .responseSchema(Schema.schema("OVER_LENGTH_DETAILADDR"))
                                        .build()
                        )
                ));
    }
}