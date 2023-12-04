package com.hwamok.api;

import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hwamok.admin.domain.Admin;
import com.hwamok.admin.domain.AdminRepository;
import com.hwamok.admin.domain.Role;
import com.hwamok.api.dto.admin.AdminCreateDto;
import com.hwamok.api.dto.admin.AdminUpdateDto;
import com.hwamok.utils.CreateValueUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AdminRepository adminRepository;

    @Test
    void 관리자_생성_성공() throws Exception {
        AdminCreateDto.Request request = new AdminCreateDto.Request("test123", "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN));

        mockMvc.perform(RestDocumentationRequestBuilders.post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success"))
                .andDo(document("관리자 생성 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Admin")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("loginId").type(JsonFieldType.STRING).description("test123"),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("test@test.com"),
                                                        PayloadDocumentation.fieldWithPath("roles").type(JsonFieldType.ARRAY).description("관리자 역할").attributes(Attributes.key("constraints").value("SUPER, ADMIN"))
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("S000"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("success"),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("AdminCreateDto.Request"))
                                        .responseSchema(Schema.schema("AdminCreateDto.Response"))
                                        .build()
                        )
                ));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 관리자_생성_실패__필수값_NULL_또는_공백(String parameter) throws Exception {
        AdminCreateDto.Request request = new AdminCreateDto.Request(parameter, "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN));

        mockMvc.perform(RestDocumentationRequestBuilders.post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다.")
                )
                .andDo(document("관리자 생성 실패 필수값 Null 또는 공백 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Admin")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("loginId").optional().type(JsonFieldType.STRING).description(parameter),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("test@test.com"),
                                                        PayloadDocumentation.fieldWithPath("roles").type(JsonFieldType.ARRAY).description("관리자 역할").attributes(Attributes.key("constraints").value("SUPER, ADMIN"))
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E001"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("필수 값이 누락되었습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("AdminCreateNoLoginIdDto.Request"))
                                        .responseSchema(Schema.schema("REQUIRED_PARAMETER"))
                                        .build()
                        )
                ));
    }

    @Test
    void 관리자_생성_실패__아이디_2글자_미만() throws Exception {
        String fakeLoginId = CreateValueUtil.stringLength(1);
        AdminCreateDto.Request request = new AdminCreateDto.Request(fakeLoginId, "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN));

        mockMvc.perform(RestDocumentationRequestBuilders.post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E002"),
                        jsonPath("message").value("아이디형식이 다릅니다.")
                )
                .andDo(document("관리자 생성 실패 아이디 2글자 미만 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Admin")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("loginId").type(JsonFieldType.STRING).description(fakeLoginId),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("test@test.com"),
                                                        PayloadDocumentation.fieldWithPath("roles").type(JsonFieldType.ARRAY).description("관리자 역할").attributes(Attributes.key("constraints").value("SUPER, ADMIN"))
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E002"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("아이디형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("AdminCreateFakeLoginIdDto.Request"))
                                        .responseSchema(Schema.schema("NOT_LOGINID_FORM"))
                                        .build()
                        )
                ));
    }

    @Test
    void 관리자_생성_실패__아이디_12글자_초과() throws Exception {
        String fakeLoginId = CreateValueUtil.stringLength(13);
        AdminCreateDto.Request request = new AdminCreateDto.Request(fakeLoginId, "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN));

        mockMvc.perform(RestDocumentationRequestBuilders.post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E002"),
                        jsonPath("message").value("아이디형식이 다릅니다.")
                )
                .andDo(document("관리자 생성 실패 아이디 12글자 초과 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Admin")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("loginId").type(JsonFieldType.STRING).description(fakeLoginId),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("test@test.com"),
                                                        PayloadDocumentation.fieldWithPath("roles").type(JsonFieldType.ARRAY).description("관리자 역할").attributes(Attributes.key("constraints").value("SUPER, ADMIN"))
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E002"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("아이디형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("AdminCreateFakeLoginIdDto.Request"))
                                        .responseSchema(Schema.schema("NOT_LOGINID_FORM"))
                                        .build()
                        )
                ));
    }

    @Test
    void 관리자_생성_실패__아이디_특수문자사용() throws Exception {
        String fakeLoginId = "!@#";
        AdminCreateDto.Request request = new AdminCreateDto.Request(fakeLoginId, "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN));

        mockMvc.perform(RestDocumentationRequestBuilders.post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E002"),
                        jsonPath("message").value("아이디형식이 다릅니다.")
                )
                .andDo(document("관리자 생성 실패 아이디 특수문자 사용 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Admin")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("loginId").type(JsonFieldType.STRING).description(fakeLoginId),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("test@test.com"),
                                                        PayloadDocumentation.fieldWithPath("roles").type(JsonFieldType.ARRAY).description("관리자 역할").attributes(Attributes.key("constraints").value("SUPER, ADMIN"))
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E002"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("아이디형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("AdminCreateFakeLoginIdDto.Request"))
                                        .responseSchema(Schema.schema("NOT_LOGINID_FORM"))
                                        .build()
                        )
                ));
    }

    @Test
    void 관리자_생성_실패__이름_한글_2글자_미만() throws Exception {
        String fakeName = CreateValueUtil.stringLength(1);
        AdminCreateDto.Request request = new AdminCreateDto.Request("test123", "1234", fakeName, "test@test.com", List.of(Role.SUPER, Role.ADMIN));

        mockMvc.perform(RestDocumentationRequestBuilders.post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E004"),
                        jsonPath("message").value("이름형식이 다릅니다.")
                )
                .andDo(document("관리자 생성 실패 이름 2글자 미만 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Admin")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("loginId").type(JsonFieldType.STRING).description("test123"),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description(fakeName),
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("test@test.com"),
                                                        PayloadDocumentation.fieldWithPath("roles").type(JsonFieldType.ARRAY).description("관리자 역할").attributes(Attributes.key("constraints").value("SUPER, ADMIN"))
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E004"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("이름형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("AdminCreateFakeNameDto.Request"))
                                        .responseSchema(Schema.schema("NOT_NAME_FORM"))
                                        .build()
                        )
                ));
    }

    @Test
    void 관리자_생성_실패__이름_한글_6글자_초과() throws Exception {
        String fakeName = CreateValueUtil.stringLength(7);
        AdminCreateDto.Request request = new AdminCreateDto.Request("test123", "1234", fakeName, "test@test.com", List.of(Role.SUPER, Role.ADMIN));

        mockMvc.perform(RestDocumentationRequestBuilders.post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E004"),
                        jsonPath("message").value("이름형식이 다릅니다.")
                )
                .andDo(document("관리자 생성 실패 이름 6글자 초과 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Admin")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("loginId").type(JsonFieldType.STRING).description("test123"),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description(fakeName),
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("test@test.com"),
                                                        PayloadDocumentation.fieldWithPath("roles").type(JsonFieldType.ARRAY).description("관리자 역할").attributes(Attributes.key("constraints").value("SUPER, ADMIN"))
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E004"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("이름형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("AdminCreateFakeNameDto.Request"))
                                        .responseSchema(Schema.schema("NOT_NAME_FORM"))
                                        .build()
                        )
                ));
    }

    @Test
    void 관리자_생성_실패__이름_영어_2글자_미만() throws Exception {
        String fakeName = "n";
        AdminCreateDto.Request request = new AdminCreateDto.Request("test123", "1234", fakeName, "test@test.com", List.of(Role.SUPER, Role.ADMIN));

        mockMvc.perform(RestDocumentationRequestBuilders.post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E004"),
                        jsonPath("message").value("이름형식이 다릅니다.")
                )
                .andDo(document("관리자 생성 실패 이름 영어 2글자 미만 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Admin")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("loginId").type(JsonFieldType.STRING).description("test123"),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description(fakeName),
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("test@test.com"),
                                                        PayloadDocumentation.fieldWithPath("roles").type(JsonFieldType.ARRAY).description("관리자 역할").attributes(Attributes.key("constraints").value("SUPER, ADMIN"))
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E004"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("이름형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("AdminCreateFakeNameDto.Request"))
                                        .responseSchema(Schema.schema("NOT_NAME_FORM"))
                                        .build()
                        )
                ));
    }

    @Test
    void 관리자_생성_실패__이름_영어_20글자_초과() throws Exception {
        String fakeName = "namenamenamenamenamen";
        AdminCreateDto.Request request = new AdminCreateDto.Request("test123", "1234", fakeName, "test@test.com", List.of(Role.SUPER, Role.ADMIN));

        mockMvc.perform(RestDocumentationRequestBuilders.post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E004"),
                        jsonPath("message").value("이름형식이 다릅니다.")
                )
                .andDo(document("관리자 생성 실패 이름 영어 20글자 초과 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Admin")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("loginId").type(JsonFieldType.STRING).description("test123"),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description(fakeName),
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("test@test.com"),
                                                        PayloadDocumentation.fieldWithPath("roles").type(JsonFieldType.ARRAY).description("관리자 역할").attributes(Attributes.key("constraints").value("SUPER, ADMIN"))
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E004"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("이름형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("AdminCreateFakeNameDto.Request"))
                                        .responseSchema(Schema.schema("NOT_NAME_FORM"))
                                        .build()
                        )
                ));
    }

    @Test
    void 관리자_생성_실패__이름_한글영어혼용() throws Exception {
        String fakeName = "이름name";
        AdminCreateDto.Request request = new AdminCreateDto.Request("test123", "1234", fakeName, "test@test.com", List.of(Role.SUPER, Role.ADMIN));

        mockMvc.perform(RestDocumentationRequestBuilders.post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E004"),
                        jsonPath("message").value("이름형식이 다릅니다.")
                )
                .andDo(document("관리자 생성 실패 이름 한글 영어 혼용 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Admin")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("loginId").type(JsonFieldType.STRING).description("test123"),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description(fakeName),
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("test@test.com"),
                                                        PayloadDocumentation.fieldWithPath("roles").type(JsonFieldType.ARRAY).description("관리자 역할").attributes(Attributes.key("constraints").value("SUPER, ADMIN"))
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E004"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("이름형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("AdminCreateFakeNameDto.Request"))
                                        .responseSchema(Schema.schema("NOT_NAME_FORM"))
                                        .build()
                        )
                ));
    }

    @Test
    void 관리자_생성_실패__이름_특수문자사용() throws Exception{
        String fakeName = "이름!";
        AdminCreateDto.Request request = new AdminCreateDto.Request("test123", "1234", fakeName, "test@test.com", List.of(Role.SUPER, Role.ADMIN));

        mockMvc.perform(RestDocumentationRequestBuilders.post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E004"),
                        jsonPath("message").value("이름형식이 다릅니다.")
                )
                .andDo(document("관리자 생성 실패 이름 특수문자 사용 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Admin")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("loginId").type(JsonFieldType.STRING).description("test123"),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description(fakeName),
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("test@test.com"),
                                                        PayloadDocumentation.fieldWithPath("roles").type(JsonFieldType.ARRAY).description("관리자 역할").attributes(Attributes.key("constraints").value("SUPER, ADMIN"))
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E004"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("이름형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("AdminCreateFakeNameDto.Request"))
                                        .responseSchema(Schema.schema("NOT_NAME_FORM"))
                                        .build()
                        )
                ));
    }

    @Test
    void 관리자_생성_실패__이메일_골뱅이없음() throws Exception {
        String fakeEmail = "testtest.com";
        AdminCreateDto.Request request = new AdminCreateDto.Request("test123", "1234", "이름", fakeEmail, List.of(Role.SUPER, Role.ADMIN));

        mockMvc.perform(RestDocumentationRequestBuilders.post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E003"),
                        jsonPath("message").value("이메일형식이 다릅니다.")
                )
                .andDo(document("관리자 생성 실패 이메일 골뱅이 없음 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Admin")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("loginId").type(JsonFieldType.STRING).description("test123"),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description(fakeEmail),
                                                        PayloadDocumentation.fieldWithPath("roles").type(JsonFieldType.ARRAY).description("관리자 역할").attributes(Attributes.key("constraints").value("SUPER, ADMIN"))
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E003"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("이메일형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("AdminCreateFakeEmailDto.Request"))
                                        .responseSchema(Schema.schema("NOT_EMAIL_FORM"))
                                        .build()
                        )
                ));
    }

    @Test
    void 관리자_생성_실패__이메일_점없음() throws Exception {
        String fakeEmail = "test@testcom";
        AdminCreateDto.Request request = new AdminCreateDto.Request("test123", "1234", "이름", fakeEmail, List.of(Role.SUPER, Role.ADMIN));

        mockMvc.perform(RestDocumentationRequestBuilders.post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E003"),
                        jsonPath("message").value("이메일형식이 다릅니다.")
                )
                .andDo(document("관리자 생성 실패 이메일 점 없음 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Admin")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("loginId").type(JsonFieldType.STRING).description("test123"),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description(fakeEmail),
                                                        PayloadDocumentation.fieldWithPath("roles").type(JsonFieldType.ARRAY).description("관리자 역할").attributes(Attributes.key("constraints").value("SUPER, ADMIN"))
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E003"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("이메일형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("AdminCreateFakeEmailDto.Request"))
                                        .responseSchema(Schema.schema("NOT_EMAIL_FORM"))
                                        .build()
                        )
                ));
    }

    @Test
    void 관리자_생성_실패__이메일_50글자초과() throws Exception {
        String fakeEmail = "testtesttesttesttesttest@testtesttesttesttest11.com";
        AdminCreateDto.Request request = new AdminCreateDto.Request("test123", "1234", "이름", fakeEmail, List.of(Role.SUPER, Role.ADMIN));

        mockMvc.perform(RestDocumentationRequestBuilders.post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E021"),
                        jsonPath("message").value("이메일의 길이가 초과되었습니다.")
                )
                .andDo(document("관리자 생성 실패 이메일 50글자 초과 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Admin")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("loginId").type(JsonFieldType.STRING).description("test123"),
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description(fakeEmail),
                                                        PayloadDocumentation.fieldWithPath("roles").type(JsonFieldType.ARRAY).description("관리자 역할").attributes(Attributes.key("constraints").value("SUPER, ADMIN"))
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E003"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("이메일형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("AdminCreateFakeEmailDto.Request"))
                                        .responseSchema(Schema.schema("NOT_EMAIL_FORM"))
                                        .build()
                        )
                ));
    }

    @Test
    void 관리자_단건조회_성공() throws Exception {
        Admin admin = adminRepository.save(new Admin("test1234", "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN)));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/admin/{id}", admin.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success"),
                        jsonPath("data.loginId").value("test1234"),
                        jsonPath("data.name").value("이름"),
                        jsonPath("data.email").value("test@test.com"),
                        jsonPath("data.roles").isArray()
                )
                .andDo(document("관리자 단건 조회 API",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                ResourceDocumentation.resource(
                        new ResourceSnippetParametersBuilder()
                                .tag("Admin")
                                .responseFields(
                                        List.of(
                                                PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("S000"),
                                                PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("success"),
                                                PayloadDocumentation.fieldWithPath("data.id").type(JsonFieldType.NUMBER).description(1),
                                                PayloadDocumentation.fieldWithPath("data.loginId").type(JsonFieldType.STRING).description("test1234"),
                                                PayloadDocumentation.fieldWithPath("data.name").type(JsonFieldType.STRING).description("이름"),
                                                PayloadDocumentation.fieldWithPath("data.email").type(JsonFieldType.STRING).description("test@test.com"),
                                                PayloadDocumentation.fieldWithPath("data.status").type(JsonFieldType.STRING).description("ACTIVATED"),
                                                PayloadDocumentation.fieldWithPath("data.roles").type(JsonFieldType.ARRAY).description("관리자 역할").attributes(Attributes.key("constraints").value("SUPER, ADMIN")),
                                                PayloadDocumentation.fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("The timestamp when the data was created")
                                        )
                                )
                                .responseSchema(Schema.schema("AdminCreateDto.Response"))
                                .build()
                )
        ));
    }

    @Test
    void 관리자_단건조회_실패__관리자정보_없음() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/admin/{id}", -1L))

                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E005"),
                        jsonPath("message").value("관리자정보를 찾을 수 없습니다.")
                )
                .andDo(document("관리자 단건 조회 실패 관리자 정보 없음 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Admin")
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E005"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("관리자정보를 찾을 수 없습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL").type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .responseSchema(Schema.schema("NOT_FOUND_ADMIN"))
                                        .build()
                        )
                ));
    }

    @Test
    void 관리자_리스트조회_성공() throws Exception {
        Admin admin1 = adminRepository.save(new Admin("test1234", "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN)));
        Admin admin2 = adminRepository.save(new Admin("test12345", "12345", "이름이", "test1@test1.com", List.of(Role.SUPER, Role.ADMIN)));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/admin/list"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                jsonPath("message").value("success"))
                .andExpectAll(관리자_리스트_검증(1, admin1))
                .andExpectAll(  관리자_리스트_검증(2, admin2)
                )
                .andDo(document("관리자 리스트 조회 API",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                ResourceDocumentation.resource(
                        new ResourceSnippetParametersBuilder()
                                .tag("Admin")
                                .responseFields(
                                        List.of(
                                                PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("S000"),
                                                PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("success"),
                                                PayloadDocumentation.fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description(1),
                                                PayloadDocumentation.fieldWithPath("data[].loginId").type(JsonFieldType.STRING).description("test1234"),
                                                PayloadDocumentation.fieldWithPath("data[].name").type(JsonFieldType.STRING).description("이름"),
                                                PayloadDocumentation.fieldWithPath("data[].email").type(JsonFieldType.STRING).description("test@test.com"),
                                                PayloadDocumentation.fieldWithPath("data[].status").type(JsonFieldType.STRING).description("ACTIVATED"),
                                                PayloadDocumentation.fieldWithPath("data[].roles").type(JsonFieldType.ARRAY).description("관리자 역할").attributes(Attributes.key("constraints").value("SUPER, ADMIN")),
                                                PayloadDocumentation.fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("The timestamp when the data was created")
                                        )
                                )
                                .responseSchema(Schema.schema("AdminCreateDto.Response"))
                                .build()
                )
        ));
    }

    @Test
    void 관리자_수정_성공() throws Exception {
        Admin admin = adminRepository.save(new Admin("test1234", "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN)));
        AdminUpdateDto.Request request = new AdminUpdateDto.Request("update1234", "수정이름", "update@update.com");

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/admin/{id}", admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success")
                )
                .andDo(document("관리자 수정 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Admin")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("update1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("수정이름"),
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("update@update.com")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("S000"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("success"),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("AdminUpdateDto.Request"))
                                        .responseSchema(Schema.schema("AdminUpdateDto.Response"))
                                        .build()
                        )
                ));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 관리자_수정_실패__필수값_null_또는_공백(String parameter) throws Exception {
        Admin admin = adminRepository.save(new Admin("test1234", "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN)));
        AdminUpdateDto.Request request = new AdminUpdateDto.Request("1234", parameter, "update@update.com");

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/admin/{id}", admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다.")
                )
                .andDo(document("관리자 수정 실패 필수값 null 또는 공백 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Admin")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("update1234"),
                                                        PayloadDocumentation.fieldWithPath("name").optional().type(JsonFieldType.STRING).description(parameter),
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("update@update.com")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E001"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("필수 값이 누락되었습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("AdminUpdateNoNameDto.Request"))
                                        .responseSchema(Schema.schema("REQUIRED_PARAMETER"))
                                        .build()
                        )
                ));
    }

    @Test
    void 관리자_수정_실패__이름_한글_2글자_미만() throws Exception {
        String fakeName = CreateValueUtil.stringLength(1);
        Admin admin = adminRepository.save(new Admin("test1234", "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN)));
        AdminUpdateDto.Request request = new AdminUpdateDto.Request("1234", fakeName, "update@update.com");

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/admin/{id}", admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E004"),
                        jsonPath("message").value("이름형식이 다릅니다.")
                )
                .andDo(document("관리자 수정 실패 이름 한글 2글자 미만 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Admin")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("update1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description(fakeName),
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("update@update.com")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E004"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("이름형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("AdminUpdateFakeNameDto.Request"))
                                        .responseSchema(Schema.schema("NOT_NAME_FORM"))
                                        .build()
                        )
                ));
    }

    @Test
    void 관리자_수정_실패__이름_한글_6글자_초과() throws Exception {
        String fakeName = CreateValueUtil.stringLength(7);
        Admin admin = adminRepository.save(new Admin("test1234", "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN)));
        AdminUpdateDto.Request request = new AdminUpdateDto.Request("1234", fakeName, "update@update.com");

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/admin/{id}", admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E004"),
                        jsonPath("message").value("이름형식이 다릅니다.")
                )
                .andDo(document("관리자 수정 실패 이름 한글 6글자 초과 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Admin")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("update1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description(fakeName),
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("update@update.com")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E004"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("이름형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("AdminUpdateFakeNameDto.Request"))
                                        .responseSchema(Schema.schema("NOT_NAME_FORM"))
                                        .build()
                        )
                ));
    }

    @Test
    void 관리자_수정_실패__이름_영어_2글자_미만() throws Exception {
        String fakeName = "n";
        Admin admin = adminRepository.save(new Admin("test1234", "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN)));
        AdminUpdateDto.Request request = new AdminUpdateDto.Request("1234", fakeName, "update@update.com");

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/admin/{id}", admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E004"),
                        jsonPath("message").value("이름형식이 다릅니다.")
                )
                .andDo(document("관리자 수정 실패 이름 영어 2글자 미만 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Admin")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("update1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description(fakeName),
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("update@update.com")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E004"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("이름형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("AdminUpdateFakeNameDto.Request"))
                                        .responseSchema(Schema.schema("NOT_NAME_FORM"))
                                        .build()
                        )
                ));
    }

    @Test
    void 관리자_수정_실패__이름_영어_20글자_초과() throws Exception {
        String fakeName = "namenamenamenamenamen";
        Admin admin = adminRepository.save(new Admin("test1234", "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN)));
        AdminUpdateDto.Request request = new AdminUpdateDto.Request("1234", fakeName, "update@update.com");

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/admin/{id}", admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E004"),
                        jsonPath("message").value("이름형식이 다릅니다.")
                )
                .andDo(document("관리자 수정 실패 이름 한글 20글자 초과 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Admin")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("update1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description(fakeName),
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("update@update.com")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E004"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("이름형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("AdminUpdateFakeNameDto.Request"))
                                        .responseSchema(Schema.schema("NOT_NAME_FORM"))
                                        .build()
                        )
                ));
    }

    @Test
    void 관리자_수정_실패__이름_한글영어혼용() throws Exception {
        String fakeName = "이름name";
        Admin admin = adminRepository.save(new Admin("test1234", "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN)));
        AdminUpdateDto.Request request = new AdminUpdateDto.Request("1234", fakeName, "update@update.com");

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/admin/{id}", admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E004"),
                        jsonPath("message").value("이름형식이 다릅니다.")
                )
                .andDo(document("관리자 수정 실패 이름 한글영어 혼용 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Admin")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("update1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description(fakeName),
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("update@update.com")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E004"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("이름형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("AdminUpdateFakeNameDto.Request"))
                                        .responseSchema(Schema.schema("NOT_NAME_FORM"))
                                        .build()
                        )
                ));
    }

    @Test
    void 관리자_수정_실패__이름_특수문자사용() throws Exception {
        String fakeName = "이름!";
        Admin admin = adminRepository.save(new Admin("test1234", "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN)));
        AdminUpdateDto.Request request = new AdminUpdateDto.Request("1234", fakeName, "update@update.com");

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/admin/{id}", admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E004"),
                        jsonPath("message").value("이름형식이 다릅니다.")
                )
                .andDo(document("관리자 수정 실패 이름 특수문자 사용 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Admin")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("update1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description(fakeName),
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("update@update.com")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E004"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("이름형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("AdminUpdateFakeNameDto.Request"))
                                        .responseSchema(Schema.schema("NOT_NAME_FORM"))
                                        .build()
                        )
                ));
    }

    @Test
    void 관리자_수정_실패__이메일_50글자초과() throws Exception {
        String fakeEmail = "testtesttesttesttest@testtesttesttesttesttest11.com";
        Admin admin = adminRepository.save(new Admin("test1234", "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN)));
        AdminUpdateDto.Request request = new AdminUpdateDto.Request("1234", "이름", fakeEmail);

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/admin/{id}", admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E021"),
                        jsonPath("message").value("이메일의 길이가 초과되었습니다.")
                )
                .andDo(document("관리자 수정 실패 이메일 50글자 초과 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Admin")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("update1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description(fakeEmail)
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E021"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("이메일의 길이가 초과되었습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("AdminUpdateFakeEmailDto.Request"))
                                        .responseSchema(Schema.schema("OVER_LENGTH_EMAIL"))
                                        .build()
                        )
                ));
    }

    @Test
    void 관리자_수정_실패__이메일_골뱅이없음() throws Exception {
        String fakeEmail = "testtest.com";
        Admin admin = adminRepository.save(new Admin("test1234", "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN)));
        AdminUpdateDto.Request request = new AdminUpdateDto.Request("1234", "이름", fakeEmail);

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/admin/{id}", admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E003"),
                        jsonPath("message").value("이메일형식이 다릅니다.")
                )
                .andDo(document("관리자 수정 실패 이메일 골뱅이 없음 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Admin")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("update1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description(fakeEmail)
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E003"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("이메일형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("AdminUpdateFakeEmailDto.Request"))
                                        .responseSchema(Schema.schema("NOT_EMAIL_FORM"))
                                        .build()
                        )
                ));
    }

    @Test
    void 관리자_수정_실패__이메일_점없음() throws Exception {
        String fakeEmail = "test@testcom";
        Admin admin = adminRepository.save(new Admin("test1234", "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN)));
        AdminUpdateDto.Request request = new AdminUpdateDto.Request("1234", "이름", fakeEmail);

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/admin/{id}", admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E003"),
                        jsonPath("message").value("이메일형식이 다릅니다.")
                )
                .andDo(document("관리자 수정 실패 이메일 점 없음 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Admin")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("update1234"),
                                                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                                        PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description(fakeEmail)
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E003"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("이메일형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .requestSchema(Schema.schema("AdminUpdateFakeEmailDto.Request"))
                                        .responseSchema(Schema.schema("NOT_EMAIL_FORM"))
                                        .build()
                        )
                ));

    }

    @Test
    void 관리자_삭제_성공() throws Exception {
        Admin admin = adminRepository.save(new Admin("test1234", "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN)));

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/admin/{id}", admin.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success")
                )
                .andDo(document("관리자 삭제 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Admin")
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("S000"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("success"),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .responseSchema(Schema.schema("AdminDeleteDto.Response"))
                                        .build()
                        )
                ));
    }

    @Test
    void 관리자_삭제_실패__관리자정보_없음() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/admin/{id}", -1L))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E005"),
                        jsonPath("message").value("관리자정보를 찾을 수 없습니다.")
                )
                .andDo(document("관리자 삭제 실패 관리자 정보 없음 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Admin")
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E005"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("관리자정보를 찾을 수 없습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").optional().type(JsonFieldType.NULL).description("NULL")
                                                )
                                        )
                                        .responseSchema(Schema.schema("NOT_FOUND_ADMIN"))
                                        .build()
                        )
                ));
    }

    private ResultMatcher[] 관리자_리스트_검증(int index, final Admin admin) {
        return List.of(
                        jsonPath("$.data.[" + index + "].id").value(admin.getId()),
                        jsonPath("$.data.[" + index + "].loginId").value(admin.getLoginId()),
                        jsonPath("$.data.[" + index + "].name").value(admin.getName()),
                        jsonPath("$.data.[" + index + "].email").value(admin.getEmail()),
                        jsonPath("$.data.[" + index + "].roles").isArray())
                .toArray(ResultMatcher[]::new);
    }
}