package com.hwamok.admin.domain;

import com.hwamok.notice.domain.Notice;
import com.hwamok.support.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends BaseEntity {
  private String loginId;
  private String password;
  private String name;
  private String email;
  @Enumerated(EnumType.STRING)
  private AdminStatus status;
}
