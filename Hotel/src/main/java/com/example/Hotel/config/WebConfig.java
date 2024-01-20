package com.example.Hotel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer
{
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // or specify a path pattern
                        .allowedOriginPatterns("https://*.csb.app")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*")
                        .allowCredentials(true);
                        //.allowedOrigins("https://8ywszs.csb.app/") // or use allowedOriginPatterns for more flexibility
                        //.allowedOrigins("https://vppxgx.csb.app/")
                        //.allowedOrigins("https://clsj5f.csb.app/")
                        
            }
        };
    
    }
}
