package com.example.demo; // Asegúrate de que el paquete sea correcto

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc // Esto puede ser opcional en Spring Boot, pero a veces ayuda
public class Config implements WebMvcConfigurer { // Asegúrate de implementar la interfaz

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Aplica CORS a todos los endpoints que comienzan con /api
                .allowedOrigins("http://localhost:3000") // Permite el origen de tu frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos permitidos
                .allowCredentials(true); // Permite credenciales (como cookies)
    }
}
