package com.hwamok.user.domain;
public enum UserStatus {
  ACTIVATED("A001", "회원"),
  INACTIVATED("A002", "회원탈퇴");

  private String code;

  private String message;

  UserStatus(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public static UserStatus converter(String status) {
    if(status.equals("ACTIVATED")) {

      return UserStatus.ACTIVATED;
    }

    return UserStatus.INACTIVATED;
  }

}
