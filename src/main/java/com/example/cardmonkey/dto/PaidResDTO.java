package com.example.cardmonkey.dto;
import com.example.cardmonkey.entity.Paid;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaidResDTO {

    private Long card_id;
    private String card_name;
    private String card_imageurl;
    private String card_type;


    public PaidResDTO(Paid paid) {
        this.card_id = paid.getCard().getId();
        this.card_name = paid.getCard().getName();
        this.card_imageurl = paid.getCard().getImageURL();
        this.card_type = paid.getCard().getCardType().name();
    }
}
