package com.hwamok.admin.domain;

import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.core.exception.HwamokExceptionTest;
import fixture.AdminFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.ArrayList;
import java.util.List;

import static com.hwamok.core.exception.HwamokExceptionTest.*;
import static org.assertj.core.api.Assertions.*;

class AdminTest {
    
    private List<Role> roles = new ArrayList<>();

    @BeforeEach
    void setUp() {
        roles.add(Role.SUPER);
        roles.add(Role.ADMIN);
    }

    @Test
    void 관리자_생성_성공() {
        Admin admin = new Admin("test123", "1234", "이름", "test@test.com", roles);

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
                .isThrownBy(()->new Admin(loginId, "1234", "이름", "test@test.com", roles));
    }

    @Test
    void 관리자_생성_실패__아이디_2글자_미만() {
        String fakeLoginId = "t";

        assertThatHwamokException(ExceptionCode.NOT_LOGINID_FORM)
                .isThrownBy(()-> new Admin(fakeLoginId, "1234", "이름", "test@test.com", roles));
    }

    @Test
    void 관리자_생성_실패__아이디_12글자_초과() {
        String fakeLoginId = "testtesttestt";

        assertThatHwamokException(ExceptionCode.NOT_LOGINID_FORM)
                .isThrownBy(()-> new Admin(fakeLoginId, "1234", "이름", "test@test.com", roles));
    }

