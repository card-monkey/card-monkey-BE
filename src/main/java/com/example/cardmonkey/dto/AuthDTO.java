package com.example.cardmonkey.dto;


import io.jsonwebtoken.Claims;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthDTO {

    private String userId;
    private String role;

    //JwtTokenProvider에서 Token 값을 추출하여 dto 객체를 만들때 사용하는 메서드
    public AuthDTO(Claims claims) {
        this.userId = claims.get("userId", String.class);
        this.role = claims.get("role", String.class);
    }

    // ROLE_USER, ROLE_ADMIN 은 정해저 있는 네이밍임, getAuthorities() 이름도 같아야함
    // 만약 하나로 구분이 아니고 여러 개의 권한을 가져야 한다면, 아래와 같이 쓸것
    // return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"),new SimpleGrantedAuthority("ROLE_ADMIN"));
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
