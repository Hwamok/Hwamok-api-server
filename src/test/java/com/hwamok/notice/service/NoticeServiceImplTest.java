package com.hwamok.notice.service;

import com.hwamok.admin.domain.Admin;
import com.hwamok.admin.domain.AdminRepository;
import com.hwamok.notice.domain.Notice;
import com.hwamok.notice.domain.NoticeRepository;
import com.hwamok.notice.domain.NoticeStatus;
import fixture.AdminFixture;
import fixture.NoticeFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class NoticeServiceImplTest {
    @Autowired
    NoticeService noticeService;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    NoticeRepository noticeRepository;

    @Test
    void 공지사항_저장_성공() {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());

        Notice notice = noticeService.create("제목","내용",admin.getId());

        assertThat(notice.getId()).isNotNull();
    }

    @Test
    void 공지사항_단건조회_성공() {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        Notice notice = noticeRepository.save(NoticeFixture.createNotice(admin));

        Notice foundedNotice = noticeService.getNotice(notice.getId());

        assertThat(foundedNotice).isEqualTo(notice);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 공지사항_리스트조회_성공__키워드_null_또는_공백(String keyword) {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        Pageable pageable = PageRequest.of(1, 10);
        noticeRepository.save(new Notice("제목test", "내용1", admin));
        noticeRepository.save(new Notice("제목2", "내용test", admin));

        Page<Notice> notices = noticeService.getNotices(keyword, "전체", pageable);

        assertThat(notices.getTotalPages()).isEqualTo(1);
        assertThat(notices.getTotalElements()).isEqualTo(2);
    }

    @Test
    void 공지사항_리스트조회_성공__필터_전체조회() {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        Pageable pageable = PageRequest.of(1, 10);
        noticeRepository.save(new Notice("제목test", "내용", admin));
        noticeRepository.save(new Notice("제목", "내용test", admin));

        Page<Notice> notices = noticeService.getNotices("test","전체", pageable);

        assertThat(notices.getTotalPages()).isEqualTo(1);
        assertThat(notices.getTotalElements()).isEqualTo(2);
    }

    @Test
    void 공지사항_리스트조회_성공__필터_제목조회() {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        Pageable pageable = PageRequest.of(1, 10);
        noticeRepository.save(new Notice("제목test", "내용1", admin));
        noticeRepository.save(new Notice("제목2", "내용test", admin));

        Page<Notice> notices = noticeService.getNotices("제목","제목", pageable);

        assertThat(notices.getTotalPages()).isEqualTo(1);
        assertThat(notices.getTotalElements()).isEqualTo(2);
    }

    @Test
    void 공지사항_리스트조회_성공__필터_내용조회() {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        Pageable pageable = PageRequest.of(1, 10);
        noticeRepository.save(new Notice("제목test", "내용1", admin));
        noticeRepository.save(new Notice("제목2", "내용test", admin));

        Page<Notice> notices = noticeService.getNotices("내용","내용", pageable);

        assertThat(notices.getTotalPages()).isEqualTo(1);
        assertThat(notices.getTotalElements()).isEqualTo(2);
    }

    @Test
    void 공지사항_수정_성공() {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        Notice notice = noticeRepository.save(NoticeFixture.createNotice(admin));

        Notice updateNotice = noticeService.update(notice.getId(), "수정제목", "수정내용");

        assertThat(updateNotice.getTitle()).isEqualTo("수정제목");
        assertThat(updateNotice.getContent()).isEqualTo("수정내용");
    }

    @Test
    void 공지사항_삭제_성공() {
        Admin admin = adminRepository.save(AdminFixture.createAdmin());
        Notice notice = noticeRepository.save(NoticeFixture.createNotice(admin));

        Notice deleteNotice = noticeService.delete(notice.getId());

        assertThat(deleteNotice.getStatus()).isEqualTo(NoticeStatus.DELETED);
    }
}