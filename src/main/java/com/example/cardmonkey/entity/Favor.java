package com.example.cardmonkey.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Favor {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favor_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    public void setFavorMember(Member member) {
        this.member = member;
        // member.getFavors().add(this);
    }

    public void setFavorCard(Card card) {
        this.card = card;
    }

    public static Favor createFavor(Member member, Card card) {
        Favor favor = new Favor();

        favor.setFavorMember(member);
        favor.setFavorCard(card);

        return favor;
    }
}
