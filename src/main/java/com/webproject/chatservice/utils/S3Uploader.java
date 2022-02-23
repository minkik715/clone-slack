package com.webproject.chatservice.utils;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j // 로깅을 위한 어노테이션
@Component // 빈 등록을 위한 어노테이션
@RequiredArgsConstructor // final 변수에 대한 의존성을 추가합니다.
public class S3Uploader implements Uploader {


    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")  // 프로퍼티에서 cloude.aws.s3.bucket에 대한 정보를 불러옵니다.
    public String bucket;

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File convertedFile = convert(multipartFile);
        return upload(convertedFile, dirName);
    }

    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + uploadFile.getName();
        log.info("putS3 = {}", fileName);
        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            return;
        }
        log.info("임시 파일이 삭제 되지 못했습니다. 파일 이름: {}", targetFile.getName());
    }

    private File convert(MultipartFile file) throws IOException {
        int pos = file.getOriginalFilename().lastIndexOf(".");
        String ext = file.getOriginalFilename().substring(pos + 1);
        File convertFile = new File(System.getProperty("user.dir") + "/" + UUID.randomUUID().toString()+"."+ext);
        log.info("convertFile = {}", convertFile);
        boolean newFileIsPresent = convertFile.createNewFile();
        log.info("convertFile.createNewFile() = {}", newFileIsPresent);
        if (newFileIsPresent) {
            log.info("여기까지는 되는데;");
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }catch (Exception e){
                e.printStackTrace();
            }
            return convertFile;
        }
        throw new IllegalArgumentException(String.format("파일 변환이 실패했습니다. 파일 이름: %s", file.getName()));
    }

 /*   private File convert(MultipartFile file) throws IOException {
        log.info("file={}", file.getOriginalFilename());
        File convertFile = new File(file.getOriginalFilename());
        file.transferTo(convertFile);
        return convertFile;
    }*/

}
