package com.example.cardmonkey.dto;


import com.example.cardmonkey.entity.Member;
import io.jsonwebtoken.Claims;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class LoginRequest {

    private String userId;
    private String password;
    private String name;
    private String role;

    @Builder
    public LoginRequest(String userId, String password, String name, String role) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    //JwtTokenProvider에서 Token 값을 추출하여 dto 객체를 만들때 사용하는 메서드
    public LoginRequest(Claims claims) {
        this.userId = claims.get("userId", String.class);
        this.name = claims.get("name", String.class);
        this.role = claims.get("role", String.class);
    }

    public Member toEntity() {
        return Member.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .role(role)
                .build();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        // ROLE_USER, ROLE_ADMIN 은 정해저 있는 네이밍임, getAuthorities() 이름도 같아야함
        // 만약 하나로 구분이 아니고 여러 개의 권한을 가져야 한다면, 아래와 같이 쓸것
        // return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"),new SimpleGrantedAuthority("ROLE_ADMIN"));
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
