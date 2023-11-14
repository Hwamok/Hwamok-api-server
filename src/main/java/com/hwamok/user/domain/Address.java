package com.hwamok.user.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class Address {
  private int post;
  private String addr;
  private String detailAddr;
}
