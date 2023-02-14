package com.example.cardmonkey.dto;

import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordDTO {

    private String currentPassword; // 현재 비밀번호
    private String newPassword; // 새로운 비밀번호
}
