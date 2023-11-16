package com.hwamok.user.domain;

public enum JoinPlatform {
  // 자사 회원 가입
  SELF("S001", "자사 회원"),
  KAKAO("K001", "카카오 회원"),
  NAVER("N001", "네이버 회원"),
  GOOGLE("Goo1", "구글 회원");

  private String code;

  private String message;

  JoinPlatform(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public static JoinPlatform converter(String platform) {
    switch (platform.toUpperCase()) {
      case "SELF":
        return JoinPlatform.SELF;
      case "KAKAO":
        return JoinPlatform.KAKAO;
      case "NAVER":
        return JoinPlatform.NAVER;
      case "GOOGLE":
        return JoinPlatform.GOOGLE;
      default:
        throw new IllegalArgumentException("UNKNOWN PLATFORM: " + platform);
    }
  }
}
