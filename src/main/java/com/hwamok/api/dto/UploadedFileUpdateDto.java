package com.hwamok.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UploadedFileUpdateDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

      private String originalFileName;

      private String savedFileName;

    }
}
