package com.webproject.chatservice.controller;

import com.webproject.chatservice.utils.Uploader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@Slf4j

@RestController
public class S3Controller {

    private final Uploader uploader;

    @Autowired
    public S3Controller(Uploader uploader) {
        this.uploader = uploader;
    }

    @PostMapping("/api/s3upload")
    public String imgUpload(@RequestParam("data") MultipartFile file) throws IOException {
        String profileUrl = uploader.upload(file, "static");
        log.info("profileUrl={}", profileUrl);
        return profileUrl;
    }

}
