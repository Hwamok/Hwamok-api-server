package com.hwamok.user.domain;

import com.hwamok.utils.PreConditions;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;


@Getter
@Embeddable
@NoArgsConstructor
public class Address {

  private int post;

  private String addr;

  private String detailAddr;

  public Address(int post, String addr, String detailAddr) {
    PreConditions.require(Strings.isNotBlank(addr));
    PreConditions.require(Strings.isNotBlank(detailAddr));

    this.post = post;
    this.addr = addr;
    this.detailAddr = detailAddr;
  }
}
