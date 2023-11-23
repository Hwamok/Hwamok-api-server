package com.hwamok.user.domain;

import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.support.BaseEntity;
import com.hwamok.utils.RegexType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import static com.hwamok.utils.PreConditions.*;
import static com.hwamok.utils.RegexUtil.*;

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
  private UserStatus status = UserStatus.ACTIVATED;

  @Embedded
  private UploadedFile profile;

  @Embedded
  private Address address;

  public User(String email, String password, String name, String birthDay, String phone, String platform,
              UploadedFile profile, Address address) {
    require(Strings.isNotBlank(email));
    require(Strings.isNotBlank(password));
    require(Strings.isNotBlank(name));
    require(Strings.isNotBlank(birthDay));
    require(Strings.isNotBlank(phone));
    require(Strings.isNotBlank(platform));
    require(email.length() <= 50);
    require(name.length() <= 20);
    require(birthDay.length() <= 10);
    require(phone.length() <= 11);
    require(platform.length() <= 11);

    validate(matches(email, RegexType.EMAIL),ExceptionCode.NOT_EMAIL_FORM);
    validate(matches(name, RegexType.NAME),ExceptionCode.NOT_NAME_FORM);
    validate(matches(birthDay, RegexType.BIRTHDAY),ExceptionCode.NOT_DATE_FORM);
    validate(matches(phone, RegexType.PHONE),ExceptionCode.NOT_PHONE_FORM);

    this.email = email;
    this.password = password;
    this.name = name;
    this.birthDay = birthDay;
    this.phone = phone;
    this.platform = JoinPlatform.converter(platform);
    this.profile = profile;
    this.address = address;
  }

  public void update(String password, String name, String birthDay, String phone, String platform,
                     UploadedFile profile, Address address) {

    require(Strings.isNotBlank(password));
    require(Strings.isNotBlank(name));
    require(Strings.isNotBlank(birthDay));
    require(Strings.isNotBlank(phone));
    require(Strings.isNotBlank(platform));
    require(name.length() <= 20);
    require(birthDay.length() <= 10);
    require(phone.length() <= 11);
    require(platform.length() <= 11);

    validate(matches(name, RegexType.NAME),ExceptionCode.NOT_NAME_FORM);
    validate(matches(birthDay, RegexType.BIRTHDAY),ExceptionCode.NOT_DATE_FORM);
    validate(matches(phone, RegexType.PHONE),ExceptionCode.NOT_PHONE_FORM);

    this.password=password;
    this.name=name;
    this.birthDay=birthDay;
    this.phone=phone;
    this.platform = JoinPlatform.converter(platform);
    this.profile = profile;
    this.address = address;
  }

  public void delete() { 
    this.status = UserStatus.INACTIVATED; 
  }
}
