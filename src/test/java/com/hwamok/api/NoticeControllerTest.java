package com.hwamok.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hwamok.admin.domain.Admin;
import com.hwamok.admin.domain.AdminRepository;
import com.hwamok.api.dto.notice.NoticeCreateDto;
import com.hwamok.api.dto.notice.NoticeUpdateDto;
import com.hwamok.notice.domain.Notice;
import com.hwamok.notice.domain.NoticeRepository;
import fixture.AdminFixture;
import fixture.NoticeFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
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
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success"));
    }

    @Test
    void 공지사항_단건조회_성공() throws Exception {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        Notice notice = noticeRepository.save(NoticeFixture.createNotice(admin));

        mockMvc.perform(MockMvcRequestBuilders.get("/notice/{id}", notice.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success"),
                        jsonPath("data.title").value("제목"),
                        jsonPath("data.content").value("내용"),
                        jsonPath("data.createdBy.loginId").value("test123"));
    }

    @Test
    void 공지사항_리스트조회_성공() throws Exception {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        noticeRepository.save(new Notice("제목test", "내용1", admin));
        noticeRepository.save(new Notice("제목2", "내용test", admin));

        mockMvc.perform(MockMvcRequestBuilders.get("/notice/list"))
                .andDo(MockMvcResultHandlers.print())
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
    void 공지사항_수정_성공() throws Exception {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        Notice notice = noticeRepository.save(new Notice("제목test", "내용1", admin));
        NoticeUpdateDto.Request request = new NoticeUpdateDto.Request("수정제목", "수정내용");

        mockMvc.perform(MockMvcRequestBuilders.patch("/notice/{id}", notice.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success"));
    }

    @Test
    void 공지사항_삭제_성공() throws Exception {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        Notice notice = noticeRepository.save(new Notice("제목test", "내용1", admin));

        mockMvc.perform(MockMvcRequestBuilders.delete("/notice/{id}", notice.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success"));
    }
}