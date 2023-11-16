package com.hwamok.core.exception;

import com.hwamok.core.response.ApiResult;
import com.hwamok.core.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler({Throwable.class}) // Throwable이 발생하면 이 에러 코드가 발생
    public ResponseEntity<ApiResult<?>> exception(Throwable throwable) {
        log.info(throwable.getMessage());
        return Result.error();
    }

    @ExceptionHandler({IllegalArgumentException.class}) // IllegalArgumentException 발생하면 이 에러 코드가 발생
    public ResponseEntity<ApiResult<?>> IllegalArgumentException(IllegalArgumentException e) {
        log.info(e.getMessage());
        return Result.error(ExceptionCode.REQUIRED_PARAMETER);

    }

    @ExceptionHandler({HwamokException.class}) // HwamokException 발생하면 이 에러 코드가 발생
    public ResponseEntity<ApiResult<?>> HwamokException(HwamokException hwamokException) {
        log.info(hwamokException.getMessage());

        return Result.error(hwamokException.getExceptionCode());
    }
}
