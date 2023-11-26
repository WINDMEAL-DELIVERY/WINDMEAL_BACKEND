
package com.windmeal.global.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${domain.url.host}")
    private String host;
    @Value("${domain.url.was_host1}")
    private String was_host1;
    @Value("${domain.url.was_host2}")
    private String was_host2;
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 요청을 허용할 출처
//                .allowedOrigins("http://localhost:3000","http://localhost:3001", host, was_host1, was_host2)
                .allowedOrigins("*")
                .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
//                .allowedHeaders("Content-Type, Access-Control-Allow-Origin, Access-Control-Allow-Headers, Authorization, X-Requested-With, requestId, Correlation-Id")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3000);
    }
}
