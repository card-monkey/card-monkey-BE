package com.example.cardmonkey.dto;

import com.example.cardmonkey.entity.Card;
import com.example.cardmonkey.entity.CardType;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardByFavorResDTO {

    private Long id;
    private String name;
    private String company;
    private String image;
    private CardType type;
    private int favor;

    public CardByFavorResDTO(Card card, int favor) {
        this.id = card.getId();
        this.name = card.getName();
        this.company = card.getCompany();
        this.image = card.getImageURL();
        this.type = card.getCardType();
        this.favor = favor;
    }
}
