package com.hwamok.utils;

public class RegexUtil {
    //아이디 형식 : 4글자 이상 12글자 이하의 대소문자 알파벳과 숫자로만 이루어짐
    private static final String LOGINID_PATTERN = "^[a-zA-Z0-9]{4,12}$";

    //이름 형식 : 2글자이상 6글자이하 한글로만 이루어져있거나 2글자이상 20글자 이하 영어로만 작성된 글자
    private static final String NAME_PATTERN = "^[가-힣]{2,6}$|^[a-zA-Z]{2,20}$";

    // 이메일형식 : 일반적인 이메일주소형식 검증
    private static final String EMAIL_PATTERN = "^[_a-z0-9-]+(.[_a-z0-9-]+)@(?:\\w+\\.)+\\w+$";

    private static final String BIRTHDAY_PATTERN = "^[\\d]{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$";

    private static final String POST_PATTERN = "^\\d{5}$";

    private static final String PHONE_PATTERN = "^01([0|1|6|7|8|9])([0-9]{4})([0-9]{4})$";

    public static Boolean matches(String s, RegexType type) {
        boolean result;

        switch (type) {
            case LOGINID -> result = s.matches(LOGINID_PATTERN);
            case NAME -> result = s.matches(NAME_PATTERN);
            case EMAIL -> result = s.matches(EMAIL_PATTERN);
            case BIRTHDAY -> result = s.matches(BIRTHDAY_PATTERN);
            case POST -> result = s.matches(POST_PATTERN);
            case PHONE -> result = s.matches(PHONE_PATTERN);
            default -> result = false;
        }

        return result;
    }
}
