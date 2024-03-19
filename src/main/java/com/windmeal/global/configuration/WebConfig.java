
package com.windmeal.global.configuration;

import com.windmeal.global.converter.OrderStatusConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
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
  @Value("${domain.url.local}")
  private String local;
  @Value("${domain.url.ws_host}")
  private String ws_host;

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(new OrderStatusConverter());
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        // 요청을 허용할 출처
        .allowedOrigins(local, host, was_host1,
            was_host2, ws_host)
//        .allowedOrigins(local)
        .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
        .allowedHeaders(
            "Content-Type, Access-Control-Allow-Origin, Access-Control-Allow-Headers, Authorization, X-Requested-With, requestId, Correlation-Id")
        .allowCredentials(true)
        .maxAge(3000);
  }
}
