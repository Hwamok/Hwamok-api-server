package fixture;

import com.hwamok.user.domain.Address;
import com.hwamok.user.domain.UploadedFile;
import com.hwamok.user.domain.User;
public class UserFixture {
    public static User create() {
        return new User("hwamok@test.com", "1234", "hwamok","2023-11-15", "01012345678",
                "GOOGLE",new UploadedFile("originalImage", "savedImage"),
                new Address(12345, "15, Deoksugung-gil, Jung-gu, Seoul, Republic of Korea", "201"));
    }
}
