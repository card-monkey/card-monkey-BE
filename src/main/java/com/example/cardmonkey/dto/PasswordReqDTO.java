package com.example.cardmonkey.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordReqDTO {

    private String currentPassword;
    private String newPassword;
}
