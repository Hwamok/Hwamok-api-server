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
    NOT_FOUND_ADMIN("E005", "관리자정보를 찾을 수 없습니다."),
    NOT_PASSWORD_MATCH("E006", "패스워드가 일치하지 않습니다."),
    NOT_FOUND_USER("E007", "사용자 정보를 찾을 수 없습니다."),
    NOT_DATE_FORM("E008", "날짜 형식이 다릅니다."),
    NOT_POST_FORM("E009", "우편 번호 형식이 다릅니다."),
    NOT_PHONE_FORM("E010", "핸드폰 번호 형식이 다릅니다."),
    NOT_BRANCH_FORM("E011", "브랜치 형식이 다릅니다."),
    NOT_CODE_FORM("E012", "코드 형식이 다릅니다."),
    NOT_LEVEL_FORM("E013", "레벨 형식이 다릅니다"),
    NOT_FOUND_CATEGORY("E014", "카테고리를 찾을 수 없습니다."),
    OVER_LENGTH_TITLE("E015", "제목의 길이가 초과되었습니다."),
    OVER_LENGTH_CONTENT("E016", "내용의 길이가 초과되었습니다."),
    NOT_PRICE_FORM("E018", "가격형식이 다릅니다"),
    NOT_FOUND_PRODUCT("E018", "상품을 찾을 수 없습니다."),
    NOT_FOUND_NOTICE("E019", "공지사항 졍보를 찾을 수 없습니다."),
    ;

    private final String code;
    private final String message;

    ExceptionCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
