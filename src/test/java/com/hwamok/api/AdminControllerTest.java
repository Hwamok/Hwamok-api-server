package com.hwamok.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hwamok.admin.domain.Admin;
import com.hwamok.admin.domain.AdminRepository;
import com.hwamok.admin.domain.Role;
import com.hwamok.api.dto.admin.AdminCreateDto;
import com.hwamok.api.dto.admin.AdminUpdateDto;
import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.core.exception.HwamokExceptionTest;
import com.hwamok.utils.CreateValueUtil;
import fixture.AdminFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.hwamok.core.exception.HwamokExceptionTest.assertThatHwamokException;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
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
        AdminCreateDto.Request request = new AdminCreateDto.Request("test123", "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN));

        mockMvc.perform(MockMvcRequestBuilders.post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 관리자_생성_실패__필수값_NULL_또는_공백(String parameter) throws Exception {
        AdminCreateDto.Request request = new AdminCreateDto.Request(parameter, "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN));

        mockMvc.perform(MockMvcRequestBuilders.post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다."));
    }

    @Test
    void 관리자_생성_실패__아이디_2글자_미만() throws Exception {
        String fakeLoginId = CreateValueUtil.stringLength(1);
        AdminCreateDto.Request request = new AdminCreateDto.Request(fakeLoginId, "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN));

        mockMvc.perform(MockMvcRequestBuilders.post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E002"),
                        jsonPath("message").value("아이디형식이 다릅니다."));
    }

    @Test
    void 관리자_생성_실패__아이디_12글자_초과() throws Exception {
        String fakeLoginId = CreateValueUtil.stringLength(13);
        AdminCreateDto.Request request = new AdminCreateDto.Request(fakeLoginId, "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN));

        mockMvc.perform(MockMvcRequestBuilders.post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E002"),
                        jsonPath("message").value("아이디형식이 다릅니다."));
    }

    @Test
    void 관리자_생성_실패__아이디_특수문자사용() throws Exception {
        String fakeLoginId = "!@#";
        AdminCreateDto.Request request = new AdminCreateDto.Request(fakeLoginId, "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN));

        mockMvc.perform(MockMvcRequestBuilders.post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E002"),
                        jsonPath("message").value("아이디형식이 다릅니다."));
    }

    @Test
    void 관리자_생성_실패__이름_한글_2글자_미만() throws Exception {
        String fakeName = CreateValueUtil.stringLength(1);
        AdminCreateDto.Request request = new AdminCreateDto.Request("test123", "1234", fakeName, "test@test.com", List.of(Role.SUPER, Role.ADMIN));

        mockMvc.perform(MockMvcRequestBuilders.post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E004"),
                        jsonPath("message").value("이름형식이 다릅니다."));
    }

    @Test
    void 관리자_생성_실패__이름_한글_6글자_초과() throws Exception {
        String fakeName = CreateValueUtil.stringLength(7);
        AdminCreateDto.Request request = new AdminCreateDto.Request("test123", "1234", fakeName, "test@test.com", List.of(Role.SUPER, Role.ADMIN));

        mockMvc.perform(MockMvcRequestBuilders.post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E004"),
                        jsonPath("message").value("이름형식이 다릅니다."));
    }

    @Test
    void 관리자_생성_실패__이름_영어_2글자_미만() throws Exception {
        String fakeName = "n";
        AdminCreateDto.Request request = new AdminCreateDto.Request("test123", "1234", fakeName, "test@test.com", List.of(Role.SUPER, Role.ADMIN));

        mockMvc.perform(MockMvcRequestBuilders.post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E004"),
                        jsonPath("message").value("이름형식이 다릅니다."));
    }

    @Test
    void 관리자_생성_실패__이름_영어_20글자_초과() throws Exception {
        String fakeName = "namenamenamenamenamen";
        AdminCreateDto.Request request = new AdminCreateDto.Request("test123", "1234", fakeName, "test@test.com", List.of(Role.SUPER, Role.ADMIN));

        mockMvc.perform(MockMvcRequestBuilders.post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E004"),
                        jsonPath("message").value("이름형식이 다릅니다."));
    }

    @Test
    void 관리자_생성_실패__이름_한글영어혼용() throws Exception {
        String fakeName = "이름name";
        AdminCreateDto.Request request = new AdminCreateDto.Request("test123", "1234", fakeName, "test@test.com", List.of(Role.SUPER, Role.ADMIN));

        mockMvc.perform(MockMvcRequestBuilders.post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E004"),
                        jsonPath("message").value("이름형식이 다릅니다."));
    }

    @Test
    void 관리자_생성_실패__이름_특수문자사용() throws Exception{
        String fakeName = "이름!";
        AdminCreateDto.Request request = new AdminCreateDto.Request("test123", "1234", fakeName, "test@test.com", List.of(Role.SUPER, Role.ADMIN));

        mockMvc.perform(MockMvcRequestBuilders.post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E004"),
                        jsonPath("message").value("이름형식이 다릅니다."));
    }

    @Test
    void 관리자_생성_실패__이메일_골뱅이없음() throws Exception {
        String fakeEmail = "testtest.com";
        AdminCreateDto.Request request = new AdminCreateDto.Request("test123", "1234", "이름", fakeEmail, List.of(Role.SUPER, Role.ADMIN));

        mockMvc.perform(MockMvcRequestBuilders.post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E003"),
                        jsonPath("message").value("이메일형식이 다릅니다."));
    }

    @Test
    void 관리자_생성_실패__이메일_점없음() throws Exception {
        String fakeEmail = "test@testcom";
        AdminCreateDto.Request request = new AdminCreateDto.Request("test123", "1234", "이름", fakeEmail, List.of(Role.SUPER, Role.ADMIN));

        mockMvc.perform(MockMvcRequestBuilders.post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E003"),
                        jsonPath("message").value("이메일형식이 다릅니다."));
    }

    @Test
    void 관리자_생성_실패__이메일_50글자초과() throws Exception {
        String fakeEmail = "testtesttesttesttesttest@testtesttesttesttest11.com";
        AdminCreateDto.Request request = new AdminCreateDto.Request("test123", "1234", "이름", fakeEmail, List.of(Role.SUPER, Role.ADMIN));

        mockMvc.perform(MockMvcRequestBuilders.post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E021"),
                        jsonPath("message").value("이메일의 길이가 초과되었습니다."));
    }

    @Test
    void 관리자_단건조회_성공() throws Exception {
        Admin admin = adminRepository.save(new Admin("test1234", "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN)));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/{id}", admin.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success"),
                        jsonPath("data.loginId").value("test1234"),
                        jsonPath("data.name").value("이름"),
                        jsonPath("data.email").value("test@test.com"),
                        jsonPath("data.roles").isArray());
    }

    @Test
    void 관리자_단건조회_실패__관리자정보_없음() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/{id}", -1L))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E005"),
                        jsonPath("message").value("관리자정보를 찾을 수 없습니다."));
    }

    @Test
    void 관리자_리스트조회_성공() throws Exception {
        Admin admin1 = adminRepository.save(new Admin("test1234", "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN)));
        Admin admin2 = adminRepository.save(new Admin("test12345", "12345", "이름이", "test1@test1.com", List.of(Role.SUPER, Role.ADMIN)));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/list"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success"))
                .andExpectAll(관리자_리스트_검증(1, admin1))
                .andExpectAll(  관리자_리스트_검증(2, admin2));
    }

    @Test
    void 관리자_수정_성공() throws Exception {
        Admin admin = adminRepository.save(new Admin("test1234", "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN)));
        AdminUpdateDto.Request request = new AdminUpdateDto.Request("update1234", "수정이름", "update@update.com");

        mockMvc.perform(MockMvcRequestBuilders.patch("/admin/{id}", admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 관리자_수정_실패__필수값_null_또는_공백(String parameter) throws Exception {
        Admin admin = adminRepository.save(new Admin("test1234", "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN)));
        AdminUpdateDto.Request request = new AdminUpdateDto.Request("1234", parameter, "update@update.com");

        mockMvc.perform(MockMvcRequestBuilders.patch("/admin/{id}", admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E001"),
                        jsonPath("message").value("필수 값이 누락되었습니다."));
    }

    @Test
    void 관리자_수정_실패__이름_한글_2글자_미만() throws Exception {
        String fakeName = CreateValueUtil.stringLength(1);
        Admin admin = adminRepository.save(new Admin("test1234", "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN)));
        AdminUpdateDto.Request request = new AdminUpdateDto.Request("1234", fakeName, "update@update.com");

        mockMvc.perform(MockMvcRequestBuilders.patch("/admin/{id}", admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E004"),
                        jsonPath("message").value("이름형식이 다릅니다."));
    }

    @Test
    void 관리자_수정_실패__이름_한글_6글자_초과() throws Exception {
        String fakeName = CreateValueUtil.stringLength(7);
        Admin admin = adminRepository.save(new Admin("test1234", "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN)));
        AdminUpdateDto.Request request = new AdminUpdateDto.Request("1234", fakeName, "update@update.com");

        mockMvc.perform(MockMvcRequestBuilders.patch("/admin/{id}", admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E004"),
                        jsonPath("message").value("이름형식이 다릅니다."));
    }

    @Test
    void 관리자_수정_실패__이름_영어_2글자_미만() throws Exception {
        String fakeName = "n";
        Admin admin = adminRepository.save(new Admin("test1234", "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN)));
        AdminUpdateDto.Request request = new AdminUpdateDto.Request("1234", fakeName, "update@update.com");

        mockMvc.perform(MockMvcRequestBuilders.patch("/admin/{id}", admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E004"),
                        jsonPath("message").value("이름형식이 다릅니다."));
    }

    @Test
    void 관리자_수정_실패__이름_영어_20글자_초과() throws Exception {
        String fakeName = "namenamenamenamenamen";
        Admin admin = adminRepository.save(new Admin("test1234", "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN)));
        AdminUpdateDto.Request request = new AdminUpdateDto.Request("1234", fakeName, "update@update.com");

        mockMvc.perform(MockMvcRequestBuilders.patch("/admin/{id}", admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E004"),
                        jsonPath("message").value("이름형식이 다릅니다."));
    }

    @Test
    void 관리자_수정_실패__이름_한글영어혼용() throws Exception {
        String fakeName = "이름name";
        Admin admin = adminRepository.save(new Admin("test1234", "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN)));
        AdminUpdateDto.Request request = new AdminUpdateDto.Request("1234", fakeName, "update@update.com");

        mockMvc.perform(MockMvcRequestBuilders.patch("/admin/{id}", admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E004"),
                        jsonPath("message").value("이름형식이 다릅니다."));
    }

    @Test
    void 관리자_수정_실패__이름_특수문자사용() throws Exception {
        String fakeName = "이름!";
        Admin admin = adminRepository.save(new Admin("test1234", "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN)));
        AdminUpdateDto.Request request = new AdminUpdateDto.Request("1234", fakeName, "update@update.com");

        mockMvc.perform(MockMvcRequestBuilders.patch("/admin/{id}", admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E004"),
                        jsonPath("message").value("이름형식이 다릅니다."));
    }

    @Test
    void 관리자_수정_실패__이메일_50글자초과() throws Exception {
        String fakeEmail = "testtesttesttesttest@testtesttesttesttesttest11.com";
        Admin admin = adminRepository.save(new Admin("test1234", "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN)));
        AdminUpdateDto.Request request = new AdminUpdateDto.Request("1234", "이름", fakeEmail);

        mockMvc.perform(MockMvcRequestBuilders.patch("/admin/{id}", admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E021"),
                        jsonPath("message").value("이메일의 길이가 초과되었습니다."));
    }

    @Test
    void 관리자_수정_실패__이메일_골뱅이없음() throws Exception {
        String fakeEmail = "testtest.com";
        Admin admin = adminRepository.save(new Admin("test1234", "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN)));
        AdminUpdateDto.Request request = new AdminUpdateDto.Request("1234", "이름", fakeEmail);

        mockMvc.perform(MockMvcRequestBuilders.patch("/admin/{id}", admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E003"),
                        jsonPath("message").value("이메일형식이 다릅니다."));
    }

    @Test
    void 관리자_수정_실패__이메일_점없음() throws Exception {
        String fakeEmail = "test@testcom";
        Admin admin = adminRepository.save(new Admin("test1234", "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN)));
        AdminUpdateDto.Request request = new AdminUpdateDto.Request("1234", "이름", fakeEmail);

        mockMvc.perform(MockMvcRequestBuilders.patch("/admin/{id}", admin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E003"),
                        jsonPath("message").value("이메일형식이 다릅니다."));
    }

    @Test
    void 관리자_삭제_성공() throws Exception {
        Admin admin = adminRepository.save(new Admin("test1234", "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN)));

        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/{id}", admin.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("S000"),
                        jsonPath("message").value("success"));
    }

    @Test
    void 관리자_삭제_실패__관리자정보_없음() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/{id}", -1L))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(
                        jsonPath("code").value("E005"),
                        jsonPath("message").value("관리자정보를 찾을 수 없습니다."));
    }

    private ResultMatcher[] 관리자_리스트_검증(int index, final Admin admin) {
        return List.of(
                        jsonPath("$.data.[" + index + "].id").value(admin.getId()),
                        jsonPath("$.data.[" + index + "].loginId").value(admin.getLoginId()),
                        jsonPath("$.data.[" + index + "].name").value(admin.getName()),
                        jsonPath("$.data.[" + index + "].email").value(admin.getEmail()),
                        jsonPath("$.data.[" + index + "].roles").isArray())
                .toArray(ResultMatcher[]::new);
    }
}