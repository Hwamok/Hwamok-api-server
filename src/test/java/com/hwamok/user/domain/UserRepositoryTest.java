package com.hwamok.user.domain;

import com.hwamok.core.exception.HwamokException;
import fixtures.UserFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.hwamok.core.exception.ExceptionCode.*;
import static com.hwamok.core.exception.HwamokExceptionTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void 회원_가입_성공() {

        User user = userRepository.save(UserFixture.create());

        assertThat(user.getId()).isNotNull();
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
    void 회원_단건_조회_성공() {

        User user = userRepository.save(UserFixture.create());

        User foundUserId = userRepository.findById(user.getId()).orElseThrow();

        assertThat(foundUserId.getId()).isEqualTo(user.getId());
    }

    @Test
    void 회원_단건_조회_성공_존재하지_않는_회원() {

        User user = userRepository.save(UserFixture.create());

//         userRepository.findById(-1L).orElseThrow(()->new HwamokException(NOT_FOUND_USER));

        assertThatHwamokException(NOT_FOUND_USER).isThrownBy(()->userRepository.findById(-1L).orElseThrow(()->new HwamokException(NOT_FOUND_USER)));
    }

    @Test
    void 회원_수정_성공() {
        User user = userRepository.save(UserFixture.create());

        userRepository.findById(user.getId()).orElseThrow();
        user.update("hwamok1@test.com", "12345", "hwamokhwa", "2023-11-16", "01012345679", "NAVER",
                "INACTIVATED", "originalImage1", "savedImage1",
                12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea", "202");

        assertThat(user.getId()).isNotNull();
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
        User user = userRepository.save(UserFixture.create());

        userRepository.findById(user.getId()).orElseThrow();
        user.withdraw();

        assertThat(user.getId()).isNotNull();
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
//        User user = userRepository.save(new User(email, "1234", "hwamok", "2023-11-15", "01012345678", "GOOGLE","ACTIVATED", "originalImage", "savedImage",12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201"));

        assertThatIllegalArgumentException().isThrownBy(()->userRepository.save(new User(email, "1234", "hwamok", "2023-11-15", "01012345678", "GOOGLE","ACTIVATED", "originalImage", "savedImage",12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201")));

    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_password_null_혹은_빈값(String password) {
//        User user = userRepository.save(new User("hwamok@test.com", password, "hwamok", "2023-11-15", "01012345678", "GOOGLE","ACTIVATED", "originalImage", "savedImage",12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201"));

        assertThatIllegalArgumentException().isThrownBy(()->userRepository.save(new User("hwamok@test.com", password, "hwamok", "2023-11-15", "01012345678", "GOOGLE","ACTIVATED", "originalImage", "savedImage",12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201")));

    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_name_null_혹은_빈값(String name) {
//        User user = userRepository.save(new User("hwamok@test.com", "1234", name, "2023-11-15", "01012345678", "GOOGLE","ACTIVATED", "originalImage", "savedImage",12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201"));

        assertThatIllegalArgumentException().isThrownBy(()->userRepository.save(new User("hwamok@test.com", "1234", name, "2023-11-15", "01012345678", "GOOGLE","ACTIVATED", "originalImage", "savedImage",12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201")));

    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_birthDay_null_혹은_빈값(String birthDay) {
//        User user = userRepository.save(new User("hwamok@test.com", "1234", "hwamok", birthDay, "01012345678", "GOOGLE","ACTIVATED", "originalImage", "savedImage",12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201"));

        assertThatIllegalArgumentException().isThrownBy(()->userRepository.save(new User("hwamok@test.com", "1234", "hwamok", birthDay, "01012345678", "GOOGLE","ACTIVATED", "originalImage", "savedImage",12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201")));

    }
}

