package com.zinn.zinnbe.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zinn.zinnbe.services.KBService;

@RestController
@RequestMapping("/file")
public class KBController {
    @Autowired
    private KBService kbService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file){
        return ResponseEntity.ok(kbService.fileUpload(file));
    }
}