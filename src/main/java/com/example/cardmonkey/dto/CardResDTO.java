package com.example.cardmonkey.dto;

import com.example.cardmonkey.entity.Card;
import com.example.cardmonkey.entity.CardType;
import com.example.cardmonkey.entity.Favor;
import com.example.cardmonkey.entity.Paid;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardResDTO {

    private Long id;
    private String name;
    private String company;
    private String image;
    private CardType type;

    public CardResDTO(Card card) {
        this.id = card.getId();
        this.name = card.getName();
        this.company = card.getCompany();
        this.image = card.getImageURL();
        this.type = card.getCardType();
    }

    public CardResDTO(Favor favor) {
        Card card = favor.getCard();
        this.id = card.getId();
        this.name = card.getName();
        this.company = card.getCompany();
        this.image = card.getImageURL();
        this.type = card.getCardType();
    }

    public CardResDTO(Paid paid) {
        Card card = paid.getCard();
        this.id = card.getId();
        this.name = card.getName();
        this.company = card.getCompany();
        this.image = card.getImageURL();
        this.type = card.getCardType();
    }
}
