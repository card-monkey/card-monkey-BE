// package com.example.cardmonkey.dto;
//
// import com.example.cardmonkey.entity.Card;
// import com.example.cardmonkey.entity.CardType;
// import com.example.cardmonkey.entity.Favor;
// import lombok.*;
//
// @Getter @Setter
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
// public class FavorResDTO {
//
//     private Long id;
//     private String name;
//     private String company;
//     private String image;
//     private CardType type;
//
//     public FavorResDTO(Favor favor) {
//         Card card = favor.getCard();
//         this.id = card.getId();
//         this.name = card.getName();
//         this.company = card.getCompany();
//         this.image = card.getImageURL();
//         this.type = card.getCardType();
//     }
// }
