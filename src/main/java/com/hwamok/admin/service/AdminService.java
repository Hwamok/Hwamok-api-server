package com.hwamok.admin.service;

import com.hwamok.admin.domain.Admin;
import com.hwamok.api.dto.admin.AdminReadDto;
import com.hwamok.utils.Role;

import java.util.List;

public interface AdminService {
     Admin create(String loginId, String password, String name, String email, List<Role> roles);

     AdminReadDto.Response getInfo(Long id);

     List<AdminReadDto.Response> getInfos();

     Admin update(Long id, String password, String name, String email);

     Admin delete(Long id);
}
