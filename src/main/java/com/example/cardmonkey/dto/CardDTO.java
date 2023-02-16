package com.example.cardmonkey.dto;

import com.example.cardmonkey.entity.CardType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardDTO {
    private Long id;
    private String name;
    private String company;
    private CardType cardType;
    private String imageURL;

}