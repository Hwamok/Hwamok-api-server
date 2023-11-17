package com.hwamok.core.response;

import com.hwamok.core.exception.ExceptionCode;
import org.springframework.http.ResponseEntity;

public class Result {
    private Result() {
    }

    //200코드인 상황 만들기 ( ok() 메서드는 정적 메서드로, HTTP 응답의 상태 코드 200 (OK)를 반환하고, ApiResult 객체를 생성하여 반환.)
    public static ResponseEntity<ApiResult<?>> ok() {
        return ResponseEntity.status(200).body(ApiResult.of(ExceptionCode.SUCCESS)); // 실패한 처리의 경우에도 200 상태코드를 내보낸다. 이유는 이 에러가 날줄알았고 이미 예외처리를 했다는 뜻으로 보내는 것임.
    }

    public static <T> ResponseEntity<ApiResult<T>> ok(T data) {
        return ResponseEntity.status(200).body(ApiResult.of(ExceptionCode.SUCCESS, data)); // 실패한 처리의 경우에도 200 상태코드를 내보낸다. 이유는 이 에러가 날줄알았고 이미 예외처리를 했다는 뜻으로 보내는 것임.
    }

    public static ResponseEntity<ApiResult<?>> created() {
        return ResponseEntity.status(201).body(ApiResult.of(ExceptionCode.SUCCESS)); // 실패한 처리의 경우에도 200 상태코드를 내보낸다. 이유는 이 에러가 날줄알았고 이미 예외처리를 했다는 뜻으로 보내는 것임.
    }

    public static ResponseEntity<ApiResult<?>> error(ExceptionCode exceptionCode) {

        return ResponseEntity.status(200).body(ApiResult.of(exceptionCode)); // 실패한 처리의 경우에도 200 상태코드를 내보낸다. 이유는 이 에러가 날줄알았고 이미 예외처리를 했다는 뜻으로 보내는 것임.
    }

}
