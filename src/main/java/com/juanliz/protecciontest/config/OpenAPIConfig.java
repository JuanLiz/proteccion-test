package com.juanliz.protecciontest.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API tareas",
                version = "1.0.0",
                contact = @Contact(
                        name = "Diego Lizarazo", email = "contact@juanliz.com", url = "https://juanliz.com"
                ),
                description = "La API de tareas permite gestionar tareas y usuarios. " +
                        "Incluye autenticación, gestión de tareas y usuarios."
        ),
        servers = {
                @Server(url = "https://protecciontest.juanliz.com", description = "Producción"),
        }
)
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenAPIConfig {
}