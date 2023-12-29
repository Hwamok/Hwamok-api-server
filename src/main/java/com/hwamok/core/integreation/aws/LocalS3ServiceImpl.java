package com.hwamok.core.integreation.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.core.exception.HwamokException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@Profile({"local", "default"})
@RequiredArgsConstructor
public class LocalS3ServiceImpl implements S3Service {

    private final AmazonS3 amazonS3;

    @Value("${s3.bucket}")
    private String bucket;

    @Override
    public Pair<String, String> upload(MultipartFile multipartFiles) {
            String savedFileName = createFileName(multipartFiles.getOriginalFilename());
            String fileName = multipartFiles.getOriginalFilename();

            ObjectMetadata objectMetadata = new ObjectMetadata();

            objectMetadata.setContentType(multipartFiles.getContentType());
            objectMetadata.setContentLength(multipartFiles.getSize());

            try(InputStream inputStream = multipartFiles.getInputStream()) {
                amazonS3.putObject(bucket, savedFileName, inputStream, objectMetadata);
            }catch (Exception e) {
                throw new HwamokException(ExceptionCode.FILE_UPLOAD_FAILED);
            }

        return Pair.of(fileName, savedFileName);
    }

    @Override
    public boolean delete(String savedFileName) {
        if(amazonS3.doesObjectExist(bucket, savedFileName)) {
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, savedFileName));

            return true;
        }

        return false;
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