package com.evaluacion.votos.evaluacion_backend_votos.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API de Sistema de Votaciones - Evaluación Backend N°2",
                version = "1.0.0",
                description = "API REST para gestionar candidatos, partidos políticos y el conteo de votos."
        )
)
public class OpenApiConfig {
}
