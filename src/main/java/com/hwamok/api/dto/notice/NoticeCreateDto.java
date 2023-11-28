package com.hwamok.api.dto.notice;

import com.hwamok.admin.domain.Admin;
import com.hwamok.notice.domain.NoticeStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class NoticeCreateDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String title;
        private String content;
    }
}
