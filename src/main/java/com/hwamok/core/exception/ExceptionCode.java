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
    NOT_BRANCH_FORM("E007", "브랜치 형식이 다릅니다."),
    NOT_CODE_FORM("E008", "코드 형식이 다릅니다."),
    NOT_LEVEL_FORM("E009", "레벨 형식이 다릅니다,"),
    NOT_PRICE_FORM("E010", "가격 형식이 다릅니다."),
    NOT_FOUND_CATEGORY("E011", "카테고리를 찾을 수 없습니다.")
    ;

    private final String code; // final은 재할당 금지
    private final String message;

    ExceptionCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
