package com.example.cardmonkey.jwt;

import com.example.cardmonkey.dto.LoginReqDTO;
import com.example.cardmonkey.service.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Getter
public class JwtFilter extends OncePerRequestFilter {
    //시큐리티 필터 전에 유저 권한이나 인증 관련 정보를 넘겨주는 클래스

    private final JwtProvider jwtProvider;

    private final TokenService tokenService;

    @Autowired
    @Builder
    private JwtFilter(JwtProvider jwtProvider,TokenService tokenService) {
        this.jwtProvider = jwtProvider;
        this.tokenService = tokenService;
    }

    public static JwtFilter of(JwtProvider jwtProvider,TokenService tokenService) {
        return new JwtFilter(jwtProvider,tokenService);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        //filter에서 header를 가져옴
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!tokenService.isTokenExists(authorizationHeader)) {
            try {
                //token 값에서 유효값 (id, role)을 추출하여 userDTO를 만듦
                LoginReqDTO user = jwtProvider.getMemberDtoOf(authorizationHeader);
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                        user,
                        "",
                        user.getAuthorities()));
            } catch (ExpiredJwtException exception) {
                logger.error("ExpiredJwtException : expired token");
            } catch (Exception exception) {
                logger.error("Exception : no token");
            }
        }
        filterChain.doFilter(request, response);
    }
}
