package com.hwamok.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AddressUpdateDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

      private int post;

      private String addr;

      private String detailAddr;

    }

}
