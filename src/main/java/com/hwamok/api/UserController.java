package com.hwamok.api;

import com.hwamok.api.dto.user.UserCreateDto;
import com.hwamok.api.dto.user.UserUpdateDto;
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

    @PostMapping
    public ResponseEntity<ApiResult<?>> create(@RequestBody UserCreateDto.Request request) {
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

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResult<User>> update(@PathVariable Long id, @RequestBody UserUpdateDto.Request request) {
        User user = userService.update(id, request.getEmail(),
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

    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<User>> getInfo(@PathVariable long id) {
        User user = userService.getInfo(id);

        return Result.ok(user);
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<ApiResult<?>> withdraw(@PathVariable long id) {
        userService.withdraw(id);

        return Result.ok();
    }
}
