package com.example.cardmonkey.dto;

import com.example.cardmonkey.entity.Comment;
import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResDTO {

    private List<String> comments;
    private int[] rank;

    public void CountReview(List<String> userPick){
        for(String msg : userPick){
            int idx = 0;
            for(String comment : this.comments){
                if(msg.equals(comment)){
                    this.rank[idx] += 1;
                }
                idx += 1;
            }
        }
    }

    public void SetComment(List<Comment> commentList){
        // 등록된 코멘트 갯수에 따라 DTO 크기 조정
        for(Comment comment : commentList){
            this.comments.add(comment.getMessage());
        }
        this.rank = new int[this.comments.size()];
    }
}
