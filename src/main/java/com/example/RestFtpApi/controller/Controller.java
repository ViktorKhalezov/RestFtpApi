package com.example.RestFtpApi.controller;

import com.example.RestFtpApi.entity.FileInfo;
import com.example.RestFtpApi.service.FtpService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/photos")
public class Controller {

    private final FtpService ftpService;


    public Controller(FtpService ftpService) {
        this.ftpService = ftpService;
        try {
            ftpService.open();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


    @GetMapping
    public List<FileInfo> getFileList() {
        return ftpService.getFileList();
    }


}


