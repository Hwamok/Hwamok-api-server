package com.hwamok.api;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hwamok.api.dto.product.ProductCreateDto;
import com.hwamok.api.dto.product.ProductReadDto;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
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

    private Category parent;

    private Product product;

    @BeforeEach
    private void setUp() {
        parent = categoryRepository.save(CategoryFixture.createCategory());
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
                )
                .andDo(document("상품생성 API",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                ResourceDocumentation.resource(new ResourceSnippetParametersBuilder()
                                        .tag("Product")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("name")
                                                                .type(JsonFieldType.STRING).description("우삼겹"),
                                                        PayloadDocumentation.fieldWithPath("code")
                                                                .type(JsonFieldType.STRING).description("S001"),
                                                        PayloadDocumentation.fieldWithPath("price")
                                                                .type(JsonFieldType.NUMBER).description("10000"),
                                                        PayloadDocumentation.fieldWithPath("category.id")
                                                                .type(JsonFieldType.NUMBER).description("2"),
                                                        PayloadDocumentation.fieldWithPath("category.createdAt")
                                                                .type(JsonFieldType.STRING).description("The timestamp when the data was created"),
                                                        PayloadDocumentation.fieldWithPath("category.branch")
                                                                .type(JsonFieldType.STRING).description("식품"),
                                                        PayloadDocumentation.fieldWithPath("category.code")
                                                                .type(JsonFieldType.STRING).description("CA002"),
                                                        PayloadDocumentation.fieldWithPath("category.name")
                                                                .type(JsonFieldType.STRING).description("소고기"),
                                                        PayloadDocumentation.fieldWithPath("category.parentCategory.id")
                                                                .type(JsonFieldType.NUMBER).description("1"),
                                                        PayloadDocumentation.fieldWithPath("category.parentCategory.createdAt")
                                                                .type(JsonFieldType.STRING).description("The timestamp when the data was created"),
                                                        PayloadDocumentation.fieldWithPath("category.parentCategory.branch")
                                                                .type(JsonFieldType.STRING).description("식품"),
                                                        PayloadDocumentation.fieldWithPath("category.parentCategory.code")
                                                                .type(JsonFieldType.STRING).description("CA001"),
                                                        PayloadDocumentation.fieldWithPath("category.parentCategory.name")
                                                                .type(JsonFieldType.STRING).description("돼지고기"),
                                                        PayloadDocumentation.fieldWithPath("category.parentCategory.parentCategory").optional()
                                                                .type(JsonFieldType.OBJECT).description("null"),
                                                        PayloadDocumentation.fieldWithPath("category.parentCategory.subCategory").optional()
                                                                .type(JsonFieldType.ARRAY).description("null"),
                                                        PayloadDocumentation.fieldWithPath("category.parentCategory.level")
                                                                .type(JsonFieldType.NUMBER).description("0"),
                                                        PayloadDocumentation.fieldWithPath("category.parentCategory.parentId").optional()
                                                                .type(JsonFieldType.NUMBER).description("null"),
                                                        PayloadDocumentation.fieldWithPath("category.parentCategory.products").optional()
                                                                .type(JsonFieldType.ARRAY).description("null"),
                                                        PayloadDocumentation.fieldWithPath("category.parentCategory.status")
                                                                .type(JsonFieldType.STRING).description("ACTIVATE"),
                                                        PayloadDocumentation.fieldWithPath("category.subCategory").optional()
                                                                .type(JsonFieldType.ARRAY).description("null"),
                                                        PayloadDocumentation.fieldWithPath("category.level")
                                                                .type(JsonFieldType.NUMBER).description("0"),
                                                        PayloadDocumentation.fieldWithPath("category.parentId").optional()
                                                                .type(JsonFieldType.NUMBER).description("2"),
                                                        PayloadDocumentation.fieldWithPath("category.products").optional()
                                                                .type(JsonFieldType.ARRAY).description("null"),
                                                        PayloadDocumentation.fieldWithPath("category.status")
                                                                .type(JsonFieldType.STRING).description("ACTIVATE")
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
                                        .requestSchema(Schema.schema("ProductCreateDto.Request"))
                                        .build()
                                )
                        )
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
                )
                .andDo(document("상품생성 실패 API",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                ResourceDocumentation.resource(new ResourceSnippetParametersBuilder()
                                        .tag("Product")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("name")
                                                                .type(JsonFieldType.STRING).description("우삼겹"),
                                                        PayloadDocumentation.fieldWithPath("code")
                                                                .type(JsonFieldType.STRING).description("S001"),
                                                        PayloadDocumentation.fieldWithPath("price")
                                                                .type(JsonFieldType.NUMBER).description("-1"),
                                                        PayloadDocumentation.fieldWithPath("category.id")
                                                                .type(JsonFieldType.NUMBER).description("2"),
                                                        PayloadDocumentation.fieldWithPath("category.createdAt")
                                                                .type(JsonFieldType.STRING).description("The timestamp when the data was created"),
                                                        PayloadDocumentation.fieldWithPath("category.branch")
                                                                .type(JsonFieldType.STRING).description("식품"),
                                                        PayloadDocumentation.fieldWithPath("category.code")
                                                                .type(JsonFieldType.STRING).description("CA002"),
                                                        PayloadDocumentation.fieldWithPath("category.name")
                                                                .type(JsonFieldType.STRING).description("소고기"),
                                                        PayloadDocumentation.fieldWithPath("category.parentCategory.id")
                                                                .type(JsonFieldType.NUMBER).description("1"),
                                                        PayloadDocumentation.fieldWithPath("category.parentCategory.createdAt")
                                                                .type(JsonFieldType.STRING).description("The timestamp when the data was created"),
                                                        PayloadDocumentation.fieldWithPath("category.parentCategory.branch")
                                                                .type(JsonFieldType.STRING).description("식품"),
                                                        PayloadDocumentation.fieldWithPath("category.parentCategory.code")
                                                                .type(JsonFieldType.STRING).description("CA001"),
                                                        PayloadDocumentation.fieldWithPath("category.parentCategory.name")
                                                                .type(JsonFieldType.STRING).description("돼지고기"),
                                                        PayloadDocumentation.fieldWithPath("category.parentCategory.parentCategory").optional()
                                                                .type(JsonFieldType.OBJECT).description("null"),
                                                        PayloadDocumentation.fieldWithPath("category.parentCategory.subCategory").optional()
                                                                .type(JsonFieldType.ARRAY).description("null"),
                                                        PayloadDocumentation.fieldWithPath("category.parentCategory.level")
                                                                .type(JsonFieldType.NUMBER).description("0"),
                                                        PayloadDocumentation.fieldWithPath("category.parentCategory.parentId").optional()
                                                                .type(JsonFieldType.NUMBER).description("null"),
                                                        PayloadDocumentation.fieldWithPath("category.parentCategory.products").optional()
                                                                .type(JsonFieldType.ARRAY).description("null"),
                                                        PayloadDocumentation.fieldWithPath("category.parentCategory.status")
                                                                .type(JsonFieldType.STRING).description("ACTIVATE"),
                                                        PayloadDocumentation.fieldWithPath("category.subCategory").optional()
                                                                .type(JsonFieldType.ARRAY).description("null"),
                                                        PayloadDocumentation.fieldWithPath("category.level")
                                                                .type(JsonFieldType.NUMBER).description("0"),
                                                        PayloadDocumentation.fieldWithPath("category.parentId").optional()
                                                                .type(JsonFieldType.NUMBER).description("2"),
                                                        PayloadDocumentation.fieldWithPath("category.products").optional()
                                                                .type(JsonFieldType.ARRAY).description("null"),
                                                        PayloadDocumentation.fieldWithPath("category.status")
                                                                .type(JsonFieldType.STRING).description("ACTIVATE")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code")
                                                                .type(JsonFieldType.STRING).description("E017"),
                                                        PayloadDocumentation.fieldWithPath("message")
                                                                .type(JsonFieldType.STRING).description("가격형식이 다릅니다."),
                                                        PayloadDocumentation.fieldWithPath("data")
                                                                .type(JsonFieldType.NULL).description("null")
                                                )
                                        )
                                        .requestSchema(Schema.schema("ProductCreateDto.Request"))
                                        .build()
                                )
                        )
                );
    }

    @Test
    void 이름으로_상품_찾기_성공() throws Exception {
        mockMvc.perform(get("/product/name")
                        .param("name", "소갈비"))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success"),
                        jsonPath("data[0].name").value("소갈비"),
                        jsonPath("data[0].code").value("S002"),
                        jsonPath("data[0].price").value(10000)
                )
                .andDo(document("상품찾기(이름) API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(new ResourceSnippetParametersBuilder()
                                .tag("Product")
                                .responseFields(
                                        List.of(
                                                PayloadDocumentation.fieldWithPath("code")
                                                        .type(JsonFieldType.STRING).description("S000"),
                                                PayloadDocumentation.fieldWithPath("message")
                                                        .type(JsonFieldType.STRING).description("success"),
                                                PayloadDocumentation.fieldWithPath("data[0].name")
                                                        .type(JsonFieldType.STRING).description("소갈비"),
                                                PayloadDocumentation.fieldWithPath("data[0].code")
                                                        .type(JsonFieldType.STRING).description("S002"),
                                                PayloadDocumentation.fieldWithPath("data[0].price")
                                                        .type(JsonFieldType.NUMBER).description("10000"),
                                                PayloadDocumentation.fieldWithPath("data[0].categoryId")
                                                        .type(JsonFieldType.NUMBER).description("2")
                                        )
                                )
                                .responseSchema(Schema.schema("ProductReadDto.Response"))
                                .build()
                        )
                ));
    }

    @Test
    void 코드로_상품_찾기_성공() throws Exception {
        mockMvc.perform(get("/product/code")
                        .param("code", "S002"))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success"),
                        jsonPath("data.name").value("소갈비"),
                        jsonPath("data.code").value("S002"),
                        jsonPath("data.price").value(10000)
                )
                .andDo(document("상품찾기(코드) API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(new ResourceSnippetParametersBuilder()
                                .tag("Product")
                                .responseFields(
                                        List.of(
                                                PayloadDocumentation.fieldWithPath("code")
                                                        .type(JsonFieldType.STRING).description("S000"),
                                                PayloadDocumentation.fieldWithPath("message")
                                                        .type(JsonFieldType.STRING).description("success"),
                                                PayloadDocumentation.fieldWithPath("data.name")
                                                        .type(JsonFieldType.STRING).description("소갈비"),
                                                PayloadDocumentation.fieldWithPath("data.code")
                                                        .type(JsonFieldType.STRING).description("S002"),
                                                PayloadDocumentation.fieldWithPath("data.price")
                                                        .type(JsonFieldType.NUMBER).description("10000"),
                                                PayloadDocumentation.fieldWithPath("data.categoryId")
                                                        .type(JsonFieldType.NUMBER).description("2")
                                        )
                                )
                                .responseSchema(Schema.schema("ProductReadDto.Response"))
                                .build()
                        )
                ));
    }

    @Test
    void 상품_업데이트_성공() throws Exception {
        ProductUpdateDto.Request request = new ProductUpdateDto.Request("안심", 12000, "S003", category);

        mockMvc.perform(patch("/product/{id}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success")
                )
                .andDo(document("상품 수정 API",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                ResourceDocumentation.resource(new ResourceSnippetParametersBuilder()
                                        .tag("Product")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("name")
                                                                .type(JsonFieldType.STRING).description("안심"),
                                                        PayloadDocumentation.fieldWithPath("code")
                                                                .type(JsonFieldType.STRING).description("S003"),
                                                        PayloadDocumentation.fieldWithPath("price")
                                                                .type(JsonFieldType.NUMBER).description("12000"),
                                                        PayloadDocumentation.fieldWithPath("category.id")
                                                                .type(JsonFieldType.NUMBER).description("2"),
                                                        PayloadDocumentation.fieldWithPath("category.createdAt")
                                                                .type(JsonFieldType.STRING).description("The timestamp when the data was created"),
                                                        PayloadDocumentation.fieldWithPath("category.branch")
                                                                .type(JsonFieldType.STRING).description("식품"),
                                                        PayloadDocumentation.fieldWithPath("category.code")
                                                                .type(JsonFieldType.STRING).description("CA002"),
                                                        PayloadDocumentation.fieldWithPath("category.name")
                                                                .type(JsonFieldType.STRING).description("소고기"),
                                                        PayloadDocumentation.fieldWithPath("category.parentCategory.id")
                                                                .type(JsonFieldType.NUMBER).description("1"),
                                                        PayloadDocumentation.fieldWithPath("category.parentCategory.createdAt")
                                                                .type(JsonFieldType.STRING).description("The timestamp when the data was created"),
                                                        PayloadDocumentation.fieldWithPath("category.parentCategory.branch")
                                                                .type(JsonFieldType.STRING).description("식품"),
                                                        PayloadDocumentation.fieldWithPath("category.parentCategory.code")
                                                                .type(JsonFieldType.STRING).description("CA001"),
                                                        PayloadDocumentation.fieldWithPath("category.parentCategory.name")
                                                                .type(JsonFieldType.STRING).description("돼지고기"),
                                                        PayloadDocumentation.fieldWithPath("category.parentCategory.parentCategory").optional()
                                                                .type(JsonFieldType.OBJECT).description("null"),
                                                        PayloadDocumentation.fieldWithPath("category.parentCategory.subCategory").optional()
                                                                .type(JsonFieldType.ARRAY).description("null"),
                                                        PayloadDocumentation.fieldWithPath("category.parentCategory.level")
                                                                .type(JsonFieldType.NUMBER).description("0"),
                                                        PayloadDocumentation.fieldWithPath("category.parentCategory.parentId").optional()
                                                                .type(JsonFieldType.NUMBER).description("null"),
                                                        PayloadDocumentation.fieldWithPath("category.parentCategory.products").optional()
                                                                .type(JsonFieldType.ARRAY).description("null"),
                                                        PayloadDocumentation.fieldWithPath("category.parentCategory.status")
                                                                .type(JsonFieldType.STRING).description("ACTIVATE"),
                                                        PayloadDocumentation.fieldWithPath("category.subCategory").optional()
                                                                .type(JsonFieldType.ARRAY).description("null"),
                                                        PayloadDocumentation.fieldWithPath("category.level")
                                                                .type(JsonFieldType.NUMBER).description("0"),
                                                        PayloadDocumentation.fieldWithPath("category.parentId").optional()
                                                                .type(JsonFieldType.NUMBER).description("2"),
                                                        PayloadDocumentation.fieldWithPath("category.products").optional()
                                                                .type(JsonFieldType.ARRAY).description("null"),
                                                        PayloadDocumentation.fieldWithPath("category.status")
                                                                .type(JsonFieldType.STRING).description("ACTIVATE")
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
                                        .requestSchema(Schema.schema("ProductCreateDto.Request"))
                                        .build()
                                )
                        )
                );
    }

    @Test
    void 상품_삭제_성공() throws Exception {
        mockMvc.perform(delete("/product/{id}", product.getId()))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success")
                )
                .andDo(document("상품 삭제 API",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                ResourceDocumentation.resource(new ResourceSnippetParametersBuilder()
                                        .tag("Product")
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
}