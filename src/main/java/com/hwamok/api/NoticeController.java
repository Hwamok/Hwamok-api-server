package com.hwamok.api;

import com.hwamok.admin.domain.Admin;
import com.hwamok.api.dto.notice.NoticeCreateDto;
import com.hwamok.api.dto.notice.NoticeReadDto;
import com.hwamok.api.dto.notice.NoticeUpdateDto;
import com.hwamok.core.response.ApiResult;
import com.hwamok.core.response.Result;
import com.hwamok.notice.domain.Notice;
import com.hwamok.notice.service.NoticeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.hwamok.api.dto.notice.NoticeReadDto.*;

@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    // TODO: 2023-11-20 Jwt 구현 후 @AuthenticationPrincipal을 통해 SecurityContextHolder에 저장된 HwamokAdmin을 받아올 예정
    @PostMapping
    public ResponseEntity<ApiResult<?>> create(@RequestBody NoticeCreateDto.Request request, HttpSession session) {
        Admin admin = (Admin)session.getAttribute("admin");

        noticeService.create(request.getTitle(), request.getContent(), admin.getId());

        return Result.created();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<Response>> getNotice(@PathVariable Long id) {
        Notice notice = noticeService.getNotice(id);

        Response response = new Response(notice.getTitle(), notice.getContent(), notice.getStatus(), notice.getCreatedBy());

        return Result.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResult<Page<NoticeReadDto.Response>>> getNotices(@RequestParam(required = false) String keyword,
                                                              @RequestParam(required = false) String filter,
                                                              @RequestParam(defaultValue = "1") int curPage,
                                                              @RequestParam(defaultValue = "10") int pageSize) {
        Page<Notice> notices = noticeService.getNotices(keyword, filter, curPage, pageSize);

        Page<NoticeReadDto.Response> responses = notices.map(Response::new);

        return Result.ok(responses);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResult<?>> update(@PathVariable Long id, @RequestBody NoticeUpdateDto.Request request) {
        noticeService.update(id, request.getTitle(), request.getContent());

        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<?>> delete(@PathVariable Long id) {
        noticeService.delete(id);

        return Result.ok();
    }
}
