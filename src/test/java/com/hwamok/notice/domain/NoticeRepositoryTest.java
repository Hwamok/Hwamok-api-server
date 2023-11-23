package com.hwamok.notice.domain;

import com.hwamok.admin.domain.Admin;
import com.hwamok.admin.domain.AdminRepository;
import fixture.AdminFixture;
import fixture.NoticeFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @ParameterizedTest
    @NullAndEmptySource
    void 공지사항_리스트조회_성공__키워드_null_또는_공백(String keyword) {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        noticeRepository.save(new Notice("제목test", "내용1", admin));
        noticeRepository.save(new Notice("제목2", "내용test", admin));
        PageRequest pageRequest = PageRequest.of(0,10);

        Page<Notice> notices = noticeRepository.getNotices(keyword, "전체", pageRequest);

        assertThat(notices.getTotalPages()).isEqualTo(1);
        assertThat(notices.getTotalElements()).isEqualTo(2);
    }

    @Test
    void 공지사항_리스트조회_성공__필터_전체조회() {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        noticeRepository.save(new Notice("제목test", "내용1", admin));
        noticeRepository.save(new Notice("제목2", "내용test", admin));
        PageRequest pageRequest = PageRequest.of(0,10);

        Page<Notice> notices = noticeRepository.getNotices("test", "전체", pageRequest);

        assertThat(notices.getTotalPages()).isEqualTo(1);
        assertThat(notices.getTotalElements()).isEqualTo(2);
    }

    @Test
    void 공지사항_리스트조회_성공__필터_제목조회() {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        noticeRepository.save(new Notice("제목test", "내용1", admin));
        noticeRepository.save(new Notice("제목2", "내용test", admin));
        PageRequest pageRequest = PageRequest.of(0,10);
        
        Page<Notice> notices = noticeRepository.getNotices("제목","제목", pageRequest);

        assertThat(notices.getTotalPages()).isEqualTo(1);
        assertThat(notices.getTotalElements()).isEqualTo(2);
    }

    @Test
    void 공지사항_리스트조회_성공__필터_내용조회() {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        noticeRepository.save(new Notice("제목test", "내용1", admin));
        noticeRepository.save(new Notice("제목2", "내용test", admin));
        PageRequest pageRequest = PageRequest.of(0,10);

        Page<Notice> notices = noticeRepository.getNotices("내용","내용", pageRequest);

        assertThat(notices.getTotalPages()).isEqualTo(1);
        assertThat(notices.getTotalElements()).isEqualTo(2);
    }
}