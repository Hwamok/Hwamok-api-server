package com.hwamok.api;

import com.hwamok.api.dto.auth.AdminLoginDto;
import com.hwamok.api.dto.auth.UserLoginDto;
import com.hwamok.auth.service.AuthService;
import com.hwamok.core.response.ApiResult;
import com.hwamok.core.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/userLogin")
    public ResponseEntity<ApiResult<UserLoginDto.Response>> userLogin(@RequestBody UserLoginDto.Request request){
        Pair<String,String> pair = authService.userLogin(request.getEmail(), request.getPassword());

        return Result.ok(new UserLoginDto.Response(pair.getFirst(), pair.getSecond()));
    }

    @PostMapping("/adminLogin")
    public ResponseEntity<ApiResult<AdminLoginDto.Response>> adminLogin(@RequestBody AdminLoginDto.Request request){
        Pair<String,String> pair = authService.adminLogin(request.getLoginId(), request.getPassword());

        return Result.ok(new AdminLoginDto.Response(pair.getFirst(), pair.getSecond()));
    }
}
