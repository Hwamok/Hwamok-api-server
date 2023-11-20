package fixture;

import com.hwamok.user.domain.User;
public class UserFixture {
    public static User create() {
        return new User("hwamok@test.com", "1234", "hwamok","2023-11-15", "01012345678",
                "GOOGLE","originalImage", "savedImage",12345,
                "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201");
    }
}
