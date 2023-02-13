package com.example.cardmonkey.dto;

import com.example.cardmonkey.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MemberDTO {

    private String userId;
    private String password;

    public Member toEntity(){
        return null;

        /* 예시코드 =====================
        return User.builder()
                .email(this.email)
                .password(this.password)
                .build();
         ===================== */
    }

    /* 예시코드  =====================
    public UserDTO(Claims claims){
        this.email=claims.get("email",String.class);
    }
    ===================== */

    public Collection<? extends GrantedAuthority> getAuthorities() {
        // ROLE_USER, ROLE_ADMIN 은 정해저 있는 네이밍임, getAuthorities() 이름도 같아야함
        // 만약 하나로 구분이 아니고 여러 개의 권한을 가져야 한다면, 아래와 닽이 쓸것
        //return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"),new SimpleGrantedAuthority("ROLE_ADMIN"));
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_MEMBER"));
    }
}
