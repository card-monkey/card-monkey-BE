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

    public AuthDTO(Claims claims) {
        this.userId = claims.get("userId", String.class);
        this.role = claims.get("role", String.class);
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
