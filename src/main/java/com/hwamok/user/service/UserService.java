package com.hwamok.user.service;

import com.hwamok.api.dto.user.AddressCreateDto;
import com.hwamok.api.dto.user.AddressUpdateDto;
import com.hwamok.user.domain.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    User create(String email, String password, String name, String birthDay, String phone, String platform,
                AddressCreateDto.Request address, MultipartFile profilePicture);

    User getInfo(long id);

    User update(long id, String password, String name, String birthDay, String phone, String platform,
                AddressUpdateDto.Request address, MultipartFile profilePicture);

    void withdraw(long id);
}
