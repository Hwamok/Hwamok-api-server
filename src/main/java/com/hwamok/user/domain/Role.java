package com.hwamok.user.domain;

public enum Role {
    USER("일반 회원");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
