package com.hwamok.user.service;

import com.hwamok.api.dto.AddressCreateDto;
import com.hwamok.api.dto.UploadedFileCreateDto;
import com.hwamok.user.domain.Address;
import com.hwamok.user.domain.UploadedFile;
import com.hwamok.user.domain.User;

public interface UserService {
    User create(String email, String password, String name, String birthDay, String phone, String platform, String status, String originalFileName, String savedFileName, int post, String addr, String detailAddr);

    User getUser(long id);

    User updateProfile(long id, String email, String password, String name, String birthDay, String phone, String platform, String status, String originalFileName, String savedFileName, int post, String addr, String detailAddr);

    void withdraw(long id);


}
