package com.example.cardmonkey.dto;

import com.example.cardmonkey.entity.Benefit;
import com.example.cardmonkey.entity.CardType;
import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardDetailDTO {
    private Long id;
    private String name;
    private String company;
    private CardType cardType;
    private String imageURL;

    private ArrayList<String> benefit;
}
