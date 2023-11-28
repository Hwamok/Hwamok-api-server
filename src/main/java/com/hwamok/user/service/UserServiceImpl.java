package com.hwamok.user.service;

import com.hwamok.api.dto.user.AddressCreateDto;
import com.hwamok.api.dto.user.AddressUpdateDto;
import com.hwamok.api.dto.user.UploadedFileCreateDto;
import com.hwamok.api.dto.user.UploadedFileUpdateDto;
import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.core.exception.HwamokException;
import com.hwamok.user.domain.Address;
import com.hwamok.user.domain.UploadedFile;
import com.hwamok.user.domain.User;
import com.hwamok.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hwamok.utils.PreConditions.require;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User create(String email, String password, String name, String birthDay, String phone, String platform,
                       UploadedFileCreateDto.Request reqProfile, AddressCreateDto.Request reqAddress) {

        UploadedFile profile = new UploadedFile(reqProfile.getOriginalFileName(), reqProfile.getSavedFileName());
        Address address = new Address(reqAddress.getPost(), reqAddress.getAddr(), reqAddress.getDetailAddr());

        require(Strings.isNotBlank(password));

        return userRepository.save(new User(email, passwordEncoder.encode(password), name, birthDay, phone, platform,
                profile, address));
    }

    @Override
    public User getInfo(long id) {
        return userRepository.findById(id).orElseThrow(() -> new HwamokException(ExceptionCode.NOT_FOUND_USER));
    }

    @Override
    public User update(long id, String password, String name, String birthDay, String phone,
                       String platform, UploadedFileUpdateDto.Request reqProfile, AddressUpdateDto.Request reqAddress) {
        User user = userRepository.findById(id).orElseThrow(() -> new HwamokException(ExceptionCode.NOT_FOUND_USER));

        UploadedFile profile = new UploadedFile(reqProfile.getOriginalFileName(), reqProfile.getSavedFileName());
        Address address = new Address(reqAddress.getPost(), reqAddress.getAddr(), reqAddress.getDetailAddr());

        require(Strings.isNotBlank(password));

        user.update(passwordEncoder.encode(password), name, birthDay, phone, platform, profile, address);

        return user;
    }

    @Override
    public void withdraw(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new HwamokException(ExceptionCode.NOT_FOUND_USER));
        user.delete();
    }
}
