package com.example.cardmonkey.dto;

import com.example.cardmonkey.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SignupRequest {

    private String userId;
    private String password;
    private String name;
    private String role;

    public Member toEntity() {
        return Member.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .role(role)
                .build();
    }
}
