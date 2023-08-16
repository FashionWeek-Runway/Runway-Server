package com.example.runway.service.util;

import com.example.runway.exception.ForbiddenException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AwsS3Service {
    List<String> uploadImage(List<MultipartFile> multipartFile, String dirName);
    void deleteImage(String fileName);
    String createFileName(String fileName) throws ForbiddenException;
    String getFileExtension(String fileName) throws ForbiddenException;
    String upload(MultipartFile multipartFile,String dirName) throws ForbiddenException, IOException;


    String uploadByteCode(byte[] bytes, String review);

}
