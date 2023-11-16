package com.hwamok.user.domain;

import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.support.BaseEntity;
import com.hwamok.util.RegexType;
import com.hwamok.util.RegexUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import static com.hwamok.core.Preconditions.*;

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

  @Column(length = 11, nullable = false)
  @Enumerated(EnumType.STRING)
  private UserStatus status;

  @Embedded
  private UploadedFile profile;

  @Embedded
  private Address address;

  public User(String email, String password, String name, String birthDay, String phone, String platform, String status,
              String originalFileName, String savedFileName, int post, String addr, String detailAddr) {
    require(Strings.isNotBlank(email));
    require(Strings.isNotBlank(password));
    require(Strings.isNotBlank(name));
    require(Strings.isNotBlank(birthDay));
    require(Strings.isNotBlank(phone));
    require(Strings.isNotBlank(platform));
    require(Strings.isNotBlank(status));
    require(email.length() <= 50);
    require(name.length() <= 20);
    require(birthDay.length() <= 10);
    require(phone.length() <= 11);
    require(platform.length() <= 11);
    require(status.length() <= 11);

    validate(RegexUtil.matches(email, RegexType.EMAIL),
            ExceptionCode.ERROR_SYSTEM);

    validate(RegexUtil.matches(name, RegexType.NAME),
            ExceptionCode.ERROR_SYSTEM);


    this.email = email;
    this.password = password;
    this.name = name;
    this.birthDay = birthDay;
    this.phone = phone;
    this.platform = JoinPlatform.converter(platform);
    this.status = UserStatus.converter(status);
    this.profile = new UploadedFile(originalFileName, savedFileName);
    this.address = new Address(post, addr, detailAddr);
  }

  public void update(String email, String password, String name, String birthDay,
                     String phone, String platform, String status,
                     String originalFileName, String savedFileName, int post, String addr, String detailAddr) {

    require(Strings.isNotBlank(email));
    require(Strings.isNotBlank(password));
    require(Strings.isNotBlank(name));
    require(Strings.isNotBlank(birthDay));
    require(Strings.isNotBlank(phone));
    require(Strings.isNotBlank(platform));
    require(Strings.isNotBlank(status));
    require(email.length() <= 50);
    require(name.length() <= 20);
    require(birthDay.length() <= 10);
    require(phone.length() <= 11);
    require(platform.length() <= 11);
    require(status.length() <= 11);

    validate(
            RegexUtil.matches(email, RegexType.EMAIL),
            ExceptionCode.ERROR_SYSTEM);

    validate(
            RegexUtil.matches(name, RegexType.NAME),
            ExceptionCode.ERROR_SYSTEM);

    this.email=email;
    this.password=password;
    this.name=name;
    this.birthDay=birthDay;
    this.phone=phone;
    this.platform = JoinPlatform.converter(platform);
    this.status=UserStatus.converter(status);
    this.profile = new UploadedFile(originalFileName, savedFileName);
    this.address = new Address(post, addr, detailAddr);
  }

  public void withdraw() {
    this.status = UserStatus.INACTIVATED;
  }
}
