package com.hwamok.user.domain;

import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.utils.RegexType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import static com.hwamok.utils.PreConditions.require;
import static com.hwamok.utils.PreConditions.validate;
import static com.hwamok.utils.RegexUtil.matches;

@Getter
@Embeddable
@NoArgsConstructor
public class Address {

  @Column(length = 5, nullable = false)
  private int post;
  private String addr;
  private String detailAddr;

  public Address(int post, String addr, String detailAddr) {
    require(Strings.isNotBlank(addr));
    require(Strings.isNotBlank(detailAddr));
    require(post > 0);

    validate(matches(String.valueOf(post), RegexType.POST), ExceptionCode.NOT_POST_FORM);

    this.post = post;
    this.addr = addr;
    this.detailAddr = detailAddr;
  }
}
