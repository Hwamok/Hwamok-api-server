package com.hwamok.api;

import com.hwamok.api.dto.UserCreateDto;
import com.hwamok.api.dto.UserUpdateDto;
import com.hwamok.core.response.ApiResult;
import com.hwamok.core.response.Result;
import com.hwamok.user.domain.User;
import com.hwamok.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping // 회원 가입
    public ResponseEntity<ApiResult<?>> signup(@RequestBody UserCreateDto.Request request) {
        userService.create(
                request.getEmail(),
                request.getPassword(),
                request.getName(),
                request.getBirthDay(),
                request.getPhone(),
                request.getPlatform(),
                request.getProfile().getOriginalFileName(),
                request.getProfile().getSavedFileName(),
                request.getAddress().getPost(),
                request.getAddress().getAddr(),
                request.getAddress().getDetailAddr());
        return Result.created();
    }

    @PatchMapping("/updateProfile/{id}") // 회원 수정
    public ResponseEntity<ApiResult<User>> updateProfile(@PathVariable Long id, @RequestBody UserUpdateDto.Request request) {
        User user = userService.updateProfile(id, request.getEmail(),
                request.getPassword(),
                request.getName(),
                request.getBirthDay(),
                request.getPhone(),
                request.getPlatform(),
                request.getProfile().getOriginalFileName(),
                request.getProfile().getSavedFileName(),
                request.getAddress().getPost(),
                request.getAddress().getAddr(),
                request.getAddress().getDetailAddr());
        return Result.ok(user);
    }

    @GetMapping("/userOne/{id}") // 회원 조회
    public ResponseEntity<ApiResult<User>> userOne(@PathVariable long id) {
        User user = userService.getUser(id);
        return Result.ok(user);
    }

    @DeleteMapping ("/withdraw/{id}") // 회원 탈퇴
    public ResponseEntity<ApiResult<User>> withdrawUser(@PathVariable long id) {
        userService.withdraw(id);
        User user = userService.getUser(id);
        return Result.ok(user);
    }
}