    @Test
    void 관리자_생성_실패__아이디_특수문자사용() {
        String fakeLoginId = "test!";

        assertThatHwamokException(ExceptionCode.NOT_LOGINID_FORM)
                .isThrownBy(()-> new Admin(fakeLoginId, "1234", "이름", "test@test.com", roles));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 관리자_생성_실패__비밀번호_null_또는_공백(String password) {
        assertThatIllegalArgumentException()
                .isThrownBy(()->new Admin("test123",password,"이름","test@test.com", roles));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 관리자_생성_실패__이름_null_또는_공백(String name) {
        assertThatIllegalArgumentException()
                .isThrownBy(()->new Admin("test123","1234",name,"test@test.com", roles));
    }

    @Test
    void 관리자_생성_실패__이름_한글_2글자_미만() {
        String fakeName = "이";

        assertThatHwamokException(ExceptionCode.NOT_NAME_FORM)
                .isThrownBy(()-> new Admin("test123","1234", fakeName,"test@test.com", roles));
    }

    @Test
    void 관리자_생성_실패__이름_한글_6글자_초과() {
        String fakeName = "이름이름이름이";

        assertThatHwamokException(ExceptionCode.NOT_NAME_FORM)
                .isThrownBy(()-> new Admin("test123", "1234", fakeName, "test@test.com", roles));
    }

    @Test
    void 관리자_생성_실패__이름_영어_2글자_미만() {
        String fakeName = "n";

        assertThatHwamokException(ExceptionCode.NOT_NAME_FORM)
                .isThrownBy(()-> new Admin("test123","1234", fakeName,"test@test.com", roles));
    }

    @Test
    void 관리자_생성_실패__이름_영어_20글자_초과() {
        String fakeName = "namenamenamenamenamen";

        assertThatHwamokException(ExceptionCode.NOT_NAME_FORM)
                .isThrownBy(()-> new Admin("test123", "1234", fakeName, "test@test.com", roles));
    }

    @Test
    void 관리자_생성_실패__이름_한글영어혼용() {
        String fakeName = "이름name";

        assertThatHwamokException(ExceptionCode.NOT_NAME_FORM)
                .isThrownBy(()-> new Admin("test123", "1234", fakeName, "test@test.com", roles));
    }

    @Test
    void 관리자_생성_실패__이름_특수문자사용() {
        String fakeName = "이름!";

        assertThatHwamokException(ExceptionCode.NOT_NAME_FORM)
                .isThrownBy(()-> new Admin("test123", "1234", fakeName, "test@test.com", roles));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 관리자_생성_실패__이메일_null_또는_공백(String email) {
        assertThatIllegalArgumentException()
                .isThrownBy(()->new Admin("test123", "1234", "이름", email, roles));
    }

    @Test
    void 관리자_생성_실패__이메일_골뱅이없음() {
        String fakeEmail = "testtest.com";

        assertThatHwamokException(ExceptionCode.NOT_EMAIL_FORM)
                .isThrownBy(()-> new Admin("test123", "1234", "이름", fakeEmail, roles));
    }

    @Test
    void 관리자_생성_실패__이메일_점없음() {
        String fakeEmail = "test@testcom";

        assertThatHwamokException(ExceptionCode.NOT_EMAIL_FORM)
                .isThrownBy(()-> new Admin("test123", "1234", "이름", fakeEmail, roles));
    }

    @Test
    void 관리자_생성_실패__이메일_50글자초과() {
        String fakeEmail = "testtesttesttesttesttest@testtesttesttesttest11.com";

        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.OVER_LENGTH_EMAIL)
                .isThrownBy(()-> new Admin("test123","1234","이름",fakeEmail, roles));
    }

    @Test
    void 관리자_생성_실패__권한이_null() {
        Assertions.assertThatIllegalArgumentException()
                .isThrownBy(()-> new Admin("test123","1234","이름", "email", null));
    }

    @Test
    void 관리자_생성_실패__권한이_비어있음() {
        Assertions.assertThatIllegalArgumentException()
                .isThrownBy(()-> new Admin("test123","1234","이름", "email", List.of()));
    }

    @Test
    void 관리자_수정_성공() {
        Admin admin = AdminFixture.createAdmin();

        admin.update("update1234", "수정이름", "update@update.com");

        assertThat(admin.getPassword()).isEqualTo("update1234");
        assertThat(admin.getName()).isEqualTo("수정이름");
        assertThat(admin.getEmail()).isEqualTo("update@update.com");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 관리자_수정_실패__비밀번호_null_또는_공백(String password) {
        Admin admin = AdminFixture.createAdmin();

        assertThatIllegalArgumentException()
                .isThrownBy(()-> admin.update(password, "수정이름", "update@update.com"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 관리자_수정_실패__이름_null_또는_공백(String name) {
        Admin admin = AdminFixture.createAdmin();

        assertThatIllegalArgumentException()
                .isThrownBy(()-> admin.update("update1234", name, "update@update.com"));
    }

    @Test
    void 관리자_수정_실패__이름_한글_2글자_미만() {
        String fakeName = "이";
        Admin admin = AdminFixture.createAdmin();

        assertThatHwamokException(ExceptionCode.NOT_NAME_FORM)
                .isThrownBy(()-> admin.update("update1234", fakeName, "update@update.com"));
    }

    @Test
    void 관리자_수정_실패__이름_한글_6글자_초과() {
        String fakeName = "이름이름이름이";
        Admin admin = AdminFixture.createAdmin();

        assertThatHwamokException(ExceptionCode.NOT_NAME_FORM)
                .isThrownBy(()-> admin.update("update1234",fakeName,"update@update.com"));
    }

    @Test
    void 관리자_수정_실패__이름_영어_2글자_미만() {
        String fakeName = "n";
        Admin admin = AdminFixture.createAdmin();

        assertThatHwamokException(ExceptionCode.NOT_NAME_FORM)
                .isThrownBy(()-> admin.update("update1234",fakeName,"update@update.com"));
    }

    @Test
    void 관리자_수정_실패__이름_영어_20글자_초과() {
        String fakeName = "namenamenamenamenamen";
        Admin admin = AdminFixture.createAdmin();

        assertThatHwamokException(ExceptionCode.NOT_NAME_FORM)
                .isThrownBy(()-> admin.update("update1234", fakeName, "update@update.com"));
    }

    @Test
    void 관리자_수정_실패__이름_한글영어혼용() {
        String fakeName = "이름name";
        Admin admin = AdminFixture.createAdmin();

        assertThatHwamokException(ExceptionCode.NOT_NAME_FORM)
                .isThrownBy(()-> admin.update("update1234", fakeName, "update@update.com"));
    }

    @Test
    void 관리자_수정_실패__이름_특수문자사용() {
        String fakeName = "이름!";
        Admin admin = AdminFixture.createAdmin();

        assertThatHwamokException(ExceptionCode.NOT_NAME_FORM)
                .isThrownBy(()-> admin.update("update1234", fakeName, "update@update.com"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 관리자_수정_실패__이메일_null_또는_공백(String email) {
        Admin admin = AdminFixture.createAdmin();

        assertThatIllegalArgumentException()
                .isThrownBy(()-> admin.update("update1234", "이름", email));
    }

    @Test
    void 관리자_수정_실패__이메일_50글자초과() {
        String fakeEmail = "testtesttesttesttest@testtesttesttesttesttest11.com";
        Admin admin = AdminFixture.createAdmin();

        HwamokExceptionTest.assertThatHwamokException(ExceptionCode.OVER_LENGTH_EMAIL)
                .isThrownBy(()-> admin.update("1234","이름", fakeEmail));
    }
    
    @Test
    void 관리자_수정_실패__이메일_골뱅이없음() {
        String fakeEmail = "testtest.com";
        Admin admin = AdminFixture.createAdmin();

        assertThatHwamokException(ExceptionCode.NOT_EMAIL_FORM)
                .isThrownBy(()-> admin.update("update1234", "이름", fakeEmail));
    }

    @Test
    void 관리자_수정_실패__이메일_점없음() {
        String fakeEmail = "test@testcom";
        Admin admin = AdminFixture.createAdmin();

        assertThatHwamokException(ExceptionCode.NOT_EMAIL_FORM)
                .isThrownBy(()-> admin.update("update1234", "이름", fakeEmail));
    }
}