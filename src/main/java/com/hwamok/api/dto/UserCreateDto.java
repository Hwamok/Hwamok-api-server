package com.hwamok.api.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserCreateDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        private String email;

        private String password;

        private String name;

        private String birthDay;

        private String phone;

        private String platform;

        private String status;

        private UploadedFileCreateDto.Request profile;

        private AddressCreateDto.Request address;
    }

}
