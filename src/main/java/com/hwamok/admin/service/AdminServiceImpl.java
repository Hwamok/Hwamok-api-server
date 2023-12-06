package com.hwamok.admin.service;

import com.hwamok.admin.domain.Admin;
import com.hwamok.admin.domain.AdminRepository;
import com.hwamok.api.dto.admin.AdminReadDto;
import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.core.exception.HwamokException;
import com.hwamok.utils.PreConditions;
import com.hwamok.utils.Role;
import jakarta.annotation.PostConstruct;
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

    @PostConstruct
    public void init() {
        if(!adminRepository.findByLoginId("admin").isPresent()) {
            adminRepository.save(new Admin("admin", passwordEncoder.encode("1"), "관리자", "admin@hwamok.com", List.of(Role.SUPER, Role.ADMIN)));
        }
    }

    @Override
    public Admin create(String loginId, String password, String name, String email, List<Role> roles) {
        return adminRepository.save(new Admin(loginId, passwordEncoder.encode(password), name, email, roles));
    }

    @Override
    public AdminReadDto.Response getInfo(Long id) {
        return adminRepository.findById(id).map(AdminReadDto.Response::new).orElseThrow(()-> new HwamokException(ExceptionCode.NOT_FOUND_ADMIN));
    }

    @Override
    public List<AdminReadDto.Response> getInfos() {
        List<Admin> adminList = adminRepository.findAll();

        PreConditions.validate(adminList.size() != 0, ExceptionCode.NOT_FOUND_ADMIN);

        return adminList.stream().map(AdminReadDto.Response::new).toList();
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
