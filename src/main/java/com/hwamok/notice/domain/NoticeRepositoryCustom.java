package com.hwamok.notice.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {
    Page<Notice> getNotices(String keyword, String filter, Pageable pageable);
}
