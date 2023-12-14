package com.hwamok.utils;

import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.core.exception.HwamokException;

import java.util.Objects;

public class PreConditions {
    public static <T> T notNull(T obj){
        return Objects.requireNonNull(obj);
    }

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
