package com.hwamok.notice.service;

import com.hwamok.admin.domain.Admin;
import com.hwamok.admin.domain.AdminRepository;
import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.core.exception.HwamokException;
import com.hwamok.notice.domain.Notice;
import com.hwamok.notice.domain.NoticeRepository;
import com.hwamok.utils.PreConditions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeServiceImpl implements NoticeService{
    private final NoticeRepository noticeRepository;
    private final AdminRepository adminRepository;

    @Override
    public Notice create(String title, String content, Long id) {
        Admin admin = adminRepository.findById(id).orElseThrow(()-> new HwamokException(ExceptionCode.NOT_FOUND_ADMIN));

        return noticeRepository.save(new Notice(title, content, admin));
    }

    @Override
    public Notice getNotice(Long id) {
        return noticeRepository.findById(id).orElseThrow(()-> new HwamokException(ExceptionCode.NOT_FOUND_NOTICE));
    }

    @Override
    public Page<Notice> getNotices(String keyword, String filter, int curPage, int pageSize) {
        PageRequest pageRequest = PageRequest.of(curPage-1, pageSize);

        Page<Notice> notices = noticeRepository.getNotices(keyword, filter, pageRequest);

        PreConditions.validate(notices.getTotalElements() != 0, ExceptionCode.NOT_FOUND_NOTICE);

        return notices;
    }

    @Override
    public Notice update(Long id, String title, String content) {
        Notice notice = noticeRepository.findById(id).orElseThrow(()-> new HwamokException(ExceptionCode.NOT_FOUND_NOTICE));;

        notice.update(title, content);

        return notice;
    }

    @Override
    public Notice delete(Long id) {
        Notice notice = noticeRepository.findById(id).orElseThrow(()-> new HwamokException(ExceptionCode.NOT_FOUND_NOTICE));;

        notice.delete();

        return notice;
    }
}
