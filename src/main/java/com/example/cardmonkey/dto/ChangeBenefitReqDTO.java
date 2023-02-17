package com.example.cardmonkey.dto;

import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeBenefitReqDTO {

    private List<String> benefit;
}
