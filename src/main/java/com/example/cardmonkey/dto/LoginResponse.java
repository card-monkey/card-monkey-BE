package com.example.cardmonkey.dto;

import com.example.cardmonkey.entity.Member;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class LoginResponse {

    private String userId;
    private String name;
    private String role;
    private String token;
    private String loginFail;

    public LoginResponse(Member member, String token) {
        this.userId = member.getUserId();
        this.name = member.getName();
        this.role = member.getRole();
        this.token = token;
    }

    public LoginResponse(String loginFail) {
        this.loginFail = loginFail;
    }
}