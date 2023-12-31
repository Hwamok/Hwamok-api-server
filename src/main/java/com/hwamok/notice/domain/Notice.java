package com.hwamok.notice.domain;

import com.hwamok.admin.domain.Admin;
import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.support.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import static com.hwamok.utils.PreConditions.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseEntity {
  @Column(length = 90, nullable = false)
  private String title;

  @Column(columnDefinition = "TEXT", nullable = false)
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

    validate(title.length() <= 90, ExceptionCode.OVER_LENGTH_TITLE);
    validate(content.length() <= 1000, ExceptionCode.OVER_LENGTH_CONTENT);

    this.title = title;
    this.content = content;
    this.createdBy = createdBy;
  }

  public void update(String title, String content){
    require(Strings.isNotBlank(title));
    require(Strings.isNotBlank(content));

    validate(title.length() <= 90, ExceptionCode.OVER_LENGTH_TITLE);
    validate(content.length() <= 1000, ExceptionCode.OVER_LENGTH_CONTENT);

    this.title = title;
    this.content = content;
  }

  public void delete(){
    this.status = NoticeStatus.DELETED;
  }
}
