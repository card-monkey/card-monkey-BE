package com.example.cardmonkey.dto;


import com.example.cardmonkey.entity.Member;
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
public class LoginReqDTO {

    private String userId;
    private String password;
    private String name; // TODO: 삭제
    private String role; // TODO: 삭제

    public Member toEntity() {
        return Member.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .role(role)
                .build();
    }

    // TODO: 위치
    //JwtTokenProvider에서 Token 값을 추출하여 dto 객체를 만들때 사용하는 메서드
    public LoginReqDTO(Claims claims) {
        this.userId = claims.get("userId", String.class);
        this.name = claims.get("name", String.class); // TODO: 삭제
        this.role = claims.get("role", String.class); // TODO: 삭제
    }

    // TODO: 위치
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // ROLE_USER, ROLE_ADMIN 은 정해저 있는 네이밍임, getAuthorities() 이름도 같아야함
        // 만약 하나로 구분이 아니고 여러 개의 권한을 가져야 한다면, 아래와 같이 쓸것
        // return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"),new SimpleGrantedAuthority("ROLE_ADMIN"));
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
