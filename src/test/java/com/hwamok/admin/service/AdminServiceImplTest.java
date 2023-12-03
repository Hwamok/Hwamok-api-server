package com.hwamok.admin.service;

import com.hwamok.admin.domain.Admin;
import com.hwamok.admin.domain.AdminRepository;
import com.hwamok.admin.domain.AdminStatus;
import com.hwamok.admin.domain.Role;
import com.hwamok.api.dto.admin.AdminReadDto;
import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.core.exception.HwamokExceptionTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class AdminServiceImplTest {

    @Autowired
    AdminService adminService;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    PasswordEncoder pwEncoder;

    private List<Role> roles = new ArrayList<>();
    @BeforeEach
    void setUp() {
        roles.add(Role.SUPER);
        roles.add(Role.ADMIN);
    }


    @Test
    void 관리자_저장_성공() {
        Admin admin = adminService
                .create("test123", "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN));

        assertThat(admin.getId()).isNotNull();
    }

    @Test
    void 관리자_단건조회_성공() {
        Admin admin = adminRepository
                .save(new Admin("test123", pwEncoder.encode("1234"), "이름", "test@test.com", roles));

        AdminReadDto.Response response = adminService.getInfo(admin.getId());

        assertThat(response.getId()).isNotNull();
    }

    @Test
    void 관리자_단건조회_실패__관리자정보_없음() {
        Long fakeId = -1L;
        adminRepository
                .save(new Admin("test123", pwEncoder.encode("1234"), "이름", "test@test.com", roles));

        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_FOUND_ADMIN)
                .isThrownBy(() -> adminService.getInfo(fakeId));
    }

    @Test
    void 관리자_리스트조회_성공() {
        adminRepository
                .save(new Admin("test123", pwEncoder.encode("1234"), "이름", "test@test.com", roles));
        adminRepository
                .save(new Admin("test1234", pwEncoder.encode("12344"), "이름은", "test1@test1.com", roles));

        List<AdminReadDto.Response> adminList = adminService.getInfos();

        assertThat(adminList.size()).isEqualTo(3);
    }

    @Test
    void 관리자_수정_성공() {
        Admin admin = adminRepository
                .save(new Admin("test123", pwEncoder.encode("1234"), "이름", "test@test.com", roles));

        Admin updateAdmin = adminService.update(admin.getId(), "update1234", "수정이름", "update@update.com");

        assertThat(pwEncoder.matches("update1234", updateAdmin.getPassword())).isTrue();
        assertThat(updateAdmin.getName()).isEqualTo("수정이름");
        assertThat(updateAdmin.getEmail()).isEqualTo("update@update.com");
    }

    @Test
    void 관리자_수정_실패__유저정보_없음() {
        Long fakeId = -1L;

        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_FOUND_ADMIN)
                .isThrownBy(() -> adminService.update(fakeId, "update1234", "수정이름", "update@update.com"));
    }

    @Test
    void 관리자_삭제_성공() {
        Admin admin = adminRepository
                .save(new Admin("test123", pwEncoder.encode("1234"), "이름", "test@test.com", roles));

        Admin deletedAdmin = adminService.delete(admin.getId());
        assertThat(deletedAdmin.getStatus())
                .isEqualTo(AdminStatus.INACTIVATED);
    }

    @Test
    void 관리자_삭제_실패__유저정보_없음() {
        Long fakeId = -1L;
        adminRepository
                .save(new Admin("test123", pwEncoder.encode("1234"), "이름", "test@test.com", roles));

        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_FOUND_ADMIN)
                .isThrownBy(() -> adminService.delete(fakeId));
    }
}