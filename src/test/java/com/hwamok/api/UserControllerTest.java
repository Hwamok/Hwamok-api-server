package com.hwamok.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hwamok.api.dto.user.AddressCreateDto;
import com.hwamok.api.dto.user.UploadedFileCreateDto;
import com.hwamok.api.dto.user.UserCreateDto;
import com.hwamok.user.domain.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 회원_가입_성공() throws Exception {
        UserCreateDto.Request request = new UserCreateDto.Request("hwamok@test.com", "1234", "hwamok",
                "2023-11-15", "01012345678", "GOOGLE",
                new UploadedFileCreateDto.Request("originalImage", "savedImage"),
                new AddressCreateDto.Request(12345,"15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));

        mockMvc.perform(post("/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success")
                );
    }
}