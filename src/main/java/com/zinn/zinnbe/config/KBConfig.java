package com.zinn.zinnbe.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class KBConfig {
    @Bean
    public Cloudinary configure(){
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dcickr0vk");
        config.put("api_secret", "7mhGtuHBWczQ6f7f7zQ7wI-kpXM");
        config.put("api_key", "129513657186975");
        return new Cloudinary(config);
    }
}
