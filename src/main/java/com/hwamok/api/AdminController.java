package com.hwamok.api;

import com.hwamok.admin.domain.Admin;
import com.hwamok.admin.service.AdminService;
import com.hwamok.api.dto.admin.AdminCreateDto;
import com.hwamok.api.dto.admin.AdminReadDto;
import com.hwamok.api.dto.admin.AdminUpdateDto;
import com.hwamok.core.response.ApiResult;
import com.hwamok.core.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping
    public ResponseEntity<ApiResult<?>> create(@RequestBody AdminCreateDto.Request request){
        adminService.create(request.getLoginId(),request.getPassword(),request.getName(),request.getEmail(), request.getRoles());

        return Result.created();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<AdminReadDto.Response>> getInfo(@PathVariable Long id){
        return Result.ok(adminService.getInfo(id));
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResult<List<AdminReadDto.Response>>> getInfos(){
        List<AdminReadDto.Response> responses = adminService.getInfos();

        return Result.ok(responses);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResult<?>> update(@PathVariable Long id, @RequestBody AdminUpdateDto.Request request){
        adminService.update(id, request.getPassword(), request.getName(), request.getEmail());

        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<?>> delete(@PathVariable Long id){
        adminService.delete(id);

        return Result.ok();
    }
}
