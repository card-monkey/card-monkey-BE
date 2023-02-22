package com.example.cardmonkey.dto;

import com.example.cardmonkey.entity.CardType;
import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardDetailResDTO {

    private Long id;
    private String name;
    private String company;
    private String image;
    private CardType type;
    private int annualFee;
    private int lastMonthPaid;
    private String apply;
    private List<String> benefit;
}
