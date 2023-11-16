package com.hwamok.admin.domain;

import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.core.exception.HwamokExceptionTest;
import fixture.AdminFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;


class AdminTest {
    @Test
    void 관리자_생성_성공() {
        Admin admin = AdminFixture.createAdmin();
        Assertions.assertThat(admin).isNotNull();
        Assertions.assertThat(admin.getLoginId()).isEqualTo("test123");
        Assertions.assertThat(admin.getPassword()).isEqualTo("1234");
        Assertions.assertThat(admin.getName()).isEqualTo("이름");
        Assertions.assertThat(admin.getEmail()).isEqualTo("test@test.com");
    }
    @ParameterizedTest
    @NullAndEmptySource
    void 관리자_생성_실패__아이디_null_또는_공백(String loginId) {
        Assertions.assertThatIllegalArgumentException().isThrownBy(()->new Admin(loginId,"1234","이름","test@test.com"));
    }

    @Test
    void 관리자_생성_실패__아이디_2글자_이하() {
        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_LOGINID_FORM).isThrownBy(()-> new Admin("t","1234","이름","test@test.com"));
    }
    @Test
    void 관리자_생성_실패__아이디_12글자_이상() {
        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_LOGINID_FORM).isThrownBy(()-> new Admin("testtesttestt","1234","이름","test@test.com"));
    }
    @Test
    void 관리자_생성_실패__아이디_특수문자사용() {
        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_LOGINID_FORM).isThrownBy(()-> new Admin("test!","1234","이름","test@test.com"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 관리자_생성_실패__비밀번호_null_또는_공백(String password) {
        Assertions.assertThatIllegalArgumentException().isThrownBy(()->new Admin("test123",password,"이름","test@test.com"));
    }
    @ParameterizedTest
    @NullAndEmptySource
    void 관리자_생성_실패__이름_null_또는_공백(String name) {
        Assertions.assertThatIllegalArgumentException().isThrownBy(()->new Admin("test123","1234",name,"test@test.com"));
    }
    @Test
    void 관리자_생성_실패__이름_2글자_이하() {
        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_NAME_FORM).isThrownBy(()-> new Admin("test123","1234","이","test@test.com"));
    }
    @Test
    void 관리자_생성_실패__이름_20글자_이상() {
        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_NAME_FORM).isThrownBy(()-> new Admin("test123","1234","이름이름이름이름이름이름이름이름이름이름이","test@test.com"));
    }
    @Test
    void 관리자_생성_실패__이름_한글영어혼용() {
        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_NAME_FORM).isThrownBy(()-> new Admin("test123","1234","이름name","test@test.com"));
    }
    @Test
    void 관리자_생성_실패__이름_특수문자사용() {
        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_NAME_FORM).isThrownBy(()-> new Admin("test123","1234","이름!","test@test.com"));
    }
    @ParameterizedTest
    @NullAndEmptySource
    void 관리자_생성_실패__이메일_null_또는_공백(String email) {
        Assertions.assertThatIllegalArgumentException().isThrownBy(()->new Admin("test123","1234","이름",email));
    }
    @Test
    void 관리자_생성_실패__이메일_골뱅이없음() {
        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_EMAIL_FORM).isThrownBy(()-> new Admin("test123","1234","이름","testtest.com"));
    }
    @Test
    void 관리자_생성_실패__이메일_점없음() {
        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_EMAIL_FORM).isThrownBy(()-> new Admin("test123","1234","이름","test@testcom"));
    }
    @Test
    void 관리자_수정_성공() {
        Admin admin = AdminFixture.createAdmin();
        admin.update("update1234","수정이름","update@update.com");
        Assertions.assertThat(admin.getPassword()).isEqualTo("update1234");
        Assertions.assertThat(admin.getName()).isEqualTo("수정이름");
        Assertions.assertThat(admin.getEmail()).isEqualTo("update@update.com");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 관리자_수정_실패__비밀번호_null_또는_공백(String password) {
        Admin admin = AdminFixture.createAdmin();
        Assertions.assertThatIllegalArgumentException().isThrownBy(()-> admin.update(password,"수정이름","update@update.com"));
    }
    @ParameterizedTest
    @NullAndEmptySource
    void 관리자_수정_실패__이름_null_또는_공백(String name) {
        Admin admin = AdminFixture.createAdmin();
        Assertions.assertThatIllegalArgumentException().isThrownBy(()-> admin.update("update1234",name,"update@update.com"));
    }
    @Test
    void 관리자_수정_실패__이름_2글자_이하() {
        Admin admin = AdminFixture.createAdmin();
        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_NAME_FORM).isThrownBy(()-> admin.update("update1234","이","update@update.com"));
    }
    @Test
    void 관리자_수정_실패__이름_20글자_이상() {
        Admin admin = AdminFixture.createAdmin();
        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_NAME_FORM).isThrownBy(()-> admin.update("update1234","이름이름이름이름이름이름이름이름이름이름이","update@update.com"));
    }
    @Test
    void 관리자_수정_실패__이름_한글영어혼용() {
        Admin admin = AdminFixture.createAdmin();
        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_NAME_FORM).isThrownBy(()-> admin.update("update1234","이름name","update@update.com"));

    }
    @Test
    void 관리자_수정_실패__이름_특수문자사용() {
        Admin admin = AdminFixture.createAdmin();
        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_NAME_FORM).isThrownBy(()-> admin.update("update1234","이름!","update@update.com"));
    }
    @ParameterizedTest
    @NullAndEmptySource
    void 관리자_수정_실패__이메일_null_또는_공백(String email) {
        Admin admin = AdminFixture.createAdmin();
        Assertions.assertThatIllegalArgumentException().isThrownBy(()-> admin.update("update1234","이름",email));
    }
    @Test
    void 관리자_수정_실패__이메일_골뱅이없음() {
        Admin admin = AdminFixture.createAdmin();
        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_EMAIL_FORM).isThrownBy(()-> admin.update("update1234","이름","testtest.com"));
    }
    @Test
    void 관리자_수정_실패__이메일_점없음() {
        Admin admin = AdminFixture.createAdmin();
        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.NOT_EMAIL_FORM).isThrownBy(()-> admin.update("update1234","이름","test@testcom"));
    }
}