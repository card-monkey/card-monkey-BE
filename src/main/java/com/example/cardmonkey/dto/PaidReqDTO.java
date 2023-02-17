package com.example.cardmonkey.dto;
import com.example.cardmonkey.entity.Card;
import com.example.cardmonkey.entity.Member;
import com.example.cardmonkey.entity.Paid;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PaidReqDTO {
    private Member member;
    private Card card;

    public Paid toEntity(){
        return Paid.builder()
                .member(this.member)
                .card(this.card)
                .build();
    }
}