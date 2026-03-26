package com.zinn.zinnbe.services.impl;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.zinn.zinnbe.DTOs.SuccessMessage;
import com.zinn.zinnbe.services.KBService;

@Service
public class KBService_impl implements KBService{
    @Autowired
    private Cloudinary cloudinary;
    public SuccessMessage fileUpload(MultipartFile file){
        SuccessMessage response = new SuccessMessage("File uploaded successfully!");
        Map<String, Object> configuration = ObjectUtils.asMap("resource_type", "auto", "access_mode", "public");
        try{
            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), configuration);
            System.out.println(result.get("secure_url").toString());
        }
        catch(IOException e){
            System.out.println(e);
        }
        return response;
    }
}
