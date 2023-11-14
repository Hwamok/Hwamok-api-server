package com.hwamok.user.domain;

import com.hwamok.support.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
  @Column(length = 50, nullable = false)
  private String email;

  private String password;

  @Column(length = 20, nullable = false)
  private String name;

  @Column(length = 10, nullable = false)
  private String birthDay;

  @Column(length = 11, nullable = false)
  private String phone;

  @Column(length = 10, nullable = false)
  @Enumerated(EnumType.STRING)
  private JoinPlatform platform;

  @Column(length = 10, nullable = false)
  @Enumerated(EnumType.STRING)
  private UserStatus status;
  @Embedded
  private UploadedFile profile;
  @Embedded
  private Address address;
}
