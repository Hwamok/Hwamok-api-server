package com.hwamok.user.service;

import com.hwamok.user.domain.Address;
import com.hwamok.user.domain.UploadedFile;
import com.hwamok.user.domain.User;
import com.hwamok.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Override
    public User create(String email, String password, String name, String birthDay, String phone, String platform, String status, String originalFileName, String savedFileName, int post, String addr, String detailAddr) {
        User user = userRepository.save(new User(email, password, name, birthDay, phone, platform, status, originalFileName, savedFileName, post, addr, detailAddr));
        return user;
    }

    @Override
    public User getUser(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("NOT_FOUND_USER"));
        return user;
    }

    @Override
    public User updateProfile(long id, String email, String password, String name, String birthDay, String phone, String platform, String status, String originalFileName, String savedFileName, int post, String addr, String detailAddr) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("NOT_FOUND_USER"));

        user.update(email, password, name, birthDay, phone, platform, status, originalFileName, savedFileName, post, addr, detailAddr);
        return user;
    }

    @Override
    public void withdraw(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("NOT_FOUND_USER"));
        user.withdraw();
        userRepository.save(user);
    }
}
