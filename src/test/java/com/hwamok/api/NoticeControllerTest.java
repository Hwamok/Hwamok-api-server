package com.hwamok.api;

import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hwamok.admin.domain.Admin;
import com.hwamok.admin.domain.AdminRepository;
import com.hwamok.api.dto.notice.NoticeCreateDto;
import com.hwamok.api.dto.notice.NoticeUpdateDto;
import com.hwamok.notice.domain.Notice;
import com.hwamok.notice.domain.NoticeRepository;
import com.hwamok.utils.CreateValueUtil;
import fixture.AdminFixture;
import fixture.NoticeFixture;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
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
class NoticeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private NoticeRepository noticeRepository;

    @Test
    @WithUserDetails
    void 공지사항_생성_성공() throws Exception {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        NoticeCreateDto.Request request = new NoticeCreateDto.Request("제목", "내용");

        mockMvc.perform(RestDocumentationRequestBuilders.post("/notice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success")
                )
                .andDo(document("공지사항 생성 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Notice")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                                        PayloadDocumentation.fieldWithPath("content").type(JsonFieldType.STRING).description("내용")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("S000"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("success"),
                                                        PayloadDocumentation.fieldWithPath("data").ignored()
                                                )
                                        )
                                        .requestSchema(Schema.schema("NoticeCreateDto.Request"))
                                        .responseSchema(Schema.schema("NoticeCreateDto.Response"))
                                        .build()
                        )
                ));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @WithUserDetails
    void 공지사항_생성_실패__제목_NULL_또는_공백(String title) throws Exception {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        NoticeCreateDto.Request request = new NoticeCreateDto.Request(title, "내용");

        mockMvc.perform(RestDocumentationRequestBuilders.post("/notice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다.")
                        )
                .andDo(document("공지사항 생성 실패 제목 NULL 또는 공백 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Notice")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("title").ignored(),
                                                        PayloadDocumentation.fieldWithPath("content").type(JsonFieldType.STRING).description("내용")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E001"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("필수 값이 누락되었습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").ignored()
                                                )
                                        )
                                        .requestSchema(Schema.schema("NoticeCreateDto.Request"))
                                        .responseSchema(Schema.schema("NoticeCreateDto.Response"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
    void 공지사항_생성_실패__제목_90글자_초과() throws Exception {
        String fakeTitle = CreateValueUtil.stringLength(91);
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        NoticeCreateDto.Request request = new NoticeCreateDto.Request(fakeTitle, "내용");

        mockMvc.perform(RestDocumentationRequestBuilders.post("/notice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E015"),
                        jsonPath("message").value("제목의 길이 초과되었습니다.")
                )
                .andDo(document("공지사항 생성 실패 제목 90글 초과 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Notice")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("title").type(JsonFieldType.STRING).description(fakeTitle),
                                                        PayloadDocumentation.fieldWithPath("content").type(JsonFieldType.STRING).description("내용")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E015"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("제목의 길이 초과되었습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").ignored()
                                                )
                                        )
                                        .requestSchema(Schema.schema("NoticeCreateDto.Request"))
                                        .responseSchema(Schema.schema("NoticeCreateDto.Response"))
                                        .build()
                        )
                ));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @WithUserDetails
    void 공지사항_생성_실패__내용_NULL_또는_공백(String content) throws Exception {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        NoticeCreateDto.Request request = new NoticeCreateDto.Request("제목", content);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/notice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다.")
                )
                .andDo(document("공지사항 생성 실패 내용 NULL 또는 공백 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Notice")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                                        PayloadDocumentation.fieldWithPath("content").ignored()
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E001"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("필수 값이 누락되었습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").ignored()
                                                )
                                        )
                                        .requestSchema(Schema.schema("NoticeCreateDto.Request"))
                                        .responseSchema(Schema.schema("NoticeCreateDto.Response"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
    void 공지사항_생성_실패__내용_1000글자_초과() throws Exception {
        String fakeContent = CreateValueUtil.stringLength(1001);
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        NoticeCreateDto.Request request = new NoticeCreateDto.Request("제목", fakeContent);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/notice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E016"),
                        jsonPath("message").value("내용의 길이가 초과되었습니다.")
                )
                .andDo(document("공지사항 생성 실패 내용 NULL 또는 공백 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Notice")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                                        PayloadDocumentation.fieldWithPath("content").type(JsonFieldType.STRING).description(fakeContent)
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E016"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("내용의 길이가 초과되었습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").ignored()
                                                )
                                        )
                                        .requestSchema(Schema.schema("NoticeCreateDto.Request"))
                                        .responseSchema(Schema.schema("NoticeCreateDto.Response"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
    void 공지사항_단건조회_성공() throws Exception {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        Notice notice = noticeRepository.save(NoticeFixture.createNotice(admin));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/notice/{id}", notice.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success"),
                        jsonPath("data.title").value("제목"),
                        jsonPath("data.content").value("내용"),
                        jsonPath("data.createdBy.loginId").value("test123")
                )
                .andDo(document("공지사항 단건 조회 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Notice")
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("S000"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("success"),
                                                        PayloadDocumentation.fieldWithPath("data.title").type(JsonFieldType.STRING).description("제목"),
                                                        PayloadDocumentation.fieldWithPath("data.content").type(JsonFieldType.STRING).description("내용"),
                                                        PayloadDocumentation.fieldWithPath("data.status").type(JsonFieldType.STRING).description("CREATED"),
                                                        PayloadDocumentation.fieldWithPath("data.createdBy.id").type(JsonFieldType.NUMBER).description(1),
                                                        PayloadDocumentation.fieldWithPath("data.createdBy.loginId").type(JsonFieldType.STRING).description("test123"),
                                                        PayloadDocumentation.fieldWithPath("data.createdBy.password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("data.createdBy.name").type(JsonFieldType.STRING).description("이름"),
                                                        PayloadDocumentation.fieldWithPath("data.createdBy.email").type(JsonFieldType.STRING).description("test@test.com"),
                                                        PayloadDocumentation.fieldWithPath("data.createdBy.status").type(JsonFieldType.STRING).description("ACTIVATED"),
                                                        PayloadDocumentation.fieldWithPath("data.createdBy.roles").type(JsonFieldType.ARRAY).description("사용자 역할").attributes(Attributes.key("constraints").value("SUPER, ADMIN")),
                                                        PayloadDocumentation.fieldWithPath("data.createdBy.createdAt").type(JsonFieldType.STRING).description("The timestamp when the data was created")
                                                )
                                        )
                                        .responseSchema(Schema.schema("NoticeCreateDto.Response"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
    void 공지사항_단건조회_실패__조회정보_없음() throws Exception {
        Long fakeNoticeId = -1L;

        mockMvc.perform(RestDocumentationRequestBuilders.get("/notice/{id}", fakeNoticeId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E019"),
                        jsonPath("message").value("공지사항 정보를 찾을 수 없습니다.")
                )
                .andDo(document("공지사항 단건조회 실패 조회 정보 없음 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Notice")
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E019"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("공지사항 정보를 찾을 수 없습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").ignored()
                                                        )
                                        )
                                        .responseSchema(Schema.schema("NoticeCreateDto.Response"))
                                        .build()
                        )
                ));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @WithUserDetails
    void 공지사항_리스트조회_성공__키워드_NULL_또는_공백(String keyword) throws Exception {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        noticeRepository.save(new Notice("제목test", "내용1", admin));
        noticeRepository.save(new Notice("제목2", "내용test", admin));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/notice/list")
                        .param("keyword", keyword))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success"),
                        jsonPath("data.totalElements").value(2),
                        jsonPath("data.content[0].title").value("제목2"),
                        jsonPath("data.content[0].content").value("내용test"),
                        jsonPath("data.content[0].status").value("CREATED"),
                        jsonPath("data.content[0].createdBy.loginId").value("test123"),
                        jsonPath("data.content[0].createdBy.roles").isArray(),
                        jsonPath("data.content[1].title").value("제목test"),
                        jsonPath("data.content[1].content").value("내용1"),
                        jsonPath("data.content[1].status").value("CREATED"),
                        jsonPath("data.content[1].createdBy.loginId").value("test123"),
                        jsonPath("data.content[0].createdBy.roles").isArray(),
                        jsonPath("data.pageable.pageSize").value(10),
                        jsonPath("data.pageable.pageNumber").value(0)
                )
                .andDo(document("공지사항 리스트조회 성공 키워드 NULL 또는 공백 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Notice")
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("S000"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("success"),
                                                        PayloadDocumentation.fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER).description(2),
                                                        PayloadDocumentation.fieldWithPath("data.last").type(JsonFieldType.BOOLEAN).description("true"),
                                                        PayloadDocumentation.fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER).description(1),
                                                        PayloadDocumentation.fieldWithPath("data.first").type(JsonFieldType.BOOLEAN).description("true"),
                                                        PayloadDocumentation.fieldWithPath("data.size").type(JsonFieldType.NUMBER).description(10),
                                                        PayloadDocumentation.fieldWithPath("data.number").type(JsonFieldType.NUMBER).description(0),
                                                        PayloadDocumentation.fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER).description(2),
                                                        PayloadDocumentation.fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN).description("true"),
                                                        PayloadDocumentation.fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN).description("false"),
                                                        PayloadDocumentation.fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN).description("true"),
                                                        PayloadDocumentation.fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER).description(2),
                                                        PayloadDocumentation.fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN).description("false"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].title").type(JsonFieldType.STRING).description("제목2"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].content").type(JsonFieldType.STRING).description("내용test"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].status").type(JsonFieldType.STRING).description("CREATED"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].createdBy.id").type(JsonFieldType.NUMBER).description(1),
                                                        PayloadDocumentation.fieldWithPath("data.content[].createdBy.loginId").type(JsonFieldType.STRING).description("test123"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].createdBy.password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].createdBy.name").type(JsonFieldType.STRING).description("이름"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].createdBy.email").type(JsonFieldType.STRING).description("test@test.com"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].createdBy.status").type(JsonFieldType.STRING).description("ACTIVATED"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].createdBy.createdAt").type(JsonFieldType.STRING).description("The timestamp when the data was created"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].createdBy.roles").type(JsonFieldType.ARRAY).description("사용자 역할").attributes(Attributes.key("constraints").value("SUPER, ADMIN")),
                                                        PayloadDocumentation.fieldWithPath("data.pageable.pageNumber").type(JsonFieldType.NUMBER).description(0),
                                                        PayloadDocumentation.fieldWithPath("data.pageable.pageSize").type(JsonFieldType.NUMBER).description(10),
                                                        PayloadDocumentation.fieldWithPath("data.pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("true"),
                                                        PayloadDocumentation.fieldWithPath("data.pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("false"),
                                                        PayloadDocumentation.fieldWithPath("data.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("true"),
                                                        PayloadDocumentation.fieldWithPath("data.pageable.offset").type(JsonFieldType.NUMBER).description(0),
                                                        PayloadDocumentation.fieldWithPath("data.pageable.paged").type(JsonFieldType.BOOLEAN).description("true"),
                                                        PayloadDocumentation.fieldWithPath("data.pageable.unpaged").type(JsonFieldType.BOOLEAN).description("false")
                                                        )
                                        )
                                        .responseSchema(Schema.schema("NoticeCreateDto.Response"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
    void 공지사항_리스트조회_성공__필터_전체조회() throws Exception {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        noticeRepository.save(new Notice("제목test", "내용1", admin));
        noticeRepository.save(new Notice("제목2", "내용test", admin));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/notice/list")
                        .param("keyword", "test")
                        .param("filter", "전체"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success"),
                        jsonPath("data.totalElements").value(2),
                        jsonPath("data.content[0].title").value("제목2"),
                        jsonPath("data.content[0].content").value("내용test"),
                        jsonPath("data.content[0].status").value("CREATED"),
                        jsonPath("data.content[0].createdBy.loginId").value("test123"),
                        jsonPath("data.content[0].createdBy.roles").isArray(),
                        jsonPath("data.content[1].title").value("제목test"),
                        jsonPath("data.content[1].content").value("내용1"),
                        jsonPath("data.content[1].status").value("CREATED"),
                        jsonPath("data.content[1].createdBy.loginId").value("test123"),
                        jsonPath("data.content[1].createdBy.roles").isArray(),
                        jsonPath("data.pageable.pageSize").value(10),
                        jsonPath("data.pageable.pageNumber").value(0)
                )
                .andDo(document("공지사항 리스트조회 성공 필터 전체조회 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Notice")
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("S000"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("success"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].title").type(JsonFieldType.STRING).description("제목2"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].content").type(JsonFieldType.STRING).description("내용test"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].status").type(JsonFieldType.STRING).description("CREATED"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].createdBy.id").type(JsonFieldType.NUMBER).description(1),
                                                        PayloadDocumentation.fieldWithPath("data.content[].createdBy.loginId").type(JsonFieldType.STRING).description("test123"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].createdBy.password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].createdBy.name").type(JsonFieldType.STRING).description("이름"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].createdBy.email").type(JsonFieldType.STRING).description("test@test.com"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].createdBy.status").type(JsonFieldType.STRING).description("ACTIVATED"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].createdBy.roles").type(JsonFieldType.ARRAY).description("사용자 역할").attributes(Attributes.key("constraints").value("SUPER, ADMIN")),
                                                        PayloadDocumentation.fieldWithPath("data.content[].createdBy.createdAt").type(JsonFieldType.STRING).description("The timestamp when the data was created"),
                                                        PayloadDocumentation.fieldWithPath("data.pageable.pageNumber").type(JsonFieldType.NUMBER).description(0),
                                                        PayloadDocumentation.fieldWithPath("data.pageable.pageSize").type(JsonFieldType.NUMBER).description(10),
                                                        PayloadDocumentation.fieldWithPath("data.pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("true"),
                                                        PayloadDocumentation.fieldWithPath("data.pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("false"),
                                                        PayloadDocumentation.fieldWithPath("data.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("true"),
                                                        PayloadDocumentation.fieldWithPath("data.pageable.offset").type(JsonFieldType.NUMBER).description(0),
                                                        PayloadDocumentation.fieldWithPath("data.pageable.paged").type(JsonFieldType.BOOLEAN).description("true"),
                                                        PayloadDocumentation.fieldWithPath("data.pageable.unpaged").type(JsonFieldType.BOOLEAN).description("false"),
                                                        PayloadDocumentation.fieldWithPath("data.last").type(JsonFieldType.BOOLEAN).description("true"),
                                                        PayloadDocumentation.fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER).description(1),
                                                        PayloadDocumentation.fieldWithPath("data.first").type(JsonFieldType.BOOLEAN).description("true"),
                                                        PayloadDocumentation.fieldWithPath("data.size").type(JsonFieldType.NUMBER).description(10),
                                                        PayloadDocumentation.fieldWithPath("data.number").type(JsonFieldType.NUMBER).description(0),
                                                        PayloadDocumentation.fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER).description(2),
                                                        PayloadDocumentation.fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN).description("true"),
                                                        PayloadDocumentation.fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN).description("false"),
                                                        PayloadDocumentation.fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN).description("true"),
                                                        PayloadDocumentation.fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER).description(2),
                                                        PayloadDocumentation.fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN).description("false")
                                                )
                                        )
                                        .responseSchema(Schema.schema("NoticeCreateDto.Response"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
    void 공지사항_리스트조회_성공__필터_제목조회() throws Exception {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        noticeRepository.save(new Notice("제목test", "내용1", admin));
        noticeRepository.save(new Notice("제목2", "내용test", admin));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/notice/list")
                        .param("keyword", "test")
                        .param("filter", "제목"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success"),
                        jsonPath("data.totalElements").value(1),
                        jsonPath("data.content[0].title").value("제목test"),
                        jsonPath("data.content[0].content").value("내용1"),
                        jsonPath("data.content[0].status").value("CREATED"),
                        jsonPath("data.content[0].createdBy.loginId").value("test123"),
                        jsonPath("data.content[0].createdBy.roles").isArray(),
                        jsonPath("data.pageable.pageSize").value(10),
                        jsonPath("data.pageable.pageNumber").value(0)
                )
                .andDo(document("공지사항 리스트조회 성공 필터 전체조회 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Notice")
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("S000"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("success"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].title").type(JsonFieldType.STRING).description("제목2"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].content").type(JsonFieldType.STRING).description("내용test"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].status").type(JsonFieldType.STRING).description("CREATED"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].createdBy.id").type(JsonFieldType.NUMBER).description(1),
                                                        PayloadDocumentation.fieldWithPath("data.content[].createdBy.loginId").type(JsonFieldType.STRING).description("test123"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].createdBy.password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].createdBy.name").type(JsonFieldType.STRING).description("이름"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].createdBy.email").type(JsonFieldType.STRING).description("test@test.com"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].createdBy.status").type(JsonFieldType.STRING).description("ACTIVATED"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].createdBy.roles").type(JsonFieldType.ARRAY).description("사용자 역할").attributes(Attributes.key("constraints").value("SUPER, ADMIN")),
                                                        PayloadDocumentation.fieldWithPath("data.content[].createdBy.createdAt").type(JsonFieldType.STRING).description("The timestamp when the data was created"),
                                                        PayloadDocumentation.fieldWithPath("data.pageable.pageNumber").type(JsonFieldType.NUMBER).description(0),
                                                        PayloadDocumentation.fieldWithPath("data.pageable.pageSize").type(JsonFieldType.NUMBER).description(10),
                                                        PayloadDocumentation.fieldWithPath("data.pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("true"),
                                                        PayloadDocumentation.fieldWithPath("data.pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("false"),
                                                        PayloadDocumentation.fieldWithPath("data.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("true"),
                                                        PayloadDocumentation.fieldWithPath("data.pageable.offset").type(JsonFieldType.NUMBER).description(0),
                                                        PayloadDocumentation.fieldWithPath("data.pageable.paged").type(JsonFieldType.BOOLEAN).description("true"),
                                                        PayloadDocumentation.fieldWithPath("data.pageable.unpaged").type(JsonFieldType.BOOLEAN).description("false"),
                                                        PayloadDocumentation.fieldWithPath("data.last").type(JsonFieldType.BOOLEAN).description("true"),
                                                        PayloadDocumentation.fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER).description(1),
                                                        PayloadDocumentation.fieldWithPath("data.first").type(JsonFieldType.BOOLEAN).description("true"),
                                                        PayloadDocumentation.fieldWithPath("data.size").type(JsonFieldType.NUMBER).description(10),
                                                        PayloadDocumentation.fieldWithPath("data.number").type(JsonFieldType.NUMBER).description(0),
                                                        PayloadDocumentation.fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER).description(2),
                                                        PayloadDocumentation.fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN).description("true"),
                                                        PayloadDocumentation.fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN).description("false"),
                                                        PayloadDocumentation.fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN).description("true"),
                                                        PayloadDocumentation.fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER).description(2),
                                                        PayloadDocumentation.fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN).description("false")
                                                )
                                        )
                                        .responseSchema(Schema.schema("NoticeCreateDto.Response"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
    void 공지사항_리스트조회_성공__필터_내용조회() throws Exception {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        noticeRepository.save(new Notice("제목test", "내용1", admin));
        noticeRepository.save(new Notice("제목2", "내용test", admin));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/notice/list")
                        .param("keyword", "test")
                        .param("filter", "내용"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success"),
                        jsonPath("data.totalElements").value(1),
                        jsonPath("data.content[0].title").value("제목2"),
                        jsonPath("data.content[0].content").value("내용test"),
                        jsonPath("data.content[0].status").value("CREATED"),
                        jsonPath("data.content[0].createdBy.loginId").value("test123"),
                        jsonPath("data.content[0].createdBy.roles").isArray(),
                        jsonPath("data.pageable.pageSize").value(10),
                        jsonPath("data.pageable.pageNumber").value(0)
                )
                .andDo(document("공지사항 리스트조회 성공 필터 전체조회 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Notice")
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("S000"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("success"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].title").type(JsonFieldType.STRING).description("제목2"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].content").type(JsonFieldType.STRING).description("내용test"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].status").type(JsonFieldType.STRING).description("CREATED"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].createdBy.id").type(JsonFieldType.NUMBER).description(1),
                                                        PayloadDocumentation.fieldWithPath("data.content[].createdBy.loginId").type(JsonFieldType.STRING).description("test123"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].createdBy.password").type(JsonFieldType.STRING).description("1234"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].createdBy.name").type(JsonFieldType.STRING).description("이름"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].createdBy.email").type(JsonFieldType.STRING).description("test@test.com"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].createdBy.status").type(JsonFieldType.STRING).description("ACTIVATED"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].createdBy.createdAt").type(JsonFieldType.STRING).description("The timestamp when the data was created"),
                                                        PayloadDocumentation.fieldWithPath("data.content[].createdBy.roles").type(JsonFieldType.ARRAY).description("사용자 역할").attributes(Attributes.key("constraints").value("SUPER, ADMIN")),
                                                        PayloadDocumentation.fieldWithPath("data.pageable.pageNumber").type(JsonFieldType.NUMBER).description(0),
                                                        PayloadDocumentation.fieldWithPath("data.pageable.pageSize").type(JsonFieldType.NUMBER).description(10),
                                                        PayloadDocumentation.fieldWithPath("data.pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("true"),
                                                        PayloadDocumentation.fieldWithPath("data.pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("false"),
                                                        PayloadDocumentation.fieldWithPath("data.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("true"),
                                                        PayloadDocumentation.fieldWithPath("data.pageable.offset").type(JsonFieldType.NUMBER).description(0),
                                                        PayloadDocumentation.fieldWithPath("data.pageable.paged").type(JsonFieldType.BOOLEAN).description("true"),
                                                        PayloadDocumentation.fieldWithPath("data.pageable.unpaged").type(JsonFieldType.BOOLEAN).description("false"),
                                                        PayloadDocumentation.fieldWithPath("data.last").type(JsonFieldType.BOOLEAN).description("true"),
                                                        PayloadDocumentation.fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER).description(1),
                                                        PayloadDocumentation.fieldWithPath("data.first").type(JsonFieldType.BOOLEAN).description("true"),
                                                        PayloadDocumentation.fieldWithPath("data.size").type(JsonFieldType.NUMBER).description(10),
                                                        PayloadDocumentation.fieldWithPath("data.number").type(JsonFieldType.NUMBER).description(0),
                                                        PayloadDocumentation.fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER).description(2),
                                                        PayloadDocumentation.fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN).description("true"),
                                                        PayloadDocumentation.fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN).description("false"),
                                                        PayloadDocumentation.fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN).description("true"),
                                                        PayloadDocumentation.fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER).description(2),
                                                        PayloadDocumentation.fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN).description("false")
                                                )
                                        )
                                        .responseSchema(Schema.schema("NoticeCreateDto.Response"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
    void 공지사항_리스트조회_실패__조회정보_없음() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/notice/list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E019"),
                        jsonPath("message").value("공지사항 정보를 찾을 수 없습니다.")
                )
                .andDo(document("공지사항 리스트조회 실패 조회정보 없음 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Notice")
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E019"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("공지사항 정보를 찾을 수 없습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").ignored()
                                                )
                                        )
                                        .responseSchema(Schema.schema("NoticeCreateDto.Response"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
    void 공지사항_수정_성공() throws Exception {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        Notice notice = noticeRepository.save(new Notice("제목test", "내용1", admin));
        NoticeUpdateDto.Request request = new NoticeUpdateDto.Request("수정제목", "수정내용");

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/notice/{id}", notice.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success")
                )
                .andDo(document("공지사항 수정 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Notice")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("title").type(JsonFieldType.STRING).description("수정제목"),
                                                        PayloadDocumentation.fieldWithPath("content").type(JsonFieldType.STRING).description("수정내용")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("S000"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("success"),
                                                        PayloadDocumentation.fieldWithPath("data").ignored()
                                                )
                                        )
                                        .requestSchema(Schema.schema("NoticeUpdateDto.Request"))
                                        .responseSchema(Schema.schema("NoticeUpdateDto.Response"))
                                        .build()
                        )
                ));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @WithUserDetails
    void 공지사항_수정_실패__제목_NULL_또는_공백(String title) throws Exception {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        Notice notice = noticeRepository.save(new Notice("제목test", "내용1", admin));
        NoticeUpdateDto.Request request = new NoticeUpdateDto.Request(title, "수정내용");

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/notice/{id}", notice.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다.")
                )
                .andDo(document("공지사항 수정 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Notice")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("title").ignored(),
                                                        PayloadDocumentation.fieldWithPath("content").type(JsonFieldType.STRING).description("수정내용")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E001"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("필수 값이 누락되었습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").ignored()
                                                )
                                        )
                                        .requestSchema(Schema.schema("NoticeUpdateDto.Request"))
                                        .responseSchema(Schema.schema("NoticeUpdateDto.Response"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
    void 공지사항_수정_실패__제목_90글자_초과() throws Exception {
        String fakeTitle = CreateValueUtil.stringLength(91);
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        Notice notice = noticeRepository.save(new Notice("제목test", "내용1", admin));
        NoticeUpdateDto.Request request = new NoticeUpdateDto.Request(fakeTitle, "수정내용");

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/notice/{id}", notice.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E015"),
                        jsonPath("message").value("제목의 길이 초과되었습니다.")
                )
                .andDo(document("공지사항 수정 실패 제목 90글자 초과 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Notice")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("title").type(JsonFieldType.STRING).description("수정제목"),
                                                        PayloadDocumentation.fieldWithPath("content").type(JsonFieldType.STRING).description("수정내용")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E015"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("제목의 길이 초과되었습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").ignored()
                                                )
                                        )
                                        .requestSchema(Schema.schema("NoticeUpdateDto.Request"))
                                        .responseSchema(Schema.schema("NoticeUpdateDto.Response"))
                                        .build()
                        )
                ));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @WithUserDetails
    void 공지사항_수정_실패__내용_NULL_또는_공백(String content) throws Exception {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        Notice notice = noticeRepository.save(new Notice("제목test", "내용1", admin));
        NoticeUpdateDto.Request request = new NoticeUpdateDto.Request("수정제목", content);

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/notice/{id}", notice.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다.")
                )
                .andDo(document("공지사항 수정 실패 내용 NULL 또는 공백 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Notice")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("title").type(JsonFieldType.STRING).description("수정제목"),
                                                        PayloadDocumentation.fieldWithPath("content").ignored()
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E001"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("필수 값이 누락되었습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").ignored()
                                                )
                                        )
                                        .requestSchema(Schema.schema("NoticeUpdateDto.Request"))
                                        .responseSchema(Schema.schema("NoticeUpdateDto.Response"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
    void 공지사항_수정_실패__내용_1000글자_초과() throws Exception {
        String fakeContent = CreateValueUtil.stringLength(1001);
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        Notice notice = noticeRepository.save(new Notice("제목test", "내용1", admin));
        NoticeUpdateDto.Request request = new NoticeUpdateDto.Request("수정제목", fakeContent);

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/notice/{id}", notice.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E016"),
                        jsonPath("message").value("내용의 길이가 초과되었습니다.")
                )
                .andDo(document("공지사항 수정 실패 내용 1000글자 초과 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Notice")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("title").type(JsonFieldType.STRING).description("수정제목"),
                                                        PayloadDocumentation.fieldWithPath("content").type(JsonFieldType.STRING).description(fakeContent)
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E016"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("내용의 길이가 초과되었습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").ignored()
                                                )
                                        )
                                        .requestSchema(Schema.schema("NoticeUpdateDto.Request"))
                                        .responseSchema(Schema.schema("NoticeUpdateDto.Response"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
    void 공지사항_수정_실패__조회정보_없음() throws Exception {
        Long fakeNoticeId = 1L;
        NoticeUpdateDto.Request request = new NoticeUpdateDto.Request("수정제목", "수정내용");

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/notice/{id}", fakeNoticeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E019"),
                        jsonPath("message").value("공지사항 정보를 찾을 수 없습니다.")
                )
                .andDo(document("공지사항 수정 실패 조회정보 없음 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Notice")
                                        .requestFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("title").type(JsonFieldType.STRING).description("수정제목"),
                                                        PayloadDocumentation.fieldWithPath("content").type(JsonFieldType.STRING).description("수정내용")
                                                )
                                        )
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E019"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("공지사항 정보를 찾을 수 없습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").ignored()
                                                )
                                        )
                                        .requestSchema(Schema.schema("NoticeUpdateDto.Request"))
                                        .responseSchema(Schema.schema("NoticeUpdateDto.Response"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
    void 공지사항_삭제_성공() throws Exception {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        Notice notice = noticeRepository.save(new Notice("제목test", "내용1", admin));

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/notice/{id}", notice.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success")
                )
                .andDo(document("공지사항 삭제 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Notice")
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("S000"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("success"),
                                                        PayloadDocumentation.fieldWithPath("data").ignored()
                                                )
                                        )
                                        .responseSchema(Schema.schema("NoticeUpdateDto.Response"))
                                        .build()
                        )
                ));
    }

    @Test
    @WithUserDetails
    void 공지사항_삭제_실패__조회정보_없음() throws Exception {
        Long fakeNoticeId = 1L;

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/notice/{id}", fakeNoticeId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E019"),
                        jsonPath("message").value("공지사항 정보를 찾을 수 없습니다.")
                )
                .andDo(document("공지사항 삭제 실패 조회정보 없음 API",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        ResourceDocumentation.resource(
                                new ResourceSnippetParametersBuilder()
                                        .tag("Notice")
                                        .responseFields(
                                                List.of(
                                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("E019"),
                                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("공지사항 정보를 찾을 수 없습니다."),
                                                        PayloadDocumentation.fieldWithPath("data").ignored()
                                                )
                                        )
                                        .responseSchema(Schema.schema("NoticeUpdateDto.Response"))
                                        .build()
                        )
                ));
    }
}