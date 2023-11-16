package com.hwamok.admin.domain;

import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.core.exception.HwamokExceptionTest;
import fixture.AdminFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static com.hwamok.core.exception.HwamokExceptionTest.*;
import static org.assertj.core.api.Assertions.*;


class AdminTest {
    // 공백을 띄우는 나만의 기준
    // 1. 메소드와 메소드 사이
    // 2. given when then ==> test code
    // - given : 준비
    // - when : 실행
    // - then : 결과
    // 3. 결이 다를 때

    @Test
    void 관리자_생성_성공() {
        Admin admin = new Admin("test123", "1234", "이름", "test@test.com");

        assertThat(admin.getId()).isNull();
        assertThat(admin.getLoginId()).isEqualTo("test123");
        assertThat(admin.getPassword()).isEqualTo("1234");
        assertThat(admin.getName()).isEqualTo("이름");
        assertThat(admin.getEmail()).isEqualTo("test@test.com");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 관리자_생성_실패__아이디_null_또는_공백(String loginId) {
        assertThatIllegalArgumentException()
                .isThrownBy(()->new Admin(loginId,"1234","이름","test@test.com"));
    }

    @Test
    void 관리자_생성_실패__아이디_2글자_이하() {
        assertThatHwamokException(ExceptionCode.NOT_LOGINID_FORM)
                .isThrownBy(()-> new Admin("t","1234","이름","test@test.com"));
    }

    @Test
    void 관리자_생성_실패__아이디_12글자_이상() {
        String fakeLoginId = "testtesttestt";

        assertThatHwamokException(ExceptionCode.NOT_LOGINID_FORM)
                .isThrownBy(()-> new Admin(fakeLoginId,"1234","이름","test@test.com"));
    }

    @Test
    void 관리자_생성_실패__아이디_특수문자사용() {
        String fakeLoginId = "test!";

        assertThatHwamokException(ExceptionCode.NOT_LOGINID_FORM)
                .isThrownBy(()-> new Admin(fakeLoginId,"1234","이름","test@test.com"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 관리자_생성_실패__비밀번호_null_또는_공백(String password) {
        assertThatIllegalArgumentException()
                .isThrownBy(()->new Admin("test123",password,"이름","test@test.com"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 관리자_생성_실패__이름_null_또는_공백(String name) {
        assertThatIllegalArgumentException()
                .isThrownBy(()->new Admin("test123","1234",name,"test@test.com"));
    }

    @Test
    void 관리자_생성_실패__이름_2글자_이하() {
        String fakeName = "이";

        assertThatHwamokException(ExceptionCode.NOT_NAME_FORM)
                .isThrownBy(()-> new Admin("test123","1234", fakeName,"test@test.com"));
    }

    @Test
    void 관리자_생성_실패__이름_20글자_이상() {
        String fakeName = "이름이름이름이름이름이름이름이름이름이름이";

        assertThatHwamokException(ExceptionCode.NOT_NAME_FORM)
                .isThrownBy(()-> new Admin("test123", "1234", fakeName, "test@test.com"));
    }

    @Test
    void 관리자_생성_실패__이름_한글영어혼용() {
        String fakeName = "이름name";

        assertThatHwamokException(ExceptionCode.NOT_NAME_FORM)
                .isThrownBy(()-> new Admin("test123","1234",fakeName,"test@test.com"));
    }

    @Test
    void 관리자_생성_실패__이름_특수문자사용() {
        assertThatHwamokException(ExceptionCode.NOT_NAME_FORM)
                .isThrownBy(()-> new Admin("test123","1234","이름!","test@test.com"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 관리자_생성_실패__이메일_null_또는_공백(String email) {
        assertThatIllegalArgumentException().isThrownBy(()->new Admin("test123","1234","이름",email));
    }

    @Test
    void 관리자_생성_실패__이메일_골뱅이없음() {
        assertThatHwamokException(ExceptionCode.NOT_EMAIL_FORM).isThrownBy(()-> new Admin("test123","1234","이름","testtest.com"));
    }

    @Test
    void 관리자_생성_실패__이메일_점없음() {
        assertThatHwamokException(ExceptionCode.NOT_EMAIL_FORM).isThrownBy(()-> new Admin("test123","1234","이름","test@testcom"));
    }

    @Test
    void 관리자_수정_성공() {
        Admin admin = AdminFixture.createAdmin();

        admin.update("update1234","수정이름","update@update.com");

        assertThat(admin.getPassword()).isEqualTo("update1234");
        assertThat(admin.getName()).isEqualTo("수정이름");
        assertThat(admin.getEmail()).isEqualTo("update@update.com");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 관리자_수정_실패__비밀번호_null_또는_공백(String password) {
        Admin admin = AdminFixture.createAdmin();

        assertThatIllegalArgumentException()
                .isThrownBy(()-> admin.update(password,"수정이름","update@update.com"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 관리자_수정_실패__이름_null_또는_공백(String name) {
        Admin admin = AdminFixture.createAdmin();

        assertThatIllegalArgumentException()
                .isThrownBy(()-> admin.update("update1234",name,"update@update.com"));
    }

    @Test
    void 관리자_수정_실패__이름_2글자_이하() {
        Admin admin = AdminFixture.createAdmin();

        assertThatHwamokException(ExceptionCode.NOT_NAME_FORM)
                .isThrownBy(()-> admin.update("update1234","이","update@update.com"));
    }

    @Test
    void 관리자_수정_실패__이름_20글자_이상() {
        Admin admin = AdminFixture.createAdmin();

        assertThatHwamokException(ExceptionCode.NOT_NAME_FORM)
                .isThrownBy(()-> admin.update("update1234","이름이름이름이름이름이름이름이름이름이름이","update@update.com"));
    }

    @Test
    void 관리자_수정_실패__이름_한글영어혼용() {
        Admin admin = AdminFixture.createAdmin();

        assertThatHwamokException(ExceptionCode.NOT_NAME_FORM)
                .isThrownBy(()-> admin.update("update1234","이름name","update@update.com"));
    }


    @Test
    void 관리자_수정_실패__이름_특수문자사용() {
        Admin admin = AdminFixture.createAdmin();
        assertThatHwamokException(ExceptionCode.NOT_NAME_FORM).isThrownBy(()-> admin.update("update1234","이름!","update@update.com"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 관리자_수정_실패__이메일_null_또는_공백(String email) {
        Admin admin = AdminFixture.createAdmin();
        assertThatIllegalArgumentException().isThrownBy(()-> admin.update("update1234","이름",email));
    }

    @Test
    void 관리자_수정_실패__이메일_골뱅이없음() {
        Admin admin = AdminFixture.createAdmin();
        assertThatHwamokException(ExceptionCode.NOT_EMAIL_FORM).isThrownBy(()-> admin.update("update1234","이름","testtest.com"));
    }

    @Test
    void 관리자_수정_실패__이메일_점없음() {
        Admin admin = AdminFixture.createAdmin();
        assertThatHwamokException(ExceptionCode.NOT_EMAIL_FORM).isThrownBy(()-> admin.update("update1234","이름","test@testcom"));
    }
}