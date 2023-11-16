package com.hwamok.core.exception;

import com.hwamok.core.response.ApiResult;
import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class HwamokException extends RuntimeException{ // RuntimeException 을 상속받아야 에러가 발생한다.
    // 여기는 진짜 에러를 발생시키는 곳
    // HwamokException에 ExceptionCode(메시지)를 넣어서 Exception이 발생하도록 한다. Result클래스는 200코드로 눈속임을 시키는 역할만 한다.

    private final ApiResult<?> apiResult;
    private final ExceptionCode exceptionCode;

    public HwamokException(ExceptionCode exceptionCode) {
        super(exceptionCode.name()); // RuntimeException 을 말함(ex. new RuntimeException("error_system"))
        this.apiResult = ApiResult.of(exceptionCode);
        this.exceptionCode = exceptionCode;
    }

}
