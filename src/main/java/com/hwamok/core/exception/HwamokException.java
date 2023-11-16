package com.hwamok.core.exception;

import com.hwamok.core.response.ApiResult;
import lombok.Getter;

@Getter
public class HwamokException extends RuntimeException {

    private final ApiResult<?> apiResult;
    private final ExceptionCode exceptionCode;

    public HwamokException(ExceptionCode exceptionCode) {
        super(exceptionCode.name());
        this.apiResult = ApiResult.of(exceptionCode);
        this.exceptionCode=exceptionCode;
    }
}












//    // HwamokException에 ExceptionCode를 넣어서 Exception 발생
//    // ExceptionCode가 ExceptionCode가지고 있어서 익셉션
//    private ApiResult<?> apiResult = Result.ok().getBody();
//    private String message;
//
//    public HwamokException(Throwable throwable) {
//        this.setApiResult(throwable);
//
//    }
//
//    private void setApiResult(Throwable throwable) {
//        ExceptionCode exceptionCode = getExceptionCode();
//        this.apiResult=ApiResult.of(throwable);
//    }

