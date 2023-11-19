package com.hwamok.core.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {

    SUCCESS("S000", "success"),
    ERROR_SYSTEM("E000", "치명적 오류가 발생 했습니다."),
    REQUIRED_PARAMETER("E001", "필수 값이 누락되었습니다."),
    NOT_LOGINID_FORM("E002", "아이디형식이 다릅니다."),
    NOT_EMAIL_FORM("E003", "이메일형식이 다릅니다."),
    NOT_NAME_FORM("E004", "이름형식이 다릅니다."),
    NOT_FOUND_ADMIN("E005","관리자정보를 찾을 수 없습니다."),
    NOT_PASSWORD_MATCH("E006", "패스워드가 일치하지 않습니다."),
    OVER_LENGTH_TITLE("E007", "제목의 길이 초과되었습니다."),
    OVER_LENGTH_CONTENT("E008", "내용의 길이가 초과되었습니다."),
    NOT_FOUND_NOTICE("E009","공지사항 졍보를 찾을 수 없습니다."),
    
    ;

    private final String code;
    private final String message;

    ExceptionCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
