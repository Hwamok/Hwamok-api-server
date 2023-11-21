package com.hwamok.notice.domain;

import com.hwamok.admin.domain.Admin;
import com.hwamok.admin.domain.AdminRepository;
import fixture.AdminFixture;
import fixture.NoticeFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class NoticeRepositoryTest {
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    NoticeRepository noticeRepository;

    @Test
    void 공지사항_생성_성공() {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());

        Notice notice = noticeRepository.save(NoticeFixture.createNotice(admin));

        assertThat(notice.getId()).isNotNull();
        assertThat(notice.getCreatedBy()).isNotNull();
    }
}