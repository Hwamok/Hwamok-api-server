package com.hwamok.api;

import com.hwamok.admin.domain.Admin;
import com.hwamok.admin.service.AdminService;
import com.hwamok.api.dto.admin.AdminCreateDto;
import com.hwamok.api.dto.admin.AdminUpdateDto;
import com.hwamok.core.response.ApiResult;
import com.hwamok.core.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping
    public ResponseEntity<ApiResult<?>> create(@RequestBody AdminCreateDto.Request request){

        adminService.create(request.getLoginId(),request.getPassword(),request.getName(),request.getEmail());

        return Result.created();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<Admin>> getInfo(@PathVariable Long id){

        Admin admin = adminService.getInfo(id);

        return Result.ok(admin);
    }
    @GetMapping("/list")
    public ResponseEntity<ApiResult<List<Admin>>> getInfos(){

        List<Admin> admin = adminService.getInfos();

        return Result.ok(admin);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResult<?>> update(@PathVariable Long id, @RequestBody AdminUpdateDto.Request request){

        Admin admin = adminService.update(id,request.getPassword(),request.getName(),request.getEmail());

        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<?>> delete(@PathVariable Long id){

        Admin admin = adminService.delete(id);

        return Result.ok();
    }
}
