package org.aibles.spaced_repetition.flashcard.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("Configuring CORS mappings for flashcard service");

        registry.addMapping("/api/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH")
                .allowedHeaders("*")
                .exposedHeaders("X-Total-Count", "X-Total-Pages", "X-Resource-ID", "Cache-Control")
                .allowCredentials(true)
                .maxAge(3600);

        log.info("CORS configuration applied successfully");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        log.info("Creating CORS configuration source for flashcard service");

        CorsConfiguration configuration = new CorsConfiguration();

        // Allow specific origins for development
        configuration.setAllowedOriginPatterns(List.of(
                "http://localhost:3000",
                "http://localhost:3001",
                "http://127.0.0.1:3000",
                "http://127.0.0.1:3001",
                "*"
        ));

        // Allow all HTTP methods
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"
        ));

        // Allow all headers
        configuration.setAllowedHeaders(List.of("*"));

        // Allow credentials (for authentication)
        configuration.setAllowCredentials(true);

        // How long the browser can cache preflight response
        configuration.setMaxAge(3600L);

        // Expose custom headers to frontend
        configuration.setExposedHeaders(Arrays.asList(
                "X-Total-Count",
                "X-Total-Pages",
                "X-Resource-ID",
                "Cache-Control",
                "Content-Range"
        ));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);

        log.info("CORS configuration source created - Allowed origins: {}",
                configuration.getAllowedOriginPatterns());

        return source;
    }
}