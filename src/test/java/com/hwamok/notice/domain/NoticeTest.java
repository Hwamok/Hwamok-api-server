package com.hwamok.notice.domain;

import com.hwamok.admin.domain.Admin;
import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.core.exception.HwamokExceptionTest;
import fixture.AdminFixture;
import fixture.NoticeFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static com.hwamok.utils.CreateValueUtil.*;
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
        String fakeTitle = stringLength(91);
        Admin admin = AdminFixture.createAdmin();

        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.OVER_LENGTH_TITLE)
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
        String fakeContent = stringLength(1001);
        Admin admin = AdminFixture.createAdmin();

        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.OVER_LENGTH_CONTENT)
                .isThrownBy(() -> new Notice("제목", fakeContent, admin));
    }

    @Test
    void 공지사항_생성_실패__작성자_NULL() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Notice("제목", "내용", null));
    }

    @Test
    void 공지사항_수정_성공() {
        Notice notice = NoticeFixture.createNotice(AdminFixture.createAdmin());
        
        notice.update("제목수정", "내용수정");
        
        assertThat(notice.getTitle()).isEqualTo("제목수정");
        assertThat(notice.getContent()).isEqualTo("내용수정");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 공지사항_수정_실패__제목_NULL_또는_공백(String title) {
        Notice notice = NoticeFixture.createNotice(AdminFixture.createAdmin());

        assertThatIllegalArgumentException().isThrownBy(()-> notice.update(title, "내용수정"));
    }

    @Test
    void 공지사항_수정_실패__제목_90글자초과() {
        String fakeTitle = stringLength(91);
        Notice notice = NoticeFixture.createNotice(AdminFixture.createAdmin());

        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.OVER_LENGTH_TITLE)
                .isThrownBy(() -> notice.update(fakeTitle, "수정내용"));
    }
    
    @ParameterizedTest
    @NullAndEmptySource
    void 공지사항_수정_실패__내용_NULL_또는_공백(String content) {
        Notice notice = NoticeFixture.createNotice(AdminFixture.createAdmin());

        assertThatIllegalArgumentException().isThrownBy(()-> notice.update("수정제목", content));
    }

    @Test
    void 공지사항_수정_실패__내용_1000글자초과() {
        String fakeContent = stringLength(1001);
        Notice notice = NoticeFixture.createNotice(AdminFixture.createAdmin());

        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.OVER_LENGTH_CONTENT)
                .isThrownBy(() -> notice.update("수정제목", fakeContent));
    }

    @Test
    void 공지사항_삭제_성공() {
        Notice notice = NoticeFixture.createNotice(AdminFixture.createAdmin());

        notice.delete();

        assertThat(notice.getStatus()).isEqualTo(NoticeStatus.DELETED);
    }
}