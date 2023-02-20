package com.example.cardmonkey.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResDTO {

    private String userId;
    private String name;
    private String role;
    private String token;
    private String loginStatus;

    public LoginResDTO(String loginStatus) {
        this.loginStatus = loginStatus;
    }
}
