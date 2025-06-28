package com.zuree.driver_tracking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customizeOpenAPI() {
        return new OpenAPI()
//                .addSecurityItem(new SecurityRequirement().addList("bearerAuth")) // apply globally
                .components(new Components().addSecuritySchemes("bearerAuth",
                        new SecurityScheme()
                                .name("Authorization")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT") // optional but good for clarity
                ))
                .info(new Info()
                        .title("Driver Tracking API")
                        .version("1.0.0")
                        .description("API documentation for driver tracking application."));
    }
}


