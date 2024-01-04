package com.hwamok.core.integreation.aws;

import org.springframework.data.util.Pair;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3Service {

    public Pair<String, String> upload(MultipartFile multipartFiles);

    public boolean delete(String savedFileName);
}
