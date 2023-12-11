package com.windmeal.global.token.util;

import static com.windmeal.global.constants.JwtConstants.ACCESS_TOKEN_EXPIRES_IN;
import static com.windmeal.global.constants.JwtConstants.AUTHORITIES_KEY;
import static com.windmeal.global.constants.JwtConstants.BEARER_PREFIX;
import static com.windmeal.global.constants.JwtConstants.EMAIL;
import static com.windmeal.global.constants.JwtConstants.REFRESH_TOKEN_EXPIRES_IN;
import static com.windmeal.global.constants.SecurityConstants.NICKNAME_KEY;

import com.windmeal.global.security.oauth2.user.UserPrincipal;
import com.windmeal.global.token.dto.TokenInfoResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * 토큰 생성, 토큰 검증, 인증 객체 조회 등의 역할을 수행하는 유사 유틸 클래스
 */
@Slf4j
@Component
public class TokenProvider implements InitializingBean {
    @Value("${jwt.secret}")
    private String secretKey;
    private Key key;

    /**
     * 의존 관계가 모두 설정된 이후, key 설정을 설정해주기 위해 사용
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    /**
     * 인증 객체를 전달 받아 해당 인증 객체에 대한 access Token과 refresh Token을 생성하는 메서드
     *
     * @param authentication
     * @return
     */
    public TokenInfoResponse.TokenDetail createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        Date currentDate = new Date();
        Long currentTime = currentDate.getTime();
        Date accessValidity = new Date(currentTime + ACCESS_TOKEN_EXPIRES_IN);
        Date refreshValidity = new Date(currentTime + REFRESH_TOKEN_EXPIRES_IN);
        UserDetails details = (UserDetails) authentication.getPrincipal();
        // 닉네임 파트
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        Map<String, Object> attributes = principal.getAttributes();
        String nickname = (String) attributes.get(NICKNAME_KEY);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(EMAIL, details.getPassword())
                .claim(AUTHORITIES_KEY, authorities)
                .claim(NICKNAME_KEY, nickname)
                .signWith(key, SignatureAlgorithm.HS512)
                .setIssuedAt(currentDate)
                .setExpiration(accessValidity)
                .compact();
        String refreshToken = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(refreshValidity)
                .setIssuedAt(currentDate)
                .compact();

        return TokenInfoResponse.TokenDetail.builder()
                .grantType(BEARER_PREFIX)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiresIn(accessValidity.getTime())
                .refreshTokenExpiresIn(refreshValidity.getTime())
                .build();
    }

    public String createAccessToken(Authentication authentication, String nickname) {
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        Date currentDate = new Date();
        Long currentTime = currentDate.getTime();
        Date accessValidity = new Date(currentTime + ACCESS_TOKEN_EXPIRES_IN);
        UserDetails details = (UserDetails) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(EMAIL, details.getPassword())
                .claim(AUTHORITIES_KEY, authorities)
                .claim(NICKNAME_KEY, nickname)
                .signWith(key, SignatureAlgorithm.HS512)
                .setIssuedAt(currentDate)
                .setExpiration(accessValidity)
                .compact();
    }

    public Authentication getAuthentication(String token) throws AuthenticationException {

        Claims claims = parseClaims(token);
        // 클레임의 권한 정보를 가져오겠다.
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        User principal = new User(claims.getSubject(), (String) claims.get(EMAIL), authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
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

    /*
        토큰이 만료되어도 클레임은 가져올 수 있다.
     */
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
