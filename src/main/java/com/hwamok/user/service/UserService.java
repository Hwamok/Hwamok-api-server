package com.hwamok.user.service;

import com.hwamok.api.dto.user.AddressCreateDto;
import com.hwamok.api.dto.user.AddressUpdateDto;
import com.hwamok.api.dto.user.UploadedFileCreateDto;
import com.hwamok.api.dto.user.UploadedFileUpdateDto;
import com.hwamok.user.domain.User;

public interface UserService {
    User create(String email, String password, String name, String birthDay, String phone, String platform,
                UploadedFileCreateDto.Request profile, AddressCreateDto.Request address);

    User getInfo(long id);

    User update(long id, String email, String password, String name, String birthDay, String phone, String platform,
                UploadedFileUpdateDto.Request profile, AddressUpdateDto.Request address);

    void withdraw(long id);
}
