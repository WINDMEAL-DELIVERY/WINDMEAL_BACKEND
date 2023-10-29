package com.windmeal.global.token.util;

import com.windmeal.global.token.dto.TokenDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static com.windmeal.global.constants.JwtConstants.*;

/**
 *  토큰 생성, 토큰 검증, 인증 객체 조회 등의 역할을 수행하는 유사 유틸 클래스
 */
@Slf4j
@Component
public class TokenProvider implements InitializingBean {
    @Value("${jwt.access-token-validity-in-seconds}")
    private Long accessTokenValidity;
    @Value("${jwt.refresh-token-validity-in-seconds}")
    private Long refreshTokenValidity;
    @Value("${jwt.secret}")
    private String secretKey;
    private Key key;

    /**
     * 의존 관계가 모두 설정된 이후, key 설정을 설정해주기 위해 사용
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    /**
     * 인증 객체를 전달 받아 해당 인증 객체에 대한 access Token과 refresh Token을 생성하는 메서드
     * @param authentication
     * @return
     */
    public TokenDTO.TokenDetail createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        Date currentDate = new Date();
        Long currentTime = currentDate.getTime();
        Date accessValidity = new Date(currentTime + this.accessTokenValidity);
        Date refreshValidity = new Date(currentTime + this.refreshTokenValidity);
        UserDetails details = (UserDetails) authentication.getPrincipal();
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(EMAIL, details.getPassword())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setIssuedAt(currentDate)
                .setExpiration(accessValidity)
                .compact();
        String refreshToken = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(refreshValidity)
                .setIssuedAt(currentDate)
                .compact();

        return TokenDTO.TokenDetail.builder()
                .grantType(BEARER_PREFIX)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiresIn(accessValidity.getTime())
                .refreshTokenExpiresIn(refreshValidity.getTime())
                .build();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

        // 클레임의 권한 정보를 가져오겠다.
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        // 여기서도 Password에 email, username에 id를 동일하게 넣어준다.\
        String email = (String) claims.get(EMAIL);
        User principal = new User(claims.getSubject(), email, authorities);
        /*
            UsernamePasswordAuthenticationToken은 2가지 생성자가 있는데, 인증이 완료되지 않은 토큰을 생성하는 것과,
            인증이 완료된 토큰을 생성하는 생성자이다. 아래의 코드는 2번째 생성자이고, 반드시 AuthenticationManager 혹은
            AuthenticationProvider에 의해서 호출되어야 한다. 하지만 이 코드가 호출되는 시점에 토큰의 Authentication이 유효하다는
            보장이 없기 때문에 첫번째 생성자를 만들고 authenticate 를 통해서 검증하겠다.
         */
//        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
        return new UsernamePasswordAuthenticationToken(principal, token);
    }

    public boolean validateToken(String token) {
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}
