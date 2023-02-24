package com.example.cardmonkey.jwt;

import com.example.cardmonkey.dto.AuthDTO;
import com.example.cardmonkey.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties jwtProperties;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 토큰 생성
     */
    public String makeToken(Member member) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Duration.ofMinutes(30).toMillis()))
                .claim("userId", member.getUserId())
                .claim("role", member.getRole())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    public AuthDTO getMemberDtoOf(String authorizationHeader) {
        validationAuthorizationHeader(authorizationHeader);
        String token = "";
        Claims claims = null;
        try {
            token = extractToken(authorizationHeader);
            claims = parsingToken(token);
            return new AuthDTO(claims);
        } catch (Exception e) {
            logger.error("토큰이 없습니다.(2)");
        }
        return null;
    }

    /**
     * Token 값을 Claims로 바꿔주는 메서드
     */
    public Claims parsingToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 헤더값이 유효한지 검증하는 메서드
     */
    private boolean validationAuthorizationHeader(String header) {
        if (header == null || !header.startsWith(jwtProperties.getTokenPrefix())) {
            logger.error("토큰이 없습니다.(1)");
        }
        return true;
    }

    /**
     * 토큰 (Bearer) 떼고 토큰값만 가져오는 메서드
     */
    public String extractToken(String authorizationHeader) {
        return authorizationHeader.substring(jwtProperties.getTokenPrefix().length());
    }
}
