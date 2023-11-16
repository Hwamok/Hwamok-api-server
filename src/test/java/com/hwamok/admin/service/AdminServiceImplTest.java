package com.hwamok.admin.service;

import com.hwamok.admin.domain.Admin;
import com.hwamok.admin.domain.AdminRepository;
import com.hwamok.admin.domain.AdminStatus;
import com.hwamok.api.dto.admin.AdminCreateDto;
import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.core.exception.HwamokExceptionTest;
import fixture.AdminFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class AdminServiceImplTest {

    @Autowired
    AdminService adminService;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void 관리자_저장_성공() {
        Admin admin = adminService.create("test123", "1234", "이름", "test@test.com");

        Assertions.assertThat(admin.getId()).isNotNull();

    }

    @Test
    void 관리자_단건조회_성공() {
        Admin admin = adminRepository.save(new Admin("test123", passwordEncoder.encode("1234"), "이름", "test@test.com"));
        Admin foundedAmdin = adminService.getInfo(admin.getId());

        Assertions.assertThat(foundedAmdin.getId()).isNotNull();

    }

    @Test
    void 관리자_단건조회_실패__유저정보_없음() {
        Admin admin = adminRepository.save(new Admin("test123", passwordEncoder.encode("1234"), "이름", "test@test.com"));

        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_FOUND_ADMIN).isThrownBy(() -> adminService.getInfo(-1L));
    }

    @Test
    void 관리자_리스트조회_성공() {
        Admin admin1 = adminRepository.save(new Admin("test123", passwordEncoder.encode("1234"), "이름", "test@test.com"));
        Admin admin2 = adminRepository.save(new Admin("test1234", passwordEncoder.encode("12344"), "이름은", "test1@test1.com"));

        List<Admin> adminList = adminService.getInfos();
        Assertions.assertThat(adminList.size()).isEqualTo(2);
    }

    @Test
    void 관리자_수정_성공() {
        Admin admin = adminRepository.save(new Admin("test123", passwordEncoder.encode("1234"), "이름", "test@test.com"));

        Admin updateAdmin = adminService.update(admin.getId(), "update1234", "수정이름", "update@update.com");
        Assertions.assertThat(passwordEncoder.matches("update1234", updateAdmin.getPassword())).isTrue();
        Assertions.assertThat(updateAdmin.getName()).isEqualTo("수정이름");
        Assertions.assertThat(updateAdmin.getEmail()).isEqualTo("update@update.com");
    }

    @Test
    void 관리자_수정_실패__유저정보_없음() {
        Admin admin = adminRepository.save(new Admin("test123", passwordEncoder.encode("1234"), "이름", "test@test.com"));

        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_FOUND_ADMIN).isThrownBy(() -> adminService.update(1L, "update1234", "수정이름", "update@update.com"));
    }

    @Test
    void 관리자_삭제_성공() {
        Admin admin = adminRepository.save(new Admin("test123", passwordEncoder.encode("1234"), "이름", "test@test.com"));

        Admin deletedAdmin = adminService.delete(admin.getId());
        Assertions.assertThat(deletedAdmin.getStatus()).isEqualTo(AdminStatus.INACTIVATED);

    }

    @Test
    void 관리자_삭제_실패__유저정보_없음() {
        Admin admin = adminRepository.save(new Admin("test123", passwordEncoder.encode("1234"), "이름", "test@test.com"));

        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_FOUND_ADMIN).isThrownBy(() -> adminService.delete(1L));


    }
}