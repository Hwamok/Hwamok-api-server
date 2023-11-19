package com.hwamok.user.domain;

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
        throw new IllegalArgumentException("UNKNOWN PLATFORM: " + platform);
    }
  }
}
