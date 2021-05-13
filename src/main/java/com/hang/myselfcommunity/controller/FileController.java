package com.hang.myselfcommunity.controller;

import com.hang.myselfcommunity.dto.FileDTO;
import com.hang.myselfcommunity.util.AliyunOOSUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class FileController {

    @PostMapping("/upload")
    public FileDTO upload(HttpServletRequest request) {
        FileDTO fileDTO = new FileDTO();

        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile file = multipartRequest.getFile("editormd-image-file");

            AliyunOOSUtil.buildOOSClient();
            String url = AliyunOOSUtil.uploadFile(file);

            fileDTO.setSuccess(1);
            fileDTO.setUrl(url);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            fileDTO.setSuccess(0);
        } finally {
            AliyunOOSUtil.destroy();
        }
        return fileDTO;
    }
}