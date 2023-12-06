package com.hwamok.security;

import com.hwamok.admin.domain.Admin;
import com.hwamok.admin.domain.AdminRepository;
import com.hwamok.security.userDetails.HwamokAdmin;
import com.hwamok.utils.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HwamokAdminTest implements UserDetailsService {
    @Autowired
    AdminRepository adminRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.save(new Admin("test123", passwordEncoder.encode("1234"), "이름", "test@test.com", List.of(Role.SUPER, Role.ADMIN)));

        return new HwamokAdmin(admin.getId(), "슈퍼 어드민");
    }
}
