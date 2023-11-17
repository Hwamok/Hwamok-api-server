package com.hwamok.notice.domain;

import com.hwamok.admin.domain.Admin;
import com.hwamok.support.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.validator.constraints.Length;

import static com.hwamok.utils.PreConditions.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseEntity {
  @Column(length = 90, nullable = false)
  private String title;

  @Column(columnDefinition = "TEXT", nullable = false)
  @Length(max = 1000)
  private String content;

  @Column(length = 20, nullable = false)
  @Enumerated(EnumType.STRING)
  private NoticeStatus status = NoticeStatus.CREATED;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "admin_id", nullable = false)
  private Admin createdBy;

  public Notice(String title, String content, Admin createdBy) {
    require(Strings.isNotBlank(title));
    require(Strings.isNotBlank(content));
    require(createdBy != null);

    require(title.length() <= 90);
    require(content.length() <= 1000);

    this.title = title;
    this.content = content;
    this.createdBy = createdBy;
  }
}
