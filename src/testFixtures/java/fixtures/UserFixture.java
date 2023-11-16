package fixtures;

import com.hwamok.user.domain.User;
public class UserFixture {
    public static User create() {
        return new User("hwamok@test.com", "1234", "hwamok", "2023-11-15", "01012345678", "GOOGLE","ACTIVATED", "originalImage", "savedImage",12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201");
    }

    public static User create(String email, String password, String name, String birthDay, String phone, String platform, String status, String originalFileName, String savedFileName, int post, String addr, String detailAddr) {
        return new User(email, password, name, birthDay, phone, platform, status, originalFileName, savedFileName, post, addr, detailAddr);
    }

    public static User create(String email, String password) {
        return new User(email, password, "hwamok", "2023-11-15", "01012345678", "GOOGLE","ACTIVATED", "originalImage", "savedImage",12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201");
    }

}
