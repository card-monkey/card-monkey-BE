package com.example.cardmonkey.repository;
import com.example.cardmonkey.entity.Card;
import com.example.cardmonkey.entity.Member;
import com.example.cardmonkey.entity.Paid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaidRepository extends JpaRepository<Paid, Long> {
    List<Paid> findByMember(Member member);
    Paid findAllByMemberAndCard(Member member, Card card);
    Optional<Paid> findByCard(Card card);
}
