package com.hwamok.user.service;

import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.core.exception.HwamokException;
import com.hwamok.user.domain.User;
import com.hwamok.user.domain.UserRepository;
import com.hwamok.user.domain.UserStatus;
import fixture.UserFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.hwamok.core.exception.HwamokExceptionTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Transactional
class UserServiceImplTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    void 회원_가입_성공() {
        User user = userRepository.save(new User("hwamok@test.com", "1234", "hwamok",
                "2023-11-15", "01012345678", "GOOGLE","originalImage",
                "savedImage",12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                "201"));

        assertThat(user.getId()).isNotNull();
    }

    @Test
    void 회원_단건_정보_조회() {
        User user = userRepository.save(UserFixture.create());

        User foundId = userRepository.findById(user.getId())
                .orElseThrow(()->new HwamokException(ExceptionCode.NOT_FOUND_USER));

        assertThat(foundId.getId()).isNotNull();
        assertThat(foundId.getEmail()).isEqualTo(user.getEmail());
        assertThat(foundId.getPassword()).isEqualTo(user.getPassword());
        assertThat(foundId.getName()).isEqualTo(user.getName());
        assertThat(foundId.getBirthDay()).isEqualTo(user.getBirthDay());
        assertThat(foundId.getPhone()).isEqualTo(user.getPhone());
        assertThat(foundId.getPlatform()).isEqualTo(user.getPlatform());
        assertThat(foundId.getProfile().getOriginalFileName()).isEqualTo(user.getProfile().getOriginalFileName());
        assertThat(foundId.getProfile().getSavedFileName()).isEqualTo(user.getProfile().getSavedFileName());
        assertThat(foundId.getAddress().getPost()).isEqualTo(user.getAddress().getPost());
        assertThat(foundId.getAddress().getAddr()).isEqualTo(user.getAddress().getAddr());
        assertThat(foundId.getAddress().getDetailAddr()).isEqualTo(user.getAddress().getDetailAddr());
    }

    @Test
    void 회원_단건_정보_조회_실패_존재하지_않는_회원() {
        User user = userRepository.save(UserFixture.create());

        assertThatHwamokException(ExceptionCode.NOT_FOUND_USER).isThrownBy(() -> userRepository.findById(-1L)
                .orElseThrow(()-> new HwamokException(ExceptionCode.NOT_FOUND_USER)));
    }

    @Test
    void 회원_수정_성공() {
        User user = userRepository.save(UserFixture.create());

        User foundId = userRepository.findById(user.getId())
                .orElseThrow(()-> new HwamokException(ExceptionCode.NOT_FOUND_USER));

        foundId = userService.update(foundId.getId(), "hwamok1@test.com", "12345", "hwamokhwa",
                "2023-11-16", "01012345679", "NAVER", "originalImage1",
                "savedImage1",12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea",
                "202");

        assertThat(foundId.getId()).isNotNull();
        assertThat(foundId.getEmail()).isEqualTo(user.getEmail());
        assertThat(foundId.getPassword()).isEqualTo(user.getPassword());
        assertThat(foundId.getName()).isEqualTo(user.getName());
        assertThat(foundId.getBirthDay()).isEqualTo(user.getBirthDay());
        assertThat(foundId.getPhone()).isEqualTo(user.getPhone());
        assertThat(foundId.getPlatform()).isEqualTo(user.getPlatform());
        assertThat(foundId.getProfile().getOriginalFileName()).isEqualTo(user.getProfile().getOriginalFileName());
        assertThat(foundId.getProfile().getSavedFileName()).isEqualTo(user.getProfile().getSavedFileName());
        assertThat(foundId.getAddress().getPost()).isEqualTo(user.getAddress().getPost());
        assertThat(foundId.getAddress().getAddr()).isEqualTo(user.getAddress().getAddr());
        assertThat(foundId.getAddress().getDetailAddr()).isEqualTo(user.getAddress().getDetailAddr());
    }

    @Test
    void 회원_탈퇴_성공() {
        User user = userRepository.save(UserFixture.create());

        userRepository.findById(user.getId()).orElseThrow();

        user.delete();

        assertThat(user.getStatus()).isEqualTo(UserStatus.INACTIVATED);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_email_null_혹은_빈값(String email) {
        assertThatIllegalArgumentException()
                .isThrownBy(()->userRepository.save(new User(email, "1234", "hwamok",
                        "2023-11-15", "01012345678", "GOOGLE", "originalImage",
                        "savedImage",12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201")));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_password_null_혹은_빈값(String password) {
        assertThatIllegalArgumentException().
                isThrownBy(()->userRepository.save(new User("hwamok@test.com", password,
                        "hwamok","2023-11-15", "01012345678", "GOOGLE", "originalImage",
                        "savedImage",12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201")));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_name_null_혹은_빈값(String name) {
        assertThatIllegalArgumentException()
                .isThrownBy(()->userRepository.save(new User("hwamok@test.com", "1234",
                        name, "2023-11-15", "01012345678", "GOOGLE", "originalImage",
                        "savedImage",12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201")));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_birthDay_null_혹은_빈값(String birthDay) {
        assertThatIllegalArgumentException()
                .isThrownBy(()->userRepository.save(new User("hwamok@test.com", "1234",
                        "hwamok", birthDay, "01012345678", "GOOGLE", "originalImage",
                        "savedImage",12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201")));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_phone_혹은_빈값(String phone) {
        assertThatIllegalArgumentException()
                .isThrownBy(()->userRepository.save(new User("hwamok@test.com", "1234",
                        "hwamok", "2023-11-15", phone, "GOOGLE", "originalImage",
                        "savedImage",12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201")));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_platform_혹은_빈값(String platform) {
        assertThatIllegalArgumentException()
                .isThrownBy(()->userRepository.save(new User("hwamok@test.com", "1234",
                        "hwamok", "2023-11-15", "01012345678", platform, "originalImage",
                        "savedImage",12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201")));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_addr_혹은_빈값(String addr) {

    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_detailAddr_혹은_빈값(String detailAddr) {

    }

    @Test
    void 회원_가입_실패_email_50글자_초과() {
        String fakeEmail = "hwamokhwamokhwamokhwamokhwamokhwamokhwamokhwamokhwamokhwamokhwamok@test.com";


    }

    @Test
    void 회원_가입_실패_name_20글자_초과() {
        String fakeName = "hwamokhwamokhwamokhwamok";

    }

    @Test
    void 회원_가입_실패_birthDay_10글자_초과() {
        String fakeBirthDay = "2023-11-1512";

    }

    @Test
    void 회원_가입_실패_phone_11글자_초과() {
        String fakePhone = "010123456789";

    }

    @Test
    void 회원_가입_실패_platform_11글자_초과() {
        String fakePlatform = "GOOGLEGOOGLE";

    }

    @Test
    void 회원_가입_실패_validate_name_특수문자() {
        String fakeName = "화목!";

    }

    @Test
    void 회원_가입_실패_validate_name_두_글자_미만() {
        String fakeName = "화";

    }

    @Test
    void 회원_가입_실패_validate_name_영문한글_혼용() {
        String fakeName = "화목hwamok";

    }

    @Test
    void 회원_가입_실패_validate_email_골뱅이_없음() {
        String fakeEmail = "hwamoktest.com";

    }

    @Test
    void 회원_가입_실패_validate_email_점_없음() {
        String fakeEmail = "hwamok@testcom";


    }

    @Test
    void 회원_가입_실패_validate_birthDay_다시_없음() {
        String fakeBirthDay = "20231115";


    }

    @Test
    void 회원_가입_실패_validate_birthDay_슬래시_변경() {
        String fakeBirthDay = "2023/11/15";


    }

    @Test
    void 회원_가입_실패_post_음수일때() {
        int fakePost = -1;

    }

    @Test
    void 회원_가입_실패_validate_post_5자리_아래일때() {
        int fakePost = 1234;


    }

    @Test
    void 회원_가입_실패_validate_phone_숫자_제외_다른_문자() {
        String fakePhone = "010-1234#56";


    }

    @Test
    void 회원_가입_실패_validate_phone_11자리_미만() {
        String fakePhone = "0101234567";


    }

    @Test
    void 회원_가입_실패_validate_phone_첫째자리_0_제외() {
        String fakePhone = "21012345678";


    }

    @Test
    void 회원_가입_실패_validate_phone_두째자리_1_제외() {
        String fakePhone = "00012345678";


    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_email_null_혹은_빈값(String email) {

    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_password_null_혹은_빈값(String password) {

    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_name_null_혹은_빈값(String name) {

    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_birthDay_null_혹은_빈값(String birthDay) {

    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_phone_null_혹은_빈값(String phone) {

    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_platform_null_혹은_빈값(String platform) {

    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_addr_null_혹은_빈값(String addr) {

    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_detailAddr_null_혹은_빈값(String detailAddr) {

    }

    @Test
    void 회원_수정_실패_email_50글자_초과() {

    }

    @Test
    void 회원_수정_실패_name_20글자_초과() {

    }

    @Test
    void 회원_수정_실패_birthDay_10글자_초과() {

    }

    @Test
    void 회원_수정_실패_phone_11글자_초과() {

    }

    @Test
    void 회원_수정_실패_platform_11글자_초과() {

    }

    @Test
    void 회원_수정_실패_validate_name_특수문자() {

    }

    @Test
    void 회원_수정_실패_validate_name_두_글자_미만() {

    }

    @Test
    void 회원_수정_실패_validate_name_영문한글_혼용() {

    }

    @Test
    void 회원_수정_실패_validate_email_골뱅이_없음() {

    }

    @Test
    void 회원_수정_실패_validate_email_점_없음() {

    }

    @Test
    void 회원_수정_실패_validate_birthDay_다시_없음() {

    }

    @Test
    void 회원_수정_실패_validate_email_슬래시_변경() {

    }

    @Test
    void 회원_수정_실패_post_음수일때() {

    }

    @Test
    void 회원_수정_실패_validate_post_5자리_아래일때() {

    }

    @Test
    void 회원_수정_실패_validate_phone_숫자_제외_다른_문자() {

    }

    @Test
    void 회원_수정_실패_validate_phone_11자리_미만() {

    }

    @Test
    void 회원_수정_실패_validate_phone_첫째자리_0_제외() {

    }

    @Test
    void 회원_수정_실패_validate_phone_두째자리_1_제외() {

    }

}