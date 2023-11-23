package com.hwamok.notice.service;

import com.hwamok.notice.domain.Notice;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NoticeService {
    Notice create(String title, String content, Long id);

    Notice getNotice(Long id);

    Page<Notice> getNotices(String keyword, String filter, int curPage, int pageSize);

    Notice update(Long id, String title, String content);

    Notice delete(Long id);
}
