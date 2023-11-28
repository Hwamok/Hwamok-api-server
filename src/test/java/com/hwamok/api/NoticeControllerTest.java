package com.hwamok.api;

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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
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
    void 공지사항_생성_성공() throws Exception {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        NoticeCreateDto.Request request = new NoticeCreateDto.Request("제목", "내용");

        mockMvc.perform(MockMvcRequestBuilders.post("/notice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                        .sessionAttr("admin",admin))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 공지사항_생성_실패__제목_NULL_또는_공백(String title) throws Exception {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        NoticeCreateDto.Request request = new NoticeCreateDto.Request(title, "내용");

        mockMvc.perform(MockMvcRequestBuilders.post("/notice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                        .sessionAttr("admin",admin))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다."));
    }

    @Test
    void 공지사항_생성_실패__제목_90글자_초과() throws Exception {
        String fakeTitle = CreateValueUtil.stringLength(91);
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        NoticeCreateDto.Request request = new NoticeCreateDto.Request(fakeTitle, "내용");

        mockMvc.perform(MockMvcRequestBuilders.post("/notice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                        .sessionAttr("admin",admin))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E015"),
                        jsonPath("message").value("제목의 길이가 초과되었습니다."));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 공지사항_생성_실패__내용_NULL_또는_공백(String content) throws Exception {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        NoticeCreateDto.Request request = new NoticeCreateDto.Request("제목", content);

        mockMvc.perform(MockMvcRequestBuilders.post("/notice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                        .sessionAttr("admin",admin))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다."));
    }

    @Test
    void 공지사항_생성_실패__내용_1000글자_초과() throws Exception {
        String fakeContent = CreateValueUtil.stringLength(1001);
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        NoticeCreateDto.Request request = new NoticeCreateDto.Request("제목", fakeContent);

        mockMvc.perform(MockMvcRequestBuilders.post("/notice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                        .sessionAttr("admin",admin))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E016"),
                        jsonPath("message").value("내용의 길이가 초과되었습니다."));
    }

    // TODO: 2023-11-22 Jwt 구현 후 테스트 예정
    @Test
    void 공지사항_생성_실패__작성자_NULL() throws Exception {

    }

    @Test
    void 공지사항_단건조회_성공() throws Exception {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        Notice notice = noticeRepository.save(NoticeFixture.createNotice(admin));

        mockMvc.perform(MockMvcRequestBuilders.get("/notice/{id}", notice.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success"),
                        jsonPath("data.title").value("제목"),
                        jsonPath("data.content").value("내용"),
                        jsonPath("data.createdBy.loginId").value("test123"));
    }

    @Test
    void 공지사항_단건조회_실패__조회정보_없음() throws Exception {
        Long fakeNoticeId = -1L;

        mockMvc.perform(MockMvcRequestBuilders.get("/notice/{id}", fakeNoticeId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E019"),
                        jsonPath("message").value("공지사항 졍보를 찾을 수 없습니다."));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 공지사항_리스트조회_성공__키워드_NULL_또는_공백(String keyword) throws Exception {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        noticeRepository.save(new Notice("제목test", "내용1", admin));
        noticeRepository.save(new Notice("제목2", "내용test", admin));

        mockMvc.perform(MockMvcRequestBuilders.get("/notice/list")
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
                        jsonPath("data.content[1].title").value("제목test"),
                        jsonPath("data.content[1].content").value("내용1"),
                        jsonPath("data.content[1].status").value("CREATED"),
                        jsonPath("data.content[1].createdBy.loginId").value("test123"),
                        jsonPath("data.pageable.pageSize").value(10),
                        jsonPath("data.pageable.pageNumber").value(0));
    }

    @Test
    void 공지사항_리스트조회_성공__필터_전체조회() throws Exception {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        noticeRepository.save(new Notice("제목test", "내용1", admin));
        noticeRepository.save(new Notice("제목2", "내용test", admin));

        mockMvc.perform(MockMvcRequestBuilders.get("/notice/list")
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
                        jsonPath("data.content[1].title").value("제목test"),
                        jsonPath("data.content[1].content").value("내용1"),
                        jsonPath("data.content[1].status").value("CREATED"),
                        jsonPath("data.content[1].createdBy.loginId").value("test123"),
                        jsonPath("data.pageable.pageSize").value(10),
                        jsonPath("data.pageable.pageNumber").value(0));
    }

    @Test
    void 공지사항_리스트조회_성공__필터_제목조회() throws Exception {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        noticeRepository.save(new Notice("제목test", "내용1", admin));
        noticeRepository.save(new Notice("제목2", "내용test", admin));

        mockMvc.perform(MockMvcRequestBuilders.get("/notice/list")
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
                        jsonPath("data.pageable.pageSize").value(10),
                        jsonPath("data.pageable.pageNumber").value(0));
    }

    @Test
    void 공지사항_리스트조회_성공__필터_내용조회() throws Exception {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        noticeRepository.save(new Notice("제목test", "내용1", admin));
        noticeRepository.save(new Notice("제목2", "내용test", admin));

        mockMvc.perform(MockMvcRequestBuilders.get("/notice/list")
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
                        jsonPath("data.pageable.pageSize").value(10),
                        jsonPath("data.pageable.pageNumber").value(0));
    }

    @Test
    void 공지사항_리스트조회_실패__조회정보_없음() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/notice/list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E019"),
                        jsonPath("message").value("공지사항 졍보를 찾을 수 없습니다."));
    }

    @Test
    void 공지사항_수정_성공() throws Exception {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        Notice notice = noticeRepository.save(new Notice("제목test", "내용1", admin));
        NoticeUpdateDto.Request request = new NoticeUpdateDto.Request("수정제목", "수정내용");

        mockMvc.perform(MockMvcRequestBuilders.patch("/notice/{id}", notice.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 공지사항_수정_실패__제목_NULL_또는_공백(String title) throws Exception {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        Notice notice = noticeRepository.save(new Notice("제목test", "내용1", admin));
        NoticeUpdateDto.Request request = new NoticeUpdateDto.Request(title, "수정내용");

        mockMvc.perform(MockMvcRequestBuilders.patch("/notice/{id}", notice.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다."));
    }

    @Test
    void 공지사항_수정_실패__제목_90글자_초과() throws Exception {
        String fakeTitle = CreateValueUtil.stringLength(91);
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        Notice notice = noticeRepository.save(new Notice("제목test", "내용1", admin));
        NoticeUpdateDto.Request request = new NoticeUpdateDto.Request(fakeTitle, "수정내용");

        mockMvc.perform(MockMvcRequestBuilders.patch("/notice/{id}", notice.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E015"),
                        jsonPath("message").value("제목의 길이가 초과되었습니다."));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 공지사항_수정_실패__내용_NULL_또는_공백(String content) throws Exception {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        Notice notice = noticeRepository.save(new Notice("제목test", "내용1", admin));
        NoticeUpdateDto.Request request = new NoticeUpdateDto.Request("수정제목", content);

        mockMvc.perform(MockMvcRequestBuilders.patch("/notice/{id}", notice.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다."));
    }

    @Test
    void 공지사항_수정_실패__내용_1000글자_초과() throws Exception {
        String fakeContent = CreateValueUtil.stringLength(1001);
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        Notice notice = noticeRepository.save(new Notice("제목test", "내용1", admin));
        NoticeUpdateDto.Request request = new NoticeUpdateDto.Request("수정제목", fakeContent);

        mockMvc.perform(MockMvcRequestBuilders.patch("/notice/{id}", notice.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E016"),
                        jsonPath("message").value("내용의 길이가 초과되었습니다."));
    }

    @Test
    void 공지사항_수정_실패__조회정보_없음() throws Exception {
        Long fakeNoticeId = 1L;
        NoticeUpdateDto.Request request = new NoticeUpdateDto.Request("수정제목", "수정내용");

        mockMvc.perform(MockMvcRequestBuilders.patch("/notice/{id}", fakeNoticeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E019"),
                        jsonPath("message").value("공지사항 졍보를 찾을 수 없습니다."));
    }

    @Test
    void 공지사항_삭제_성공() throws Exception {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        Notice notice = noticeRepository.save(new Notice("제목test", "내용1", admin));

        mockMvc.perform(MockMvcRequestBuilders.delete("/notice/{id}", notice.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success"));
    }

    @Test
    void 공지사항_삭제_실패__조회정보_없음() throws Exception {
        Long fakeNoticeId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/notice/{id}", fakeNoticeId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E019"),
                        jsonPath("message").value("공지사항 졍보를 찾을 수 없습니다."));
    }
}