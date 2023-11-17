package com.hwamok.admin.service;

import com.hwamok.admin.domain.Admin;

import java.util.List;

public interface AdminService {
     Admin create(String loginId, String password, String name, String email);

     Admin getInfo(Long id);

     List<Admin> getInfos();

     Admin update(Long id, String password, String name, String email);

     Admin delete(Long id);
}
