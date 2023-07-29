package com.example.runway.service.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.runway.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import marvin.image.MarvinImage;
import org.marvinproject.image.transform.scale.Scale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.example.runway.constants.CommonResponseStatus.FAIL_UPLOAD_IMG;
import static com.example.runway.constants.CommonResponseStatus.WRONG_FORMAT_FILE;

@Service
@RequiredArgsConstructor
public class AwsS3ServiceImpl implements AwsS3Service{

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.url}")
    private String url;

    private final AmazonS3 amazonS3;

    public String upload(MultipartFile multipartFile,String dirName) throws ForbiddenException, IOException {
        String fileName = dirName+"/"+UUID.randomUUID().toString() + ".jpg";

        //MultipartFile resizeFile=resizeImage(fileName,multipartFile.getContentType().substring(multipartFile.getContentType().lastIndexOf("/") + 1),multipartFile,600);

        byte[] bytes = multipartFile.getBytes();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/jpeg");
        metadata.setContentLength(bytes.length);
        amazonS3.putObject(bucket, fileName, byteArrayInputStream, metadata);


        return amazonS3.getUrl(bucket, fileName).toString();
    }


    @Override
    public String uploadByteCode(byte[] bytes, String dirName) {
        String s3FileName=dirName+"/"+UUID.randomUUID().toString()+".jpg";
        ObjectMetadata metadata = new ObjectMetadata();

        metadata.setContentLength(bytes.length);
        metadata.setContentType("image/jpeg");

        amazonS3.putObject(new PutObjectRequest(bucket,s3FileName,new ByteArrayInputStream(bytes),metadata));

        return amazonS3.getUrl(bucket,s3FileName).toString();
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
        int index=fileName.indexOf(url);
        String fileRoute=fileName.substring(index+url.length()+1);
        System.out.println("deletefilename : "+fileRoute);
        try {
            boolean isObjectExist = amazonS3.doesObjectExist(bucket, fileRoute);
            if (isObjectExist) {
                amazonS3.deleteObject(bucket,fileRoute);
            } else {
                System.out.println( "file not found");
            }
        } catch (Exception e) {
            System.out.println("Delete File failed");
        }
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

    /*

    MultipartFile resizeImage(String fileName, String fileFormatName, MultipartFile originalImage, int targetWidth) {
        try {
            // MultipartFile -> BufferedImage Convert
            BufferedImage image = ImageIO.read(originalImage.getInputStream());
            // newWidth : newHeight = originWidth : originHeight
            int originWidth = image.getWidth();
            int originHeight = image.getHeight();

            // origin 이미지가 resizing될 사이즈보다 작을 경우 resizing 작업 안 함
            if(originWidth < targetWidth)
                return originalImage;

            MarvinImage imageMarvin = new MarvinImage(image);

            Scale scale = new Scale();
            scale.load();
            scale.setAttribute("newWidth", targetWidth);
            scale.setAttribute("newHeight", targetWidth * originHeight / originWidth);
            scale.process(imageMarvin.clone(), imageMarvin, null, null, false);

            BufferedImage imageNoAlpha = imageMarvin.getBufferedImageNoAlpha();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(imageNoAlpha, fileFormatName, baos);
            baos.flush();

            return new CustomMultipartFile(fileName,fileFormatName, originalImage.getContentType() ,baos.toByteArray());

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 리사이즈에 실패했습니다.");
        }
    }

     */


    private static class CustomMultipartFile implements MultipartFile {
        private final String name;

        private final String originalFilename;

        private final String contentType;

        private final byte[] content;
        boolean isEmpty;


        public CustomMultipartFile(String name, String originalFilename, String contentType, byte[] content) {
            Assert.hasLength(name, "Name must not be null");
            this.name = name;
            this.originalFilename = (originalFilename != null ? originalFilename : "");
            this.contentType = contentType;
            this.content = (content != null ? content : new byte[0]);
            this.isEmpty = false;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public String getOriginalFilename() {
            return this.originalFilename;
        }

        @Override
        public String getContentType() {
            return this.contentType;
        }

        @Override
        public boolean isEmpty() {
            return (this.content.length == 0);
        }

        @Override
        public long getSize() {
            return this.content.length;
        }

        @Override
        public byte[] getBytes() throws IOException {
            return this.content;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(this.content);
        }

        @Override
        public void transferTo(File dest) throws IOException, IllegalStateException {
            FileCopyUtils.copy(this.content, dest);
        }
    }
}
