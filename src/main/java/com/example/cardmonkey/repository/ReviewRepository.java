package com.example.cardmonkey.repository;

import com.example.cardmonkey.entity.Card;
import com.example.cardmonkey.entity.Member;
import com.example.cardmonkey.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByCard(Card card);
    boolean existsByMemberAndCard(Member member, Card card);

    void deleteByMemberAndCard(Member member, Card card);
}
