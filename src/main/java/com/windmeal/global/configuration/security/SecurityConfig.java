package com.windmeal.global.configuration.security;

import com.windmeal.global.token.security.JwtAccessDeniedHandler;
import com.windmeal.global.token.security.JwtAuthenticationEntryPoint;
import com.windmeal.global.token.util.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Bean
    public PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder();}
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // REST 형식을 활용하기 때문에 CSRF는 비활성화 해주겠음
                .csrf().disable()
                .cors()

                // 401, 403 에러애 대한 핸들러를 등록하겠음
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // click-jacking 공격을 방지하기 위해 frame과 관련된 option을 sameOrigin으로 설정해준다.
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                // JWT를 활용하기 때문에 세션을 사용할 필요가 없다.
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // HTTP 요청 접근을 제한한다.
                // 일단은 api/auth로 시작하는 요청들은 모두 허용해두었다.
                .and()
                .authorizeHttpRequests()
                .antMatchers("/api/auth/*").permitAll()
                .anyRequest().authenticated()

                // JwtSecurityConfig 설정
                .and()
                .apply(new JwtSecurityConfig(tokenProvider, authenticationManagerBuilder.getOrBuild()));

                // 아래는 Spring security가 제공하는 OAuth2를 활용하는 방식인데, 결국엔 세션이나 쿠키를 사용해야 한다는 문제점이 있다.
                // 따라서 아래의 방식을 사용하지 않고, 직접 OAuth2를 활용하여 소셜 로그인을 구현하겠다.
                // oauth2 설정 시작
                // 해당 방식은 스프링 시큐리티에서 지원해주는 방식으로, 많은 과정을 내부적으로 자동화해준다고 한다.
//                .and()
//                .oauth2Login()      // oauth2 로그인을 활성화 하겠다
//                .authorizationEndpoint()    // 인증에 대한 엔드 포인트를 지정한다. 즉, 아래의 uri에 접근하면 oauth 인증을 진행한다.
//                .baseUri("/oauth2/authorization")
//
//                .and()
//                .redirectionEndpoint()  // oauth 인증 후 리다이렉트되는 엔드 포인트를 지정한다. 아래의 uri에 대해서 리다이렉트를 허용한다.
//                .baseUri("/*/oauth2/code/google");
//
//                .and()
//                .userInfoEndpoint()
//                .userService()   // 여기서 지정한 userService() 메서드에서 사용자, 즉 resource owner의 정보를 받는다.
//                .successHandler()   //위 과정을 통해 성공적으로 회원가입을 진행하거나 유저를 매핑했다면, successHandler를 호출하여 jwt 생성
//                .failureHandler()   // 실패했다면 실패 핸들러를 호출
        return http.build();
    }
}
