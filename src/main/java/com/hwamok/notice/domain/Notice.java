package com.hwamok.notice.domain;

import com.hwamok.admin.domain.Admin;
import com.hwamok.support.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseEntity {
  private String title;
  private String content;

  @Enumerated(EnumType.STRING)
  private NoticeStatus status = NoticeStatus.CREATED;

  @ManyToOne(fetch = FetchType.LAZY)
  private Admin createdBy;


}
