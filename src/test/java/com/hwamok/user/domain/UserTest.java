package com.hwamok.user.domain;

import com.hwamok.core.exception.HwamokExceptionTest;
import fixtures.UserFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

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
                "01012345678", "GOOGLE","ACTIVATED",
                "originalImage", "savedImage",
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
                "01012345679", "NAVER", "INACTIVATED",
                "originalImage1", "savedImage1",
                12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202");

        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo("hwamok1@test.com");
        assertThat(user.getPassword()).isEqualTo("12345");
        assertThat(user.getName()).isEqualTo("hwamokhwa");
        assertThat(user.getBirthDay()).isEqualTo("2023-11-16");
        assertThat(user.getPhone()).isEqualTo("01012345679");
        assertThat(user.getPlatform()).isEqualTo(JoinPlatform.NAVER);
        assertThat(user.getStatus()).isEqualTo(UserStatus.INACTIVATED);
        assertThat(user.getProfile().getOriginalFileName()).isEqualTo("originalImage1");
        assertThat(user.getProfile().getSavedFileName()).isEqualTo("savedImage1");
        assertThat(user.getAddress().getPost()).isEqualTo(12346);
        assertThat(user.getAddress().getAddr()).isEqualTo("17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea");
        assertThat(user.getAddress().getDetailAddr()).isEqualTo("202");
    }

    @Test
    void 회원_탈퇴_성공() {
        User user = UserFixture.create();

        user.withdraw();

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

//        User user = new User(email, "1234", "hwamok", "2023-11-15", "01012345678", "GOOGLE",
//                "ACTIVATED", "originalImage", "savedImage",
//                12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201");

        assertThatIllegalArgumentException()
                .isThrownBy(() -> new User(email, "1234", "hwamok", "2023-11-15",
                        "01012345678", "GOOGLE", "ACTIVATED",
                        "originalImage", "savedImage",
                        12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_password_null_혹은_빈값(String password) {

//        User user = new User("hwamok@test.com", password, "hwamok", "2023-11-15", "01012345678", "GOOGLE",
//                "ACTIVATED", "originalImage", "savedImage",
//                12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201");

        assertThatIllegalArgumentException()
                .isThrownBy(() -> new User("hwamok@test.com", password, "hwamok", "2023-11-15", "01012345678", "GOOGLE",
                "ACTIVATED", "originalImage", "savedImage",
                12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_name_null_혹은_빈값(String name) {

//        User user = new User("hwamok@test.com", "1234", name, "2023-11-15", "01012345678", "GOOGLE",
//                "ACTIVATED", "originalImage", "savedImage",
//                12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201");

        assertThatIllegalArgumentException().isThrownBy(() -> new User("hwamok@test.com", "1234", name, "2023-11-15", "01012345678", "GOOGLE",
                "ACTIVATED", "originalImage", "savedImage",
                12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_birthDay_null_혹은_빈값(String birthDay) {
//                User user = new User("hwamok@test.com", "1234", "hwamok", birthDay, "01012345678", "GOOGLE",
//                "ACTIVATED", "originalImage", "savedImage",
//                12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201");

        assertThatIllegalArgumentException().isThrownBy(() -> new User("hwamok@test.com", "1234", "hwamok", birthDay, "01012345678", "GOOGLE",
                "ACTIVATED", "originalImage", "savedImage",
                12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_phone_null_혹은_빈값(String phone) {
//                User user = new User("hwamok@test.com", "1234", "hwamok", "2023-11-15", phone, "GOOGLE",
//                "ACTIVATED", "originalImage", "savedImage",
//                12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201");

        assertThatIllegalArgumentException().isThrownBy(() -> new User("hwamok@test.com", "1234", "hwamok", "2023-11-15", phone, "GOOGLE",
                "ACTIVATED", "originalImage", "savedImage",
                12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_platform_null_혹은_빈값(String platform) {
//        User user = new User("hwamok@test.com", "1234", "hwamok", "2023-11-15", "01012345678", platform,
//                "ACTIVATED", "originalImage", "savedImage",
//                12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201");

        assertThatIllegalArgumentException().isThrownBy(() -> new User("hwamok@test.com", "1234", "hwamok", "2023-11-15", "01012345678", platform,
                "ACTIVATED", "originalImage", "savedImage",
                12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201"));
    }


    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_status_null_혹은_빈값(String status) {
//        User user = new User("hwamok@test.com", "1234", "hwamok", "2023-11-15", "01012345678", "GOOGLE",
//                status, "originalImage", "savedImage",
//                12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201");

        assertThatIllegalArgumentException().isThrownBy(() -> new User("hwamok@test.com", "1234", "hwamok", "2023-11-15", "01012345678", "GOOGLE",
                status, "originalImage", "savedImage",
                12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201"));
    }


    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_addr_null_혹은_빈값(String addr) {
//        User user = new User("hwamok@test.com", "1234", "hwamok", "2023-11-15", "01012345678", "GOOGLE",
//                "ACTIVATED", "originalImage", "savedImage",
//                12345, addr, "201");

        assertThatIllegalArgumentException().isThrownBy(() -> new User("hwamok@test.com", "1234", "hwamok", "2023-11-15", "01012345678", "GOOGLE",
                "ACTIVATED", "originalImage", "savedImage",
                12345, addr, "201"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_detailAddr_null_혹은_빈값(String detailAddr) {
//        User user = new User("hwamok@test.com", "1234", "hwamok", "2023-11-15", "01012345678", "GOOGLE",
//                "ACTIVATED", "originalImage", "savedImage",
//                12345, addr, detailAddr);

        assertThatIllegalArgumentException().isThrownBy(() -> new User("hwamok@test.com", "1234", "hwamok", "2023-11-15", "01012345678", "GOOGLE",
                "ACTIVATED", "originalImage", "savedImage",
                12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", detailAddr));
    }

    @Test
    void 회원_가입_실패_email_50글자_이상() {
//        User user = new User("hwamokhwamokhwamokhwamokhwamokhwamokhwamokhwamokhwamokhwamokhwamok@test.com", "1234", "hwamok", "2023-11-15", "01012345678", "GOOGLE",
//                "ACTIVATED", "originalImage", "savedImage",
//                12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201");

        assertThatIllegalArgumentException()
                .isThrownBy(()->new User("hwamokhwamokhwamokhwamokhwamokhwamokhwamokhwamokhwamokhwamokhwamok@test.com", "1234", "hwamok", "2023-11-15", "01012345678", "GOOGLE",
                        "ACTIVATED", "originalImage", "savedImage",
                        12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201"));
    }

    @Test
    void 회원_가입_실패_name_20글자_이상() {
//        User user = new User("hwamok@test.com", "1234", "hwamokhwamokhwamokhwamok", "2023-11-15", "01012345678", "GOOGLE",
//                "ACTIVATED", "originalImage", "savedImage",
//                12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201");

        assertThatIllegalArgumentException()
                .isThrownBy(()->new User("hwamok@test.com", "1234", "hwamokhwamokhwamokhwamok", "2023-11-15", "01012345678", "GOOGLE",
                        "ACTIVATED", "originalImage", "savedImage",
                        12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201"));
    }

    @Test
    void 회원_가입_실패_birthDay_10글자_이상() {
//        User user = new User("hwamok@test.com", "1234", "hwamok", "2023-11-1512", "01012345678", "GOOGLE",
//                "ACTIVATED", "originalImage", "savedImage",
//                12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201");

        assertThatIllegalArgumentException()
                .isThrownBy(()->new User("hwamok@test.com", "1234", "hwamok", "2023-11-1512", "01012345678", "GOOGLE",
                        "ACTIVATED", "originalImage", "savedImage",
                        12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201"));
    }

    @Test
    void 회원_가입_실패_phone_11글자_이상() {
//        User user = new User("hwamok@test.com", "1234", "hwamok", "2023-11-15", "010123456789", "GOOGLE",
//                "ACTIVATED", "originalImage", "savedImage",
//                12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201");

        assertThatIllegalArgumentException()
                .isThrownBy(()->new User("hwamok@test.com", "1234", "hwamok", "2023-11-15", "010123456789", "GOOGLE",
                        "ACTIVATED", "originalImage", "savedImage",
                        12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201"));
    }

    @Test
    void 회원_가입_실패_platform_11글자_이상() {
//        User user = new User("hwamok@test.com", "1234", "hwamok", "2023-11-15", "01012345678", "GOOGLEGOOGLE",
//                "ACTIVATED", "originalImage", "savedImage",
//                12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201");

        assertThatIllegalArgumentException()
                .isThrownBy(()->new User("hwamok@test.com", "1234", "hwamok", "2023-11-15", "01012345678", "GOOGLEGOOGLE",
                        "ACTIVATED", "originalImage", "savedImage",
                        12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201"));
    }

    @Test
    void 회원_가입_실패_status_10글자_이상() {
//        User user = new User("hwamok@test.com", "1234", "hwamok", "2023-11-15", "01012345678", "GOOGLE",
//                "ACTIVATEDACTIVATED", "originalImage", "savedImage",
//                12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201");

        assertThatIllegalArgumentException()
                .isThrownBy(()->new User("hwamok@test.com", "1234", "hwamok", "2023-11-15", "01012345678", "GOOGLE",
                        "ACTIVATEDACTIVATED", "originalImage", "savedImage",
                        12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_email_null_혹은_빈값(String email) {
        User user = UserFixture.create();

//        user.update(email, "12345", "hwamokhwa", "2023-11-16", "01012345679", "NAVER",
//                "INACTIVATED", "originalImage1", "savedImage1",
//                12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202");

        assertThatIllegalArgumentException()
                .isThrownBy( () -> user.update(email, "12345", "hwamokhwa", "2023-11-16", "01012345679", "NAVER",
                        "INACTIVATED", "originalImage1", "savedImage1",
                        12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_password_null_혹은_빈값(String password) {
        User user = UserFixture.create();

//        user.update("hwamok@test.com", password, "hwamokhwa", "2023-11-16", "01012345679", "NAVER",
//                "INACTIVATED", "originalImage1", "savedImage1",
//                12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202");

        assertThatIllegalArgumentException()
                .isThrownBy( () -> user.update("hwamok@test.com", password, "hwamokhwa", "2023-11-16", "01012345679", "NAVER",
                        "INACTIVATED", "originalImage1", "savedImage1",
                        12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_name_null_혹은_빈값(String name) {
        User user = UserFixture.create();

//        user.update("hwamok@test.com", "1234", name, "2023-11-16", "01012345679", "NAVER",
//                "INACTIVATED", "originalImage1", "savedImage1",
//                12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202");

        assertThatIllegalArgumentException()
                .isThrownBy( () -> user.update("hwamok@test.com", "1234", name, "2023-11-16", "01012345679", "NAVER",
                        "INACTIVATED", "originalImage1", "savedImage1",
                        12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_birthDay_null_혹은_빈값(String birthDay) {
        User user = UserFixture.create();

//        user.update("hwamok@test.com", "1234", "hwamokhwa", birthDay, "01012345679", "NAVER",
//                "INACTIVATED", "originalImage1", "savedImage1",
//                12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202");

        assertThatIllegalArgumentException()
                .isThrownBy( () -> user.update("hwamok@test.com", "1234", "hwamokhwa", birthDay, "01012345679", "NAVER",
                        "INACTIVATED", "originalImage1", "savedImage1",
                        12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_phone_null_혹은_빈값(String phone) {
        User user = UserFixture.create();

//        user.update("hwamok@test.com", "1234", "hwamokhwa", "2023-11-16", phone, "NAVER",
//                "INACTIVATED", "originalImage1", "savedImage1",
//                12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202");

        assertThatIllegalArgumentException()
                .isThrownBy( () -> user.update("hwamok@test.com", "1234", "hwamokhwa", "2023-11-16", phone, "NAVER",
                        "INACTIVATED", "originalImage1", "savedImage1",
                        12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_platform_null_혹은_빈값(String platform) {
        User user = UserFixture.create();

//        user.update("hwamok@test.com", "1234", "hwamokhwa", "2023-11-16", "01012345679", platform,
//                "INACTIVATED", "originalImage1", "savedImage1",
//                12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202");

        assertThatIllegalArgumentException()
                .isThrownBy( () -> user.update("hwamok@test.com", "1234", "hwamokhwa", "2023-11-16", "01012345679", platform,
                        "INACTIVATED", "originalImage1", "savedImage1",
                        12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_status_null_혹은_빈값(String status) {
        User user = UserFixture.create();

//        user.update("hwamok@test.com", "1234", "hwamokhwa", "2023-11-16", "01012345679", "NAVER",
//                status, "originalImage1", "savedImage1",
//                12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202");

        assertThatIllegalArgumentException()
                .isThrownBy( () -> user.update("hwamok@test.com", "1234", "hwamokhwa", "2023-11-16", "01012345679", "NAVER",
                        status, "originalImage1", "savedImage1",
                        12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_addr_null_혹은_빈값(String addr) {
        User user = UserFixture.create();

//        user.update("hwamok@test.com", "1234", "hwamokhwa", "2023-11-16", "01012345679", "NAVER",
//                "INACTIVATED", "originalImage", "savedImage1",
//                12346, addr, "202");

        assertThatIllegalArgumentException()
                .isThrownBy( () -> user.update("hwamok@test.com", "1234", "hwamokhwa", "2023-11-16", "01012345679", "NAVER",
                        "INACTIVATED", "originalImage", "savedImage1",
                        12346, addr, "202"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_detailAddr_null_혹은_빈값(String detailAddr) {
        User user = UserFixture.create();

//        user.update("hwamok@test.com", "1234", "hwamokhwa", "2023-11-16", "01012345679", "NAVER",
//                "INACTIVATED", "originalImage", "savedImage1",
//                12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", detailAddr);

        assertThatIllegalArgumentException()
                .isThrownBy( () -> user.update("hwamok@test.com", "1234", "hwamokhwa", "2023-11-16", "01012345679", "NAVER",
                        "INACTIVATED", "originalImage", "savedImage1",
                        12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", detailAddr));
    }

    @Test
    void 회원_수정_실패_email_50글자_이상() {
        User user = UserFixture.create();

//        user.update("hwamokhwamokhwamokhwamokhwamokhwamokhwamok@test.com", "1234", "hwamokhwa", "2023-11-16", "01012345679", "NAVER",
//                "INACTIVATED", "originalImage", "savedImage1",
//                12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202");

        assertThatIllegalArgumentException()
                .isThrownBy( () -> user.update("hwamokhwamokhwamokhwamokhwamokhwamokhwamok@test.com", "1234", "hwamokhwa", "2023-11-16", "01012345679", "NAVER",
                        "INACTIVATED", "originalImage", "savedImage1",
                        12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202"));
    }

    @Test
    void 회원_수정_실패_name_20글자_이상() {
        User user = UserFixture.create();

//        user.update("hwamokhwamokhwamokhwamokhwamokhwamokhwamok@test.com", "1234", "hwamokhwamokhwamokhwamok", "2023-11-16", "01012345679", "NAVER",
//                "INACTIVATED", "originalImage", "savedImage1",
//                12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202");

        assertThatIllegalArgumentException()
                .isThrownBy( () -> user.update("hwamok@test.com", "1234", "hwamokhwamokhwamokhwamok", "2023-11-16", "01012345679", "NAVER",
                        "INACTIVATED", "originalImage", "savedImage1",
                        12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202"));
    }

    @Test
    void 회원_수정_실패_birthDay_10글자_이상() {
        User user = UserFixture.create();

//        user.update("hwamok@test.com", "1234", "hwamok", "2023-11-167", "01012345679", "NAVER",
//                "INACTIVATED", "originalImage", "savedImage1",
//                12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202");

        assertThatIllegalArgumentException()
                .isThrownBy( () -> user.update("hwamok@test.com", "1234", "hwamok", "2023-11-167", "01012345679", "NAVER",
                        "INACTIVATED", "originalImage", "savedImage1",
                        12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202"));
    }

    @Test
    void 회원_수정_실패_phone_11글자_이상() {
        User user = UserFixture.create();

//        user.update("hwamok@test.com", "1234", "hwamok", "2023-11-16", "010123456789", "NAVER",
//                "INACTIVATED", "originalImage", "savedImage1",
//                12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202");

        assertThatIllegalArgumentException()
                .isThrownBy( () -> user.update("hwamok@test.com", "1234", "hwamok", "2023-11-16", "010123456789", "NAVER",
                        "INACTIVATED", "originalImage", "savedImage1",
                        12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202"));
    }

    @Test
    void 회원_수정_실패_platform_11글자_이상() {
        User user = UserFixture.create();

//        user.update("hwamok@test.com", "1234", "hwamok", "2023-11-16", "01012345679", "NAVERNAVERNAVER",
//                "INACTIVATED", "originalImage", "savedImage1",
//                12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202");

        assertThatIllegalArgumentException()
                .isThrownBy( () -> user.update("hwamok@test.com", "1234", "hwamok", "2023-11-16", "01012345679", "NAVERNAVERNAVER",
                        "INACTIVATED", "originalImage", "savedImage1",
                        12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202"));
    }

    @Test
    void 회원_수정_실패_status_11글자_이상() {
        User user = UserFixture.create();

//        user.update("hwamok@test.com", "1234", "hwamok", "2023-11-16", "01012345679", "NAVER",
//                "INACTIVATEDA", "originalImage", "savedImage1",
//                12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202");

        assertThatIllegalArgumentException()
                .isThrownBy( () -> user.update("hwamok@test.com", "1234", "hwamok", "2023-11-16", "01012345679", "NAVERNAVERNAVER",
                        "INACTIVATEDA", "originalImage", "savedImage1",
                        12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202"));
    }
}