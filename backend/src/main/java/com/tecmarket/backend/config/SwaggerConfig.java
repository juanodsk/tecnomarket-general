/*
 * TecMarket Backend
 * Module: Config
 */

package com.tecmarket.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI tecMarketOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TecMarket Backend API")
                        .description("API REST para marketplace y gestion interna de negocios de servicio tecnico.")
                        .version("v1")
                        .contact(new Contact()
                                .name("TecMarket")
                                .email("juanph.dv@gmail.com"))
                        .license(new License()
                                .name("Private")))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
