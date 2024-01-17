package com.windmeal.global.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.windmeal.global.security.handler.CustomFailureHandler;
import com.windmeal.global.security.handler.CustomSuccessHandler;
import com.windmeal.global.security.oauth2.impl.CustomOAuth2UserService;
import com.windmeal.global.security.oauth2.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.windmeal.global.token.dao.RefreshTokenDAO;
import com.windmeal.global.token.dao.RefreshTokenDAOImpl;
import com.windmeal.global.token.security.JwtAccessDeniedHandler;
import com.windmeal.global.token.security.JwtAuthenticationEntryPoint;
import com.windmeal.global.token.util.TokenProvider;
import com.windmeal.global.util.AES256Util;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final AES256Util aes256Util;
  private final ObjectMapper objectMapper;
  private final TokenProvider tokenProvider;
  private final RedisTemplate redisTemplate;
  private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
  private final CustomOAuth2UserService customOAuth2UserService;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  private static final String[] PERMIT_URL_ARRAY = {
      /* swagger v3 */
      "/v3/api-docs/**",
      "/swagger-ui/**",
      "/swagger-ui.html",
      "/swagger-resources/**",
      "**/api-docs/**"
  };

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public RefreshTokenDAO refreshTokenDAO() {
    return new RefreshTokenDAOImpl(objectMapper, redisTemplate);
  }

  @Bean
  public OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository() {
    return new OAuth2AuthorizationRequestBasedOnCookieRepository();
  }


  @Bean
  public SimpleUrlAuthenticationSuccessHandler simpleUrlAuthenticationSuccessHandler() {
    return new CustomSuccessHandler(aes256Util, tokenProvider, refreshTokenDAO(),
        authorizationRequestRepository());
  }

  @Bean
  public SimpleUrlAuthenticationFailureHandler simpleUrlAuthenticationFailureHandler() {
    return new CustomFailureHandler(authorizationRequestRepository());
  }

  // 리다이렉트 url에서 '//'를 허용하기 위해 방화벽의 설정을 허용해준다.
  @Bean
  public HttpFirewall customStrictHttpFirewall() {
    StrictHttpFirewall firewall = new StrictHttpFirewall();
    firewall.setAllowUrlEncodedDoubleSlash(true);
    return firewall;
  }


  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .cors()
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        .accessDeniedHandler(jwtAccessDeniedHandler)

        .and()
        .headers()
        .frameOptions()
        .sameOrigin()

        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        .and()
        .authorizeHttpRequests()
        .antMatchers(PERMIT_URL_ARRAY).permitAll()
        .antMatchers("/auth/**").permitAll()
        .antMatchers("/oauth2/**").permitAll()
        .antMatchers("/auth/logout").authenticated()
        .anyRequest().authenticated()
        .and()
        .apply(new JwtSecurityConfig(aes256Util, objectMapper, tokenProvider))

        .and()
        .oauth2Login()
        .authorizationEndpoint()
        .authorizationRequestRepository(authorizationRequestRepository())

        .and()
        .redirectionEndpoint()
        .baseUri("/login/oauth2/code/*")

        .and()
        .userInfoEndpoint()
        .userService(customOAuth2UserService)
        .and()
        .successHandler(
            simpleUrlAuthenticationSuccessHandler())
        .failureHandler(simpleUrlAuthenticationFailureHandler());

    return http.build();
  }
}
