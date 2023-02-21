package com.example.cardmonkey.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordReqDTO {

    private String currentPassword; // 현재 비밀번호
    private String newPassword; // 새로운 비밀번호
}
