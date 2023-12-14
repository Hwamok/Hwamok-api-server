package com.hwamok.admin.domain;

import com.hwamok.utils.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class AdminRepositoryTest {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void 관리자_저장_성공() {
        Admin admin = adminRepository.save(new Admin("test123", passwordEncoder.encode("1234"), "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN)));

        assertThat(admin.getId()).isNotNull();
    }

    @Test
    void 관리자_로그인_아이디로_조회_성공() {
        adminRepository.save(new Admin("test123", passwordEncoder.encode("1234"), "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN)));

        Optional<Admin> foundAdmin = adminRepository.findByLoginId("test123");

        assertThat(foundAdmin.isPresent()).isTrue();
    }

    @Test
    void 관리자_로그인_아이디로_조회_실패__존재_하지_않는_아이디() {
        adminRepository.save(new Admin("test123", passwordEncoder.encode("1234"), "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN)));

        Optional<Admin> foundAdmin = adminRepository.findByLoginId("fakeId");

        assertThat(foundAdmin.isPresent()).isFalse();
    }
}