package com.ds3.team8.products_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración para la documentación de la API usando OpenAPI (Swagger).
 */
@Configuration
public class SwaggerConfig {

    /**
     * Bean que configura la instancia de OpenAPI para Swagger UI.
     *
     * @return instancia personalizada de OpenAPI con información de la API.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Products Microservice API")
                        .version("1.0.0")
                        .description("API documentation for the product microservice"));
    }
}
