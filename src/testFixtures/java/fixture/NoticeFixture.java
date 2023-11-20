package fixture;

import com.hwamok.admin.domain.Admin;
import com.hwamok.notice.domain.Notice;

public class NoticeFixture {
    public static Notice createNotice(Admin admin){
        return new Notice("제목", "내용", admin);
    }
}