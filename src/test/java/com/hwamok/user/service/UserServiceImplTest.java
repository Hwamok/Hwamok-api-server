package com.hwamok.user.service;

import com.hwamok.api.dto.user.AddressCreateDto;
import com.hwamok.api.dto.user.AddressUpdateDto;
import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.user.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    @Autowired
    PasswordEncoder passwordEncoder;

    MockMultipartFile mockFile = null;

    @BeforeEach
    void setUp() throws IOException {
        Path path = Paths.get("imageProfile/winter background.png");
        byte[] fileContent = Files.readAllBytes(path);

        mockFile = new MockMultipartFile(
                "profilePicture",
                "Capture001.png",
                "image/png",
                fileContent
        );
    }

    @Test
    void 회원_가입_성공() {
        User user = userService.create("hwamok@test.com", passwordEncoder.encode("1234"), "hwamok",
                "2023-11-15", "01012345678", "GOOGLE",
                new AddressCreateDto.Request(12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                "201"),mockFile);

        assertThat(user.getId()).isNotNull();
    }

    @Test
    void 회원_단건_정보_조회_성공() {
        User user = userRepository.save(new User("hwamok@test.com", passwordEncoder.encode("1234"),
                "hwamok","2023-11-15", "01012345678", "GOOGLE",
                new UploadedFile("originalImage", "savedImage"),
                new Address(12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201")));

        User foundId = userService.getInfo(user.getId());

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
        assertThatHwamokException(ExceptionCode.NOT_FOUND_USER).isThrownBy(() -> userService.getInfo(-1L));
    }

    @Test
    void 회원_수정_성공() {
        User user = userRepository.save(new User("hwamok@test.com", passwordEncoder.encode("1234"),
                "hwamok","2023-11-15", "01012345678", "GOOGLE",
                new UploadedFile("originalImage", "savedImage"),
                new Address(12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201")));

        User updateInfo = userService.update(user.getId(), "12345", "hwamokhwa","2023-11-16",
                "01012345679", "NAVER",new AddressUpdateDto.Request(12346,"17, " +
                        "Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea","202"), mockFile);

        assertThat(updateInfo.getId()).isNotNull();
        assertThat(updateInfo.getEmail()).isEqualTo(user.getEmail());
        assertThat(updateInfo.getPassword()).isEqualTo(user.getPassword());
        assertThat(updateInfo.getName()).isEqualTo(user.getName());
        assertThat(updateInfo.getBirthDay()).isEqualTo(user.getBirthDay());
        assertThat(updateInfo.getPhone()).isEqualTo(user.getPhone());
        assertThat(updateInfo.getPlatform()).isEqualTo(user.getPlatform());
        assertThat(updateInfo.getProfile().getOriginalFileName()).isEqualTo(user.getProfile().getOriginalFileName());
        assertThat(updateInfo.getProfile().getSavedFileName()).isEqualTo(user.getProfile().getSavedFileName());
        assertThat(updateInfo.getAddress().getPost()).isEqualTo(user.getAddress().getPost());
        assertThat(updateInfo.getAddress().getAddr()).isEqualTo(user.getAddress().getAddr());
        assertThat(updateInfo.getAddress().getDetailAddr()).isEqualTo(user.getAddress().getDetailAddr());
    }

    @Test
    void 회원_수정_실패_존재하지_않는_회원() {
        assertThatHwamokException(ExceptionCode.NOT_FOUND_USER)
                .isThrownBy(() -> userService.update(-1L, "1234", "hwamokhwa",
                        "2023-11-16", "01012345679", "NAVER",
                        new AddressUpdateDto.Request(12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea",
                        "202"), mockFile));
    }

    @Test
    void 회원_탈퇴_성공() {
        User user = userRepository.save(userRepository.save(new User("hwamok@test.com", passwordEncoder.encode("1234"),
                "hwamok","2023-11-15", "01012345678", "GOOGLE",
                new UploadedFile("originalImage", "savedImage"),
                new Address(12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"))));

        userService.withdraw(user.getId());

        assertThat(user.getStatus()).isEqualTo(UserStatus.INACTIVATED);
    }

    @Test
    void 회원_탈퇴_실패_존재하지_않는_회원() {
        assertThatHwamokException(ExceptionCode.NOT_FOUND_USER)
                .isThrownBy(() -> userService.withdraw(-1L));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_가입_실패_password_null_혹은_빈값(String password) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> userService.create("hwamok@test.com", password,
                        "hwamok","2023-11-15", "01012345678", "GOOGLE",
                new AddressCreateDto.Request(12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201"), mockFile));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_수정_실패_password_null_혹은_빈값(String password) {
        User user = userRepository.save(new User("hwamok@test.com", passwordEncoder.encode("1234"),
                "hwamok","2023-11-15", "01012345678", "GOOGLE",
                new UploadedFile("originalImage", "savedImage"),
                new Address(12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                        "201")));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> userService.update(user.getId(),
                password, "hwamokhwa","2023-11-16","01012345679", "NAVER",
                new AddressUpdateDto.Request(12346,"17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea",
                        "202"), mockFile));
    }
}