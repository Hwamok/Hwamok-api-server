package com.hwamok.user.domain;

import com.hwamok.core.exception.ExceptionCode;
import fixture.UserFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static com.hwamok.core.exception.HwamokExceptionTest.assertThatHwamokException;
import static org.assertj.core.api.Assertions.*;

class UserTest {
    // 공백을 띄우는 기준
    // 1. 메소드와 메소드 사이
    // 2. given when then ==> test code
    // - given : 준비
    // - when : 실행
    // - then : 결과
    // 3. 결이 다를 때

    @Test
    void 회원_가입_성공() {
        User user = new User("hwamok@test.com", "1234", "hwamok", "2023-11-15",
                "01012345678", "GOOGLE","originalImage", "savedImage",
                12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201");

        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo("hwamok@test.com");
        assertThat(user.getPassword()).isEqualTo("1234");
        assertThat(user.getName()).isEqualTo("hwamok");
        assertThat(user.getBirthDay()).isEqualTo("2023-11-15");
        assertThat(user.getPhone()).isEqualTo("01012345678");
        assertThat(user.getPlatform()).isEqualTo(JoinPlatform.GOOGLE);
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVATED);
        assertThat(user.getProfile().getOriginalFileName()).isEqualTo("originalImage");
        assertThat(user.getProfile().getSavedFileName()).isEqualTo("savedImage");
        assertThat(user.getAddress().getPost()).isEqualTo(12345);
        assertThat(user.getAddress().getAddr()).isEqualTo("15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea");
        assertThat(user.getAddress().getDetailAddr()).isEqualTo("201");
    }

    @Test
    void 회원_수정_성공() {
        User user = UserFixture.create();

        user.update("hwamok1@test.com", "12345", "hwamokhwa", "2023-11-16",
                "01012345679", "NAVER", "originalImage1", "savedImage1",
                12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202");

        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo("hwamok1@test.com");
        assertThat(user.getPassword()).isEqualTo("12345");
        assertThat(user.getName()).isEqualTo("hwamokhwa");
        assertThat(user.getBirthDay()).isEqualTo("2023-11-16");
        assertThat(user.getPhone()).isEqualTo("01012345679");
        assertThat(user.getPlatform()).isEqualTo(JoinPlatform.NAVER);
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVATED);
        assertThat(user.getProfile().getOriginalFileName()).isEqualTo("originalImage1");
        assertThat(user.getProfile().getSavedFileName()).isEqualTo("savedImage1");
        assertThat(user.getAddress().getPost()).isEqualTo(12346);
        assertThat(user.getAddress().getAddr()).isEqualTo("17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea");
        assertThat(user.getAddress().getDetailAddr()).isEqualTo("202");
    }

