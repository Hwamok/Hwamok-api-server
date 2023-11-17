package com.hwamok.notice.domain;

import com.hwamok.admin.domain.Admin;
import fixture.AdminFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.*;

class NoticeTest {
    @Test
    void 공지사항_생성_성공() {
        Admin admin = AdminFixture.createAdmin();

        Notice notice = new Notice("제목", "내용", admin);

        assertThat(notice.getTitle()).isEqualTo("제목");
        assertThat(notice.getContent()).isEqualTo("내용");
        assertThat(notice.getCreatedBy().getLoginId()).isEqualTo("test123");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 공지사항_생성_실패__제목_NULL_또는_공백(String title) {
        Admin admin = AdminFixture.createAdmin();

        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Notice(title, "내용", admin));
    }

    @Test
    void 공지사항_생성_실패__제목_90글자초과() {
        String fakeTitle = "제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제";
        Admin admin = AdminFixture.createAdmin();

        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Notice(fakeTitle, "내용", admin));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 공지사항_생성_실패__내용_NULL_또는_공백(String content) {
        Admin admin = AdminFixture.createAdmin();

        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Notice("제목", content, admin));
    }

    @Test
    void 공지사항_생성_실패__내용_1000글자초과() {
        String fakeContent = "내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내";
        Admin admin = AdminFixture.createAdmin();

        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Notice("제목", fakeContent, admin));
    }

    @Test
    void 공지사항_생성_실패__작성자_NULL() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Notice("제목", "내용", null));
    }
}