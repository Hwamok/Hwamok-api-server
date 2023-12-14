package com.hwamok.api;

import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hwamok.admin.domain.Admin;
import com.hwamok.admin.domain.AdminRepository;
import com.hwamok.api.dto.auth.AdminLoginDto;
import com.hwamok.utils.Role;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
class AdminAuthControllerTest {
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
                )
                .andDo(document("관리자로그인 API",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                ResourceDocumentation.resource(
                        new ResourceSnippetParametersBuilder()
                                .tag("Auth")
                                .requestFields(
                                        List.of(
                                                PayloadDocumentation.fieldWithPath("loginId").type(JsonFieldType.STRING).description("loginId"),
                                                PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234")
                                        )
                                )
                                .responseFields(
                                        List.of(
                                                PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("S000"),
                                                PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("success"),
                                                PayloadDocumentation.fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("access token"),
                                                PayloadDocumentation.fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("refresh token")
                                        )
                                )
                                .requestSchema(Schema.schema("UserLoginDto.Request"))
                                .responseSchema(Schema.schema("UserLoginDto.Response"))
                                .build()
                )
        ));
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
                )
                .andDo(document("관리자로그인 실패, 필수값_NULL_또는_공백",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                ResourceDocumentation.resource(
                        new ResourceSnippetParametersBuilder()
                                .tag("Auth")
                                .requestFields(
                                        List.of(
                                                PayloadDocumentation.fieldWithPath("loginId").type(JsonFieldType.STRING).optional().description(parameter),
                                                PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234")
                                        )
                                )
                                .responseFields(
                                        List.of(
                                                PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E005"),
                                                PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("관리자정보를 찾을 수 없습니다."),
                                                PayloadDocumentation.fieldWithPath("data").type(JsonFieldType.NULL).description("NULL")
                                        )
                                )
                                .requestSchema(Schema.schema("AdminLoginDto.Request"))
                                .responseSchema(Schema.schema("NOT_FOUND_ADMIN"))
                                .build()
                )
        ));
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
                )
                .andDo(document("관리자로그인 실패, 관리자정보 없음",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Auth")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("loginId").type(JsonFieldType.STRING).optional().description("loginId"),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E005"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("관리자정보를 찾을 수 없습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("AdminLoginDto.Request"))
                                        .responseSchema(Schema.schema("NOT_FOUND_ADMIN"))
                                        .build()
                        )
                ));
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
                )
                .andDo(document("관리자로그인 실패, 비밀번호 불일치",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Auth")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("loginId").type(JsonFieldType.STRING).optional().description("loginId"),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description(fakePassword)
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E005"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("관리자정보를 찾을 수 없습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("AdminLoginDto.Request"))
                                        .responseSchema(Schema.schema("NOT_FOUND_ADMIN"))
                                        .build()
                        )
                ));
    }
}