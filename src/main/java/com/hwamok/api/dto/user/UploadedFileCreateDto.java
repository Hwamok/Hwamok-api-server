package com.hwamok.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UploadedFileCreateDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
      private String originalFileName;
      private String savedFileName;
    }
}
