package com.example.runway.service.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.runway.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.runway.constants.CommonResponseStatus.FAIL_UPLOAD_IMG;
import static com.example.runway.constants.CommonResponseStatus.WRONG_FORMAT_FILE;

@Service
@RequiredArgsConstructor
public class AwsS3ServiceImpl implements AwsS3Service{

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public String upload(MultipartFile multipartFile,String dirName) throws ForbiddenException, IOException {
        String s3FileName=dirName+"/"+createFileName(multipartFile.getOriginalFilename());
        System.out.println(s3FileName);
        ObjectMetadata objMeta = new ObjectMetadata();


        //파일 형식 구하기
        String ext = s3FileName.split("\\.")[1];
        String contentType = multipartFile.getContentType();

        switch (ext) {
            case "jpeg":
                contentType = "image/jpeg";
                break;
            case "png":
                contentType = "image/png";
                break;
            case "jpg":
                contentType = "image/jpg";
                break;

        }

        objMeta.setContentType(contentType);
        objMeta.setContentLength(multipartFile.getInputStream().available());

        amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta);

        return amazonS3.getUrl(bucket, s3FileName).toString();
    }

    public List<String> uploadImage(List<MultipartFile> multipartFile){
        List<String> fileNameList = new ArrayList<>();

        multipartFile.forEach(file -> {
            String fileName = null;
            try {
                fileName = createFileName(file.getOriginalFilename());
            } catch (ForbiddenException e) {
                e.printStackTrace();
            }
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try(InputStream inputStream = file.getInputStream()) {
                amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch(IOException e) {
                try {
                    throw new ForbiddenException(FAIL_UPLOAD_IMG);
                } catch (ForbiddenException ex) {
                    ex.printStackTrace();
                }
            }

            fileNameList.add(fileName);
        });

        return fileNameList;
    }

    public void deleteImage(String fileName) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }

    public String createFileName(String fileName) throws ForbiddenException {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    public String getFileExtension(String fileName) throws ForbiddenException {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ForbiddenException(WRONG_FORMAT_FILE);
        }
    }
}
