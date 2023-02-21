package com.example.cardmonkey.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidationDTO {

    private String userId; // 회원가입시 아이디 중복체크
}
