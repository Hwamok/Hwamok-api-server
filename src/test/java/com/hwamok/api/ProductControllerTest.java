package com.hwamok.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hwamok.api.dto.product.ProductCreateDto;
import com.hwamok.api.dto.product.ProductUpdateDto;
import com.hwamok.category.domain.Category;
import com.hwamok.category.domain.CategoryRepository;
import com.hwamok.product.domain.Product;
import com.hwamok.product.domain.ProductRepository;
import fixture.CategoryFixture;
import fixture.ProductFixture;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    private Category category;

    private Product product;

    @BeforeEach
    private void setUp() {
        Category parent = CategoryFixture.createCategory();
        category = categoryRepository.save(CategoryFixture.createCategory(parent));

        product = productRepository.save(ProductFixture.createProduct("소갈비", "S002", category));
    }

    @Test
    void 상품_생성_성공() throws Exception {
        ProductCreateDto.Request request = new ProductCreateDto.Request("우삼겹", "S001", 10000, category);

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isCreated())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success")
                );
    }

    @Test
    void 상품_생성_실패_가격이_0원_미만() throws Exception {
        ProductCreateDto.Request request = new ProductCreateDto.Request("우삼겹", "S001", -1, category);

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E017"),
                        jsonPath("message").value("가격형식이 다릅니다.")
                );
    }

    @Test
    void 이름으로_상품_찾기_성공() throws Exception {
        mockMvc.perform(get("/product/name")
                .param("name","소갈비"))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success"),
                        jsonPath("data[0].name").value("소갈비"),
                        jsonPath("data[0].code").value("S002"),
                        jsonPath("data[0].price").value(10000),
                        jsonPath("data[0].category.name").value("소고기")
                );
    }

    @Test
    void 코드로_상품_찾기_성공() throws Exception {
                mockMvc.perform(get("/product/code")
                    .param("code","S002"))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success"),
                        jsonPath("data[0].name").value("소갈비"),
                        jsonPath("data[0].code").value("S002"),
                        jsonPath("data[0].price").value(10000),
                        jsonPath("data[0].category.name").value("소고기")
                );
    }

    @Test
    void 상품_업데이트_성공() throws Exception {
        ProductUpdateDto.Request request = new ProductUpdateDto.Request("안심", 12000, "S003", category);

        mockMvc.perform(patch("/product/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success")
                );
    }
}