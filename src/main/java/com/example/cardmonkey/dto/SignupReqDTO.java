package com.example.cardmonkey.dto;

import com.example.cardmonkey.entity.Benefit;
import com.example.cardmonkey.entity.Member;
import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupReqDTO {

    private String userId;
    private String password;
    private String name;
    private String role;
    private List<String> benefit;

    public Member toEntity(List<String> benefits) {
        return Member.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .role(role)
                .benefit(new Benefit(benefits))
                .build();
    }
}
