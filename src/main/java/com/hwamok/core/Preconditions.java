package com.hwamok.core;

import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.core.exception.HwamokException;

public class Preconditions {

    public static void require(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }

    public static void checkPost(Integer post) {
        if(post == null || post <= 0) {
            throw new IllegalArgumentException();
        }

    }

    public static void validate(boolean expression, ExceptionCode exceptionCode) {
        if(!expression){
            throw new HwamokException(exceptionCode);
        }
    }


}
