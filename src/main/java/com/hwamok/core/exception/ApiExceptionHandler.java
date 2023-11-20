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
    @ExceptionHandler({Throwable.class})
    public ResponseEntity<ApiResult<?>> exception(Throwable throwable) {
        log.info(throwable.getMessage());

        return Result.error(ExceptionCode.ERROR_SYSTEM);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ApiResult<?>> illegalArgumentException(IllegalArgumentException e) {
        log.info(e.getMessage());

        return Result.error(ExceptionCode.REQUIRED_PARAMETER);
    }

    @ExceptionHandler({HwamokException.class})
    public ResponseEntity<ApiResult<?>> hwamokException(HwamokException e) {
        log.info(e.getMessage());

        return Result.error(e.getExceptionCode());
    }
}

