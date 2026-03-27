package com.zinn.zinnbe.services.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.zinn.zinnbe.DTOs.FileUploadRequest;
import com.zinn.zinnbe.DTOs.FileUploadResponse;
import com.zinn.zinnbe.models.Document;
import com.zinn.zinnbe.models.Users;
import com.zinn.zinnbe.repositories.DocRepository;
import com.zinn.zinnbe.services.KBService;

@Service
public class KBService_impl implements KBService{
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private DocRepository docRepo;

    public FileUploadResponse fileUpload(MultipartFile file, FileUploadRequest request){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principles = auth.getPrincipal();
        Users loggedInUser = (Users) principles;
        Map<String, Object> configuration = ObjectUtils.asMap("resource_type", "auto", "access_mode", "public");
        try{
            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), configuration);
            System.out.println(result.get("secure_url").toString());
            FileUploadResponse response = new FileUploadResponse();
            Map<String, String> metaData = new HashMap<>();
            metaData.put("source", result.get("secure_url").toString());
            metaData.put("tenant_id", loggedInUser.getTenant().getId().toString());
            metaData.put("user_id", loggedInUser.getId().toString());
            metaData.put("public_id", result.get("public_id").toString());

            Document newDoc = new Document();
            newDoc.setTenant(loggedInUser.getTenant());
            newDoc.setFileType(file.getContentType());
            newDoc.setFileName(file.getName());
            newDoc.setMetaData(metaData);
            newDoc.setContent(request.getKbDescription());
            Timestamp now = new Timestamp(System.currentTimeMillis());
            newDoc.setCreatedAt(now);
            docRepo.save(newDoc);

            response.setFile_name(file.getName());
            response.setTenant_id(loggedInUser.getTenant().getId().toString());
            response.setFormat(file.getContentType());
            response.setSource(result.get("secure_url").toString());
            response.setUser_id(loggedInUser.getId().toString());
            response.setPublic_id(result.get("public_id").toString());
            return response;
        }
        catch(IOException e){
            System.out.println(e);
            throw new RuntimeException("Upload failed");
        }
    }
}
