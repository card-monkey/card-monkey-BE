package com.example.cardmonkey.jwt;

import com.example.cardmonkey.dto.LoginReqDTO;
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

@RequiredArgsConstructor
@Component
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
                // TODO: 테스트를 위해서 일시적으로 토큰 유효기간 재설정
                .setExpiration(new Date(now.getTime() + Duration.ofDays(3).toMillis())) // 만료시간 3일
                .claim("userId", member.getUserId())
                .claim("role", member.getRole())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    public LoginReqDTO getMemberDtoOf(String authorizationHeader) {
        validationAuthorizationHeader(authorizationHeader); //토큰이 Bearer로 시작하는지 형식이 맞는지 확인
        String token = "";
        Claims claims = null;
        try {
            token = extractToken(authorizationHeader); // header에서 토큰 추출 (Bearer 제거)
            claims = parsingToken(token);
            return new LoginReqDTO(claims);
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
