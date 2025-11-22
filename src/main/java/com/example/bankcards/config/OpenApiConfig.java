package com.example.bankcards.config;



import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Bank_REST")
                        .version("1.0")
                        .description("Test Project API"))
                .tags(Arrays.asList(
                        new Tag().name("Authentication").description("Auth endpoints"),
                        new Tag().name("Admin actions").description("Admin actions section"),
                        new Tag().name("User actions").description("User actions section")
                ))
                .addSecurityItem(new SecurityRequirement().addList("Bearer JWT-token"))
                .components(new Components()
                        .addSecuritySchemes("Bearer JWT-token", new SecurityScheme()
                                .name("Bearer JWT-token")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }


    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("bank-api")
                .pathsToMatch("/**")
                .addOpenApiCustomizer(this::customizeOpenApi)
                .build();
    }


    private void customizeOpenApi(OpenAPI openApi) {
        List<Tag> tags = Arrays.asList(
                new Tag().name("Authentication").description("Auth endpoints"),
                new Tag().name("Admin actions").description("Admin actions section"),
                new Tag().name("User actions").description("User actions section")
        );
        openApi.setTags(tags);
    }
}

