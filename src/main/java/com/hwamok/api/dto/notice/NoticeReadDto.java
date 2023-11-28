package com.hwamok.api.dto.notice;

import com.hwamok.admin.domain.Admin;
import com.hwamok.notice.domain.Notice;
import com.hwamok.notice.domain.NoticeStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class NoticeReadDto {
    @Getter
    @NoArgsConstructor
    public static class Response {
        private String title;
        private String content;
        private NoticeStatus status;
        private Admin createdBy;

        public Response(String title, String content, NoticeStatus status, Admin createdBy) {
            this.title = title;
            this.content = content;
            this.status = status;
            this.createdBy = createdBy;
        }

        public Response(Notice notice) {
            this.title = notice.getTitle();
            this.content = notice.getContent();
            this.status = notice.getStatus();
            this.createdBy = notice.getCreatedBy();
        }
    }
}
