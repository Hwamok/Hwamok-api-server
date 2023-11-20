package com.hwamok.core.response;

import com.hwamok.core.exception.ExceptionCode;
import org.springframework.http.ResponseEntity;
public class Result {
    private Result() {
    }
    public static ResponseEntity<ApiResult<?>> ok() {
        return ResponseEntity.status(200).body(ApiResult.of(ExceptionCode.SUCCESS));
    }

    public static <T> ResponseEntity<ApiResult<T>> ok(T data) {
        return ResponseEntity.status(200).body(ApiResult.of(ExceptionCode.SUCCESS, data));
    }

    public static ResponseEntity<ApiResult<?>> created() {
        return ResponseEntity.status(201).body(ApiResult.of(ExceptionCode.SUCCESS));
    }

    public static ResponseEntity<ApiResult<?>> error(ExceptionCode exceptionCode) {
        return ResponseEntity.status(200).body(ApiResult.of(exceptionCode));
    }

}
