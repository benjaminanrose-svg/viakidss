package com.viakids.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class CorsConfig {

    @Value("${app.cors.origins}")
    private String allowedOrigins;

    @Bean
    public CorsFilter corsFilter() {
        // Convert patterns to regex: convert wildcard patterns to proper regex
        List<String> patterns = List.of(allowedOrigins.split(","))
            .stream()
            .map(String::trim)
            .map(this::convertToRegex)
            .collect(Collectors.toList());

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(patterns);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

    /**
     * Convert simple patterns with wildcards to regex patterns
     * Examples:
     * - "http://localhost:5173" -> "http://localhost:5173"
     * - "https://*.vercel.app" -> "https://.*\\.vercel\\.app"
     */
    private String convertToRegex(String pattern) {
        return pattern
            .replace(".", "\\.")
            .replace("*", ".*");
    }
}
