package com.example.cardmonkey.dto;

import com.example.cardmonkey.entity.Card;
import com.example.cardmonkey.entity.CardType;
import com.example.cardmonkey.entity.Favor;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class FavorResponseDTO {

    private Long id;
    private String name;
    private String company;
    private String image;
    private CardType type;

    @Builder
    public FavorResponseDTO(Favor favor) {
        Card card = favor.getCard();
        this.id = card.getId();
        this.name = card.getName();
        this.company = card.getCompany();
        this.image = card.getImageURL();
        this.type = card.getCardType();
    }
}
