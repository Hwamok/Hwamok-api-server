package com.hwamok.admin.service;

import com.hwamok.admin.domain.Admin;
import com.hwamok.admin.domain.AdminRepository;
import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.core.exception.HwamokException;
import com.hwamok.utils.PreConditions;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService{
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Admin create(String loginId, String password, String name, String email) {
        return adminRepository.save(new Admin(loginId, passwordEncoder.encode(password), name, email));
    }

    @Override
    public Admin getInfo(Long id) {
        return adminRepository.findById(id).orElseThrow(()-> new HwamokException(ExceptionCode.NOT_FOUND_ADMIN));
    }

    @Override
    public List<Admin> getInfos() {
        List<Admin> adminList = adminRepository.findAll();

        PreConditions.validate(adminList.size() != 0, ExceptionCode.NOT_FOUND_ADMIN);

        return adminRepository.findAll();
    }

    @Override
    public Admin update(Long id, String password, String name, String email) {
        Admin admin = adminRepository.findById(id).orElseThrow(()-> new HwamokException(ExceptionCode.NOT_FOUND_ADMIN));

        admin.update(passwordEncoder.encode(password),name,email);

        return admin;
    }

    @Override
    public Admin delete(Long id) {
        Admin admin = adminRepository.findById(id).orElseThrow(()-> new HwamokException(ExceptionCode.NOT_FOUND_ADMIN));

        admin.delete();

        return admin;
    }
}
