package com.example.cardmonkey.dto;

import com.example.cardmonkey.entity.*;
import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewReqDTO {
    private List<ReviewComment> reviewComments;

    public Review toEntity(Member member, Card card, List<Comment> comments){

        // 사용자가 선택한 코멘트들을 ReviewComments에 대입
        for(Comment comment : comments){
            this.reviewComments.add(ReviewComment.builder()
                    .comment(comment)
                    .build());
        }

        // DB 저장을 위해 Review Entity로 변환
        return Review.builder()
                .member(member)
                .card(card)
                .reviewComments(this.getReviewComments())
                .build();
    }
}
