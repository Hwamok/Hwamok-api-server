package com.hwamok.core.response;

import com.hwamok.core.exception.ExceptionCode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResult<T> {
    private String code;
    private String message;
    private T data;

    public static ApiResult<?> of(ExceptionCode exceptionCode){
        return ApiResult.of(exceptionCode, null);
    }

    public static <T> ApiResult<T> of(ExceptionCode exceptionCode, T data) {
        return ApiResult.<T>builder()
                .code(exceptionCode.getCode())
                .message(exceptionCode.getMessage())
                .data(data)
                .build();
    }
}
