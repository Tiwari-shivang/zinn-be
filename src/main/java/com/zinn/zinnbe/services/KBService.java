package com.zinn.zinnbe.services;

import org.springframework.web.multipart.MultipartFile;

import com.zinn.zinnbe.DTOs.SuccessMessage;

public interface KBService {
    SuccessMessage fileUpload(MultipartFile file);
}
