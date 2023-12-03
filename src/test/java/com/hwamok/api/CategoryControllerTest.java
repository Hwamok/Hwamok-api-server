package com.hwamok.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hwamok.api.dto.category.CategoryCreateDTO;
import com.hwamok.category.domain.Category;
import com.hwamok.category.domain.CategoryRepository;
import fixture.CategoryFixture;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category1;

    private Category category2;

    @BeforeEach
    private void setup() {
        Category category1 = CategoryFixture.createCategory();
        Category category2 = categoryRepository.save(CategoryFixture.createCategory(category1));
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
                );
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
        mockMvc.perform(MockMvcRequestBuilders.get("/category/branch")
                        .param("branch", "식품"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success")
//                        jsonPath("data[0].getBranch").value("화목한쇼핑몰"),
//                        jsonPath("data[0].getCode").value("CA002"),
//                        jsonPath("data[0].getName").value("의류"),
//                        jsonPath("data[0].getLevel").value("0")
                );
    }
}