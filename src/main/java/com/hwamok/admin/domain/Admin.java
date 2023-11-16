package com.hwamok.admin.domain;

import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.core.exception.HwamokException;
import com.hwamok.notice.domain.Notice;
import com.hwamok.support.BaseEntity;
import com.hwamok.utils.PreConditions;
import com.hwamok.utils.RegexType;
import com.hwamok.utils.RegexUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.List;

import static com.hwamok.core.exception.ExceptionCode.*;
import static com.hwamok.utils.PreConditions.*;
import static com.hwamok.utils.RegexUtil.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends BaseEntity {
  @Column(length = 12, nullable = false)
  private String loginId;

  @Column(nullable = false)
  private String password;

  @Column(length = 20, nullable = false)
  private String name;

  @Column(length = 50, nullable = false)
  private String email;

  @Column(length = 20, nullable = false)
  @Enumerated(EnumType.STRING)
  private AdminStatus status = AdminStatus.ACTIVATED;

  public Admin(String loginId, String password, String name, String email) {
    require(Strings.isNotBlank(loginId));
    require(Strings.isNotBlank(password));
    require(Strings.isNotBlank(name));
    require(Strings.isNotBlank(email));

    validate(matches(loginId, RegexType.LOGINID), NOT_LOGINID_FORM);
    validate(matches(name, RegexType.NAME), NOT_NAME_FORM);
    validate(matches(email, RegexType.EMAIL), NOT_EMAIL_FORM);

    this.loginId = loginId;
    this.password = password;
    this.name = name;
    this.email = email;
  }

  public void update(String password, String name, String email){
    require(Strings.isNotBlank(password));
    require(Strings.isNotBlank(name));
    require(Strings.isNotBlank(email));

    validate(matches(name, RegexType.NAME), NOT_NAME_FORM);
    validate(matches(email, RegexType.EMAIL), NOT_EMAIL_FORM);

    this.password = password;
    this.name = name;
    this.email = email;
  }

  public void delete(){
    this.status = AdminStatus.INACTIVATED;
  }
}
