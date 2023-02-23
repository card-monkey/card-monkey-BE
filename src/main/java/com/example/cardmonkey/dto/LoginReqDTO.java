package com.example.cardmonkey.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginReqDTO {

    private String userId;
    private String password;
}
