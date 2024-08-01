package com.kaitech.student_crm.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfigs {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("Bearer token", securityScheme()))
                .info(new Info().title("CRM").description("Backend"))
                .security(List.of(new SecurityRequirement().addList("Bearer token")));
    }

    private SecurityScheme securityScheme() {
        return new SecurityScheme()
                .name("Authorization")
                .description("JWT token")
                .in(SecurityScheme.In.HEADER)
                .type(SecurityScheme.Type.HTTP)
                .scheme("Bearer");
    }
}
