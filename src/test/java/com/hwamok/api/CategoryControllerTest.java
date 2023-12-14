package com.hwamok.api;

import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hwamok.api.dto.category.CategoryCreateDto;
import com.hwamok.api.dto.category.CategoryUpdateDto;
import com.hwamok.category.domain.Category;
import com.hwamok.category.domain.CategoryRepository;
import fixture.CategoryFixture;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category1;

    @BeforeEach
    private void setup() {
        category1 = CategoryFixture.createCategory();
        categoryRepository.save(category1);
    }

    @Test
    void 카테고리_생성_성공() throws Exception {
        CategoryCreateDto.Request request = new CategoryCreateDto.Request("식품", "CA003", "과자", -1L);

        ResultActions resultActions = mockMvc.perform(post("/category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));

        resultActions.andExpect(status().isCreated())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success")
                )
                .andDo(document("카테고리생성 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Category")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("branch")
                                                                .type(JsonFieldType.STRING).description("식품"),
                                                        PayloadDocumentation.fieldWithPath("code")
                                                                .type(JsonFieldType.STRING).description("CA003"),
                                                        PayloadDocumentation.fieldWithPath("name")
                                                                .type(JsonFieldType.STRING).description("과자"),
                                                        PayloadDocumentation.fieldWithPath("parentId")
                                                                .type(JsonFieldType.NUMBER).description("-1L")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code")
                                                                .type(JsonFieldType.STRING).description("S000"),
                                                        PayloadDocumentation.fieldWithPath("message")
                                                                .type(JsonFieldType.STRING).description("success"),
                                                        PayloadDocumentation.fieldWithPath("data")
                                                                .type(JsonFieldType.NULL).description("null")
                                                )
                                        )
                                        .requestSchema(Schema.schema("CategoryCreateDTO.Request"))
                                        .build()
                        )
                ));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 카테고리_생성_실패__이름이_null_또는_공백(String name) throws Exception {
        CategoryCreateDto.Request request = new CategoryCreateDto.Request("식품", "CA001", name, -1L);

        ResultActions resultActions = mockMvc.perform(post("/category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));

        resultActions.andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다.")
                )
                .andDo(document("카테고리 생성 실패 제목이 NULL API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Category")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("branch")
                                                                .type(JsonFieldType.STRING).description("식품"),
                                                        PayloadDocumentation.fieldWithPath("code")
                                                                .type(JsonFieldType.STRING).description("CA003"),
                                                        PayloadDocumentation.fieldWithPath("name").optional()
                                                                .type(JsonFieldType.STRING).description(name),
                                                        PayloadDocumentation.fieldWithPath("parentId")
                                                                .type(JsonFieldType.NUMBER).description("-1L")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code")
                                                                .type(JsonFieldType.STRING).description("E001"),
                                                        PayloadDocumentation.fieldWithPath("message")
                                                                .type(JsonFieldType.STRING).description("필수 값이 누락되었습니다."),
                                                        PayloadDocumentation.fieldWithPath("data")
                                                                .type(JsonFieldType.NULL).description("null")
                                                )
                                        )
                                        .requestSchema(Schema.schema("CategoryCreateDTO.Request"))
                                        .build()
                        )
                ));
    }

    @Test
    void 브랜치로_모든_카테고리_가져오기_성공() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/category/branch")
                        .param("branch", "식품"))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("data.length()").value("1")
                )
                .andDo(document("브랜치로 카테고리 가져오기 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Category")
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code")
                                                                .type(JsonFieldType.STRING).description("S000"),
                                                        PayloadDocumentation.fieldWithPath("message")
                                                                .type(JsonFieldType.STRING).description("success"),
                                                        PayloadDocumentation.fieldWithPath("data[].id")
                                                                .type(JsonFieldType.NUMBER).description("1"),
                                                        PayloadDocumentation.fieldWithPath("data[].createdAt")
                                                                .type(JsonFieldType.STRING).description("The timestamp when the data was created"),
                                                        PayloadDocumentation.fieldWithPath("data[].branch")
                                                                .type(JsonFieldType.STRING).description("식품"),
                                                        PayloadDocumentation.fieldWithPath("data[].code")
                                                                .type(JsonFieldType.STRING).description("CA001"),
                                                        PayloadDocumentation.fieldWithPath("data[].name")
                                                                .type(JsonFieldType.STRING).description("돼지고기"),
                                                        PayloadDocumentation.fieldWithPath("data[].parentCategory").optional()
                                                                .type(JsonFieldType.OBJECT).description("parentCategory"),
                                                        PayloadDocumentation.fieldWithPath("data[].subCategory")
                                                                .type(JsonFieldType.ARRAY).description("subcategory"),
                                                        PayloadDocumentation.fieldWithPath("data[].level")
                                                                .type(JsonFieldType.NUMBER).description("0"),
                                                        PayloadDocumentation.fieldWithPath("data[].products")
                                                                .type(JsonFieldType.ARRAY).description("products"),
                                                        PayloadDocumentation.fieldWithPath("data[].status")
                                                                .type(JsonFieldType.STRING).description("ACTIVATE"),
                                                        PayloadDocumentation.fieldWithPath("data[].parentId").optional()
                                                                .type(JsonFieldType.NUMBER).description("null")
                                                )
                                        )
                                        .build()
                        )
                ));
    }

    @Test
    void 이름으로_카테고리_가져오기_성공() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/category/name")
                        .param("name", "돼지고기"))
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("code").value("S000"),
                        jsonPath("data.code").value("CA001"),
                        jsonPath("data.level").value(0),
                        jsonPath("data.branch").value("식품")
                )
                .andDo(document("이름으로 카테고리 가져오기 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Category")
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code")
                                                                .type(JsonFieldType.STRING).description("S000"),
                                                        PayloadDocumentation.fieldWithPath("message")
                                                                .type(JsonFieldType.STRING).description("success"),
                                                        PayloadDocumentation.fieldWithPath("data.branch")
                                                                .type(JsonFieldType.STRING).description("식품"),
                                                        PayloadDocumentation.fieldWithPath("data.code")
                                                                .type(JsonFieldType.STRING).description("CA001"),
                                                        PayloadDocumentation.fieldWithPath("data.name")
                                                                .type(JsonFieldType.STRING).description("돼지고기"),
                                                        PayloadDocumentation.fieldWithPath("data.level")
                                                                .type(JsonFieldType.NUMBER).description("0"),
                                                        PayloadDocumentation.fieldWithPath("data.parentId").optional()
                                                                .type(JsonFieldType.NUMBER).description("null")
                                                )
                                        )
                                        .responseSchema(Schema.schema("CategoryReadDTO.Response"))
                                        .build()
                        )
                ));
    }

    @Test
    void 이름으로_카테고리_가져오기_실패__없는_이름() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/category/name")
                        .param("name", "밥"))
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("code").value("E014"),
                        jsonPath("message").value("카테고리를 찾을 수 없습니다.")
                )
                .andDo(document("이름으로 카테고리 가져오기 실패 없는 이름 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Category")
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code")
                                                                .type(JsonFieldType.STRING).description("E014"),
                                                        PayloadDocumentation.fieldWithPath("message")
                                                                .type(JsonFieldType.STRING).description("카테고리를 찾을 수 없습니다."),
                                                        PayloadDocumentation.fieldWithPath("data")
                                                                .type(JsonFieldType.NULL).description("null")
                                                )
                                        )
                                        .build()
                        )
                ));
    }

    @Test
    void 코드로_카테고리_가져오기_성공() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/category/code")
                        .param("code", "CA001"))
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("code").value("S000"),
                        jsonPath("data.name").value("돼지고기"),
                        jsonPath("data.level").value(0),
                        jsonPath("data.branch").value("식품")
                )
                .andDo(document("코드로 카테고리 가져오기 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Category")
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code")
                                                                .type(JsonFieldType.STRING).description("S000"),
                                                        PayloadDocumentation.fieldWithPath("message")
                                                                .type(JsonFieldType.STRING).description("success"),
                                                        PayloadDocumentation.fieldWithPath("data.branch")
                                                                .type(JsonFieldType.STRING).description("식품"),
                                                        PayloadDocumentation.fieldWithPath("data.code")
                                                                .type(JsonFieldType.STRING).description("CA001"),
                                                        PayloadDocumentation.fieldWithPath("data.name")
                                                                .type(JsonFieldType.STRING).description("돼지고기"),
                                                        PayloadDocumentation.fieldWithPath("data.level")
                                                                .type(JsonFieldType.NUMBER).description("0"),
                                                        PayloadDocumentation.fieldWithPath("data.parentId").optional()
                                                                .type(JsonFieldType.NUMBER).description("null")
                                                )
                                        )
                                        .responseSchema(Schema.schema("CategoryReadDTO.Response"))
                                        .build()
                        )
                ));
    }

    @Test
    void 코드로_카테고리_가져오기_실패__없는_코드() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/category/code")
                        .param("code", "FA000"))
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("code").value("E014"),
                        jsonPath("message").value("카테고리를 찾을 수 없습니다.")
                )
                .andDo(document("코드로 카테고리 가져오기 실패 없는 코드 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Category")
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code")
                                                                .type(JsonFieldType.STRING).description("E014"),
                                                        PayloadDocumentation.fieldWithPath("message")
                                                                .type(JsonFieldType.STRING).description("카테고리를 찾을 수 없습니다."),
                                                        PayloadDocumentation.fieldWithPath("data")
                                                                .type(JsonFieldType.NULL).description("null")
                                                )
                                        )
                                        .build()
                        )
                ));
    }

    @Test
    void 카테고리_업데이트하기_성공() throws Exception {
        CategoryUpdateDto.Request request = new CategoryUpdateDto.Request("가구", "FA001", "의자");

        mockMvc.perform(patch("/category/{id}", category1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("code").value("S000"))
                .andDo(document("카테고리 업데이트 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Category")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("branch")
                                                                .type(JsonFieldType.STRING).description("가구"),
                                                        PayloadDocumentation.fieldWithPath("code")
                                                                .type(JsonFieldType.STRING).description("FA001"),
                                                        PayloadDocumentation.fieldWithPath("name")
                                                                .type(JsonFieldType.STRING).description("의자")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code")
                                                                .type(JsonFieldType.STRING).description("S000"),
                                                        PayloadDocumentation.fieldWithPath("message")
                                                                .type(JsonFieldType.STRING).description("success"),
                                                        PayloadDocumentation.fieldWithPath("data")
                                                                .type(JsonFieldType.NULL).description("null")
                                                )
                                        )
                                        .requestSchema(Schema.schema("CategoryUpdateDTO.Request"))
                                        .build()
                        )
                ));
    }

    @Test
    void 카테고리_업데이트하기_실패__브랜치_null() throws Exception {
        CategoryUpdateDto.Request request = new CategoryUpdateDto.Request(null, "FA001", "의자");

        mockMvc.perform(patch("/category/{id}", category1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다."))
                .andDo(document("카테고리 업데이트 실패 브랜치 null API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Category")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("branch").optional()
                                                                .type(JsonFieldType.STRING).description("null"),
                                                        PayloadDocumentation.fieldWithPath("code")
                                                                .type(JsonFieldType.STRING).description("FA001"),
                                                        PayloadDocumentation.fieldWithPath("name")
                                                                .type(JsonFieldType.STRING).description("의자")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code")
                                                                .type(JsonFieldType.STRING).description("E001"),
                                                        PayloadDocumentation.fieldWithPath("message")
                                                                .type(JsonFieldType.STRING).description("필수 값이 누락되었습니다."),
                                                        PayloadDocumentation.fieldWithPath("data")
                                                                .type(JsonFieldType.NULL).description("null")
                                                )
                                        )
                                        .build()
                        )
                ));
    }

    @Test
    void 카테고리_업데이트하기_실패__없는_id() throws Exception {
        CategoryUpdateDto.Request request = new CategoryUpdateDto.Request("가구", "FA001", "의자");

        mockMvc.perform(patch("/category/{id}", -1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("code").value("E014"),
                        jsonPath("message").value("카테고리를 찾을 수 없습니다."))
                .andDo(document("카테고리 업데이트 실패 없는 ID API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Category")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("branch").optional()
                                                                .type(JsonFieldType.STRING).description("null"),
                                                        PayloadDocumentation.fieldWithPath("code")
                                                                .type(JsonFieldType.STRING).description("FA001"),
                                                        PayloadDocumentation.fieldWithPath("name")
                                                                .type(JsonFieldType.STRING).description("의자")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code")
                                                                .type(JsonFieldType.STRING).description("E014"),
                                                        PayloadDocumentation.fieldWithPath("message")
                                                                .type(JsonFieldType.STRING).description("카테고리를 찾을 수 없습니다."),
                                                        PayloadDocumentation.fieldWithPath("data")
                                                                .type(JsonFieldType.NULL).description("null")
                                                )
                                        )
                                        .build()
                        )
                ));
    }

    @Test
    void 카테고리_삭제하기_성공() throws Exception {
        mockMvc.perform(delete("/category/{id}", category1.getId()))
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("code").value("S000"))
                .andDo(document("카테고리 삭제 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Category")
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code")
                                                                .type(JsonFieldType.STRING).description("S000"),
                                                        PayloadDocumentation.fieldWithPath("message")
                                                                .type(JsonFieldType.STRING).description("success"),
                                                        PayloadDocumentation.fieldWithPath("data")
                                                                .type(JsonFieldType.NULL).description("null")
                                                )
                                        )
                                        .build()
                        )
                ));
    }

    @Test
    void 카테고리_삭제하기_실패__없는_id() throws Exception {
        mockMvc.perform(delete("/category/{id}", -1))
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("code").value("E014"),
                        jsonPath("message").value("카테고리를 찾을 수 없습니다."))
                .andDo(document("카테고리 삭제 실패 없는 ID API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Category")
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code")
                                                                .type(JsonFieldType.STRING).description("E014"),
                                                        PayloadDocumentation.fieldWithPath("message")
                                                                .type(JsonFieldType.STRING).description("카테고리를 찾을 수 없습니다."),
                                                        PayloadDocumentation.fieldWithPath("data")
                                                                .type(JsonFieldType.NULL).description("null")
                                                )
                                        )
                                        .build()
                        )
                ));
    }
}