    @Test
    void 회원_탈퇴_성공() {
        User user = UserFixture.create();

        user.delete();

        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo("hwamok@test.com");
        assertThat(user.getPassword()).isEqualTo("1234");
        assertThat(user.getName()).isEqualTo("hwamok");
        assertThat(user.getBirthDay()).isEqualTo("2023-11-15");
        assertThat(user.getPhone()).isEqualTo("01012345678");
        assertThat(user.getPlatform()).isEqualTo(JoinPlatform.GOOGLE);
        assertThat(user.getStatus()).isEqualTo(UserStatus.INACTIVATED);
        assertThat(user.getProfile().getOriginalFileName()).isEqualTo("originalImage");
        assertThat(user.getProfile().getSavedFileName()).isEqualTo("savedImage");
        assertThat(user.getAddress().getPost()).isEqualTo(12345);
        assertThat(user.getAddress().getAddr()).isEqualTo("15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea");
        assertThat(user.getAddress().getDetailAddr()).isEqualTo("201");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_email_null_혹은_빈값(String email) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new User(email, "1234", "hwamok", "2023-11-15",
                        "01012345678", "GOOGLE","originalImage", "savedImage",
                        12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_password_null_혹은_빈값(String password) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new User("hwamok@test.com", password, "hwamok", "2023-11-15",
                        "01012345678", "GOOGLE","originalImage", "savedImage",
                        12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_name_null_혹은_빈값(String name) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new User("hwamok@test.com", "1234", name, "2023-11-15",
                        "01012345678", "GOOGLE","originalImage", "savedImage",
                        12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_birthDay_null_혹은_빈값(String birthDay) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new User("hwamok@test.com", "1234", "hwamok", birthDay,
                        "01012345678", "GOOGLE","originalImage", "savedImage",
                        12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_phone_null_혹은_빈값(String phone) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new User("hwamok@test.com", "1234", "hwamok",
                        "2023-11-15", phone, "GOOGLE","originalImage", "savedImage",
                        12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_platform_null_혹은_빈값(String platform) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new User("hwamok@test.com", "1234", "hwamok",
                        "2023-11-15", "01012345678", platform,"originalImage",
                        "savedImage",12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_addr_null_혹은_빈값(String addr) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new User("hwamok@test.com", "1234", "hwamok",
                        "2023-11-15", "01012345678", "GOOGLE","originalImage",
                        "savedImage",12345, addr, "201"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_detailAddr_null_혹은_빈값(String detailAddr) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new User("hwamok@test.com", "1234", "hwamok",
                        "2023-11-15", "01012345678", "GOOGLE","originalImage",
                        "savedImage",12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        detailAddr));
    }

    @Test
    void 회원_가입_실패_email_50글자_이상() {
        String fakeEmail = "hwamokhwamokhwamokhwamokhwamokhwamokhwamokhwamokhwamokhwamokhwamok@test.com";

        assertThatIllegalArgumentException()
                .isThrownBy(()->new User(fakeEmail, "1234", "hwamok", "2023-11-15",
                        "01012345678", "GOOGLE","originalImage", "savedImage",
                        12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201"));
    }

    @Test
    void 회원_가입_실패_name_20글자_이상() {
        String fakeName = "hwamokhwamokhwamokhwamok";

        assertThatIllegalArgumentException()
                .isThrownBy(()->new User("hwamok@test.com", "1234", fakeName, "2023-11-15",
                        "01012345678", "GOOGLE","originalImage", "savedImage",
                        12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201"));
    }

    @Test
    void 회원_가입_실패_birthDay_10글자_이상() {
        String fakeBirthDay = "2023-11-1512";

        assertThatIllegalArgumentException()
                .isThrownBy(()->new User("hwamok@test.com", "1234", "hwamok", fakeBirthDay,
                        "01012345678", "GOOGLE","originalImage", "savedImage",
                        12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201"));
    }

    @Test
    void 회원_가입_실패_phone_11글자_이상() {
        String fakePhone = "010123456789";

        assertThatIllegalArgumentException()
                .isThrownBy(()->new User("hwamok@test.com", "1234", "hwamok",
                        "2023-11-15", fakePhone, "GOOGLE", "originalImage",
                        "savedImage",12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));
    }

    @Test
    void 회원_가입_실패_platform_11글자_이상() {
        String fakePlatform = "GOOGLEGOOGLE";

        assertThatIllegalArgumentException()
                .isThrownBy(()->new User("hwamok@test.com", "1234", "hwamok",
                        "2023-11-15", "01012345678", fakePlatform, "originalImage",
                        "savedImage",12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));
    }

    @Test
    void 회원_가입_실패_validate_name_특수문자() {
        String fakeName = "화목!";

        assertThatHwamokException(ExceptionCode.NOT_NAME_FORM)
                .isThrownBy(()-> new User("hwamok@test.com", "1234", fakeName,
                        "2023-11-15", "01012345678", "GOOGLE", "originalImage",
                        "savedImage",12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));
    }

    @Test
    void 회원_가입_실패_validate_name_두_글자_이하() {
        String fakeName = "화";

                assertThatHwamokException(ExceptionCode.NOT_NAME_FORM)
                .isThrownBy(()-> new User("hwamok@test.com", "1234", fakeName,
                        "2023-11-15", "01012345678", "GOOGLE", "originalImage",
                        "savedImage",12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));
    }

    @Test
    void 회원_가입_실패_validate_name_영문한글_혼용() {
        String fakeName = "화목hwamok";

        assertThatHwamokException(ExceptionCode.NOT_NAME_FORM)
                .isThrownBy(()-> new User("hwamok@test.com", "1234", fakeName,
                        "2023-11-15", "01012345678", "GOOGLE", "originalImage",
                        "savedImage",12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));
    }

    @Test
    void 회원_가입_실패_validate_email_골뱅이_없음() {
        String fakeEmail = "hwamoktest.com";

        assertThatHwamokException(ExceptionCode.NOT_EMAIL_FORM)
                .isThrownBy(()-> new User(fakeEmail, "1234", "hwamok","2023-11-15",
                        "01012345678", "GOOGLE", "originalImage","savedImage",
                        12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea","201"));
    }

    @Test
    void 회원_가입_실패_validate_email_점_없음() {
        String fakeEmail = "hwamok@testcom";

        assertThatHwamokException(ExceptionCode.NOT_EMAIL_FORM)
                .isThrownBy(()-> new User(fakeEmail, "1234", "hwamok","2023-11-15",
                        "01012345678", "GOOGLE", "originalImage","savedImage",
                        12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea","201"));
    }

    @Test
    void 회원_가입_실패_validate_birthDay_다시_없음() {
        String fakeBirthDay = "20231115";

        assertThatHwamokException(ExceptionCode.NOT_DATE_FORM)
                .isThrownBy(()-> new User("hwamok@test.com", "1234", "hwamok",fakeBirthDay,
                        "01012345678", "GOOGLE", "originalImage","savedImage",
                        12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea","201"));
    }

    @Test
    void 회원_가입_실패_validate_email_슬래시_변경() {
        String fakeBirthDay = "2023/11/15";

        assertThatHwamokException(ExceptionCode.NOT_DATE_FORM)
                .isThrownBy(()-> new User("hwamok@test.com", "1234", "hwamok",fakeBirthDay,
                        "01012345678", "GOOGLE", "originalImage","savedImage",
                        12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea","201"));
    }

    @Test
    void 회원_가입_실패_post_음수일때() {
        int fakePost = -1;

        assertThatIllegalArgumentException()
                .isThrownBy(()-> new User("hwamok@test.com", "1234", "hwamok", "2023-11-15",
                        "01012345678", "GOOGLE", "originalImage","savedImage",
                        fakePost, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea","201"));
    }

    @Test
    void 회원_가입_실패_validate_post_5자리_아래일때() {
        int fakePost = 1234;

        assertThatHwamokException(ExceptionCode.NOT_POST_FORM)
                .isThrownBy(()-> new User("hwamok@test.com", "1234", "hwamok", "2023-11-15",
                        "01012345678", "GOOGLE", "originalImage","savedImage",
                        fakePost, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea","201"));
    }

    @Test
    void 회원_가입_실패_validate_phone_숫자_제외_다른_문자() {
        String fakePhone = "010-1234#56";

        assertThatHwamokException(ExceptionCode.NOT_PHONE_FORM)
                .isThrownBy(()-> new User("hwamok@test.com", "1234", "hwamok", "2023-11-15",
                        fakePhone, "GOOGLE", "originalImage","savedImage",
                        12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea","201"));
    }

    @Test
    void 회원_가입_실패_validate_phone_11자리_이하() {
        String fakePhone = "0101234567";

        assertThatHwamokException(ExceptionCode.NOT_PHONE_FORM)
                .isThrownBy(()-> new User("hwamok@test.com", "1234", "hwamok", "2023-11-15",
                        fakePhone, "GOOGLE", "originalImage","savedImage",
                        12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea","201"));
    }

    @Test
    void 회원_가입_실패_validate_phone_첫째자리_0_제외() {
        String fakePhone = "21012345678";

        assertThatHwamokException(ExceptionCode.NOT_PHONE_FORM)
                .isThrownBy(()-> new User("hwamok@test.com", "1234", "hwamok", "2023-11-15",
                        fakePhone, "GOOGLE", "originalImage","savedImage",
                        12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea","201"));
    }

    @Test
    void 회원_가입_실패_validate_phone_두째자리_1_제외() {
        String fakePhone = "00012345678";

        assertThatHwamokException(ExceptionCode.NOT_PHONE_FORM)
                .isThrownBy(()-> new User("hwamok@test.com", "1234", "hwamok", "2023-11-15",
                        fakePhone, "GOOGLE", "originalImage","savedImage",
                        12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea","201"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_email_null_혹은_빈값(String email) {
        User user = UserFixture.create();

        assertThatIllegalArgumentException()
                .isThrownBy( () -> user.update(email, "12345", "hwamokhwa", "2023-11-16",
                        "01012345679", "NAVER","originalImage1", "savedImage1",
                        12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_password_null_혹은_빈값(String password) {
        User user = UserFixture.create();

        assertThatIllegalArgumentException()
                .isThrownBy( () -> user.update("hwamok@test.com", password, "hwamokhwa",
                        "2023-11-16", "01012345679", "NAVER","originalImage1",
                        "savedImage1",12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea",
                        "202"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_name_null_혹은_빈값(String name) {
        User user = UserFixture.create();

        assertThatIllegalArgumentException()
                .isThrownBy( () -> user.update("hwamok@test.com", "1234", name, "2023-11-16",
                        "01012345679", "NAVER","originalImage1", "savedImage1",
                        12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_birthDay_null_혹은_빈값(String birthDay) {
        User user = UserFixture.create();

        assertThatIllegalArgumentException()
                .isThrownBy( () -> user.update("hwamok@test.com", "1234", "hwamokhwa",
                        birthDay, "01012345679", "NAVER","originalImage1", "savedImage1",
                        12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_phone_null_혹은_빈값(String phone) {
        User user = UserFixture.create();

        assertThatIllegalArgumentException()
                .isThrownBy( () -> user.update("hwamok@test.com", "1234", "hwamokhwa",
                        "2023-11-16", phone, "NAVER","originalImage1", "savedImage1",
                        12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_platform_null_혹은_빈값(String platform) {
        User user = UserFixture.create();

        assertThatIllegalArgumentException()
                .isThrownBy( () -> user.update("hwamok@test.com", "1234", "hwamokhwa",
                        "2023-11-16", "01012345679", platform, "originalImage1",
                        "savedImage1",12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea",
                        "202"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_addr_null_혹은_빈값(String addr) {
        User user = UserFixture.create();

        assertThatIllegalArgumentException()
                .isThrownBy( () -> user.update("hwamok@test.com", "1234", "hwamokhwa",
                        "2023-11-16", "01012345679", "NAVER","originalImage",
                        "savedImage1",12346, addr, "202"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_detailAddr_null_혹은_빈값(String detailAddr) {
        User user = UserFixture.create();

        assertThatIllegalArgumentException()
                .isThrownBy( () -> user.update("hwamok@test.com", "1234", "hwamokhwa",
                        "2023-11-16", "01012345679", "NAVER", "originalImage",
                        "savedImage1",12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea",
                        detailAddr));
    }

    @Test
    void 회원_수정_실패_email_50글자_이상() {
        User user = UserFixture.create();

        String fakeEmail = "hwamokhwamokhwamokhwamokhwamokhwamokhwamokhwamokhwamokhwamokhwamok@test.com";

        assertThatIllegalArgumentException()
                .isThrownBy( () -> user.update(fakeEmail, "1234", "hwamokhwa", "2023-11-16",
                        "01012345679", "NAVER","originalImage", "savedImage1",
                        12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202"));
    }

    @Test
    void 회원_수정_실패_name_20글자_이상() {
        User user = UserFixture.create();

        String fakeName = "hwamokhwamokhwamokhwamok";

        assertThatIllegalArgumentException()
                .isThrownBy( () -> user.update("hwamok@test.com", "1234", fakeName,
                        "2023-11-16", "01012345679", "NAVER","originalImage",
                        "savedImage1",12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea",
                        "202"));
    }

    @Test
    void 회원_수정_실패_birthDay_10글자_이상() {
        User user = UserFixture.create();

        String fakeBirthDay = "2023-11-1512";


        assertThatIllegalArgumentException()
                .isThrownBy( () -> user.update("hwamok@test.com", "1234", "hwamok",
                        fakeBirthDay, "01012345679", "NAVER", "originalImage",
                        "savedImage1",12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea",
                        "202"));
    }

    @Test
    void 회원_수정_실패_phone_11글자_이상() {
        User user = UserFixture.create();

        String fakePhone = "010123456789";

        assertThatIllegalArgumentException()
                .isThrownBy( () -> user.update("hwamok@test.com", "1234", "hwamok",
                        "2023-11-16", fakePhone, "NAVER","originalImage",
                        "savedImage1",12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea",
                        "202"));
    }

    @Test
    void 회원_수정_실패_platform_11글자_이상() {
        User user = UserFixture.create();

        String fakePlatform = "NAVERNAVERNAVER";

        assertThatIllegalArgumentException()
                .isThrownBy( () -> user.update("hwamok@test.com", "1234", "hwamok",
                        "2023-11-16", "01012345679", fakePlatform, "originalImage",
                        "savedImage1",12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea",
                        "202"));
    }

    @Test
    void 회원_수정_실패_validate_name_특수문자() {
        User user = UserFixture.create();

        String fakeName = "화목!";

        assertThatHwamokException(ExceptionCode.NOT_NAME_FORM)
                .isThrownBy(()-> user.update("hwamok@test.com", "1234", fakeName,
                        "2023-11-15", "01012345678", "GOOGLE", "originalImage",
                        "savedImage",12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));
    }

    @Test
    void 회원_수정_실패_validate_name_두_글자_이하() {
        User user = UserFixture.create();

        String fakeName = "화";

        assertThatHwamokException(ExceptionCode.NOT_NAME_FORM)
                .isThrownBy(()-> user.update("hwamok@test.com", "1234", fakeName,
                        "2023-11-15", "01012345678", "GOOGLE", "originalImage",
                        "savedImage",12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));
    }

    @Test
    void 회원_수정_실패_validate_name_영문한글_혼용() {
        User user = UserFixture.create();

        String fakeName = "화목hwamok";

        assertThatHwamokException(ExceptionCode.NOT_NAME_FORM)
                .isThrownBy(()-> user.update("hwamok@test.com", "1234", fakeName,
                        "2023-11-15", "01012345678", "GOOGLE", "originalImage",
                        "savedImage",12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));
    }

    @Test
    void 회원_수정_실패_validate_email_골뱅이_없음() {
        User user = UserFixture.create();

        String fakeEmail = "hwamoktest.com";

        assertThatHwamokException(ExceptionCode.NOT_EMAIL_FORM)
                .isThrownBy(()-> user.update(fakeEmail, "1234", "hwamok","2023-11-15",
                        "01012345678", "GOOGLE", "originalImage","savedImage",
                        12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea","201"));
    }

    @Test
    void 회원_수정_실패_validate_email_점_없음() {
        User user = UserFixture.create();

        String fakeEmail = "hwamok@testcom";

        assertThatHwamokException(ExceptionCode.NOT_EMAIL_FORM)
                .isThrownBy(()-> user.update(fakeEmail, "1234", "hwamok","2023-11-15",
                        "01012345678", "GOOGLE", "originalImage","savedImage",
                        12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea","201"));
    }

    @Test
    void 회원_수정_실패_validate_birthDay_다시_없음() {
        User user = UserFixture.create();

        String fakeBirthDay = "20231115";

        assertThatHwamokException(ExceptionCode.NOT_DATE_FORM)
                .isThrownBy(()-> user.update("hwamok@test.com", "1234", "hwamok",fakeBirthDay,
                        "01012345678", "GOOGLE", "originalImage","savedImage",
                        12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea","201"));
    }

    @Test
    void 회원_수정_실패_validate_email_슬래시_변경() {
        User user = UserFixture.create();

        String fakeBirthDay = "2023/11/15";

        assertThatHwamokException(ExceptionCode.NOT_DATE_FORM)
                .isThrownBy(()-> user.update("hwamok@test.com", "1234", "hwamok",fakeBirthDay,
                        "01012345678", "GOOGLE", "originalImage","savedImage",
                        12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea","201"));
    }

    @Test
    void 회원_수정_실패_post_음수일때() {
        User user = UserFixture.create();

        int fakePost = -1;

        assertThatIllegalArgumentException()
                .isThrownBy(()-> user.update("hwamok@test.com", "1234", "hwamok",
                        "2023-11-15","01012345678", "GOOGLE", "originalImage",
                        "savedImage", fakePost, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));
    }

    @Test
    void 회원_수정_실패_validate_post_5자리_아래일때() {
        User user = UserFixture.create();

        int fakePost = 1234;

        assertThatHwamokException(ExceptionCode.NOT_POST_FORM)
                .isThrownBy(()-> user.update("hwamok@test.com", "1234", "hwamok",
                        "2023-11-15","01012345678", "GOOGLE", "originalImage",
                        "savedImage", fakePost, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"));
    }

    @Test
    void 회원_수정_실패_validate_phone_숫자_제외_다른_문자() {
        User user = UserFixture.create();

        String fakePhone = "010-1234#56";

        assertThatHwamokException(ExceptionCode.NOT_PHONE_FORM)
                .isThrownBy(()-> user.update("hwamok@test.com", "1234", "hwamok", "2023-11-15",
                        fakePhone, "GOOGLE", "originalImage","savedImage",
                        12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea","201"));
    }

    @Test
    void 회원_수정_실패_validate_phone_11자리_이하() {
        User user = UserFixture.create();

        String fakePhone = "0101234567";

        assertThatHwamokException(ExceptionCode.NOT_PHONE_FORM)
                .isThrownBy(()-> user.update("hwamok@test.com", "1234", "hwamok", "2023-11-15",
                        fakePhone, "GOOGLE", "originalImage","savedImage",
                        12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea","201"));
    }

    @Test
    void 회원_수정_실패_validate_phone_첫째자리_0_제외() {
        User user = UserFixture.create();

        String fakePhone = "2101234567";

        assertThatHwamokException(ExceptionCode.NOT_PHONE_FORM)
                .isThrownBy(()-> user.update("hwamok@test.com", "1234", "hwamok", "2023-11-15",
                        fakePhone, "GOOGLE", "originalImage","savedImage",
                        12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea","201"));
    }

    @Test
    void 회원_수정_실패_validate_phone_두째자리_1_제외() {
        User user = UserFixture.create();

        String fakePhone = "00012345678";

        assertThatHwamokException(ExceptionCode.NOT_PHONE_FORM)
                .isThrownBy(()-> user.update("hwamok@test.com", "1234", "hwamok", "2023-11-15",
                        fakePhone, "GOOGLE", "originalImage","savedImage",
                        12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea","201"));
    }
}