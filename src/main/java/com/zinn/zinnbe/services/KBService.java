package com.zinn.zinnbe.services;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.zinn.zinnbe.DTOs.FileUploadRequest;
import com.zinn.zinnbe.DTOs.FileUploadResponse;

public interface KBService {
    FileUploadResponse fileUpload(MultipartFile file, FileUploadRequest request);
}
