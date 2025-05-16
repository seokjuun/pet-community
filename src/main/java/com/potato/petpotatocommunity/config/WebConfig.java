package com.potato.petpotatocommunity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**") // 클라이언트에서 접근할 경로
                .addResourceLocations("file:/Users/seokjun/PetPotato/pet-community/uploads/images/"); // 실제 저장 경로
    }
}