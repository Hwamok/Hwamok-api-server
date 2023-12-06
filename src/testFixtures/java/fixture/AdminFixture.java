package fixture;

import com.hwamok.admin.domain.Admin;
import com.hwamok.utils.Role;

import java.util.List;

public class AdminFixture {
    public static Admin createAdmin(){
        return new Admin("test123", "1234", "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN));
    }
}
