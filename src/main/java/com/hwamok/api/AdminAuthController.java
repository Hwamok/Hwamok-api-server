package com.hwamok.api;

import com.hwamok.api.dto.auth.AdminLoginDto;
import com.hwamok.auth.service.AdminAuthService;
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
public class AdminAuthController {
    private final AdminAuthService adminAuthService;

    @PostMapping("/adminLogin")
    public ResponseEntity<ApiResult<AdminLoginDto.Response>> login(@RequestBody AdminLoginDto.Request request){
        Pair<String,String> pair = adminAuthService.login(request.getLoginId(), request.getPassword());

        return Result.ok(new AdminLoginDto.Response(pair.getFirst(), pair.getSecond()));
    }
}
