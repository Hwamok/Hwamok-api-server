package com.hwamok.core.exception;

import com.hwamok.core.response.ApiResult;
import lombok.Getter;

@Getter
public class HwamokException extends RuntimeException{
    private final ApiResult<?> apiResult;
    private final ExceptionCode exceptionCode;

    public HwamokException(ExceptionCode exceptionCode) {
        super(exceptionCode.name());
        this.apiResult = ApiResult.of(exceptionCode);
        this.exceptionCode = exceptionCode;
    }
}
