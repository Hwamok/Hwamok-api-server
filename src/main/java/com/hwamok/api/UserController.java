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
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResult<?>> create(@RequestBody UserCreateDto.Request request, @RequestPart MultipartFile profilePicture) {
        userService.create(
                request.getEmail(),
                request.getPassword(),
                request.getName(),
                request.getBirthDay(),
                request.getPhone(),
                request.getPlatform(),
                request.getAddress(),
                profilePicture);

        return Result.created();
    }

    @PostMapping("/{id}")
    public ResponseEntity<ApiResult<?>> update(@PathVariable Long id,
                                               @RequestBody UserUpdateDto.Request request, @RequestPart MultipartFile profilePicture) {
        userService.update(id,
                request.getPassword(),
                request.getName(),
                request.getBirthDay(),
                request.getPhone(),
                request.getPlatform(),
                request.getAddress(),
                profilePicture);

        return Result.ok();
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