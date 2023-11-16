package com.hwamok.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hwamok.admin.domain.Admin;
import com.hwamok.admin.domain.AdminRepository;
import com.hwamok.api.dto.admin.AdminCreateDto;
import com.hwamok.api.dto.admin.AdminUpdateDto;
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
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AdminRepository adminRepository;

    @Test
    void 관리자_생성_성공() throws Exception {
        AdminCreateDto.Request request = new AdminCreateDto.Request("test123", "1234", "이름", "test@test.com");

        mockMvc.perform(MockMvcRequestBuilders.post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success")
                );
    }

    @Test
    void 관리자_단건조회_성공() throws Exception {
        Admin admin = adminRepository.save(new Admin("test1234", "1234", "이름", "test@test.com"));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/{id}", admin.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success"),
                        jsonPath("data.loginId").value("test1234"),
                        jsonPath("data.password").isNotEmpty(),
                        jsonPath("data.name").value("이름"),
                        jsonPath("data.email").value("test@test.com")
                );
    }

    @Test
    void 관리자_리스트조회_성공() throws Exception {
        adminRepository.save(new Admin("test1234", "1234", "이름", "test@test.com"));
        adminRepository.save(new Admin("test12345", "12345", "이름이", "test1@test1.com"));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/list"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success"),
                        jsonPath("data[0].loginId").value("test1234"),
                        jsonPath("data[0].password").isNotEmpty(),
                        jsonPath("data[0].name").value("이름"),
                        jsonPath("data[0].email").value("test@test.com"),
                        jsonPath("data[1].loginId").value("test12345"),
                        jsonPath("data[1].password").isNotEmpty(),
                        jsonPath("data[1].name").value("이름이"),
                        jsonPath("data[1].email").value("test1@test1.com")
                );
    }

    @Test
    void 관리자_수정_성공() throws Exception {
        Admin admin = adminRepository.save(new Admin("test1234", "1234", "이름", "test@test.com"));

        AdminUpdateDto.Request request = new AdminUpdateDto.Request("update1234", "수정이름", "update@update.com");

        mockMvc.perform(MockMvcRequestBuilders.patch("/admin/{id}", admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success")
                );
    }

    @Test
    void 관리자_삭제_성공() throws Exception {
        Admin admin = adminRepository.save(new Admin("test1234", "1234", "이름", "test@test.com"));

        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/{id}", admin.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success")
                );
    }

}