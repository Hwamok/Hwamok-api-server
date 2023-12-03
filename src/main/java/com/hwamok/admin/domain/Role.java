package com.hwamok.admin.domain;

public enum Role {
  ADMIN("어드민"),
  SUPER("슈퍼 어드민");

  private final String name;

  Role(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
