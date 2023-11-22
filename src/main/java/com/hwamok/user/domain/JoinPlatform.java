package com.hwamok.user.domain;

import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.core.exception.HwamokException;

public enum JoinPlatform {
  SELF,
  KAKAO,
  NAVER,
  GOOGLE;

  public static JoinPlatform converter(String platform) {
    switch (platform.toUpperCase()) {
      case "SELF":
        return SELF;

      case "KAKAO":
        return KAKAO;

      case "NAVER":
        return NAVER;

      case "GOOGLE":
        return GOOGLE;

      default:
        throw new HwamokException(ExceptionCode.NOT_KNOWN_PLATFORM);
    }
  }
}
