package fixture;

import com.hwamok.admin.domain.Admin;
import com.hwamok.notice.domain.Notice;

public class AdminFixture {


    public static Admin createAdmin(){
        return new Admin("test123", "1234", "이름", "test@test.com");
    }

}
