package com.appia.incidents.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Incidents API",
                version = "1.0",
                description = "API de Gestão de Ocorrências do desafio Fullstack (Angular + Spring Boot). " +
                        "<br><br><b>Autenticação:</b><br>" +
                        "1) Chamar POST /auth/login com email e senha.<br>" +
                        "2) Copiar o token JWT retornado.<br>" +
                        "3) Clicar em 'Authorize' no Swagger e inserir: <code>Bearer SEU_TOKEN</code>.",
                contact = @Contact(name = "Desafio Appia")
        ),
        security = { @SecurityRequirement(name = "bearer-key") }
)
@SecurityScheme(
        name = "bearer-key",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {
}
