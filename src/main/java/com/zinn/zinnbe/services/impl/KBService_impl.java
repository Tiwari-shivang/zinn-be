package com.zinn.zinnbe.services.impl;

import org.springframework.stereotype.Service;

import com.zinn.zinnbe.DTOs.SuccessMessage;
import com.zinn.zinnbe.services.KBService;

@Service
public class KBService_impl implements KBService{
    public SuccessMessage fileUpload(){
        SuccessMessage response = new SuccessMessage("File uploaded successfully!");
        return response;
    }
}
