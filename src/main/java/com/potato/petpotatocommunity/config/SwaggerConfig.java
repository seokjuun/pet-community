package com.potato.petpotatocommunity.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Pet Potato Community API",
                description = "이 프로젝트는 반려동물 커뮤니티 플랫폼입니다.",
                version = "v1.0"

        )
)

public class SwaggerConfig {
}
