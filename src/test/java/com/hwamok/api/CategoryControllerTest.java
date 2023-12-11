package com.hwamok.api;

import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hwamok.api.dto.category.CategoryCreateDTO;
import com.hwamok.api.dto.category.CategoryUpdateDTO;
import com.hwamok.category.domain.Category;
import com.hwamok.category.domain.CategoryRepository;
import fixture.CategoryFixture;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
        CategoryCreateDTO.Request request = new CategoryCreateDTO.Request("식품", "CA003", "과자", null);

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
                                                                .type(JsonFieldType.NULL).description("null")
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

    @Test
    void 카테고리_생성_실패__제목이_null() throws Exception {
        CategoryCreateDTO.Request request = new CategoryCreateDTO.Request("식품", "CA001", null, null);

        ResultActions resultActions = mockMvc.perform(post("/category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));

        resultActions.andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다.")
                );
    }

    @Test
    void 브랜치로_모든_카테고리_가져오기_성공() throws  Exception {
        mockMvc.perform(get("/category/branch")
                        .param("branch", "식품"))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("data.length()").value("1")
                );
    }

    @Test
    void 이름으로_카테고리_가져오기_성공() throws Exception {
        mockMvc.perform(get("/category/name")
                        .param("name", "돼지고기"))
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("code").value("S000"),
                        jsonPath("data.code").value("CA001"),
                        jsonPath("data.level").value(0),
                        jsonPath("data.branch").value("식품")
                        );
    }

    @Test
    void 이름으로_카테고리_가져오기_실패__없는_이름() throws Exception {
        mockMvc.perform(get("/category/name")
                        .param("name", "밥"))
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("code").value("E014"),
                        jsonPath("message").value("카테고리를 찾을 수 없습니다.")
                );
    }

    @Test
    void 코드로_카테고리_가져오기_성공() throws Exception {
        mockMvc.perform(get("/category/code")
                        .param("code", "CA001"))
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("code").value("S000"),
                        jsonPath("data.name").value("돼지고기"),
                        jsonPath("data.level").value(0),
                        jsonPath("data.branch").value("식품")
                );
    }

    @Test
    void 코드로_카테고리_가져오기_실패__없는_코드() throws Exception {
        mockMvc.perform(get("/category/code")
                        .param("code", "FA000"))
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("code").value("E014"),
                        jsonPath("message").value("카테고리를 찾을 수 없습니다.")
                );
    }

    @Test
    void 카테고리_업데이트하기_성공() throws Exception {
        CategoryUpdateDTO.Request request = new CategoryUpdateDTO.Request("가구", "FA001", "의자");

        mockMvc.perform(patch("/category/{id}", category1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("code").value("S000"));
    }

    @Test
    void 카테고리_업데이트하기_실패__브랜치_null() throws Exception {
        CategoryUpdateDTO.Request request = new CategoryUpdateDTO.Request(null, "FA001", "의자");

        mockMvc.perform(patch("/category/{id}", category1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("code").value("E001"));
    }

    @Test
    void 카테고리_업데이트하기_실패__없는_id() throws Exception {
        CategoryUpdateDTO.Request request = new CategoryUpdateDTO.Request("가구", "FA001", "의자");

        mockMvc.perform(patch("/category/{id}", -1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("code").value("E014"),
                        jsonPath("message").value("카테고리를 찾을 수 없습니다."));
    }

    @Test
    void 카테고리_삭제하기_성공() throws Exception {
        mockMvc.perform(delete("/category/{id}", category1.getId()))
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("code").value("S000"));
    }

    @Test
    void 카테고리_삭제하기_실패__없는_id() throws Exception {
        mockMvc.perform(delete("/category/{id}", -1))
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("code").value("E014"),
                        jsonPath("message").value("카테고리를 찾을 수 없습니다."));
    }
}