package com.hwamok.utils;

import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.core.exception.HwamokException;

public class PreConditions {

    public static void require(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }

    public static void validate(boolean expression, ExceptionCode exceptionCode){
        if(!expression){
            throw new HwamokException(exceptionCode);
        }
    }
}
