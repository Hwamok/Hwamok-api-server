package com.hwamok.user.service;

import com.hwamok.api.dto.user.AddressCreateDto;
import com.hwamok.api.dto.user.AddressUpdateDto;
import com.hwamok.api.dto.user.UploadedFileCreateDto;
import com.hwamok.api.dto.user.UploadedFileUpdateDto;
import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.user.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void 회원_가입_성공() {
        User user = userService.create("hwamok@test.com", "1234", "hwamok",
                "2023-11-15", "01012345678", "GOOGLE",
                new UploadedFileCreateDto.Request("originalImage", "savedImage"),
                new AddressCreateDto.Request(12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea",
                "201"));

        assertThat(user.getId()).isNotNull();
    }

    @Test
    void 회원_단건_정보_조회() {
        User user = userRepository.save(new User("hwamok@test.com", passwordEncoder.encode("1234"),
                "hwamok","2023-11-15", "01012345678","GOOGLE",
                new UploadedFile("originalImage", "savedImage"),
                new Address(12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201")));

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
                "hwamok","2023-11-15", "01012345678","GOOGLE",
                new UploadedFile("originalImage", "savedImage"),
                new Address(12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201")));

        User updateInfo = userService.update(user.getId(), "12345", "hwamokhwa","2023-11-16",
                "01012345679", "NAVER", new UploadedFileUpdateDto.Request("originalImage1",
                "savedImage1"), new AddressUpdateDto.Request(12346, 
                        "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea","202"));

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
                        new UploadedFileUpdateDto.Request("originalImage1","savedImage1"),
                        new AddressUpdateDto.Request(12346, "17, Deoksugung-gil1, Jung-gu1, Seoul, Republic of Korea",
                        "202")));
    }

    @Test
    void 회원_탈퇴_성공() {
        User user = userRepository.save(new User("hwamok@test.com", passwordEncoder.encode("1234"),
                "hwamok","2023-11-15", "01012345678","GOOGLE",
                new UploadedFile("originalImage", "savedImage"),
                new Address(12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201")));

        userService.withdraw(user.getId());

        assertThat(user.getStatus()).isEqualTo(UserStatus.INACTIVATED);
    }

    @Test
    void 회원_탈퇴_실패_존재하지_않는_회원() {
        assertThatHwamokException(ExceptionCode.NOT_FOUND_USER)
                .isThrownBy(() -> userService.withdraw(-1L));
    }
}