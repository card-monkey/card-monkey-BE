package com.example.cardmonkey.jwt;

import com.example.cardmonkey.dto.LoginRequest;
import com.example.cardmonkey.dto.MemberDTO;
import com.example.cardmonkey.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@Component
//@RequiredArgsConstructor
@Getter
public class JwtFilter extends OncePerRequestFilter {
    //시큐리티 필터 전에 유저 권한이나 인증 관련 정보를 넘겨주는 클래스

    private final JwtProvider jwtProvider;

    @Builder
    private JwtFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public static JwtFilter of(JwtProvider jwtProvider) {
        return JwtFilter.builder()
                .jwtProvider(jwtProvider)
                .build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        //filter에서 header를 가져옴
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        try {
            //token 값에서 유효값 (id, role)을 추출하여 userDTO를 만듦
            LoginRequest user = jwtProvider.getMemberDtoOf(authorizationHeader);
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                    user,
                    "",
                    user.getAuthorities()));
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException exception) {
            logger.error("ExpiredJwtException : expired token");
        } catch (Exception exception) {
            logger.error("Exception : no token");
        }
    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
//                                    FilterChain filterChain) throws IOException, ServletException {
//
//        // filter에서 header를 가져옴
//        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
//
//        try {
//            if (!tokenRepository.existsByToken(token)) {
//                Claims claims = jwtProvider.parsingToken(token);
//                if (claims != null) {
//                    MemberDTO dto = new MemberDTO(claims);
//                    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(dto, null, dto.getAuthorities()));
//                }
//            }
//
//            //token 값에서 유효값 (id, role)을 추출하여 userDTO를 만듦
//            LoginRequest user = jwtProvider.getMemberDtoOf(token);
//            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities()));
//
//            filterChain.doFilter(request, response);
//        } catch (ExpiredJwtException exception) {
//            logger.error("ExpiredJwtException : expired token");
//        } catch (Exception exception) {
//            logger.error("Exception : no token");
//            return ;
//        }
//    }
}
