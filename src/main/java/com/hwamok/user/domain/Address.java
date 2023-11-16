package com.hwamok.user.domain;

import com.hwamok.core.Preconditions;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.Internal;
import org.springframework.security.core.parameters.P;

@Getter
@Embeddable
@NoArgsConstructor
public class Address {

  private int post;

  private String addr;

  private String detailAddr;

  public Address(int post, String addr, String detailAddr) {

    Preconditions.require(Strings.isNotBlank(addr));
    Preconditions.require(Strings.isNotBlank(detailAddr));

    this.post = post;
    this.addr = addr;
    this.detailAddr = detailAddr;
  }

  public void updateAddr(int post, String addr, String detailAddr) {

    Preconditions.require(Strings.isNotBlank(addr));
    Preconditions.require(Strings.isNotBlank(detailAddr));

    this.post = post;
    this.addr = addr;
    this.detailAddr = detailAddr;
  }

}
