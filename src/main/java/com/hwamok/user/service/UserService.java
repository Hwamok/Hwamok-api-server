package com.hwamok.user.service;

import com.hwamok.user.domain.User;

public interface UserService {
    User create(String email, String password, String name, String birthDay, String phone, String platform, String originalFileName, String savedFileName, int post, String addr, String detailAddr);

    User getInfo(long id);

    User update(long id, String email, String password, String name, String birthDay, String phone, String platform, String originalFileName, String savedFileName, int post, String addr, String detailAddr);

    void withdraw(long id);
}
