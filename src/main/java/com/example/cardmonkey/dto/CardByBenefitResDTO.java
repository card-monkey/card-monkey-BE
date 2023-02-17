package com.example.cardmonkey.dto;

import com.example.cardmonkey.entity.CardType;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardByBenefitResDTO {

    private long id;
    private String benefit;
    private String name;
    private String company;
    private String image;
    private CardType type;
}
