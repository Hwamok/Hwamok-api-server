package com.hwamok.core.exception;

public enum ExceptionCode {

    SUCCESS("S000", "success"),

    ERROR_SYSTEM("E000", "치명적인 오류가 발생했습니다."),

    REQUIRED_PARAMETER("E001", "필수 값이 누락되었습니다."),

    INVALID_NOTICE("E002", " 공지사항 작성 중 에러가 발생하였습니다."),

    NOT_FOUND_NOTICE("E003", "공지사항을 찾을 수가 없습니다."),
    FAILED_TO_MODIFY_NOTICE("E004", "공지사항을 수정하지 못 했습니다."),
    NOT_FOUND_USER("E005", "유저를 찾을 수가 없습니다."),

    FAIL_LOGIN_REQUEST("E006", "아이디 또는 패스워드를 확인해주세요."),

    ACCESS_DNEIED("E007", "로그인을 먼저 해주세요."),

    ERROR_FORMAT("E008", "형식이 일치하지 않습니다.");

    private final String code;
    private final String message;



    ExceptionCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
