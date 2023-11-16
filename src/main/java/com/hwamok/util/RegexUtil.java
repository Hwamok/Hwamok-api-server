package com.hwamok.util;

public class RegexUtil {

    // 이메일형식 : 일반적인 이메일주소형식 검증
    private static final String EMAIL_PATTERN = "^[_a-z0-9-]+(.[_a-z0-9-]+)@(?:\\w+\\.)+\\w+$";

    //이름 형식 : 2글자이상 20글자이하 한글로만 이루어져있거나 영어로만 작성된 글자
    private static final String NAME_PATTERN = "^[가-힣]{2,20}$|^[a-zA-Z]{2,20}$";

    public static Boolean matches(String s, RegexType type) {
        boolean result;
        switch (type) {
            case EMAIL -> result = s.matches(EMAIL_PATTERN);

            case NAME -> result = s.matches(NAME_PATTERN);

            default -> result = false;

        }
        return result;
    }
}
