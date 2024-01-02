package com.hwamok.core.integreation.aws;

import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.core.exception.HwamokException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Profile({"local", "default"})
@RequiredArgsConstructor
public class LocalS3ServiceImpl implements S3Service {

    @Override
    public Pair<String, String> upload(MultipartFile multipartFiles) {
            String savedFileName = createFileName(multipartFiles.getOriginalFilename());
            String fileName = multipartFiles.getOriginalFilename();

        return Pair.of(fileName, savedFileName);
    }

    @Override
    public boolean delete(String savedFileName) {
        return true;
    }

    private String createFileName(String name) {
        return "F_" + System.currentTimeMillis() + getExtension(name);
    }

    private String getExtension(String name) {
        try {
            return name.substring(name.lastIndexOf("."));
        }catch (StringIndexOutOfBoundsException e) {
            throw new HwamokException(ExceptionCode.NOT_FILE_FORM);
        }
    }
}