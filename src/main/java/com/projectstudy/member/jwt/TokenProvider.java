package com.projectstudy.member.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider implements InitializingBean {
    private static final String AUTHORITIES_KEY = "auth";
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.token-validity-in-seconds}")
    private Long tokenValidityInMilliseconds;

    private Key key;

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = DatatypeConverter.parseBase64Binary(secret);
        this.key = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
    }

    // JWT 토큰 생성 메소드
    public String createToken(Authentication authentication) {
        // 권한 가져오기
        String authorites = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Long now = new Date().getTime();

        Date Validity = new Date(now + this.tokenValidityInMilliseconds * 1000);

        return Jwts.builder()
                .setSubject(authentication.getName()) // 토큰 제목
                .claim(AUTHORITIES_KEY, authorites) // 사용자 정의 Claim, 권한 설정(auth Claim)
                .setExpiration(Validity) // 만료기간
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    // 인증 성공 시 SecurityContextHolder에 저장할 Authentication 객체 생성 (권한 정보를 파싱하여 Authentication 객체 리턴)
    public Authentication getAuthentication(String token) {
        // 토큰에서 payload(Claim) 추출
        Claims claims = Jwts
                .parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();

        // Claim에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (SignatureException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (MalformedJwtException e) {
            log.info("JWT 토큰이 올바르게 구성되지 않았습니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰의 인수가 잘못되었습니다.");
        }
        return false;
    }
}
