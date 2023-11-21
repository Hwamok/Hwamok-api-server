package com.hwamok.utils;

public class CreateValueUtil {
    public static String stringLength(int num){
        StringBuilder value = new StringBuilder();

        for(int i=0; i<num; i++){
            value.append("값");
        }

        return value.toString();
    }
}
